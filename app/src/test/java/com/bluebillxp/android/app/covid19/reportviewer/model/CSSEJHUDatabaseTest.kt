package com.bluebillxp.android.app.covid19.reportviewer.model

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUProvinceReport
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUProvinceReportDao
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CSSEJHUDatabaseTest {

    private lateinit var provinceReportDao: CSSEJHUProvinceReportDao
    private lateinit var db: CSSEJHUDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, CSSEJHUDatabase::class.java).build()
        provinceReportDao = db.provinceReportDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndLoadReports() {
        val report = testReport()

    }

    private fun testReport(): CSSEJHUProvinceReport {
        return CSSEJHUProvinceReport("province", "country", "last_update",
            99999, 3333, 77777,
            "latitude", "longitude")
    }
}