package com.mygdx.myandroidgame.managers

import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.input.GestureDetector.GestureAdapter
import kotlin.math.abs


class SimpleDirectionGestureDetector(directionListener: DirectionListener) : GestureDetector(DirectionGestureListener(directionListener)) {
    interface DirectionListener {
        fun onLeft()
        fun onRight()
        fun onUp()
        fun onDown()
    }

    private class DirectionGestureListener(var directionListener: DirectionListener) : GestureAdapter() {
        override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
            if (abs(velocityX) > abs(velocityY)) {
                if (velocityX > 0) {
                    directionListener.onRight()
                    print("bonjour")
                } else {
                    directionListener.onLeft()
                    print("bonjour")
                }
            } else {
                if (velocityY > 0) {
                    directionListener.onDown()
                    print("bonjour")
                } else {
                    directionListener.onUp()
                    print("bonjour")
                }
            }
            return super.fling(velocityX, velocityY, button)
        }
    }
}