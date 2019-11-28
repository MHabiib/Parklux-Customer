package com.future.pms.ui.receipt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.addCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.oauth.Token
import com.future.pms.model.receipt.Receipt
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.RECEIPT_FRAGMENT
import com.future.pms.util.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_receipt.*
import kotlinx.android.synthetic.main.fragment_receipt.view.*
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class ReceiptFragment : Fragment(), ReceiptContract {
  @Inject lateinit var presenter: ReceiptPresenter
  private lateinit var rootView: View
  private lateinit var idBooking: String
  private lateinit var imagePath: File

  companion object {
    const val TAG: String = RECEIPT_FRAGMENT
  }

  fun newInstance(): ReceiptFragment {
    return ReceiptFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireActivity().onBackPressedDispatcher.addCallback(this) {
      val activity = activity as MainActivity?
      activity?.presenter?.onHomeIconClick()
    }
    val builder = StrictMode.VmPolicy.Builder()
    StrictMode.setVmPolicy(builder.build())
    injectDependency()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    rootView = inflater.inflate(R.layout.fragment_receipt, container, false)
    val backButton = rootView.findViewById(R.id.button_back_receipt) as ImageButton
    val shareButton = rootView.findViewById(R.id.button_share_receipt) as ImageButton
    backButton.setOnClickListener {
      val activity = activity as MainActivity?
      activity?.presenter?.onHomeIconClick()
    }
    shareButton.setOnClickListener {
      val bitmap = takeScreenshot()
      saveBitmap(bitmap)
      shareIt()
    }
    idBooking = this.arguments?.getString("idBooking").toString()
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val accessToken = Gson().fromJson(
      context?.getSharedPreferences(
        Constants.AUTHENTCATION, Context.MODE_PRIVATE
      )?.getString(Constants.TOKEN, null), Token::class.java
    ).accessToken
    presenter.attach(this)
    presenter.subscribe()
    idBooking.let { presenter.loadData(accessToken, it) }
    initView()
  }

  private fun initView() {
    //TODO
  }

  override fun showProgress(show: Boolean) {
    if (null != progressBar && show) {
      progressBar.visibility = View.VISIBLE
    } else {
      progressBar.visibility = View.GONE
    }
  }

  override fun showErrorMessage(error: String) {
    Timber.tag(Constants.ERROR).e(error)
  }

  override fun loadReceiptSuccess(receipt: Receipt) {
    println(receipt)
    rootView.booking_id.text = receipt.idBooking
    rootView.parking_zone_name.text = receipt.parkingZoneName
    rootView.address.text = receipt.address
    rootView.parking_slot.text = receipt.slotName
    rootView.price.text = String.format("IDR %s0/hour", receipt.price.toString())
    rootView.in_date.text = Utils.convertLongToTime(receipt.dateIn)
    rootView.out_date.text = Utils.convertLongToTime(receipt.dateOut)
    rootView.hours.text = receipt.totalHours.toString()
    rootView.minutes.text = receipt.totalMinutes.toString()
    rootView.total_price.text = String.format("IDR %s0", receipt.totalPrice)
  }

  fun takeScreenshot(): Bitmap {
    rootView.isDrawingCacheEnabled = true
    return rootView.drawingCache
  }

  fun saveBitmap(bitmap: Bitmap) {
    imagePath = File(String.format("%s/screenshot.png", Environment.getExternalStorageDirectory()))
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
      FileProvider.getUriForFile(
        it, context?.applicationContext?.packageName + ".provider", imagePath
        //https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed
      )
    }
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "image/*"
    val shareBody = "This is my parking receipt using PMS apps."
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Parking Management System (P M S)")
    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)

    startActivity(Intent.createChooser(sharingIntent, "Share via"))
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()
    homeComponent.inject(this)
  }
}