package com.bluebillxp.android.app.covid19.reportviewer.util.github

import com.google.gson.annotations.SerializedName

/**
 * The JSON class for processing response from GitHub Contents API.
 *
 * @see "https://developer.github.com/v3/repos/contents/"
 *
 * @author bluebillxp
 */
class Content {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("sha")
    var sha: String? = null

    @SerializedName("download_url")
    val downloadUrl: String? = null
}