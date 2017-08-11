package com.dat.kotrenettelin

/**
 * Created by Dat on 8/11/2017.
 */
interface KotrenettelinView {
    fun onLoadingImageBundle()

    fun onLoadImageBundleSuccess()

    fun onLoadImageBundleFailure(e: Throwable)

    fun bindImageData(imagePath: String?)

    fun bindPaginationData(currentImageIndex: Int, size: Int)
}