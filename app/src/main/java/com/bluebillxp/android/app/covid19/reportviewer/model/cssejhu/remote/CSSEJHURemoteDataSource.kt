package com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.remote

import android.util.Log
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUProvinceReport
import com.bluebillxp.android.app.covid19.reportviewer.util.github.Content
import com.bluebillxp.android.app.covid19.reportviewer.util.github.GitHub
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * The data source for loading data from GitHub.
 *
 * @author bluebillxp
 */
class CSSEJHURemoteDataSource {

    /**
     * Static definitions of remote data source repo.
     *
     * @see "https://github.com/CSSEGISandData/COVID-19/tree/master/csse_covid_19_data/csse_covid_19_daily_reports"
     */
    companion object {
        private const val GITHUB_USER = "CSSEGISandData"

        private const val GITHUB_REPO = "COVID-19"

        private const val GITHUB_CONTENT = "csse_covid_19_data/csse_covid_19_daily_reports"

        private const val GITHUB_BRANCH = "master"

        private const val FILE_EXT = ".csv"

        private const val CSV_DELIMITER = ','

        private const val CSV_SPECIAL_CHAR = '"'
    }

    private val gitHubAPI: GitHub.API by lazy {
        Retrofit.Builder()
            .baseUrl(GitHub.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHub.API::class.java)
    }

    fun loadReportSources() : List<String>? {
        val contents = getContents() ?: return null
        val last = contents.size - 1
        val sources = ArrayList<String>()
        for (i in last downTo 0) {
            val content = contents[i]
            val url = content.downloadUrl ?: continue
            if (!url.endsWith(FILE_EXT)) continue
            Log.v("bluebillxp", " source: ${content.name}")
            sources.add(url)
        }
        return sources
    }

    private fun getContents(): List<Content>? {
        val response = gitHubAPI
            .contents(GITHUB_USER, GITHUB_REPO, GITHUB_CONTENT, GITHUB_BRANCH)
            .execute()

        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    fun downloadReports(url: String): List<CSSEJHUProvinceReport> {
        val response = gitHubAPI.download(url).execute()
        val reports = ArrayList<CSSEJHUProvinceReport>()
        if (response.isSuccessful) {
            val reader = response.body()?.charStream() ?: return reports
            reader.use { readerStream ->
                val lines = readerStream.readLines()
                for (line in lines) {
                    val values = line.toCSVValues(CSV_DELIMITER, CSV_SPECIAL_CHAR)
                    val report = newReport(values) ?: continue
                    report.version = url.toReportVersion()
                    reports.add(report)
                }
            }
        }
        return reports
    }

    private fun newReport(values: List<String>): CSSEJHUProvinceReport? {
        return try {
            CSSEJHUProvinceReport(
                values[0], values[1], values[2],
                values[3].toInt(), values[4].toInt(),
                values[5].toInt(), values[6], values[7])
        } catch (e: NumberFormatException) {
            Log.e("bluebillxp", "${e.message}")
            null
        }
    }

    private fun String.toCSVValues(delimiter: Char, specialChar: Char): List<String> {
        val values = this.split(delimiter)
        val verifiedValues = ArrayList<String>()
        var newValue: String? = null
        for (value in values) {
            if (!value.contains(specialChar)) {
                verifiedValues.add(value)
                continue
            }
            // This is to handle CSV with "value, value"
            // For example: "Korea, South" -> South Korea
            if (!newValue.isNullOrEmpty()) {
                newValue = "${value.trim()} $newValue"
                verifiedValues.add(newValue.replace(specialChar.toString(), ""))
            }
            newValue = value.trim()
        }
        return verifiedValues
    }

    private fun String.toReportVersion(): String {
        return this.split('/').last().split('.').first()
    }
}