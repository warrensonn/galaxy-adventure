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
import com.mygdx.myandroidgame.states.MainMenu
import com.mygdx.myandroidgame.utils.Constants

class BackgroundPersonnalization(gsm: GameStateManager) : GameState(gsm) {

    private val backgroundTexture: Texture = Texture(Gdx.files.internal("menu/backgroundTitle.png"))
    private val returnTexture: Texture = Texture(Gdx.files.internal("menu/return.png"))
    private val backgroundHighlighter: Texture = Texture(Gdx.files.internal("menu/selectHighlight.png"))
    private val backgroundImages: HashMap<Int, Texture> = HashMap()

    private val backgroundImageSize: Vector2 = Vector2(500f, 200f)
    private val backgroundImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/2.1f - (backgroundImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/2.5f-(backgroundImageSize.y/2))

    private val returnImageSize: Vector2 = Vector2(100f, 100f)
    private val returnImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/4.2f - (returnImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/2.3f-(returnImageSize.y/2))

    private val backgroundDisplayedSize: Vector2 = Vector2(400f, 225f)
    private val backgroundDisplayedPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH / 4.3f,
            Constants.WINDOWS_HEIGHT / 40f)

    private val highlighterSize: Vector2 = Vector2(100f, 10f)
    private val highlighterPosition: Vector2 = Vector2((Constants.WINDOWS_WIDTH / 3.23f),
            (Constants.WINDOWS_HEIGHT / 80f))

    private var backgroundsBoundsHashMap : HashMap<Int, Rectangle> = HashMap<Int, Rectangle>()

    private var distanceBetween2Images : Vector2 = Vector2(Constants.WINDOWS_WIDTH / 3.5f, 150f)


    override fun handleInput() {
        if(Gdx.input.isTouched) {
            val click = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            cam.unproject(click)

            val returnTextureBounds = Rectangle(returnImagePosition.x, returnImagePosition.y, returnImageSize.x, returnImageSize.y)

            when {
                returnTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(PersonalizationMenu(gsm))
                }
            }

            for (i in 1..gsm.backgroundsAvailable) {
                when {
                    backgroundsBoundsHashMap[i]!!.contains(click.x, click.y) -> {
                        gsm.selectedBackground = i-1
                    }
                }
            }
        }
    }

    override fun update(dt: Float) {
        //cam.position.x = Constants.WINDOWS_WIDTH/2.1f
        //cam.update()
        handleInput()
    }

    override fun render(sb: SpriteBatch) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.begin()

        sb.draw(backgroundTexture, backgroundImagePosition.x, backgroundImagePosition.y, backgroundImageSize.x, backgroundImageSize.y)
        sb.draw(returnTexture, returnImagePosition.x, returnImagePosition.y, returnImageSize.x, returnImageSize.y)

        var indexx = 0
        var indexy = 0
        for (i in 1..gsm.backgroundsAvailable) {
            sb.draw(backgroundImages[i-1], backgroundDisplayedPosition.x + indexx * distanceBetween2Images.x, backgroundDisplayedPosition.y - indexy * distanceBetween2Images.y, backgroundDisplayedSize.x, backgroundDisplayedSize.y)
            indexx++
            if (indexx % 2 == 0) {
                indexx = 0
                indexy++
            }
        }

        sb.draw(backgroundHighlighter, highlighterPosition.x + (((gsm.selectedBackground)%2)*distanceBetween2Images.x), highlighterPosition.y - (gsm.selectedBackground/2).toInt()*distanceBetween2Images.y, highlighterSize.x, highlighterSize.y)

        sb.end()
    }

    override fun dispose() {
        backgroundTexture.dispose()
        returnTexture.dispose()
        backgroundHighlighter.dispose()

        for (i in 1..gsm.backgroundsAvailable)
            backgroundImages[i-1]?.dispose()
    }

    init {
        cam.position.x = Constants.WINDOWS_WIDTH/2.1f
        cam.update()

        for (i in 1..gsm.backgroundsAvailable)
            backgroundImages[i-1] = Texture(Gdx.files.internal("backgrounds/background"+(i-1)+".jpg"))

        var indX = 0
        var indY = 0
        for (i in 1..gsm.backgroundsAvailable) {
            backgroundsBoundsHashMap[i] = Rectangle(Constants.WINDOWS_WIDTH / 4.3f + (indX*550), (Constants.WINDOWS_HEIGHT / 40f) - (indY*150), 400f, 225f)
            indX++
            if (indX%2 == 0) {
                indX=0
                indY++
            }
        }
    }
}