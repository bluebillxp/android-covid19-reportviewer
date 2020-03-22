package com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

/**
 * The [Dao] to [CSSEJHUCountryReport]
 *
 * @author bluebillxp
 */
@Dao
interface CSSEJHUCountryReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReports(reports: List<CSSEJHUCountryReport>)

    @Query("""
        UPDATE country_reports SET activated = '0' 
        WHERE activated = '1' AND version != :versionToKeep""")
    fun deactivateOldReports(versionToKeep: String)

    @Query("SELECT * FROM country_reports WHERE activated = '1' ORDER BY confirmed_cases DESC")
    fun activatedLiveReports(): LiveData<List<CSSEJHUCountryReport>>

    @Query("SELECT * FROM country_reports WHERE activated = '1' ORDER BY confirmed_cases DESC")
    fun activatedReports(): List<CSSEJHUCountryReport>

    @Transaction
    fun activateNewReports(reports: List<CSSEJHUCountryReport>) {
        insertReports(reports)
        if (reports.isNotEmpty()) deactivateOldReports(reports[0].version!!)
    }
}