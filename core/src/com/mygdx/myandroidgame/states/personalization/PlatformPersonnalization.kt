package com.mygdx.myandroidgame.states.personalization

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.myandroidgame.managers.GameStateManager
import com.mygdx.myandroidgame.states.GameState
import com.mygdx.myandroidgame.utils.Constants

class PlatformPersonnalization(gsm: GameStateManager) : GameState(gsm) {
    private val platformTexture: Texture = Texture(Gdx.files.internal("menu/platformTitle.png"))
    private val platformImageSize: Vector2 = Vector2(500f, 200f)
    private val platformImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/2.1f - (platformImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/2.5f-(platformImageSize.y/2))

    private val returnTexture: Texture = Texture(Gdx.files.internal("menu/return.png"))
    private val returnImageSize: Vector2 = Vector2(100f, 100f)
    private val returnImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/4.2f - (returnImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/2.3f-(returnImageSize.y/2))

    private val airPlatform: Texture = Texture(Gdx.files.internal("menu/air.png"))
    private val airImageSize: Vector2 = Vector2(200f, 200f)
    private val airImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH / 4.5f,
            Constants.WINDOWS_HEIGHT/6f)

    private val groundPlatform: Texture = Texture(Gdx.files.internal("menu/ground.png"))
    private val groundImageSize: Vector2 = Vector2(200f, 200f)
    private val groundImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH / 4.5f,
            Constants.WINDOWS_HEIGHT/40f)

    private val platformDisplayedSize: Vector2 = Vector2(100f, 10f)
    private val platformDisplayedPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/4f,
            Constants.WINDOWS_HEIGHT/4.5f)

    private val platformHighlighter: Texture = Texture(Gdx.files.internal("menu/selectHighlight.png"))
    private val highlighterSize: Vector2 = Vector2(70f, 8f)
    private val highlighterPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/2.84f,
            Constants.WINDOWS_HEIGHT/5f)

    private val returnTextureBounds : Rectangle = Rectangle(returnImagePosition.x, returnImagePosition.y, returnImageSize.x, returnImageSize.y)

    private var airBoundsHashMap : HashMap<Int, Rectangle> = HashMap()
    private var airImages : HashMap<Int, Texture> = HashMap()

    private var groundBoundsHashMap : HashMap<Int, Rectangle> = HashMap()
    private var groundImages : HashMap<Int, Texture> = HashMap()

    private var distanceBetween2Images : Vector2 = Vector2(180f, 135f)

    override fun handleInput() {
        if(Gdx.input.isTouched) {
            val click = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            cam.unproject(click)

            when {
                returnTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(PersonalizationMenu(gsm))
                }
            }
            for (i in 1..gsm.platformsAvailable) {
                when {
                    airBoundsHashMap[i]!!.contains(click.x, click.y) -> {
                        gsm.selectedAirPlatform = i-1
                    } groundBoundsHashMap[i]!!.contains(click.x, click.y) -> {
                    gsm.selectedGroundPlatform = i-1
                    }
                }
            }

        }
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render(sb: SpriteBatch) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.begin()

        sb.draw(platformTexture, platformImagePosition.x, platformImagePosition.y, platformImageSize.x, platformImageSize.y)
        sb.draw(returnTexture, returnImagePosition.x, returnImagePosition.y, returnImageSize.x, returnImageSize.y)
        sb.draw(airPlatform, airImagePosition.x, airImagePosition.y, airImageSize.x, airImageSize.y)
        sb.draw(groundPlatform, groundImagePosition.x, groundImagePosition.y, groundImageSize.x, groundImageSize.y)

        for (i in 1..gsm.platformsAvailable) {
            sb.draw(airImages[i], platformDisplayedPosition.x + (i*distanceBetween2Images.x), platformDisplayedPosition.y, platformDisplayedSize.x, platformDisplayedSize.y)
            sb.draw(groundImages[i], platformDisplayedPosition.x + (i*distanceBetween2Images.x), platformDisplayedPosition.y - distanceBetween2Images.y, platformDisplayedSize.x, platformDisplayedSize.y)
        }

        sb.draw(platformHighlighter, highlighterPosition.x + (gsm.selectedAirPlatform*distanceBetween2Images.x), highlighterPosition.y/1, highlighterSize.x, highlighterSize.y)
        sb.draw(platformHighlighter, highlighterPosition.x + (gsm.selectedGroundPlatform*distanceBetween2Images.x), highlighterPosition.y-distanceBetween2Images.y, highlighterSize.x, highlighterSize.y)

        sb.end()
    }

    override fun dispose() {
        returnTexture.dispose()
        platformTexture.dispose()
        airPlatform.dispose()
        groundPlatform.dispose()

        for (i in 1..gsm.platformsAvailable) {
            groundImages[i]?.dispose()
            airImages[i]?.dispose()
        }
    }

    init {
        cam.position.x = Constants.WINDOWS_WIDTH/2.1f
        cam.update()

        for (i in 1..gsm.platformsAvailable) {
            airBoundsHashMap[i] = Rectangle(platformDisplayedPosition.x + (i*distanceBetween2Images.x), platformDisplayedPosition.y, platformDisplayedSize.x, platformDisplayedSize.y+20)
            groundBoundsHashMap[i] = Rectangle(platformDisplayedPosition.x + (i*distanceBetween2Images.x), platformDisplayedPosition.y - distanceBetween2Images.y, platformDisplayedSize.x, platformDisplayedSize.y+20)

            groundImages[i] = Texture(Gdx.files.internal("grounds/ground${i-1}.png"))
            airImages[i] = Texture(Gdx.files.internal("grounds/air${i-1}.png"))
        }
    }
}