package com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

/**
 * The [Dao] to [CSSEJHUGlobalReport]
 *
 * @author bluebillxp
 */
@Dao
interface CSSEJHUGlobalReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReports(reports: List<CSSEJHUGlobalReport>)

    @Query("""
        UPDATE global_reports SET activated = '0' 
        WHERE activated = '1' AND version != :versionToKeep""")
    fun deactivateOldReports(versionToKeep: String)

    @Query("SELECT * FROM global_reports WHERE activated = '1' ORDER BY confirmed_cases DESC")
    fun activatedLiveReports(): LiveData<List<CSSEJHUGlobalReport>>

    @Query("SELECT * FROM global_reports WHERE activated = '1' ORDER BY confirmed_cases DESC")
    fun activatedReports(): List<CSSEJHUGlobalReport>

    @Transaction
    fun activateNewReports(reports: List<CSSEJHUGlobalReport>) {
        insertReports(reports)
        if (reports.isNotEmpty()) deactivateOldReports(reports[0].version!!)
    }
}