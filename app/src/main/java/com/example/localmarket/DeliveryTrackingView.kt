package com.example.localmarket

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.atan2

class DeliveryTrackingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var progress = 0f

    private var bike: Bitmap? = null

    init {
        paint.color = Color.GRAY
        paint.strokeWidth = 6f
        paint.style = Paint.Style.STROKE
        paint.pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
    }

    fun updateProgress(p: Float) {
        progress = p
        invalidate()
    }

    // ✅ SAFE BITMAP LOADER
    private fun getResizedBitmap(resId: Int, width: Int, height: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inSampleSize = 2
        val bitmap = BitmapFactory.decodeResource(resources, resId, options)
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        // 🛣 PATH
        path.reset()
        path.moveTo(50f, h * 0.7f)

        path.cubicTo(w * 0.2f, h * 0.2f, w * 0.4f, h * 0.8f, w * 0.6f, h * 0.4f)
        path.cubicTo(w * 0.7f, h * 0.1f, w * 0.9f, h * 0.7f, w - 50f, h * 0.3f)

        canvas.drawPath(path, paint)

        val measure = PathMeasure(path, false)
        val pos = FloatArray(2)
        val tan = FloatArray(2)

        measure.getPosTan(measure.length * progress, pos, tan)

        val angle = Math.toDegrees(atan2(tan[1], tan[0]).toDouble()).toFloat()

        // ✅ LOAD BIKE ONCE (NO CRASH)
        try {
            if (bike == null) {
                bike = getResizedBitmap(R.drawable.img_61, 80, 80)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        bike?.let {
            val matrix = Matrix()
            matrix.postTranslate(-it.width / 2f, -it.height / 2f)
            matrix.postRotate(angle)
            matrix.postTranslate(pos[0], pos[1])

            // Shadow
            val shadow = Paint()
            shadow.color = Color.BLACK
            shadow.alpha = 60
            canvas.drawCircle(pos[0], pos[1] + 25, 18f, shadow)

            canvas.drawBitmap(it, matrix, null)
        }
    }
}