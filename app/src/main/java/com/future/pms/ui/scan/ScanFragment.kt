package com.future.pms.ui.scan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.databinding.FragmentScanBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.oauth.Token
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.SCAN_FRAGMENT
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_scan.*
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ScanFragment : Fragment(), ScanContract {
  private var barcodeDetector: BarcodeDetector? = null
  private var cameraSource: CameraSource? = null
  private lateinit var intentData: String
  private lateinit var accessToken: String
  private var mSurfaceView: SurfaceView? = null
  private lateinit var binding: FragmentScanBinding

  companion object {
    private const val REQUEST_CAMERA_PERMISSION = 0
    const val TAG: String = SCAN_FRAGMENT
  }

  @Inject lateinit var presenter: ScanPresenter

  fun newInstance(): ScanFragment {
    return ScanFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireActivity().onBackPressedDispatcher.addCallback(this) {
      val activity = activity as MainActivity?
      activity?.presenter?.onHomeIconClick()
    }
    injectDependency()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    checkPermission()
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scan, container, false)
    accessToken = Gson().fromJson(
      context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
        Constants.TOKEN, null
      ), Token::class.java
    ).accessToken
    val toggleFlash = binding.toggleFlash
    toggleFlash.setOnClickListener { flashToggle() }
    mSurfaceView = binding.surfaceView
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    initialiseDetectorsAndSources()
  }

  override fun showErrorMessage(error: String) {
    Timber.tag(Constants.ERROR).e(error)
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

  private fun initialiseDetectorsAndSources() {
    barcodeDetector = BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build()

    cameraSource =
      CameraSource.Builder(context, barcodeDetector).setRequestedPreviewSize(1920, 1080)
        .setAutoFocusEnabled(true).build()

    surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
      override fun surfaceCreated(holder: SurfaceHolder) {
        try {
          cameraSource?.start(surfaceView.holder)
        } catch (e: IOException) {
          e.printStackTrace()
        }
      }

      override fun surfaceChanged(
        holder: SurfaceHolder, format: Int, width: Int, height: Int
      ) {
        try {
          cameraSource?.start(surfaceView.holder)
        } catch (e: IOException) {
          e.printStackTrace()
        }
      }

      override fun surfaceDestroyed(holder: SurfaceHolder) {
        cameraSource?.stop()
      }
    })
    barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
      override fun release() {
        //No implementation required
      }

      override fun receiveDetections(detections: Detector.Detections<Barcode>) {
        val barcodes = detections.detectedItems
        if (barcodes.size() != 0) {
          txtBarcodeValue.post {
            if (barcodes.valueAt(0).displayValue.startsWith("QR")) {
              stopCamera()
              showProgress(true)
              intentData = barcodes.valueAt(0).displayValue
              val idSlot = intentData.substringAfter("idSlot=").substringBefore(')')
              presenter.createBooking(idSlot, accessToken)
            }
          }
        }
      }
    })
  }

  private fun checkPermission() {
    when (context?.let {
      ActivityCompat.checkSelfPermission(it, Manifest.permission.CAMERA)
    } != PackageManager.PERMISSION_GRANTED) {
      true -> {
        requestPermission()
      }
    }
  }

  private fun requestPermission() {
    ActivityCompat.requestPermissions(
      context as Activity, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
    )
  }

  override fun bookingSuccess(idBooking: String) {
    val activity = activity as MainActivity?
    activity?.presenter?.showBookingDetail(idBooking)
  }

  override fun bookingFailed() {
    val activity = activity as MainActivity?
    activity?.presenter?.showBookingFailed()
  }

  fun stopCamera() {
    if (null != barcodeDetector) {
      barcodeDetector?.release()
    }
  }

  fun flashToggle() {
    //TODO
  }

  private fun injectDependency() {
    val scanComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()
    scanComponent.inject(this)
  }
}