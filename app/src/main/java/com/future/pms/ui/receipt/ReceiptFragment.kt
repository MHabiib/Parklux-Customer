package com.future.pms.ui.receipt

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
import com.future.pms.R
import com.future.pms.databinding.FragmentReceiptBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.oauth.Token
import com.future.pms.model.receipt.Receipt
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.RECEIPT_FRAGMENT
import com.future.pms.util.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_receipt.*
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class ReceiptFragment : BottomSheetDialogFragment(), ReceiptContract {
  @Inject lateinit var presenter: ReceiptPresenter
  private lateinit var binding: FragmentReceiptBinding
  private lateinit var idBooking: String
  private lateinit var imagePath: File

  companion object {
    const val PERMISSION_REQUEST_CODE = 707
    const val TAG: String = RECEIPT_FRAGMENT
  }

  fun newInstance(): ReceiptFragment {
    return ReceiptFragment()
  }

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
    injectDependency()
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
    idBooking = this.arguments?.getString("idBooking").toString()
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.attach(this)
    presenter.subscribe()
    idBooking.let { presenter.loadData(accessToken, it) }
  }

  override fun showProgress(show: Boolean) {
    if (null != progressBar) {
      if (show) {
        progressBar.visibility = View.VISIBLE
      } else {
        progressBar.visibility = View.GONE
      }
    }
  }

  override fun onFailed(message: String) {
    Timber.tag(Constants.ERROR).e(message)
  }

  override fun loadReceiptSuccess(receipt: Receipt) {
    println(receipt)
    with(binding) {
      buttonShareReceipt.visibility = View.VISIBLE
      bookingId.text = receipt.idBooking
      parkingZoneName.text = receipt.parkingZoneName
      address.text = receipt.address
      parkingSlot.text = receipt.slotName
      price.text = String.format(getString(R.string.price_per_hour), Utils.thousandSeparator(receipt.price.toInt()))
      inDate.text = Utils.convertLongToTime(receipt.dateIn)
      outDate.text = Utils.convertLongToTime(receipt.dateOut)
      hours.text = receipt.totalHours.toString()
      minutes.text = receipt.totalMinutes.toString()
      totalPrice.text = String.format(getString(R.string.total_price), Utils.thousandSeparator(receipt.totalPrice.toInt()))
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
          imagePath
          //https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed
      )
    }
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.apply {
      type = "image/*"
      val shareBody = "This is my parking receipt using PMS apps."
      putExtra(Intent.EXTRA_SUBJECT, "Parking Management System (P M S)")
      putExtra(Intent.EXTRA_TEXT, shareBody)
      putExtra(Intent.EXTRA_STREAM, uri)

      startActivity(Intent.createChooser(sharingIntent, "Share via"))
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

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(
        FragmentModule(this)).build()
    homeComponent.inject(this)
  }
}