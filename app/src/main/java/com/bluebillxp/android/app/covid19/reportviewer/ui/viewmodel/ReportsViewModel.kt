package com.bluebillxp.android.app.covid19.reportviewer.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluebillxp.android.app.covid19.reportviewer.model.CSSEJHURepository
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUCountryReport
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUGlobalReport
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUProvinceReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Shared [ViewModel] for the data from [CSSEJHURepository]
 *
 * @author bluebillxp
 */
class ReportsViewModel(
    application: Application
) : ViewModel() {

    private val repo = CSSEJHURepository(application)

    val provincesReports: LiveData<List<CSSEJHUProvinceReport>>? by lazy {
        repo.provinceReports
    }

    val countryReports: LiveData<List<CSSEJHUCountryReport>>? by lazy {
        repo.countryReports
    }

    val globalReports: LiveData<List<CSSEJHUGlobalReport>>? by lazy {
        repo.globalReports
    }

    fun syncReports() {
        viewModelScope.launch {
            syncReportsAsync()
        }.start()
    }

    private suspend fun syncReportsAsync() = withContext(Dispatchers.IO) {
        repo.syncReports()
    }
}