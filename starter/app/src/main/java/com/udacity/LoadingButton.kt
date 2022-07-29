package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.view.updateLayoutParams
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var loadingLong = ObjectAnimator()
    private var progress = 0
    private val mainColor = Color.CYAN
    private val size = 320
    var rect: Rect = Rect(0, 100, right, 0)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)



    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Loading -> {
                loadingAnimator?.start()
            }
            ButtonState.Completed -> {
                loadingAnimator?.end()
            }
        }
    }

  val loadingAnimator = loadingAnimation()


    init {

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        loadingMain(canvas)
        loadingMain1(canvas)

    }

    private fun loadingMain(canvas: Canvas?){
        paint.color = mainColor
        paint.style = Paint.Style.FILL
        val length = 320
        val height = 16
        val rect: Rect = Rect(0, heightSize,width,0)
        canvas?.drawRect(rect, paint)

        paint.color = Color.BLACK
        paint.textSize = 30.0f
        paint.textAlign = Paint.Align.CENTER
        //paint.typeface = Typeface.create("", Typeface.BOLD)
        canvas?.drawText(
            "Downloading",
            width / 2.toFloat(),
            (heightSize + 30) / 2.toFloat(),
            paint
        )

        paint.style = Paint.Style.STROKE
        val rect2: Rect = Rect(0, heightSize + 1,width + 1,0)
        canvas?.drawRect(rect2, paint)
    }
    private fun loadingMain1(canvas: Canvas?) {
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
        val length = 320
        val height = heightSize
        val rect3: Rect = Rect(0, height,progress,0)
        canvas?.drawRect(rect3, paint)
    }


    fun loadingAnimation(): ValueAnimator? {
        val initialValue = 0
        val finalValue = 1000

        val loadingAnimator = ValueAnimator.ofInt(
            initialValue,
            finalValue
        )

        loadingAnimator.duration = 1000
        loadingAnimator.repeatMode = ValueAnimator.RESTART
        loadingAnimator.repeatCount = ValueAnimator.INFINITE

        loadingAnimator.addUpdateListener {
            progress = it.animatedValue as Int
            this@LoadingButton.invalidate()
        }

        return loadingAnimator
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}