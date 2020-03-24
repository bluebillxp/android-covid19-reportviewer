package com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.remote

import android.util.Log
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUDailyReport
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

    fun loadReportSources(): List<String>? {
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
                var headerMap: HashMap<String, Int>? = null
                val reportMap = HashMap<String, CSSEJHUProvinceReport>()
                for (line in lines) {
                    val values = line.toCSVValues(CSV_DELIMITER, CSV_SPECIAL_CHAR)
                    if (headerMap.isNullOrEmpty()) {
                        headerMap = buildHeaderIndex(values)
                        continue
                    }
                    val newReport = newReport(headerMap, values) ?: continue
                    newReport.version = url.toReportVersion()
                    val reportKey = "${newReport.province}${newReport.country}"
                    when (val report = reportMap[reportKey]) {
                        null -> {
                            reportMap[reportKey] = newReport
                            reports.add(newReport)
                        }

                        else -> {
                            report.apply {
                                confirmed += newReport.confirmed
                                deaths += newReport.deaths
                                recovered += newReport.recovered
                            }
                        }
                    }
                }
            }
        }
        return reports
    }

    private fun buildHeaderIndex(headers: List<String>): HashMap<String, Int> {
        val headerMap = HashMap<String, Int>()
        for (index in headers.indices) {
            when (headers[index]) {
                in CSSEJHUDailyReport.RAW_HEADER_PROVINCE -> {
                    headerMap[CSSEJHUDailyReport.RAW_HEADER_PROVINCE[0]] = index
                }

                in CSSEJHUDailyReport.RAW_HEADER_COUNTRY -> {
                    headerMap[CSSEJHUDailyReport.RAW_HEADER_COUNTRY[0]] = index
                }

                in CSSEJHUDailyReport.RAW_HEADER_LAST_UPDATE -> {
                    headerMap[CSSEJHUDailyReport.RAW_HEADER_LAST_UPDATE[0]] = index
                }

                in CSSEJHUDailyReport.RAW_HEADER_LATITUDE -> {
                    headerMap[CSSEJHUDailyReport.RAW_HEADER_LATITUDE[0]] = index
                }

                in CSSEJHUDailyReport.RAW_HEADER_LONGITUDE -> {
                    headerMap[CSSEJHUDailyReport.RAW_HEADER_LONGITUDE[0]] = index
                }

                in CSSEJHUDailyReport.RAW_HEADER_CONFIRMED -> {
                    headerMap[CSSEJHUDailyReport.RAW_HEADER_CONFIRMED[0]] = index
                }

                in CSSEJHUDailyReport.RAW_HEADER_DEATHS -> {
                    headerMap[CSSEJHUDailyReport.RAW_HEADER_DEATHS[0]] = index
                }

                in CSSEJHUDailyReport.RAW_HEADER_RECOVERED -> {
                    headerMap[CSSEJHUDailyReport.RAW_HEADER_RECOVERED[0]] = index
                }

                else -> {
                }
            }
        }
        return headerMap
    }

    private fun newReport(headerIndex: Map<String, Int>, values: List<String>):
            CSSEJHUProvinceReport? {
        return try {
            CSSEJHUProvinceReport(
                values[headerIndex[
                        CSSEJHUDailyReport.RAW_HEADER_PROVINCE[0]] ?: error("Province")
                ],
                values[headerIndex[
                        CSSEJHUDailyReport.RAW_HEADER_COUNTRY[0]] ?: error("Country")
                ],
                values[headerIndex[
                        CSSEJHUDailyReport.RAW_HEADER_LAST_UPDATE[0]] ?: error("Last Update")
                ],
                values[headerIndex[
                        CSSEJHUDailyReport.RAW_HEADER_CONFIRMED[0]] ?: error("Confirmed")
                ].toInt(),
                values[headerIndex[
                        CSSEJHUDailyReport.RAW_HEADER_DEATHS[0]] ?: error("Deaths")
                ].toInt(),
                values[headerIndex[
                        CSSEJHUDailyReport.RAW_HEADER_RECOVERED[0]] ?: error("Recovered")
                ].toInt(),
                values[headerIndex[
                        CSSEJHUDailyReport.RAW_HEADER_LATITUDE[0]] ?: error("Latitude")
                ],
                values[headerIndex[
                        CSSEJHUDailyReport.RAW_HEADER_LONGITUDE[0]] ?: error("Longitude")
                ]
            )
        } catch (e: NumberFormatException) {
            Log.e("bluebillxp", "${e.message}")
            null
        } catch (e: IllegalStateException) {
            Log.e("bluebillxp", "Unable to find header: ${e.message}")
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