package com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUDatabase.Companion.VERSION

/**
 * The [RoomDatabase] for storing data.
 *
 * @author bluebillxp
 */

@Database(entities = [
    CSSEJHUProvinceReport::class, CSSEJHUCountryReport::class, CSSEJHUGlobalReport::class
], version = VERSION)
abstract class CSSEJHUDatabase : RoomDatabase() {

    abstract fun provinceReportDao(): CSSEJHUProvinceReportDao

    abstract fun countryReportDao(): CSSEJHUCountryReportDao

    abstract fun globalReportDao(): CSSEJHUGlobalReportDao

    companion object {
        const val VERSION = 2
        private const val DB_FILE = "covid19-csse-jhu.db"

        @Volatile
        private var sINSTANCE: CSSEJHUDatabase? = null

        fun getInstance(application: Application): CSSEJHUDatabase? {
            if (sINSTANCE == null) {
                synchronized(CSSEJHUDatabase::class.java) {
                    sINSTANCE = Room.databaseBuilder(
                            application,
                            CSSEJHUDatabase::class.java,
                            DB_FILE)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return sINSTANCE
        }
    }
}