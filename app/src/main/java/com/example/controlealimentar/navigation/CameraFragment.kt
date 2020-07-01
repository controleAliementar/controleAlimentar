package com.example.controlealimentar.navigation
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.preview.*
import java.io.ByteArrayOutputStream


class CameraFragment : FragmentActivity(), SurfaceHolder.Callback, Camera.PictureCallback {


    override fun onPictureTaken(data: ByteArray?, camera: Camera?) {

        val imageOriginal = BitmapFactory.decodeByteArray(data, 0, data!!.size, null)
        var width = view.width
        var height = view.height // for width
        val narrowSize = Math.min(width, height) // for height
        val differ =
            Math.abs((imageOriginal.height - imageOriginal.width) / 2.0f).toInt()  // for dimension
        width = if (width == narrowSize) 0 else differ
        height = if (width == 0) differ else 0

        val rotationMatrix = Matrix()
        rotationMatrix.postRotate(90F) // for orientation

        val imageCropped = Bitmap.createBitmap(
            imageOriginal,
            width,
            height,
            narrowSize,
            narrowSize,
            rotationMatrix,
            false
        )

        val byteArrayOutputStream = ByteArrayOutputStream()
        imageCropped.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)

        Toast.makeText(baseContext, "Show !", Toast.LENGTH_LONG).show()
    }

    internal lateinit var mCamera: Camera
    internal lateinit var mPreview: SurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.controlealimentar.R.layout.preview)
        mPreview = findViewById<View>(com.example.controlealimentar.R.id.preview) as SurfaceView
        mPreview.holder.addCallback(this)
        mPreview.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        mCamera = Camera.open()

        cameraCaptureImageButton.setOnClickListener {
            mCamera.takePicture(null, null, null, this)
        }

    }

    public override fun onPause() {
        super.onPause()
        mCamera.stopPreview()
    }

    public override fun onDestroy() {
        super.onDestroy()
        mCamera.release()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        val parameters = mCamera.getParameters()
        val previewSizes = parameters.getSupportedPreviewSizes()
        val previewSize = previewSizes.get(4) //480h x 720w

        parameters.setPreviewSize(previewSize.width, previewSize.height)
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO)
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO)

        mCamera.setParameters(parameters)

        val display =
            (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        if (display.rotation == Surface.ROTATION_0) {
            mCamera.setDisplayOrientation(90)
        } else if (display.rotation == Surface.ROTATION_270) {
            mCamera.setDisplayOrientation(180)
        }

        mCamera.startPreview()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            mCamera.setPreviewDisplay(mPreview.holder)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.i("PREVIEW", "surfaceDestroyed")
    }
}