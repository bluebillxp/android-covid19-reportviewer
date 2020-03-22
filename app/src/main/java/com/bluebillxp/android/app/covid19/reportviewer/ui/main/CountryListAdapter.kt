package com.bluebillxp.android.app.covid19.reportviewer.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bluebillxp.android.app.covid19.reportviewer.R
import com.bluebillxp.android.app.covid19.reportviewer.model.cssejhu.local.CSSEJHUCountryReport
import com.bluebillxp.android.app.covid19.reportviewer.util.setColoredGrowthNumber

class CountryListAdapter : RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>() {

    var list: List<CSSEJHUCountryReport> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_province_country, parent, false)
        return CountryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val report = list[position]
        holder.textCountry.text = report.country
        holder.textConfirmed.text = report.confirmed.toString()
        holder.textDeaths.text = report.deaths.toString()
        holder.textRecovered.text = report.recovered.toString()

        holder.textNewConfirmed.setColoredGrowthNumber(report.newConfirmed, false)
        holder.textNewDeaths.setColoredGrowthNumber(report.newDeaths, false)
        holder.textNewRecovered.setColoredGrowthNumber(report.newRecovered, true)
    }

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textCountry: TextView = itemView.findViewById(R.id.text_country)
        var textConfirmed: TextView = itemView.findViewById(R.id.text_confirmed)
        var textDeaths: TextView = itemView.findViewById(R.id.text_deaths)
        var textRecovered: TextView = itemView.findViewById(R.id.text_recovered)
        var textNewConfirmed: TextView = itemView.findViewById(R.id.text_new_confirmed)
        var textNewDeaths: TextView = itemView.findViewById(R.id.text_new_deaths)
        var textNewRecovered: TextView = itemView.findViewById(R.id.text_new_recovered)
    }
}