package com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * The [Entity] class to a global status.
 *
 * @author bluebillxp
 */
@Entity(
    tableName = "global_reports",
    indices = [Index(value = ["version"], unique = true)])
data class CSSEJHUGlobalReport(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int?,
    @ColumnInfo(name = "last_update")
    var lastUpdate: String? = null,
    @ColumnInfo(name = "version")
    var version: String? = null,
    @ColumnInfo(name = "province_count")
    var provinceCount: Int = 0,
    @ColumnInfo(name = "country_count")
    var countryCount: Int = 0,
    @ColumnInfo(name = "confirmed_cases")
    var confirmed: Int = 0,
    @ColumnInfo(name = "deaths")
    var deaths: Int = 0,
    @ColumnInfo(name = "recovered_cases")
    var recovered: Int = 0,
    @ColumnInfo(name = "new_province_count")
    var newProvinceCount: Int = 0,
    @ColumnInfo(name = "new_country_count")
    var newCountryCount: Int = 0,
    @ColumnInfo(name = "new_confirmed_cases")
    var newConfirmed: Int = 0,
    @ColumnInfo(name = "new_deaths")
    var newDeaths: Int = 0,
    @ColumnInfo(name = "new_recovered_cases")
    var newRecovered: Int = 0,
    @ColumnInfo(name = "activated", defaultValue = "1")
    var activated: Boolean = true,
    @ColumnInfo(name = "date_added", defaultValue = "CURRENT_TIMESTAMP")
    val dateAdded: Long = System.currentTimeMillis()
) {
    constructor() : this(null)
}