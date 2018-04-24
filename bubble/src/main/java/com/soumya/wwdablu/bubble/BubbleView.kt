package com.soumya.wwdablu.bubble

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class BubbleView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val MAX_BUBBLE_COUNT = 10

    private val mBubblePainter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mBubbles: ArrayList<Bubble> = ArrayList()
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0

    internal var bubbleCount: Int = 1
    set(value) {
        field = when (value) {
            in 1..MAX_BUBBLE_COUNT -> {
                value
            }
            else -> MAX_BUBBLE_COUNT
        }
    }

    internal var refreshTime: Long = 1
    set(value) {
        if(value <= 0) {
            field = 33
        } else {
            field = value
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BubbleView)
        bubbleCount = typedArray.getInt(R.styleable.BubbleView_bubbleCount, 1)
        refreshTime = typedArray.getInt(R.styleable.BubbleView_refreshTime, 33).toLong()
        typedArray.recycle()

        for(i in 0 until bubbleCount) {
            mBubbles.add(Bubble(context, attributeSet))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mViewWidth = w
        mViewHeight = h

        for(i in 0 until bubbleCount) {
            mBubbles[i].currentPoint.x = getRandomNumberBetween(0, w)
            mBubbles[i].currentPoint.y = getRandomNumberBetween(0, h)
        }
    }

    override fun onDraw(canvas: Canvas?) {

        //Incase of invalid canvas return
        canvas?: return

        for(i in 0 until bubbleCount) {
            canvas.drawBitmap(mBubbles[i].bubble, mBubbles[i].currentPoint.x.toFloat(),
                    mBubbles[i].currentPoint.y.toFloat(), mBubblePainter)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        destroy()
    }

    fun setBubbles(bubbleArray: ArrayList<Bubble>) {

        destroy()
        bubbleCount = if (MAX_BUBBLE_COUNT < bubbleArray.size) MAX_BUBBLE_COUNT else bubbleArray.size
        for(i in 0 until bubbleCount) {
            mBubbles.add(bubbleArray[i])
        }
    }

    private fun startBubbleEngine() {

        Observable.interval(refreshTime, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
            .subscribeWith(object : DisposableObserver<Long>() {
                override fun onComplete() {
                    //
                }

                override fun onNext(t: Long) {

                    //If 0, then do not draw
                    if(mBubbles.size == 0) {
                        return
                    }

                    for (i in 0 until bubbleCount) {

                        val direction = whichDirectionToMoveFor(i)
                        mBubbles[i].direction = direction

                        when (direction) {
                            Direction.TopLeft -> {
                                mBubbles[i].currentPoint.x -= mBubbles[i].bubbleSpeed
                                mBubbles[i].currentPoint.y -= mBubbles[i].bubbleSpeed
                            }
                            Direction.TopRight -> {
                                mBubbles[i].currentPoint.x += mBubbles[i].bubbleSpeed
                                mBubbles[i].currentPoint.y -= mBubbles[i].bubbleSpeed
                            }
                            Direction.BottomRight -> {
                                mBubbles[i].currentPoint.x += mBubbles[i].bubbleSpeed
                                mBubbles[i].currentPoint.y += mBubbles[i].bubbleSpeed
                            }
                            Direction.BottomLeft -> {
                                mBubbles[i].currentPoint.x -= mBubbles[i].bubbleSpeed
                                mBubbles[i].currentPoint.y += mBubbles[i].bubbleSpeed
                            }
                        }
                    }

                    invalidate()
                }

                override fun onError(e: Throwable) {
                    //
                }
            })
    }

    private fun whichDirectionToMoveFor(bubbeIndex: Int): Direction {

        val bitmapWidth = mBubbles[bubbeIndex].bubbleWidth
        val bitmapHeight = mBubbles[bubbeIndex].bubbleHeight

        val lastPoint: Point = mBubbles[bubbeIndex].currentPoint
        var moveDirection: Direction = mBubbles[bubbeIndex].direction

        //First check if the bitmap has reached the boundary based on the current direction
        if(!hasReachedBoundaryFor(moveDirection, lastPoint, bitmapWidth, bitmapHeight)) {
            return moveDirection
        }

        moveDirection = Direction.values()[getRandomNumberBetween(0, 4)]
        return moveDirection
    }

    fun getRandomNumberBetween(from: Int, to: Int) : Int {
        return Random().nextInt(to - from) + from
    }

    private fun hasReachedBoundaryFor(direction: Direction, lastPoint: Point, bmpWidth: Int, bmpHeight: Int): Boolean {

        var onBoundary = false
        when(direction) {
            Direction.TopLeft -> {
                if(lastPoint.x <= 0 || lastPoint.y <= 0) {
                    onBoundary = true
                }
            }
            Direction.BottomLeft -> {
                if(lastPoint.x <= 0 || lastPoint.y + bmpHeight > mViewHeight) {
                    onBoundary = true
                }
            }
            Direction.BottomRight -> {
                if(lastPoint.x + bmpWidth > mViewWidth || lastPoint.y + bmpHeight > mViewHeight) {
                    onBoundary = true
                }
            }
            Direction.TopRight -> {
                if(lastPoint.x + bmpWidth > mViewWidth || lastPoint.y <= 0) {
                    onBoundary = true
                }
            }
        }

        return onBoundary
    }

    private fun destroy() {
        for (bubble in mBubbles) {
            bubble.bubble?.recycle()
        }

        mBubbles.clear()
    }

    init {
        startBubbleEngine()
    }
}