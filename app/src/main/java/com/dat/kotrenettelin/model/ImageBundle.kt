package com.dat.kotrenettelin.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Dat on 8/11/2017.
 */
data class ImageBundle(@SerializedName("images") val images: List<String>)