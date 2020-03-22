package com.bluebillxp.android.app.covid19.reportviewer.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUCountryReport
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUGlobalReport
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHULocalDataSource
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUProvinceReport
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.remote.CSSEJHURemoteDataSource

/**
 * The repository for [ViewModel].
 *
 * @author bluebillxp
 */
class CSSEJHURepository(application: Application) {

    private val localDataSource = CSSEJHULocalDataSource(application)

    private val remoteDataSource = CSSEJHURemoteDataSource()

    val provinceReports: LiveData<List<CSSEJHUProvinceReport>>? by lazy {
        localDataSource.activatedLiveProvinceReports()
    }

    val countryReports: LiveData<List<CSSEJHUCountryReport>>? by lazy {
        localDataSource.activatedLiveCountryReports()
    }

    val globalReports: LiveData<List<CSSEJHUGlobalReport>>? by lazy {
        localDataSource.activatedLiveGlobalReports()
    }

    fun syncReports() {
        val timeStart = System.currentTimeMillis()
        val activatedProvinceReports = localDataSource.activatedProvinceReports()
        val remoteSources = remoteDataSource.loadReportSources() ?: return
        when {
            activatedProvinceReports.isNullOrEmpty() -> {
                // There are no previous downloaded reports
                Log.e("bluebillxp", "syncReports: There are no previous downloaded reports")
                val latestProvinces = remoteDataSource.downloadReports(remoteSources[0])
                val referenceProvinces = remoteDataSource.downloadReports(remoteSources[1])
                saveReports(latestProvinces, referenceProvinces)
            }

            activatedProvinceReports[0].version!! in remoteSources[0] -> {
                // The latest reports equal to current activated reports
                Log.i("bluebillxp", "syncReports: The latest reports equal to current activated" +
                        ", version: ${activatedProvinceReports[0].version!!}")
                return
            }

            else -> {
                // New reports available
                Log.i("bluebillxp", "syncReports: New reports available")
                val latestProvinces = remoteDataSource.downloadReports(remoteSources[0])
                saveReports(latestProvinces, activatedProvinceReports)
            }
        }
        val timeEnd = System.currentTimeMillis()
        Log.d("bluebillxp", "syncReports: time ${timeEnd - timeStart}")
    }

    private fun saveReports(
        latestProvinces: List<CSSEJHUProvinceReport>,
        referenceProvinces: List<CSSEJHUProvinceReport>
    ) {
        if (latestProvinces.isEmpty()) {
            Log.e("bluebillxp", "syncReports: failed, empty data")
            return
        }
        saveProvinceReports(latestProvinces, referenceProvinces)
        val latestCountries = ArrayList<CSSEJHUCountryReport>()
        val referenceCountries = ArrayList<CSSEJHUCountryReport>()
        saveCountryReports(latestProvinces, referenceProvinces, latestCountries, referenceCountries)
        saveGlobalReports(latestProvinces, referenceProvinces, latestCountries, referenceCountries)
    }

    private fun saveProvinceReports(
        latestProvinces: List<CSSEJHUProvinceReport>,
        referenceProvinces: List<CSSEJHUProvinceReport>
    ) {
        if (!referenceProvinces.isNullOrEmpty()) {
            val referenceProvinceMap = HashMap<String, CSSEJHUProvinceReport>()
            for (provinceReport in referenceProvinces) {
                val pKey = "${provinceReport.province}${provinceReport.country}"
                provinceReport.activated = false
                referenceProvinceMap[pKey] = provinceReport
            }
            for (latestReport in latestProvinces) {
                val pkey = "${latestReport.province}${latestReport.country}"
                val reference = referenceProvinceMap[pkey] ?: continue
                latestReport.apply {
                    newConfirmed = latestReport.confirmed - reference.confirmed
                    newDeaths = latestReport.deaths - reference.deaths
                    newRecovered = latestReport.recovered - reference.recovered
                }
            }
        }

        localDataSource.activateNewProvinceReports(latestProvinces)
        Log.i("bluebillxp", "Activated provinces = ${latestProvinces.size}")
    }

    private fun saveCountryReports(
        latestProvinces: List<CSSEJHUProvinceReport>,
        referenceProvinces: List<CSSEJHUProvinceReport>,
        latestCountries: ArrayList<CSSEJHUCountryReport>,
        referenceCountries: ArrayList<CSSEJHUCountryReport>
    ) {
        val latestCountryMap = buildCountryMap(latestProvinces)
        val preferenceCountryMap = buildCountryMap(referenceProvinces)

        for ((key, country) in latestCountryMap) {
            latestCountries.add(country)
            val refCountry = preferenceCountryMap[key] ?: continue
            country.apply {
                newConfirmed = country.confirmed - refCountry.confirmed
                newDeaths = country.deaths - refCountry.deaths
                newRecovered = country.recovered - refCountry.recovered
            }
            refCountry.activated = false
            referenceCountries.add(refCountry)
        }

        localDataSource.activateNewCountryReports(latestCountries)
        Log.i("bluebillxp", "Activated countries = ${latestCountries.size}")
    }

    private fun buildCountryMap(
        provinces: List<CSSEJHUProvinceReport>?
    ): Map<String, CSSEJHUCountryReport> {
        val countryMap = HashMap<String, CSSEJHUCountryReport>()
        if (provinces.isNullOrEmpty()) {
            return countryMap
        }
        for (province in provinces) {
            val cKey = "${province.country}"
            when (val country = countryMap[cKey]) {
                null -> countryMap[cKey] = CSSEJHUCountryReport(
                    province.country, province.lastUpdate,
                    province.confirmed, province.deaths, province.recovered,
                    province.latitude, province.longitude)
                    .apply {
                        newConfirmed = province.newConfirmed
                        newDeaths = province.newDeaths
                        newRecovered = province.newRecovered
                        version = province.version
                    }

                else -> country.apply {
                    confirmed += province.confirmed
                    deaths += province.deaths
                    recovered += province.recovered
                    newConfirmed += province.newConfirmed
                    newDeaths += province.newDeaths
                    newRecovered += province.newRecovered
                }
            }
        }

        return countryMap
    }

    private fun saveGlobalReports(
        latestProvinces: List<CSSEJHUProvinceReport>,
        referenceProvinces: List<CSSEJHUProvinceReport>,
        latestCountries: ArrayList<CSSEJHUCountryReport>,
        referenceCountries: ArrayList<CSSEJHUCountryReport>
    ) {
        val globalReport = CSSEJHUGlobalReport().apply {
            lastUpdate = latestCountries[0].lastUpdate
            version = latestCountries[0].version
            provinceCount = latestProvinces.size
            countryCount = latestCountries.size
            newProvinceCount = latestProvinces.size - referenceProvinces.size
            newCountryCount = latestCountries.size - referenceCountries.size
        }
        for (country in latestCountries) {
            globalReport.apply {
                confirmed += country.confirmed
                deaths += country.deaths
                recovered += country.recovered
                newConfirmed += country.newConfirmed
                newDeaths += country.newDeaths
                newRecovered += country.newRecovered
            }
        }
        localDataSource.activateNewGlobalReports(ArrayList<CSSEJHUGlobalReport>().apply { add(globalReport) })
    }
}