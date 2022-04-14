package com.mygdx.myandroidgame.utils

import com.mygdx.myandroidgame.managers.GameStateManager
import com.mygdx.myandroidgame.states.levels.StateLevel2

object Constants {
    const val VIEWPORT_WIDTH = 720f
    const val VIEWPORT_HEIGHT = 1080f
    const val WINDOWS_WIDTH = 1920
    const val WINDOWS_HEIGHT = 1080
    const val GAME_TITLE = "Walking Man"
    const val GRAVITY = -100
    const val PPM = 32f // pixel per meter
    const val SCALE = 2.0f
    const val BIT_WALL: Short = 1
    const val BIT_INVISIBLE_WALL: Short = 32
    const val BIT_PLAYER: Short = 2
    const val BIT_SENSOR: Short = 4
    const val BIT_NOLIGHT: Short = 8
    const val BIT_BREAKABLE: Short = 16
    const val numberOfLevels: Int = 6
    const val playerWidth = 32
    const val playerHeight = 70
}