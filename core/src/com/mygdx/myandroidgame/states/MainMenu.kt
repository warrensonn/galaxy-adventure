package com.mygdx.myandroidgame.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.myandroidgame.managers.GameStateManager
import com.mygdx.myandroidgame.utils.Constants
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.mygdx.myandroidgame.states.personalization.PersonalizationMenu

class MainMenu(gsm: GameStateManager) : GameState(gsm) {

    // Textures
    private val background: Texture = Texture(Gdx.files.internal("backgrounds/background"+gsm.selectedBackground+".jpg"))
    private val playTexture: Texture = Texture(Gdx.files.internal("menu/Play.jpg"))
    private val customTexture: Texture = Texture(Gdx.files.internal("menu/custom.png"))
    private val levelSelectTexture: Texture = Texture(Gdx.files.internal("menu/levelSelect.jpg"))

    private val playImageSize: Vector2 = Vector2(300f, 90f)
    private val playImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/2.1f - (playImageSize.x/2.1f),
                                                     Constants.WINDOWS_HEIGHT/8f-(playImageSize.y/2))

    private val customImageSize: Vector2 = Vector2(250f, 150f)
    private val customImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/3.5f-(customImageSize.x/2.1f),
                                                       Constants.WINDOWS_WIDTH/4.5f-(customImageSize.y/2))

    private val levelImageSize: Vector2 = Vector2(300f, 150f)
    private val levelImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/1.5f-(levelImageSize.x/2),
                                                       Constants.WINDOWS_WIDTH/4.5f-(levelImageSize.y/2))
    // FA55E9 -> couleur écriture
    // 85 -> taille ecriture


    override fun handleInput() {
        if(Gdx.input.isTouched) {
            val click  = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            cam.unproject(click);
            val playTextureBounds = Rectangle(playImagePosition.x, playImagePosition.y, playImageSize.x, playImageSize.y);
            val customTextureBounds = Rectangle(customImagePosition.x, customImagePosition.y, customImageSize.x, customImageSize.y)
            val levelTextureBounds = Rectangle(levelImagePosition.x, levelImagePosition.y, levelImageSize.x, levelImageSize.y)

            when {
                playTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(gsm.switchGameState(gsm.stateLevelNumber))
                }
                customTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(PersonalizationMenu(gsm))
                }
                levelTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(LevelSelect(gsm))
                }
            }
        }
    }

    override fun update(dt: Float) {
        handleInput()
        // cam.position.x = Constants.WINDOWS_WIDTH/2.1f
        // cam.update()
    }

    override fun render(sb: SpriteBatch) {
        sb.projectionMatrix = cam.combined
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        sb.begin()
        sb.draw(background, -45f, -150f, Constants.WINDOWS_WIDTH.toFloat(), Constants.WINDOWS_HEIGHT.toFloat());
        sb.draw(playTexture, playImagePosition.x, playImagePosition.y, playImageSize.x, playImageSize.y)
        sb.draw(customTexture, customImagePosition.x, customImagePosition.y, customImageSize.x, customImageSize.y)
        sb.draw(levelSelectTexture, levelImagePosition.x, levelImagePosition.y, levelImageSize.x, levelImageSize.y)
        sb.end()
    }

    override fun dispose() {
        background.dispose()
        playTexture.dispose()
        customTexture.dispose()
        levelSelectTexture.dispose()
    }

    init {
        cam.position.x = Constants.WINDOWS_WIDTH/2.1f
        cam.update()
    }
}