package com.bluebillxp.android.app.covid19.reportviewer.util.github

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * GitHub Contents API.
 *
 * @see "https://developer.github.com/v3/repos/contents/"
 *
 * @author bluebillxp
 */
class GitHub {

    companion object {
        const val API_URL = "https://api.github.com/"
    }

    interface API {
        /**
         * @see "https://developer.github.com/v3/repos/contents/#get-contents"
         */
        @GET("/repos/{user}/{repo}/contents/{content_path}")
        fun contents(
            @Path("user") user: String,
            @Path("repo") repo: String,
            @Path("content_path") contentPath: String,
            @Query("ref") branch: String
        ): Call<List<Content>>

        @GET
        @Streaming
        fun download(@Url url:String): Call<ResponseBody>
    }
}