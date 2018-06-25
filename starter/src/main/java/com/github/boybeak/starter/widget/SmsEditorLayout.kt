package com.github.boybeak.starter.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.github.boybeak.starter.R

/**
 * Created by gaoyunfei on 2018/2/1.
 */

class SmsEditorLayout : FrameLayout, TextWatcher {

    private var mInputEt: AppCompatEditText? = null
    private var mHolderContainer: LinearLayout? = null

    private var mMaxLength = 6

//    private val mHolderStr = "—"

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initThis(context, attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initThis(context, attrs)
    }

    private fun initThis(context: Context, attrs: AttributeSet?) {

        mInputEt = AppCompatEditText(context, attrs)
        mInputEt!!.isCursorVisible = false
        mInputEt!!.setTextAppearance(getContext(), R.style.TextAppearance_AppCompat_Headline)
        mInputEt!!.setTextColor(Color.TRANSPARENT)
        mInputEt!!.setBackgroundColor(Color.TRANSPARENT)
        mInputEt!!.maxLines = 1
        mInputEt!!.gravity = Gravity.CENTER
        mInputEt!!.inputType = InputType.TYPE_CLASS_NUMBER

        mHolderContainer = LinearLayout(context, attrs)
        mHolderContainer!!.orientation = LinearLayout.HORIZONTAL
        mHolderContainer!!.gravity = Gravity.CENTER

        setMaxLength(mMaxLength)
        for (i in 0 until mMaxLength) {
            val params = LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            params.weight = 1f
            val v = makeHolderView()
            v.tag = i
            mHolderContainer!!.addView(v, params)
        }

        val holderParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        holderParams.gravity = Gravity.CENTER
        addView(mHolderContainer, holderParams)

        val inputParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        inputParams.gravity = Gravity.CENTER
        addView(mInputEt, inputParams)
    }

    fun setMaxLength(length: Int) {
        mMaxLength = length
        val array = arrayOfNulls<InputFilter>(1)
        array[0] = InputFilter.LengthFilter(mMaxLength)
        mInputEt!!.filters = array
    }

    fun getCodeText (): String {
        return mInputEt!!.text.toString()
    }

    private fun makeHolderView(): AppCompatTextView {
        val tv = SingleCodeView(context)
//        tv.text = mHolderStr
        tv.gravity = Gravity.CENTER
        tv.typeface = Typeface.DEFAULT_BOLD
        tv.setPadding(24, 0, 24, 0)
        tv.setTextAppearance(context, R.style.TextAppearance_AppCompat_Headline)
        tv.includeFontPadding = false
        tv.maxLines = 1
        return tv
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mInputEt!!.addTextChangedListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mInputEt!!.removeTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        val length = s.length
        val count = mHolderContainer!!.childCount
        for (i in 0 until count) {
            val tv = mHolderContainer!!.getChildAt(i) as AppCompatTextView
            if (i < length) {
                tv.text = s[i].toString()
            } else {
                tv.text = null
            }

        }
    }

    class SingleCodeView @JvmOverloads constructor(
            context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : AppCompatTextView(context, attrs, defStyleAttr) {

        private val rectF: RectF = RectF()
        private val holderPaint: Paint = Paint()

        init {
            holderPaint.color = Color.BLACK
            holderPaint.strokeWidth = 4f
            holderPaint.isAntiAlias = true
        }

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            if (text == null || text.isEmpty()) {
                val centerLine: Float = (height - paddingTop - paddingBottom).toFloat() / 2
                val round = 6f
                rectF.left = paddingStart.toFloat()
                rectF.top = centerLine - round
                rectF.right = (width - paddingEnd).toFloat()
                rectF.bottom = centerLine + round
                canvas!!.drawRoundRect(rectF, round, round, holderPaint)
            }
        }
    }
}
