package com.github.boybeak.de

import android.content.Context
import android.support.annotation.IntDef
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.OverScroller

class DragExitLayout : FrameLayout {

    companion object {
        private val TAG = DragExitLayout::class.java.simpleName

        const val EDGE_LEFT = -1
        const val EDGE_RIGHT = 1
        const val EDGE_BOTH = 0

        private const val LAZY_RATIO_DEFAULT = 1f
        private const val EXIT_LINE_RATION_DEFAULT = 0.25f
        private const val ALPHA_MIN_DEFAULT = 0.8f
        private const val SCALE_MIN_DEFAULT = 0.8f
    }

    @IntDef(EDGE_LEFT, EDGE_RIGHT, EDGE_BOTH)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Edge

    private var edgeSlop = 0
    private var mTouchSlop: Int = 0
    private var interceptDownX = 0f

    private var isInDragMode = false

    private var lastTouchDownX = 0f
    private var lastTouchMoveX = 0f
    private var deltaTouchX = 0f

    private var exitLineRatio = EXIT_LINE_RATION_DEFAULT
    private var lazyRatio = 1f

    private val scroller: OverScroller = OverScroller(context)
//    private var velocityTracker : VelocityTracker? = null

    private var isFling = false

    private var stuckEffect = false
    private var edges = EDGE_BOTH

    private var alphaEffect = false
    private var alphaMin = ALPHA_MIN_DEFAULT
    private var alphaUnit = 0f

    private var scaleEffect = false
    private var scaleMin = SCALE_MIN_DEFAULT
    private var scaleUnit = 0f

    private var lastTransX = 0f

    private var exitListener: OnExitListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initThis(context, attrs)
    }

    private fun initThis(context: Context, attrs: AttributeSet?) {

        val configuration = ViewConfiguration.get(context)
        edgeSlop = configuration.scaledEdgeSlop
        mTouchSlop = configuration.scaledTouchSlop

        val ta = context.obtainStyledAttributes(attrs, R.styleable.DragExitLayout)
        try {
            setActiveEdges(ta.getInt(R.styleable.DragExitLayout_activeEdges, EDGE_BOTH))
            setExitLineRatio(ta.getFloat(R.styleable.DragExitLayout_exitLineRatio, EXIT_LINE_RATION_DEFAULT))
            setLazy(ta.getFloat(R.styleable.DragExitLayout_lazy, LAZY_RATIO_DEFAULT))

            setStuckEffect(ta.getBoolean(R.styleable.DragExitLayout_stuckEffect, false))

            setAlphaEffect(ta.getBoolean(R.styleable.DragExitLayout_alphaEffect, false))
            setAlphaMin(ta.getFloat(R.styleable.DragExitLayout_alphaMin, ALPHA_MIN_DEFAULT))

            setScaleEffect(ta.getBoolean(R.styleable.DragExitLayout_scaleEffect, false))
            setScaleMin(ta.getFloat(R.styleable.DragExitLayout_scaleMin, SCALE_MIN_DEFAULT))

        } finally {
            ta.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculateAlphaUnit()
        calculateScaleUtil()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        checkChildCount()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val result = true
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                interceptDownX = ev.rawX
                if (!isFling and isValidDownX(interceptDownX)) {
                    isInDragMode = true
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
//                if (velocityTracker == null) {
//                    velocityTracker = VelocityTracker.obtain()
//                } else {
//                    velocityTracker!!.clear()
//                }
//                velocityTracker!!.addMovement(event)
                val x = event.rawX
                if (isFling) {
                    return false
                }
                if (!isInEdgeArea(x)) {
                    return false
                }
                if (isExitAtLeft() && isRightEdgeArea()) {
                    return false
                }
                if (isExitAtRight() && isLeftEdgeArea()) {
                    return false
                }

                if (isInDragMode) {
                    lastTouchDownX = x
                    lastTouchMoveX = lastTouchDownX

                    lastTransX = theOne().translationX

                    return true
                }

            }
            MotionEvent.ACTION_MOVE -> {
//                velocityTracker!!.addMovement(event)
//                velocityTracker!!.computeCurrentVelocity(1000)
                if (isInDragMode) {
                    /*float x = event.getRawX();
                    if (x - mDispatchDownX > mTouchSlop) {
                        Log.v(TAG, "onTouchEvent ACTION_MOVE in slop");
                        return true;
                    }*/
                    val x = event.rawX
                    val deltaX = x - lastTouchMoveX
                    val newTransX = theOne().translationX + deltaX * lazyRatio
                    setTheOneTranslationX(getTranslationXForLazy(newTransX))
                    lastTouchMoveX = x
                    deltaTouchX = deltaX
                    return true
                }
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
//                Log.v(TAG, "END SPEED = ${velocityTracker!!.xVelocity}")
//                velocityTracker!!.clear()
//                Log.v(TAG, "ACTION_UP or ACTION_CANCEL")
                val transX = theOne().translationX.toInt()
                if (isCancelGesture()) {
                    scroller.startScroll(transX, 0, -transX, 0)
                } else {
                    if (isLeftEdgeArea()) {
                        val endTransX = if (transX < 0) {
                            0
                        } else {
                            width
                        }
                        scroller.startScroll(transX, 0, endTransX - transX, 0)
                    } else if (isRightEdgeArea()) {
                        val endTransX = if (transX > 0) {
                            0
                        } else {
                            -width
                        }
                        scroller.startScroll(transX, 0, endTransX - transX, 0)
                    }
                }
                ViewCompat.postInvalidateOnAnimation(this)
                isInDragMode = false
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        checkChildCount()
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            if (!isFling) {
                isFling = true
                //TODO onStartFling
            }
            setTheOneTranslationX(scroller.currX.toFloat())
            postInvalidate()
        } else {
            if (isFling) {
                isFling = false
                //TODO onStopFling
                if (Math.abs(theOne().translationX).toInt() == width) {
                    exitListener?.onExit()
                }
            }
        }
    }

    private fun checkChildCount() {
        if (childCount > 1) {
            throw IllegalStateException(this.javaClass.name + " can accept only direct child view")
        }
    }

    private fun isInEdgeArea(x: Float = interceptDownX): Boolean {
        return isLeftEdgeArea(x) || isRightEdgeArea(x)
    }

    private fun isLeftEdgeArea(x: Float = interceptDownX): Boolean {
        return x <= edgeSlop
    }

    private fun isRightEdgeArea(x: Float = interceptDownX): Boolean {
        return x >= width - edgeSlop
    }

    private fun isValidDownX (x: Float): Boolean {
        return when(edges) {
            EDGE_BOTH -> {
                isInEdgeArea(x)
            }
            EDGE_LEFT -> {
                isLeftEdgeArea(x)
            }
            EDGE_RIGHT -> {
                isRightEdgeArea(x)
            }
            else -> {
                isInEdgeArea(x)
            }
        }
    }

    private fun isCancelGesture(): Boolean {
        val xOffset = lastTouchMoveX - lastTouchDownX
        return if (deltaTouchX == 0f) {
            Math.abs(xOffset) < getExitLineSize()
        } else {
            xOffset / deltaTouchX < 0
        }
    }

    private fun isExitAtLeft(): Boolean {
        return theOne().translationX.toInt() == -width
    }

    private fun isExitAtRight(): Boolean {
        return theOne().translationX.toInt() == width
    }

    private fun getExitLineSize(): Float {
        return width * exitLineRatio
    }

    private fun getTranslationXForLazy(newTransX: Float): Float {
        val position1 = getExitLineSize()
        val position2 = width - getExitLineSize()
        val transX = theOne().translationX
        return if (stuckEffect) {
            when {
                -position1 in newTransX..transX -> -position1
                position1 in transX..newTransX -> position1
                -position2 in transX..newTransX -> -position2
                position2 in newTransX..transX -> position2
                else -> newTransX
            }
        } else {
            newTransX
        }
    }

    private fun theOne(): View {
        return getChildAt(0)
    }

    private fun setTheOneTranslationX(transX: Float) {
        theOne().translationX = transX
        onTheOneTransXChanged(transX)
        lastTransX = transX
    }

    private fun onTheOneTransXChanged(transX: Float) {
        val deltaTransX = transX - lastTransX
        if (alphaEffect) {
            when {
                transX == 0f -> theOne().alpha = 1f
                (transX == -width.toFloat()) or (transX == width.toFloat()) -> theOne().alpha = alphaMin
                else -> {
                    val newAlpha = if (transX > 0) {
                        theOne().alpha - deltaTransX * alphaUnit
                    } else {
                        theOne().alpha + deltaTransX * alphaUnit
                    }
                    theOne().alpha = Math.max(alphaMin, Math.min(1f, newAlpha))
                }
            }
        }
        if (scaleEffect) {
            when {
                transX == 0f -> {
                    theOne().scaleX = 1f
                    theOne().scaleY = 1f
                }
                (transX == -width.toFloat()) or (transX == width.toFloat()) -> {
                    theOne().scaleX = scaleMin
                    theOne().scaleY = scaleMin
                }
                else -> {
                    val newScale = if (transX > 0) {
                        theOne().scaleX - deltaTransX * scaleUnit
                    } else {
                        theOne().scaleX + deltaTransX * scaleUnit
                    }
                    val scale = Math.max(scaleMin, Math.min(1f, newScale))
                    theOne().scaleX = scale
                    theOne().scaleY = scale
                }
            }
        }
    }

    private fun calculateAlphaUnit() {
        alphaUnit = (1f - alphaMin) / getExitLineSize()
    }

    private fun calculateScaleUtil() {
        scaleUnit = (1f - scaleMin) / getExitLineSize()
    }

    fun setActiveEdges(@Edge edge: Int) {
        this.edges = edge
    }

    fun setExitLineRatio(ratio: Float) {
        if (ratio <= 0f || ratio >= 1f) {
            throw IllegalArgumentException("ratio can not be <= 0f nor >= 1f")
        }
        exitLineRatio = ratio

        calculateAlphaUnit()
        calculateScaleUtil()
    }

    fun setStuckEffect(stuckEffect: Boolean) {
        this.stuckEffect = stuckEffect
    }

    fun setAlphaEffect(alphaEffect: Boolean) {
        this.alphaEffect = alphaEffect
    }

    fun setAlphaMin(alphaMin: Float) {
        if (alphaMin < 0f || alphaMin > 1f) {
            throw IllegalArgumentException("alphaMin can not be < 0f nor > 1f")
        }

        this.alphaMin = alphaMin

        calculateAlphaUnit()
    }

    fun setScaleEffect(scaleEffect: Boolean) {
        this.scaleEffect = scaleEffect
    }

    fun setScaleMin(scaleMin: Float) {
        if (scaleMin < 0f || scaleMin > 1f) {
            throw IllegalArgumentException("scaleMin can not be < 0f nor > 1f")
        }

        this.scaleMin = scaleMin

        calculateScaleUtil()
    }

    fun setLazy(lazy: Float) {
        if (lazy <= 0f || lazy > 1f) {
            throw IllegalArgumentException("lazy can not be <= 0f nor > 1f")
        }
        lazyRatio = lazy
    }

    fun getLazy(): Float {
        return lazyRatio
    }

    fun setOnExitListener (listener: OnExitListener) {
        exitListener = listener
    }

    interface OnExitListener {
        fun onExit()
    }

}
