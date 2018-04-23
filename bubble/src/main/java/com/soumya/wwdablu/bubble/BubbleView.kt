package com.soumya.wwdablu.bubble

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class BubbleView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private enum class Direction {
        TopLeft, TopRight, BottomLeft, BottomRight
    }

    private val mBubblePainter: Paint
    private val mBubbleAttributes: BubbleAttributes = BubbleAttributes(context, attributeSet)

    private var mBubbles: ArrayList<Bitmap> = ArrayList()
    private var mBubblePoints: ArrayList<Point> = ArrayList()
    private var mBubbleDirection: ArrayList<Direction> = ArrayList()
    private var mBubbleSpeed: ArrayList<Int> = ArrayList()
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0

    init {
        mBubbles.add(createBubble(mBubbleAttributes.bubbleWidth, mBubbleAttributes.bubbleHeight))
        mBubblePoints.add(Point(0,0))
        mBubbleDirection.add(Direction.BottomRight)
        mBubbleSpeed.add(5)
        mBubblePainter = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mViewWidth = w
        mViewHeight = h
    }

    override fun onDraw(canvas: Canvas?) {

        //Incase of invalid canvas return
        canvas?: return

        canvas.drawBitmap(mBubbles[0], mBubblePoints[0].x.toFloat(), mBubblePoints[0].y.toFloat(), mBubblePainter)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        destroy()
    }

    private fun startBubbleEngine() {

        Observable.interval(33L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
            .subscribeWith(object : DisposableObserver<Long>() {
                override fun onComplete() {
                    //
                }

                override fun onNext(t: Long) {
                    val direction = whichDirectionToMoveFor(0)
                    mBubbleDirection[0] = direction

                    when(direction) {
                        Direction.TopLeft -> {
                            mBubblePoints[0].x -= mBubbleSpeed[0]
                            mBubblePoints[0].y -= mBubbleSpeed[0]
                        }
                        Direction.TopRight -> {
                            mBubblePoints[0].x += mBubbleSpeed[0]
                            mBubblePoints[0].y -= mBubbleSpeed[0]
                        }
                        Direction.BottomRight -> {
                            mBubblePoints[0].x += mBubbleSpeed[0]
                            mBubblePoints[0].y += mBubbleSpeed[0]
                        }
                        Direction.BottomLeft -> {
                            mBubblePoints[0].x -= mBubbleSpeed[0]
                            mBubblePoints[0].y += mBubbleSpeed[0]
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

        val bitmapWidth = mBubbles[bubbeIndex].width
        val bitmapHeight = mBubbles[bubbeIndex].height

        val lastPoint: Point = mBubblePoints[bubbeIndex]
        var moveDirection: Direction = mBubbleDirection[bubbeIndex]

        //First check if the bitmap has reached the boundary based on the current direction
        if(!hasReachedBoundaryFor(moveDirection, lastPoint, bitmapWidth, bitmapHeight)) {
            return moveDirection
        }

        moveDirection = Direction.values()[randomizeDirection(0, 4)]

        Log.d("TAG", "Direction: " + moveDirection.toString())
        return moveDirection
    }

    fun randomizeDirection(from: Int, to: Int) : Int {
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

    private fun createBubble(width:Int, height:Int): Bitmap {

        val bubble = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bubble.setHasAlpha(true)

        var bubbleRadius = height/2F
        if(width < height) {
            bubbleRadius = width/2F
        }

        val canvas = Canvas(bubble)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#88FFFFFF")
        canvas.drawCircle((width/2).toFloat(), (height/2).toFloat(), bubbleRadius, paint)

        return bubble
    }

    private fun destroy() {
        for (bubble in mBubbles) {
            bubble.recycle()
        }

        mBubbles.clear()
    }

    init {
        startBubbleEngine()
    }
}