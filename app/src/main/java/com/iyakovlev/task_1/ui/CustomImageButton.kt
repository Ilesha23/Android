package com.iyakovlev.task_1.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
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
    private var buttonFontFamily = 0
    private var buttonTextAllCaps = true

    private var buttonImage = 0
    private var buttonSpacing = 0
    private var buttonSpaceBetween = 0f

    private var buttonBackgroundDrawable: Drawable? = null
    private var buttonBackgroundColor = 0
    private var buttonRadius = 0f

    private var buttonStrokeWidth = 0f
    private var buttonStrokeColor = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImageButton)

        // Text
        buttonText = 
            typedArray.getString(R.styleable.CustomImageButton_android_text) ?: ""
        buttonTextColor = 
            typedArray.getColor(R.styleable.CustomImageButton_android_textColor, 0)
        buttonFontFamily =
            typedArray.getResourceId(R.styleable.CustomImageButton_android_fontFamily, 0)
        buttonTextAllCaps =
            typedArray.getBoolean(R.styleable.CustomImageButton_android_textAllCaps, true)

        // Background (color/drawable)
        // TODO: draw image near text with spacing, add left to right and right to left 
        buttonImage =
            typedArray.getResourceId(R.styleable.CustomImageButton_android_src, 0)
        buttonSpacing = 
            typedArray.getInt(R.styleable.CustomImageButton_customImageButton_spacing, 0)
        buttonSpaceBetween = 
            typedArray.getDimension(R.styleable.CustomImageButton_customImageButton_space_between, 0f)

        val backgroundResource =
            typedArray.getResourceId(R.styleable.CustomImageButton_android_background, 0)
        if (backgroundResource != 0) {
            try {
                buttonBackgroundDrawable = ContextCompat.getDrawable(context, backgroundResource)
            } catch (e: Exception) {
                buttonBackgroundColor =
                    typedArray.getColor(R.styleable.CustomImageButton_android_background, 0)
            }
        }
        buttonRadius = typedArray.getDimension(
            R.styleable.CustomImageButton_customImageButton_radius,
            0f
        )

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
        textSize = 16f
        typeface = ResourcesCompat.getFont(context, buttonFontFamily)
    }

    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }


    private val strokePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL //
        strokeWidth = buttonStrokeWidth
        color = buttonStrokeColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Background (color/drawable)
        buttonBackgroundDrawable?.let { drawable ->
            drawable.setBounds(0, 0, measuredWidth, measuredHeight)
            drawable.draw(canvas)
        } ?: run {
            backgroundPaint.color = buttonBackgroundColor
            canvas.drawRoundRect(buttonRect, buttonRadius, buttonRadius, backgroundPaint)
        }

        // Text
        val textX = measuredWidth / 2 - textPaint.measureText(buttonText) / 2
        val textY = measuredHeight / 2 - (textPaint.descent() + textPaint.ascent()) / 2
        if (buttonTextAllCaps) {
            buttonText = buttonText.uppercase()
        }
        canvas.drawText(buttonText, textX, textY, textPaint)

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            canvas.drawDoubleRoundRect(
                buttonRect, buttonRadius, buttonRadius,
                buttonStrokeRect, buttonRadius * 0.75f, buttonRadius * 0.75f,
                strokePaint
            )
        }

    }
}