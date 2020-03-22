package com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local

import android.app.Application
import androidx.lifecycle.LiveData

/**
 * The data source for aggregating operations of each tables locally.
 *
 * @author bluebillxp
 */
class CSSEJHULocalDataSource(
    private var application: Application
) {

    private val provinceReportDao: CSSEJHUProvinceReportDao? by lazy {
        CSSEJHUDatabase.getInstance(application)
            ?.provinceReportDao()
    }

    private val countryReportDao: CSSEJHUCountryReportDao? by lazy {
        CSSEJHUDatabase.getInstance(application)
            ?.countryReportDao()
    }

    private val globalReportDao: CSSEJHUGlobalReportDao? by lazy {
        CSSEJHUDatabase.getInstance(application)
            ?.globalReportDao()
    }

    fun activatedLiveProvinceReports(): LiveData<List<CSSEJHUProvinceReport>>? = provinceReportDao?.activatedLiveReports()

    fun activatedProvinceReports(): List<CSSEJHUProvinceReport>? = provinceReportDao?.activatedReports()

    fun activateNewProvinceReports(reports: List<CSSEJHUProvinceReport>) = provinceReportDao?.activateNewReports(reports)

    fun activatedLiveCountryReports(): LiveData<List<CSSEJHUCountryReport>>? = countryReportDao?.activatedLiveReports()

    fun activateNewCountryReports(reports: List<CSSEJHUCountryReport>) = countryReportDao?.activateNewReports(reports)

    fun activatedLiveGlobalReports(): LiveData<List<CSSEJHUGlobalReport>>? = globalReportDao?.activatedLiveReports()

    fun activateNewGlobalReports(reports: List<CSSEJHUGlobalReport>) = globalReportDao?.activateNewReports(reports)

}