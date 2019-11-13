package com.future.pms.ui.scan

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.util.Constants.Companion.SCAN_FRAGMENT
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.fragment_scan.*
import java.io.IOException
import javax.inject.Inject

class ScanFragment : Fragment(), ScanContract {
    private var barcodeDetector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null
    internal var intentData = ""

    @Inject
    lateinit var presenter: ScanPresenter

    fun newInstance(): ScanFragment {
        return ScanFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scan, container, false)
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(
                Manifest.permission.CAMERA
            ), REQUEST_CAMERA_PERMISSION
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
    }

    private fun initialiseDetectorsAndSources() {
        barcodeDetector = BarcodeDetector.Builder(context)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        cameraSource = CameraSource.Builder(context, barcodeDetector!!)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            .build()

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (context?.let {
                            ActivityCompat.checkSelfPermission(it, Manifest.permission.CAMERA)
                        } == PackageManager.PERMISSION_GRANTED) {
                        cameraSource?.start(surfaceView.holder)
                    } else {
                        ActivityCompat.requestPermissions(
                            context as Activity, arrayOf(Manifest.permission.CAMERA)
                            , REQUEST_CAMERA_PERMISSION
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                //No implementation Required
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource!!.stop()
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
                            intentData = barcodes.valueAt(0).displayValue
                            //Call API
                            Toast.makeText(context, intentData, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initialiseDetectorsAndSources()
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun injectDependency() {
        val scanComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()
        scanComponent.inject(this)
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 201
        const val TAG: String = SCAN_FRAGMENT
    }
}