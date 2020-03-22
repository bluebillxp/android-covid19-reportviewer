package com.bluebillxp.android.app.covid19.reportviewer.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bluebillxp.android.app.covid19.reportviewer.R
import com.bluebillxp.android.app.covid19.reportviewer.ui.viewmodel.ReportsViewModel
import com.bluebillxp.android.app.covid19.reportviewer.ui.viewmodel.ReportsViewModelFactory
import com.bluebillxp.android.app.covid19.reportviewer.util.setColoredGrowthNumber

class GlobalSummaryFragment : Fragment() {

    private val viewModel: ReportsViewModel by activityViewModels {
        ReportsViewModelFactory(requireActivity().application)
    }
    private lateinit var confirmedInfoHolder: GlobalSummaryViewHolder
    private lateinit var deathInfoHolder: GlobalSummaryViewHolder
    private lateinit var recoveredInfoHolder: GlobalSummaryViewHolder
    private lateinit var countryInfoHolder: GlobalSummaryViewHolder
    private lateinit var provinceInfoHolder: GlobalSummaryViewHolder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_global_report, container, false)

        val confirmedInfo = rootView.findViewById<CardView>(R.id.info_global_confirmed)
        confirmedInfoHolder = GlobalSummaryViewHolder(confirmedInfo, R.string.global_title_confirmed_cases)
        val deathInfo = rootView.findViewById<CardView>(R.id.info_global_deaths)
        deathInfoHolder = GlobalSummaryViewHolder(deathInfo, R.string.global_title_deaths)
        val recoveredInfo = rootView.findViewById<CardView>(R.id.info_global_recovered)
        recoveredInfoHolder = GlobalSummaryViewHolder(recoveredInfo, R.string.global_title_recovered_cases)
        val countryInfo = rootView.findViewById<CardView>(R.id.info_global_countries)
        countryInfoHolder = GlobalSummaryViewHolder(countryInfo, R.string.global_title_countries)
        val provinceInfo = rootView.findViewById<CardView>(R.id.info_global_provinces)
        provinceInfoHolder = GlobalSummaryViewHolder(provinceInfo, R.string.global_title_provinces)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.globalReports?.observe(viewLifecycleOwner, Observer { t ->
            Log.i("bluebillxp", "New list size: ${t.size}")

            if (t.isNullOrEmpty()) {
                (requireActivity() as AppCompatActivity).supportActionBar?.apply {
                    setSubtitle(R.string.app_subtitle_loading)
                }
            } else {
                val report = t[0]
                (requireActivity() as AppCompatActivity).supportActionBar?.apply {
                    subtitle = resources.getString(R.string.app_subtitle, report.version)
                }
                confirmedInfoHolder.apply {
                    textSum.text = report.confirmed.toString()
                    textNew.setColoredGrowthNumber(report.newConfirmed, false)
                }
                deathInfoHolder.apply {
                    textSum.text = report.deaths.toString()
                    textNew.setColoredGrowthNumber(report.newDeaths, false)
                }
                recoveredInfoHolder.apply {
                    textSum.text = report.recovered.toString()
                    textNew.setColoredGrowthNumber(report.newRecovered, true)
                }
                countryInfoHolder.apply {
                    textSum.text = report.countryCount.toString()
                    textNew.setColoredGrowthNumber(report.newCountryCount, false)
                }
                provinceInfoHolder.apply {
                    textSum.text = report.provinceCount.toString()
                    textNew.setColoredGrowthNumber(report.newProvinceCount, false)
                }
            }
        })
    }

    inner class GlobalSummaryViewHolder(rootView: View, titleId: Int) {
        val textSum: TextView = rootView.findViewById(R.id.text_sum)
        val textNew: TextView = rootView.findViewById(R.id.text_new)

        init {
            val textTitle: TextView = rootView.findViewById(R.id.text_title)
            textTitle.setText(titleId)
        }
    }
}