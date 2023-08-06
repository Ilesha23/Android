package com.iyakovlev.task_1.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.iyakovlev.task_1.R

class CustomImageButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val buttonRect = RectF()
    private val buttonStrokeRect = RectF()

    private var buttonText = ""
    private var buttonTextColor = 0
    private var buttonTextSize = 0f
    private var buttonFontFamily = 0
    private var buttonTextAllCaps = true

    private var buttonImage = 0
    private var buttonImageWidth = 0f
    private var buttonImageHeight = 0f

    private var buttonSpaceBetween = 0f

    private var buttonBackgroundDrawable: Drawable? = null

    private var buttonStrokeWidth = 0f
    private var buttonStrokeColor = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImageButton)

        // Text
        buttonText =
            typedArray.getString(R.styleable.CustomImageButton_android_text) ?: ""
        buttonTextColor =
            typedArray.getColor(R.styleable.CustomImageButton_android_textColor, 0)
        buttonTextSize =
            typedArray.getDimension(R.styleable.CustomImageButton_android_textSize, 0f)
        buttonFontFamily =
            typedArray.getResourceId(R.styleable.CustomImageButton_android_fontFamily, 0)
        buttonTextAllCaps =
            typedArray.getBoolean(R.styleable.CustomImageButton_android_textAllCaps, true)

        // Background (color/drawable)
        buttonImage =
            typedArray.getResourceId(R.styleable.CustomImageButton_android_src, 0)
        buttonImageWidth =
            typedArray.getDimension(R.styleable.CustomImageButton_customImageButton_image_width, 0f)
        buttonImageHeight =
            typedArray.getDimension(
                R.styleable.CustomImageButton_customImageButton_image_height,
                0f
            )
        buttonSpaceBetween =
            typedArray.getDimension(
                R.styleable.CustomImageButton_customImageButton_space_between,
                0f
            )

        val backgroundResource =
            typedArray.getResourceId(R.styleable.CustomImageButton_android_background, 0)
        if (backgroundResource != 0) {
            buttonBackgroundDrawable = ContextCompat.getDrawable(context, backgroundResource)
        }

        // Stroke
        buttonStrokeWidth = typedArray.getDimension(
            R.styleable.CustomImageButton_customImageButton_stroke_width,
            0f
        )
        buttonStrokeColor =
            typedArray.getColor(R.styleable.CustomImageButton_customImageButton_stroke_color, 0)

        typedArray.recycle()
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = buttonTextColor
        textSize = buttonTextSize
        typeface = ResourcesCompat.getFont(context, buttonFontFamily)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Background (color/drawable)
        buttonBackgroundDrawable?.let { drawable ->
            drawable.setBounds(0, 0, measuredWidth, measuredHeight)
            drawable.draw(canvas)
        }

        // Text and image
        drawImageWithText(canvas)

        // Stroke
        buttonRect.apply {
            top = 0f
            left = 0f
            right = measuredWidth.toFloat()
            bottom = measuredHeight.toFloat()
        }

        buttonStrokeRect.apply {
            top = buttonStrokeWidth
            left = buttonStrokeWidth
            right = measuredWidth.toFloat() - buttonStrokeWidth
            bottom = measuredHeight.toFloat() - buttonStrokeWidth
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                animate().alpha(0.5f).setDuration(100).start()
            }

            MotionEvent.ACTION_UP -> {
                animate().alpha(1f).setDuration(100).start()
                performClick()
            }

            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        // Call the super method to handle accessibility events
        super.performClick()
        // Handle the custom click action here
        // Implement your custom click behavior
        return true
    }

    private fun drawImageWithText(canvas: Canvas) {
        if (buttonTextAllCaps) {
            buttonText = buttonText.uppercase()
        }

        val image = ContextCompat.getDrawable(context, buttonImage)
        image?.let {
            val imageWidth = it.intrinsicWidth
            val imageHeight = it.intrinsicHeight

            if (buttonImageWidth <= 0 && buttonImageHeight <= 0) {
                // Both dimensions are not provided, use measuredHeight as the default
                buttonImageHeight = measuredHeight * 0.66f
                buttonImageWidth =
                    buttonImageHeight * (imageWidth.toFloat() / imageHeight.toFloat())
            } else if (buttonImageWidth <= 0) {
                // Only height is provided, calculate width based on the aspect ratio
                buttonImageWidth =
                    buttonImageHeight * (imageWidth.toFloat() / imageHeight.toFloat())
            } else if (buttonImageHeight <= 0) {
                // Only width is provided, calculate height based on the aspect ratio
                buttonImageHeight =
                    buttonImageWidth / (imageWidth.toFloat() / imageHeight.toFloat())
            }

            // Check if the dimensions are larger than the measured size
            if (buttonImageWidth > measuredWidth) {
                buttonImageWidth = measuredWidth.toFloat()
                buttonImageHeight =
                    buttonImageWidth / (imageWidth.toFloat() / imageHeight.toFloat())
            }

            if (buttonImageHeight > measuredHeight) {
                buttonImageHeight = measuredHeight.toFloat()
                buttonImageWidth =
                    buttonImageHeight * (imageWidth.toFloat() / imageHeight.toFloat())
            }
        }

        val textWidth = textPaint.measureText(buttonText)
        val commonWidth = textWidth + buttonImageWidth + buttonSpaceBetween
        val imageX = measuredWidth / 2 - commonWidth / 2
        val imageY = measuredHeight / 2 - buttonImageHeight / 2
        val textX = imageX + buttonImageWidth + buttonSpaceBetween
        val textY = measuredHeight / 2 - (textPaint.descent() + textPaint.ascent()) / 2

        image?.setBounds(
            imageX.toInt(),
            imageY.toInt(),
            imageX.toInt() + buttonImageWidth.toInt(),
            imageY.toInt() + buttonImageHeight.toInt()
        )
        image?.draw(canvas)

        canvas.drawText(buttonText, textX, textY, textPaint)

    }
}