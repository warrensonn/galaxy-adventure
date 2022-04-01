package com.mygdx.myandroidgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera
import com.mygdx.myandroidgame.managers.GameStateManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.myandroidgame.utils.Constants

abstract class GameState(gsm: GameStateManager) {

    protected var cam: OrthographicCamera
    protected var gsm: GameStateManager
    private val widthS: Float

    abstract fun handleInput() // Capter les évènements avec Gdx.input.

    abstract fun update(dt: Float) // Pour mettre à jour les données, à afficher ou à prendre en compte, aucun

    abstract fun render(sb: SpriteBatch) // Dessine les textures

    abstract fun dispose() // Libère la memoire

    open fun resize(width: Int, height: Int) {
        this.cam.setToOrtho(false, width / Constants.SCALE, height / Constants.SCALE)
    }

    init {
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        widthS = Constants.VIEWPORT_WIDTH
        cam = OrthographicCamera()
        cam.setToOrtho(false, w/2, h/2)
        this.gsm = gsm
    }
}
