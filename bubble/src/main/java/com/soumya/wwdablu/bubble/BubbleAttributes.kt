package com.soumya.wwdablu.bubble

import android.content.Context
import android.util.AttributeSet

class BubbleAttributes internal constructor(context: Context, attributeSet: AttributeSet) {

    private val MAX_BUBBLE_COUNT = 5
    private val MAX_BUBBLE_SPEED = 5

    internal var bubbleCount: Int = 1
    internal var bubbleSpeed: Int = 1
    internal var bubbleWidth: Int = 100
    internal var bubbleHeight: Int = 100

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BubbleView)

        //Read bubble count value
        bubbleCount = typedArray.getResourceId(R.styleable.BubbleView_bubbleColor, 1)
        bubbleCount = when (bubbleCount) {
            in 1..MAX_BUBBLE_COUNT -> {
                bubbleCount
            }
            else -> MAX_BUBBLE_COUNT
        }

        //Read bubble speed
        bubbleSpeed = typedArray.getResourceId(R.styleable.BubbleView_bubbleSpeed, 1)
        bubbleSpeed = when (bubbleSpeed) {
            in 1..MAX_BUBBLE_SPEED -> {
                bubbleSpeed
            }
            else -> MAX_BUBBLE_SPEED
        }

        //Read bubble width
        bubbleWidth = typedArray.getResourceId(R.styleable.BubbleView_bubbleWidth, 100)
        if (bubbleWidth < 10) {
            bubbleWidth = 10
        }

        //Read bubble height
        bubbleHeight = typedArray.getResourceId(R.styleable.BubbleView_bubbleHeight, 100)
        if (bubbleHeight < 10) {
            bubbleHeight = 10
        }

        typedArray.recycle()
    }
}