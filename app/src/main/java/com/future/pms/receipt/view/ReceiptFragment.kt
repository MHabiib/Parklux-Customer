package com.future.pms.receipt.view

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.model.Receipt
import com.future.pms.core.model.Token
import com.future.pms.databinding.FragmentReceiptBinding
import com.future.pms.receipt.injection.DaggerReceiptComponent
import com.future.pms.receipt.injection.ReceiptComponent
import com.future.pms.receipt.presenter.ReceiptPresenter
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.RECEIPT_FRAGMENT
import com.future.pms.util.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class ReceiptFragment : BottomSheetDialogFragment(), ReceiptContract {
  private var daggerBuild: ReceiptComponent = DaggerReceiptComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: ReceiptPresenter
  @Inject lateinit var gson: Gson
  private lateinit var binding: FragmentReceiptBinding
  private lateinit var idBooking: String
  private lateinit var imagePath: File

  companion object {
    const val PERMISSION_REQUEST_CODE = 707
    const val TAG: String = RECEIPT_FRAGMENT
  }

  fun newInstance(): ReceiptFragment = ReceiptFragment()

  override fun setupDialog(dialog: Dialog, style: Int) {
    super.setupDialog(dialog, style)
    val inflatedView = View.inflate(context, R.layout.fragment_receipt, null)
    dialog.setContentView(inflatedView)

    val params = (inflatedView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
    params.behavior

    val parent = inflatedView.parent as View
    parent.fitsSystemWindows = true
    inflatedView.measure(0, 0)

    val displaymetrics = DisplayMetrics()
    activity?.windowManager?.defaultDisplay?.getMetrics(displaymetrics)

    val screenHeight = displaymetrics.heightPixels
    BottomSheetBehavior.from(parent).peekHeight = screenHeight
    params.height = screenHeight
    parent.layoutParams = params
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val builder = StrictMode.VmPolicy.Builder()
    StrictMode.setVmPolicy(builder.build())
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_receipt, container, false)

    val shareButton = binding.buttonShareReceipt
    shareButton.setOnClickListener {
      if (checkPermission()) {
        val bitmap = takeScreenshot()
        saveBitmap(bitmap)
        shareIt()
      } else {
        requestPermission()
      }
    }
    idBooking = this.arguments?.getString(getString(R.string.id_booking)).toString()
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val accessToken = gson.fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.attach(this)
    presenter.subscribe()
    idBooking.let { presenter.loadData(accessToken, it) }
  }

  override fun showProgress(show: Boolean) {
    if (show) {
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.progressBar.visibility = View.GONE
    }
  }

  override fun onFailed(message: String) = Timber.tag(Constants.ERROR).e(message)

  override fun loadReceiptSuccess(receipt: Receipt) {
    println(receipt)
    with(binding) {
      buttonShareReceipt.visibility = View.VISIBLE
      bookingId.text = receipt.idBooking
      parkingZoneName.text = receipt.parkingZoneName
      address.text = receipt.address
      parkingSlot.text = receipt.slotName
      price.text = String.format(getString(R.string.price_per_hour),
          Utils.thousandSeparator(receipt.price.toInt()))
      inDate.text = Utils.convertLongToTime(receipt.dateIn)
      outDate.text = Utils.convertLongToTime(receipt.dateOut)
      hours.text = receipt.totalHours.toString()
      minutes.text = receipt.totalMinutes.toString()
      totalPrice.text = String.format(getString(R.string.total_price),
          Utils.thousandSeparator(receipt.totalPrice.toInt()))
    }
  }

  private fun takeScreenshot(): Bitmap {
    binding.root.isDrawingCacheEnabled = true
    return binding.root.drawingCache
  }

  private fun saveBitmap(bitmap: Bitmap) {
    imagePath = File(String.format(getString(R.string.screenshot_png),
        Environment.getExternalStorageDirectory()))
    val fos: FileOutputStream
    try {
      fos = FileOutputStream(imagePath)
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
      fos.flush()
      fos.close()
    } catch (e: FileNotFoundException) {
      Timber.tag(Constants.ERROR).e(e)
    } catch (e: IOException) {
      Timber.tag(Constants.ERROR).e(e)
    }
  }

  private fun shareIt() {
    val uri = context?.let {
      FileProvider.getUriForFile(it, context?.applicationContext?.packageName + ".provider",
          imagePath)
    }
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.apply {
      type = "image/*"
      val shareBody = getString(R.string.share_body)
      putExtra(Intent.EXTRA_SUBJECT, getString(R.string.parklux))
      putExtra(Intent.EXTRA_TEXT, shareBody)
      putExtra(Intent.EXTRA_STREAM, uri)

      startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)))
    }
  }

  private fun checkPermission(): Boolean {
    val result = context?.let {
      ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    return result == PackageManager.PERMISSION_GRANTED
  }

  private fun requestPermission() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      requestAppPermissions()
    } else {
      activity?.let {
        ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE)
      }
    }
  }

  private fun requestAppPermissions() {
    activity?.let {
      ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
    }
  }

  override fun onDestroy() {
    presenter.detach()
    super.onDestroy()
  }
}