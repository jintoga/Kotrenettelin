package com.dat.kotrenettelin.api

import com.dat.kotrenettelin.model.ImageBundle
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by Dat on 8/11/2017.
 */
interface ImageBundleApi {
    @GET("/bins/{address}")
    fun getImageBundle(@Path(value = "address", encoded = true) imageBundleAddress: String): Observable<ImageBundle>
}