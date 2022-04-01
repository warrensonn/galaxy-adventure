package com.mygdx.myandroidgame.states.personalization

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.myandroidgame.managers.GameStateManager
import com.mygdx.myandroidgame.states.GameState
import com.mygdx.myandroidgame.states.MainMenu
import com.mygdx.myandroidgame.utils.Constants

class PersonalizationMenu(gsm: GameStateManager) : GameState(gsm) {

    private val background: Texture = Texture(Gdx.files.internal("backgrounds/background"+gsm.selectedBackground+".jpg"))
    private val custom: Texture = Texture(Gdx.files.internal("menu/custom.png"))
    private val returnTexture: Texture = Texture(Gdx.files.internal("menu/return.png"))
    private val characterTexture: Texture = Texture(Gdx.files.internal("menu/character.png"))
    private val backgroundTexture: Texture = Texture(Gdx.files.internal("menu/background.png"))
    private val platformTexture: Texture = Texture(Gdx.files.internal("menu/platform.png"))
    private val meteorTexture: Texture = Texture(Gdx.files.internal("menu/meteor.png"))

    private val customImageSize: Vector2 = Vector2(500f, 200f)
    private val customImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/2.1f - (customImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/2.5f-(customImageSize.y/2))

    private val returnImageSize: Vector2 = Vector2(100f, 100f)
    private val returnImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/4.2f - (returnImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/2.3f-(returnImageSize.y/2))

    private val characterImageSize: Vector2 = Vector2(350f, 150f)
    private val characterImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/3f - (characterImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/4.3f-(characterImageSize.y/2))

    private val backgroundImageSize: Vector2 = Vector2(350f, 150f)
    private val backgroundImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/1.6f - (backgroundImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/4.6f-(backgroundImageSize.y/2))

    private val platformImageSize: Vector2 = Vector2(350f, 150f)
    private val platformImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/3f - (platformImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/12f-(platformImageSize.y/2))

    private val meteorImageSize: Vector2 = Vector2(350f, 150f)
    private val meteorImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/1.6f - (backgroundImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/12f-(platformImageSize.y/2))

    override fun handleInput() {
        if(Gdx.input.isTouched) {
            val click = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            cam.unproject(click)

            val returnTextureBounds = Rectangle(returnImagePosition.x, returnImagePosition.y, returnImageSize.x, returnImageSize.y)
            val backgroundTextureBounds = Rectangle(backgroundImagePosition.x, backgroundImagePosition.y, backgroundImageSize.x, backgroundImageSize.y)
            val characterTextureBounds = Rectangle(characterImagePosition.x, characterImagePosition.y, characterImageSize.x, characterImageSize.y)
            val platformTextureBounds = Rectangle(platformImagePosition.x, platformImagePosition.y, platformImageSize.x, platformImageSize.y)
            val meteorTextureBounds = Rectangle(meteorImagePosition.x, meteorImagePosition.y, meteorImageSize.x, meteorImageSize.y)

            when {
                returnTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(MainMenu(gsm))
                } backgroundTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(BackgroundPersonnalization(gsm))
                } characterTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(CharacterPersonalization(gsm))
                } platformTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(PlatformPersonnalization(gsm))
                } meteorTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(MeteorPersonnalization(gsm))
                }
            }
        }
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render(sb: SpriteBatch) {
        sb.begin()

        sb.draw(background, -45f, -150f, Constants.WINDOWS_WIDTH.toFloat(), Constants.WINDOWS_HEIGHT.toFloat())
        sb.draw(custom, customImagePosition.x, customImagePosition.y, customImageSize.x, customImageSize.y)
        sb.draw(returnTexture, returnImagePosition.x, returnImagePosition.y, returnImageSize.x, returnImageSize.y)
        sb.draw(backgroundTexture, backgroundImagePosition.x, backgroundImagePosition.y, backgroundImageSize.x, backgroundImageSize.y)
        sb.draw(characterTexture, characterImagePosition.x, characterImagePosition.y, characterImageSize.x, characterImageSize.y)
        sb.draw(platformTexture, platformImagePosition.x, platformImagePosition.y, platformImageSize.x, platformImageSize.y)
        sb.draw(meteorTexture, meteorImagePosition.x, meteorImagePosition.y, meteorImageSize.x, meteorImageSize.y)

        sb.end()
    }

    override fun dispose() {
        background.dispose()
        custom.dispose()
        returnTexture.dispose()
        characterTexture.dispose()
        backgroundTexture.dispose()
        platformTexture.dispose()
    }

    init {
        cam.position.x = Constants.WINDOWS_WIDTH/2.1f
        cam.update()
    }
}