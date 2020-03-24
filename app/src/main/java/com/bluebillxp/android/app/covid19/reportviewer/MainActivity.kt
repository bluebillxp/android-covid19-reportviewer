package com.bluebillxp.android.app.covid19.reportviewer

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bluebillxp.android.app.covid19.reportviewer.ui.main.AboutFragment
import com.bluebillxp.android.app.covid19.reportviewer.ui.main.CountryListFragment
import com.bluebillxp.android.app.covid19.reportviewer.ui.main.GlobalSummaryFragment
import com.bluebillxp.android.app.covid19.reportviewer.ui.main.ProvinceListFragment
import com.bluebillxp.android.app.covid19.reportviewer.ui.viewmodel.ReportsViewModel
import com.bluebillxp.android.app.covid19.reportviewer.ui.viewmodel.ReportsViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs
import kotlin.math.max

private const val NUM_PAGES = 4
private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f

/**
 * Main Activity
 *
 * @author bluebillxp
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.actionbarPrimary)

        setContentView(R.layout.main_viewpager)

        viewPager = findViewById(R.id.main_viewpager)
        viewPager.adapter = ViewPagerAdapter(this)
        viewPager.setPageTransformer(ZoomOutPageTransformer())
        tabLayout = findViewById(R.id.main_tablayout)
        TabLayoutMediator(tabLayout, viewPager, true,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = "Global"
                    1 -> tab.text = "Country/Region"
                    2 -> tab.text = "Province/State"
                    3 -> tab.text = "About"
                }
            }).attach()
    }

    override fun onResume() {
        super.onResume()
        val viewModel: ReportsViewModel by viewModels { ReportsViewModelFactory(application) }
        viewModel.syncReports()
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment =
            when (position) {
                0 -> GlobalSummaryFragment()
                1 -> CountryListFragment()
                2 -> ProvinceListFragment()
                else -> AboutFragment()
            }
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams: WindowManager.LayoutParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    /**
     * ViewPager transition animation.
     *
     * @see "https://developer.android.com/training/animation/screen-slide-2#zoom-out"
     */
    private inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }

                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }

                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}
