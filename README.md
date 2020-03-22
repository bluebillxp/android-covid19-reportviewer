# COVID-19 Daily Report Viewer for Android
This repository is to help individuals who are consuming the COVID-19
daily reports open-sourced by the Johns Hopkins University Center for
Systems Science and Engineering (JHU CSSE). The data source is
specifically connected to the master branch of
[CSSEGISandData/COVID-19/csse_covid_19_data/csse_covid_19_daily_reports/](https://github.com/CSSEGISandData/COVID-19/tree/master/csse_covid_19_data/csse_covid_19_daily_reports).

You can view the daily reports directly on your Android devices and the
results are presented by global, country/region and province/state
categories with differences from the previous day.

This repository also focuses on Android application development
techniques aimed to share with the knowledge of how to aggregate, store
and display data available on public GitHub projects on modern
practices.

## Android modules and techniques used in this repo
* Kotlin basis
* AndroidX architecture components
* AndroidX UI components
* MVVM
* Shared ViewModel between fragments and activity
* ViewModelFactory
* ROOM
* LiveData
* Accessing GitHub APIs using Retrofit2
* RecyclerView
* ViewPager2 + TabLayout
* ConstraintLayout
* CardView
* NestedScrollView
* Translucent status bar

## On-going items
* Unit Tests
* Proguard enabling
* Time series data

## References:
* Android Developer: [https://developer.android.com/]()
* [https://developer.android.com/kotlin/coroutines]()
* [https://developer.android.com/training/animation/screen-slide]()
* [https://developer.android.com/training/data-storage/room/defining-data#kotlin]()
* [https://developer.android.com/jetpack/androidx/releases/room]()
* [https://developer.android.com/training/data-storage/room/defining-data]()
* Kotlin.org: [https://kotlinlang.org/]()
* [https://kotlinlang.org/docs/tutorials/coroutines/async-programming.html]()
* StackOverflow: [https://stackoverflow.com/]()
* [https://stackoverflow.com/questions/29311078/android-completely-transparent-status-bar]()
* [https://stackoverflow.com/questions/54313453/how-to-instantiate-viewmodel-in-androidx]()
* [https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we]()
* [https://stackoverflow.com/questions/46665621/android-room-persistent-appdatabase-impl-does-not-exist/53187335]()
* [https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we]()
* [https://stackoverflow.com/questions/32559333/retrofit-2-dynamic-url]()
* Medium: [https://medium.com/]()
* [https://medium.com/@DarishJoy/constraintlayout-vs-coordinatorlayout-brief-comparison-aa1e64292462]()
* [https://medium.com/@daniel_novak/making-incremental-kapt-work-speed-up-your-kotlin-projects-539db1a771cf]()
* [https://medium.com/@hinchman_amanda/working-with-recyclerview-in-android-kotlin-84a62aef94ec]()
* [https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1]()
* [https://medium.com/@sreekumar_av/how-to-export-backup-room-db-and-view-all-the-tables-using-sqlite-viewer-9b053d44690f]()
* [https://medium.com/mindorks/android-architecture-components-room-and-kotlin-f7b725c8d1d]()
* [https://medium.com/@imstudio/android-downloadfile-from-server-using-retrofit-part-2-beware-with-large-files-use-streaming-eba001dfc08a]()
* Others
* [https://cyrilmottier.com/2013/01/23/android-app-launching-made-gorgeous/]()
* [https://xleon.net/xamarin/android/a-simple-page-indicator-for-your-android-viewpager.html]()
* [https://blog.mindorks.com/shared-viewmodel-in-android-shared-between-fragments]()
* [https://spin.atomicobject.com/2019/04/01/android-room-tips/]()
* [https://android.jlelse.eu/manage-rest-api-with-okhttp3-retrofit2-gson-and-rxjava2-aa5bea1e8a92]()
* [https://codingwithmitch.com/]()
* [https://www.youtube.com/playlist?list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118]()
* [https://github.com/square/retrofit]()
* Color Gradients:
  [https://digitalsynopsis.com/design/beautiful-color-gradients-backgrounds/]()
* appsinpp.com for CardView design:
  [https://appsnipp.com/free-multipurpose-home-design-with-dark-mode-for-android/]()
* I might not list exactly every reference I've used. If you've found
  the content familiar, please feel free to contact me.

## Thank you
* Special thanks to Johns Hopkins University Center for Systems
Science and Engineering (JHU CSSE)
* Special thanks to all the developers whom have published their
  knowledge, questions and solutions.

## Contact
* Email: bluebillxp@gmail.com

## Terms of Use
This GitHub repository and its implementations herein, including all
files, copyright 2020 bluebillxp, all rights reserved, is provided to
the public strictly for Android development purposes.

The author hereby disclaims any and all representations and warranties
with respect to the COVID-19 data presented, including accuracy, fitness
for use, and merchantability. Reliance on the application for medical
guidance or commercial use is strictly prohibited.

By accessing this repository, you have also agreed with the terms of use
of [CSSEGISandData/COVID-19](https://github.com/CSSEGISandData/COVID-19)
claimed by Johns Hopkins University Center for Systems Science and
Engineering (JHU CSSE).
