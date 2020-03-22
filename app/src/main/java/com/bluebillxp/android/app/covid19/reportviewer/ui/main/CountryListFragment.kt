package com.bluebillxp.android.app.covid19.reportviewer.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluebillxp.android.app.covid19.reportviewer.R
import com.bluebillxp.android.app.covid19.reportviewer.ui.viewmodel.ReportsViewModel
import com.bluebillxp.android.app.covid19.reportviewer.ui.viewmodel.ReportsViewModelFactory

class CountryListFragment : Fragment() {

    private var adapter: CountryListAdapter? = null

    private val viewModel: ReportsViewModel by activityViewModels { ReportsViewModelFactory(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false)
        val recyclerView: RecyclerView? = rootView.findViewById(R.id.recycler_list)
        recyclerView?.layoutManager = LinearLayoutManager(activity)

        adapter = CountryListAdapter()
        recyclerView?.adapter = adapter
        recyclerView?.itemAnimator = DefaultItemAnimator()

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.countryReports?.observe(viewLifecycleOwner, Observer { t ->
            Log.i("bluebillxp", "New list size: ${t.size}")
            (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = if (t
                    .isNotEmpty()
            ) {
                resources.getString(R.string.app_subtitle, t[0].version)
            } else {
                resources.getString(R.string.app_subtitle_loading)
            }
            adapter?.list = t
            adapter?.notifyDataSetChanged()
        })
    }
}
