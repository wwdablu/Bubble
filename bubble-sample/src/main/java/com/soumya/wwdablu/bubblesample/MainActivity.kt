package com.soumya.wwdablu.bubblesample

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.soumya.wwdablu.bubble.Bubble
import com.soumya.wwdablu.bubble.BubbleView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bubbleView = findViewById<BubbleView>(R.id.bubble_view)
        val bubbles = ArrayList<Bubble>()

        //Red Bubble
        var bubble = Bubble()
        bubble.bubbleSpeed = 2
        bubble.bubbleColor = Color.parseColor("#88FF0000")
        bubble.bubbleHeight = 100
        bubble.bubbleWidth = 100
        bubble.createBubble()
        bubbles.add(bubble)

        //Green Bubble
        bubble = Bubble()
        bubble.bubbleSpeed = 2
        bubble.bubbleColor = Color.parseColor("#8800FF00")
        bubble.bubbleHeight = 150
        bubble.bubbleWidth = 150
        bubble.createBubble()
        bubbles.add(bubble)

        //Blue
        bubble = Bubble()
        bubble.bubbleSpeed = 2
        bubble.bubbleColor = Color.parseColor("#880000FF")
        bubble.bubbleHeight = 200
        bubble.bubbleWidth = 200
        bubble.createBubble()
        bubbles.add(bubble)

        //ABCDED
        bubble = Bubble()
        bubble.bubbleSpeed = 2
        bubble.bubbleColor = Color.parseColor("#88ABCDEF")
        bubble.bubbleHeight = 250
        bubble.bubbleWidth = 250
        bubble.createBubble()
        bubbles.add(bubble)

        bubbleView.setBubbles(bubbles)
    }
}
