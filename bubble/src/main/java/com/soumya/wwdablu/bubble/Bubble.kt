package com.soumya.wwdablu.bubble

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

class Bubble {

    private val MAX_BUBBLE_SPEED = 5

    internal val currentPoint: Point = Point(0, 0)
    internal var direction: Direction = Direction.BottomRight

    internal var bubble: Bitmap? = null

    var bubbleSpeed: Int = 1
    set(value) {
        field = when (value) {
            in 1..MAX_BUBBLE_SPEED -> {
                value
            }
            else -> MAX_BUBBLE_SPEED
        }
    }

    var bubbleWidth: Int = 100
    set(value) {
        field = if (value < 10) 10 else value
    }

    var bubbleHeight: Int = 100
    set(value) {
        field = if (value < 10) 10 else value
    }

    var bubbleColor: Int = Color.parseColor("#88FFFFFF")

    internal constructor(context: Context, attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BubbleView)

        //Read bubble speed
        bubbleSpeed = typedArray.getInt(R.styleable.BubbleView_bubbleSpeed, 1)

        //Read bubble width
        bubbleWidth = typedArray.getDimensionPixelSize(R.styleable.BubbleView_bubbleWidth, 100)

        //Read bubble height
        bubbleHeight = typedArray.getDimensionPixelSize(R.styleable.BubbleView_bubbleHeight, 100)

        bubbleColor = typedArray.getColor(R.styleable.BubbleView_bubbleColor, Color.WHITE)

        //Be a good citizen and recycle
        typedArray.recycle()

        //Create the bubble based on the data provided
        createBubble()
    }

    constructor() {
        //
    }

    fun createBubble() {

        bubble = Bitmap.createBitmap(bubbleWidth, bubbleHeight, Bitmap.Config.ARGB_8888)

        var bubbleRadius = bubbleHeight/2F
        if(bubbleWidth < bubbleHeight) {
            bubbleRadius = bubbleWidth/2F
        }

        val canvas = Canvas(bubble)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = bubbleColor
        canvas.drawCircle((bubbleWidth/2).toFloat(), (bubbleHeight/2).toFloat(), bubbleRadius, paint)
    }
}