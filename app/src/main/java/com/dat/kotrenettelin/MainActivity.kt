package com.dat.kotrenettelin

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.widget.EditText
import butterknife.ButterKnife
import butterknife.OnClick
import com.dat.kotrenettelin.api.ImageBundleService
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity(), KotrenettelinView {

    private var bundleAddress: EditText? = null
    private var loadImageBundleDialog: AlertDialog? = null

    private lateinit var presenter: KotrenettelinPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        presenter = KotrenettelinPresenter(ImageBundleService(), this)
        if (savedInstanceState != null) {
            presenter.restoreData(savedInstanceState)
        } else {
            presenter.initLocalImagePaths()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.createSwitchImageObservable()
    }

    override fun onStop() {
        super.onStop()
        presenter.destroySwitchImageObservable()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_load_image_bundle) {
            showLoadImageBundleDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoadImageBundleDialog() {
        if (loadImageBundleDialog == null) {
            @SuppressLint("InflateParams")
            val rootView = LayoutInflater.from(this).inflate(R.layout.load_image_bundle_dialog, null)
            bundleAddress = rootView.findViewById<EditText>(R.id.address)
            loadImageBundleDialog = DialogBuilder(this)
                    .addCustomView(rootView)
                    .setCancelable(true)
                    .setNegativeButton(getString(R.string.cancel), null)
                    .setPositiveButton(getString(R.string.load),
                            DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                                presenter.loadImageBundle(bundleAddress!!.getText().toString())
                                bundleAddress!!.setText("")
                            })
                    .build()
            loadImageBundleDialog!!.setTitle(R.string.load_image_bundle_dialog_title)
        }
        loadImageBundleDialog!!.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        presenter.saveData(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onLoadingImageBundle() {
        toast(R.string.loading_image_bundle)
    }

    override fun onLoadImageBundleSuccess() {
        toast(R.string.loaded_image_bundle)
    }

    override fun onLoadImageBundleFailure(e: Throwable) {
        toast(e.localizedMessage)
    }

    override fun bindImageData(imagePath: String?) {
        if (imagePath!!.isEmpty()) {
            return
        }
        val playAnimation = AtomicBoolean(true)
        Picasso.with(this)
                .load(imagePath)
                .fit()
                .centerInside()
                .into(image, object : Callback {
                    override fun onSuccess() {
                        if (playAnimation.get()) {
                            val fadeIn = AlphaAnimation(0f, 1f)
                            fadeIn.interpolator = AccelerateInterpolator()
                            fadeIn.duration = 750
                            image.startAnimation(fadeIn)

                        }
                        playAnimation.set(false)
                    }

                    override fun onError() {
                        playAnimation.set(false)
                    }
                })
    }

    override fun bindPaginationData(currentImageIndex: Int, size: Int) {
        pageNumber.text = String.format(Locale.getDefault(), getString(R.string.page_number), currentImageIndex + 1, size)
    }

    @OnClick(R.id.button)
    fun clicked() {
        toast("clicked")
    }
}
