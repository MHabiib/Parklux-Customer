package com.future.pms.ui.scan

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.ui.profile.ProfileContract
import com.future.pms.ui.profile.ProfileFragment
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_scan.*
import kotlinx.android.synthetic.main.fragment_scan.progressBar
import java.io.IOException
import javax.inject.Inject

class ScanFragment : Fragment(), ScanContract.View {

    @Inject
    lateinit var presenter: ScanContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): ScanFragment {
        return ScanFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun loadMessageSuccess(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var barcodeDetector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null
    internal var intentData = ""
    internal var isEmail = false

    private fun initViews() {


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scan, container, false)

        initViews()
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(
                Manifest.permission.CAMERA
            ), REQUEST_CAMERA_PERMISSION
        )

        return view
    }

    private fun initialiseDetectorsAndSources() {
        barcodeDetector = BarcodeDetector.Builder(context)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(context, barcodeDetector!!)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (context?.let {
                            ActivityCompat.checkSelfPermission(
                                it,
                                Manifest.permission.CAMERA
                            )
                        } == PackageManager.PERMISSION_GRANTED) {
                        cameraSource!!.start(surfaceView.holder)
                    } else {
                        ActivityCompat.requestPermissions(
                            context as Activity, arrayOf(
                                Manifest.permission.CAMERA
                            ),
                            REQUEST_CAMERA_PERMISSION
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
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource!!.stop()
            }
        })


        barcodeDetector!!.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post {
                        if (barcodes.valueAt(0).email != null) {
                            txtBarcodeValue.removeCallbacks(null)
                            intentData = barcodes.valueAt(0).email.address
                            txtBarcodeValue.text = intentData
                            isEmail = true
                        } else {
                            isEmail = false
                            intentData = barcodes.valueAt(0).displayValue
                            txtBarcodeValue.text = intentData

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

    private fun injectDependency() {
        val scanComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()
        scanComponent.inject(this)
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 201
        const val TAG: String = "ScanFragment"
    }
}