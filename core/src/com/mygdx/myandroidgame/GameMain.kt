package com.mygdx.myandroidgame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.myandroidgame.managers.GameStateManager
import com.mygdx.myandroidgame.managers.SimpleDirectionGestureDetector
import com.mygdx.myandroidgame.managers.SimpleDirectionGestureDetector.DirectionListener
import com.mygdx.myandroidgame.states.EndOfLevelState
import com.mygdx.myandroidgame.states.MainMenu


class GameMain : ApplicationAdapter() {
    private lateinit var sb: SpriteBatch
    private lateinit var gsm: GameStateManager

    override fun create() {
        sb = SpriteBatch()
        gsm = GameStateManager()
        gsm.push(MainMenu(gsm))

        Gdx.input.inputProcessor = SimpleDirectionGestureDetector(object : DirectionListener {
            override fun onUp() {
                print("bonjour")
            }
            override fun onRight() {
                print("bonjour")
            }
            override fun onLeft() {
                print("bonjour")
            }
            override fun onDown() {
                print("bonjour")
            }
        })

    }

    override fun render() {
        //Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gsm.update(Gdx.graphics.deltaTime)
        gsm.render(sb)
    }

    override fun dispose() {
        sb.dispose()
    }
}