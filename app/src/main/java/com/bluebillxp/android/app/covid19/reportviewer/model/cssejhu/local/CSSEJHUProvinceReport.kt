package com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local

import androidx.room.Entity
import androidx.room.Index

/**
 * The [Entity] class to a province/state.
 *
 * @author bluebillxp
 */
@Entity(
    tableName = "province_reports",
    indices = [Index(value = ["country_region", "province_state", "version"], unique = true)])
class CSSEJHUProvinceReport(
    province: String?,
    country: String?,
    lastUpdate: String? = null,
    confirmed: Int = 0,
    deaths: Int = 0,
    recovered: Int = 0,
    latitude: String?,
    longitude: String?,
    version: String? = null,
    newConfirmed: Int = 0,
    newDeaths: Int = 0,
    newRecovered: Int = 0,
    activated: Boolean = true,
    dateAdded: Long = System.currentTimeMillis()
) : CSSEJHUDailyReport(
    null,
    province,
    country,
    lastUpdate,
    confirmed,
    deaths,
    recovered,
    latitude,
    longitude,
    version,
    newConfirmed,
    newDeaths,
    newRecovered,
    activated,
    dateAdded
)