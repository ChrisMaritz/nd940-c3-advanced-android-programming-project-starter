package com.udacity

import android.R.attr.*
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils
import androidx.core.content.res.TypedArrayUtils.*
import kotlin.properties.Delegates
import androidx.core.content.withStyledAttributes as withStyledAttributes1


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var progress = 0
    private var progressRadius = 0
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var attrs1 = "Download"
    var color = 0
    var widthAttr = 0
    var heightAttr = 0
    var colorAnimation = 0
    var colorCircle = 0


    init {
        context.withStyledAttributes1(attrs, R.styleable.LoadingButton){

            color = getColor(R.styleable.LoadingButton_color, 0)
            widthAttr =getInt(R.styleable.LoadingButton_widthRect, 0)
            heightAttr = getInt(R.styleable.LoadingButton_heightRect, 0)
            colorAnimation = getInt(R.styleable.LoadingButton_colorAnimation, 0)
            colorCircle = getColor(R.styleable.LoadingButton_colorCircle, 0)
        }
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Loading -> {
                loadingAnimator?.start()
                circleAnimator?.start()
                attrs1 = "Downloading"
                Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()

            }
            ButtonState.Completed -> {
                loadingAnimator?.end()
                circleAnimator?.end()
                Toast.makeText(context, "Download finished", Toast.LENGTH_SHORT).show()
            }

            ButtonState.Failed -> {
                loadingAnimator?.end()
                circleAnimator?.end()
                attrs1 = "Download"
                Toast.makeText(context, "Download Failed, Try again", Toast.LENGTH_LONG).show()
            }
        }
    }

  val loadingAnimator = loadingAnimation()
    val circleAnimator = circleAnimation()
    init {

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        loadingMain(canvas)
        loadingMain1(canvas)
        drawCircle(canvas)

    }

    private fun loadingMain(canvas: Canvas?){
        paint.color = color
        paint.style = Paint.Style.FILL
        val rect: Rect = Rect(0, heightAttr,widthAttr,0)
        val textButton: String = attrs1
        canvas?.drawRect(rect, paint)

        paint.color = Color.BLACK
        paint.textSize = 30.0f
        paint.textAlign = Paint.Align.CENTER
        canvas?.drawText(
            textButton,
            width / 2.toFloat(),
            (heightSize + 30) / 2.toFloat(),
            paint
        )
    }
    private fun loadingMain1(canvas: Canvas?) {
        paint.color = colorAnimation
        paint.style = Paint.Style.FILL
        val rect3: Rect = Rect(0, heightAttr,progress,0)
        canvas?.drawRect(rect3, paint)
    }

    fun loadingAnimation(): ValueAnimator? {
        val initialValue = 0
        val finalValue = widthAttr

        val loadingAnimator = ValueAnimator.ofInt(
            initialValue,
            finalValue
        )

        loadingAnimator.duration = 3500
        loadingAnimator.repeatMode = ValueAnimator.RESTART
        loadingAnimator.repeatCount = ValueAnimator.INFINITE

        loadingAnimator.addUpdateListener {
            progress = it.animatedValue as Int
            this@LoadingButton.invalidate()
        }

        return loadingAnimator
    }

    fun circleAnimation(): ValueAnimator?{
        val initialValue = 0
        val finalValue = 360

        val loadingAnimator = ValueAnimator.ofInt(
            initialValue,
            finalValue
        )

        loadingAnimator.duration = 3500
        loadingAnimator.repeatMode = ValueAnimator.RESTART
        loadingAnimator.repeatCount = ValueAnimator.INFINITE

        loadingAnimator.addUpdateListener {
            progressRadius = it.animatedValue as Int
            this@LoadingButton.invalidate()
        }

        return loadingAnimator
    }

    fun drawCircle(canvas: Canvas?){
        val paint1 = Paint()
        paint1.setStyle(Paint.Style.FILL)
        paint1.color = colorCircle
        val rectF: RectF = RectF(
            widthSize.div(2).minus(50).toFloat(), // left top X
            heightSize.div(2).minus(50).toFloat(), // left top Y
            widthSize.div(2).plus(50).toFloat(), // right bottom X
            heightSize.div(2).plus(50).toFloat() // right bottom Y
        )
        canvas?.drawArc(rectF, 90.0f, progressRadius.toFloat(), true, paint1)

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
