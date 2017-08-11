package com.dat.kotrenettelin

import android.os.Bundle
import com.dat.kotrenettelin.api.ImageBundleService
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Dat on 8/11/2017.
 */
class KotrenettelinPresenter(private val imageBundleService: ImageBundleService, private val view: KotrenettelinView) {
    private val SAVED_CURRENT_IMAGE_INDEX = "SAVED_CURRENT_IMAGE_INDEX"
    private val SAVED_IMAGE_PATHS = "SAVED_IMAGE_PATHS"

    private val SWITCH_TIME = 10

    private var subscription: Subscription? = null

    private val ASSET_PATH = "file:///android_asset/"
    private val localImageNames = arrayOf("carissa-gan-76325.jpg", "eaters-collective-132772.jpg",
            "eaters-collective-132773.jpg", "jakub-kapusnak-296128.jpg")
    private var imagePaths = ArrayList<String>()

    private var currentImageIndex = 0


    fun loadImageBundle(imageBundleAddress: String) {
        view.onLoadingImageBundle()
        imageBundleService.getApi()
                .getImageBundle(imageBundleAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ imageBundle ->
                    imagePaths.addAll(imageBundle.images)
                    view.bindPaginationData(currentImageIndex, imagePaths.size)
                },
                        { throwable -> view.onLoadImageBundleFailure(throwable) },
                        { view.onLoadImageBundleSuccess() })
    }

    fun createSwitchImageObservable() {
        if (subscription == null) {
            subscription = Observable.interval(SWITCH_TIME.toLong(), TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .doOnNext { n ->
                        increaseCurrentImageIndex()
                        bindImageAndPaginationData()
                    }
                    .doOnSubscribe({ bindImageAndPaginationData() })
                    .subscribe()
        }
    }

    fun destroySwitchImageObservable() {
        if (subscription!!.isUnsubscribed) {
            subscription!!.unsubscribe()
            subscription = null
        }
    }

    private fun bindImageAndPaginationData() {
        view.bindImageData(getImagePath())
        view.bindPaginationData(currentImageIndex, imagePaths.size)
    }

    private fun getImagePath(): String? {
        if (imagePaths.isEmpty()) {
            return null
        }
        return imagePaths.get(currentImageIndex)
    }

    private fun increaseCurrentImageIndex() {
        if (currentImageIndex >= 0 && currentImageIndex < imagePaths.size - 1) {
            currentImageIndex++
        } else {
            currentImageIndex = 0
        }
    }

    fun saveData(outState: Bundle) {
        outState.putInt(SAVED_CURRENT_IMAGE_INDEX, currentImageIndex)
        outState.putStringArrayList(SAVED_IMAGE_PATHS, imagePaths)
    }

    fun restoreData(savedInstanceState: Bundle) {
        currentImageIndex = savedInstanceState.getInt(SAVED_CURRENT_IMAGE_INDEX, 0)
        val savedImagePaths = savedInstanceState.getStringArrayList(SAVED_IMAGE_PATHS)
        if (savedImagePaths != null) {
            imagePaths = savedImagePaths
        } else {
            initLocalImagePaths()
        }
    }

    fun initLocalImagePaths() {
        for (imageAssetsName in localImageNames) {
            val imagePath = ASSET_PATH + imageAssetsName
            imagePaths.add(imagePath)
        }
    }
}