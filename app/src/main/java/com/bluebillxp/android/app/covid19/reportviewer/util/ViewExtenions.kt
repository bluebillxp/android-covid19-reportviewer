package com.bluebillxp.android.app.covid19.reportviewer.util

import android.util.TypedValue
import android.widget.TextView
import com.bluebillxp.android.app.covid19.reportviewer.R

/**
 * Set [number] to be colored in red or green.
 *
 * @param number The number to be presented on [TextView]
 * @param growthPositive Indicate if the growth should be seen as positive.
 */
fun TextView.setColoredGrowthNumber(number: Int, growthPositive: Boolean) {
    val colorRed = TypedValue()
    val colorGreen = TypedValue()
    resources.getValue(android.R.color.holo_red_light, colorRed, false)
    resources.getValue(android.R.color.holo_green_light, colorGreen, false)

    var color: Int?
    when {
        number == 0 -> {
            text = resources.getString(R.string.positive_value, number.toString())
            color = if (growthPositive) colorRed.data else colorGreen.data
        }

        number < 0 -> {
            text = "$number"
            color = if (growthPositive) colorRed.data else colorGreen.data
        }

        else -> {
            text = resources.getString(R.string.positive_value, number.toString())
            color = if (growthPositive) colorGreen.data else colorRed.data
        }
    }
    setTextColor(color)
}
