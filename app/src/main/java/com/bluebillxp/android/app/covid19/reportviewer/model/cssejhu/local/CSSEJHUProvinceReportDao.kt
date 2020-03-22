package com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

/**
 * The [Dao] to [CSSEJHUProvinceReport]
 *
 * @author bluebillxp
 */
@Dao
interface CSSEJHUProvinceReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReports(reports: List<CSSEJHUProvinceReport>)

    @Query("""
        UPDATE province_reports SET activated = '0' 
        WHERE activated = '1' AND version != :versionToKeep""")
    fun deactivateOldReports(versionToKeep: String)

    @Query("SELECT * FROM province_reports WHERE activated = '1' ORDER BY confirmed_cases DESC")
    fun activatedLiveReports(): LiveData<List<CSSEJHUProvinceReport>>

    @Query("SELECT * FROM province_reports WHERE activated = '1' ORDER BY confirmed_cases DESC")
    fun activatedReports(): List<CSSEJHUProvinceReport>

    @Transaction
    fun activateNewReports(reports: List<CSSEJHUProvinceReport>) {
        insertReports(reports)
        if (reports.isNotEmpty()) deactivateOldReports(reports[0].version!!)
    }
}