package com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/**
 * The base class maps to the source of data.
 *
 * @author bluebillxp
 */
open class CSSEJHUDailyReport(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int?,
    @ColumnInfo(name = "province_state")
    var province: String?,
    @ColumnInfo(name = "country_region")
    var country: String?,
    @ColumnInfo(name = "last_update")
    var lastUpdate: String?,
    @ColumnInfo(name = "confirmed_cases")
    var confirmed: Int = 0,
    @ColumnInfo(name = "deaths")
    var deaths: Int = 0,
    @ColumnInfo(name = "recovered_cases")
    var recovered: Int = 0,
    @ColumnInfo(name = "latitude")
    var latitude: String?,
    @ColumnInfo(name = "longitude")
    var longitude: String?,
    @ColumnInfo(name = "version")
    var version: String? = null,
    @ColumnInfo(name = "new_confirmed_cases")
    var newConfirmed: Int = 0,
    @ColumnInfo(name = "new_deaths")
    var newDeaths: Int = 0,
    @ColumnInfo(name = "new_recovered_cases")
    var newRecovered: Int = 0,
    @ColumnInfo(name = "activated", defaultValue = "1")
    var activated: Boolean = true,
    @ColumnInfo(name = "date_added", defaultValue = "CURRENT_TIMESTAMP")
    var dateAdded: Long = System.currentTimeMillis()
)