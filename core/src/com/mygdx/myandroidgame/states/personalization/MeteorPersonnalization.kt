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

class MeteorPersonnalization(gsm: GameStateManager) : GameState(gsm) {
    private val meteorTexture: Texture = Texture(Gdx.files.internal("menu/meteorTitle.png"))
    private val returnTexture: Texture = Texture(Gdx.files.internal("menu/return.png"))
    private val meteorHighlighter: Texture = Texture(Gdx.files.internal("menu/selectHighlight.png"))
    private val meteorImages: HashMap<Int, Texture> = HashMap()

    private val meteorImageSize: Vector2 = Vector2(500f, 200f)
    private val meteorImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/2.1f - (meteorImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/2.5f-(meteorImageSize.y/2))

    private val returnImageSize: Vector2 = Vector2(100f, 100f)
    private val returnImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/4.2f - (returnImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/2.3f-(returnImageSize.y/2))

    private val meteorDisplayedSize: Vector2 = Vector2(150f, 150f)
    private val meteorDisplayedPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/4.3f,
            Constants.WINDOWS_HEIGHT/10f)

    private val highlighterSize: Vector2 = Vector2(60f, 8f)
    private val highlighterPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH / 3.91f,
            Constants.WINDOWS_HEIGHT / 13f)

    private var meteorsBoundsHashMap : HashMap<Int, Rectangle> = HashMap<Int, Rectangle>()
    private val returnTextureBounds : Rectangle = Rectangle(returnImagePosition.x, returnImagePosition.y, returnImageSize.x, returnImageSize.y)

    private val distanceBetween2Images : Vector2 = Vector2(Constants.WINDOWS_WIDTH / 12f, 150f)


    override fun handleInput() {
        if(Gdx.input.isTouched) {
            val click = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            cam.unproject(click)

            when {
                returnTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(PersonalizationMenu(gsm))
                }
            }

            for (i in 1..gsm.meteorsAvailable) {
                when {
                    meteorsBoundsHashMap[i]!!.contains(click.x, click.y) -> {
                        gsm.selectedMeteor = i-1
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

        sb.draw(meteorTexture, meteorImagePosition.x, meteorImagePosition.y, meteorImageSize.x, meteorImageSize.y)
        sb.draw(returnTexture, returnImagePosition.x, returnImagePosition.y, returnImageSize.x, returnImageSize.y)

        var indexx = 0
        var indexy = 0
        for (i in 1..gsm.meteorsAvailable) {
            sb.draw(meteorImages[i-1], meteorDisplayedPosition.x + (indexx*distanceBetween2Images.x), meteorDisplayedPosition.y - (indexy*distanceBetween2Images.y), meteorDisplayedSize.x, meteorDisplayedSize.y)
            indexx++
            if (indexx % 2 == 0) {
                indexx = 0
                indexy++
            }
        }

        sb.draw(meteorHighlighter, highlighterPosition.x + (((gsm.selectedMeteor)%2)*distanceBetween2Images.x), highlighterPosition.y - (gsm.selectedMeteor/2).toInt()*distanceBetween2Images.y, highlighterSize.x, highlighterSize.y)

        sb.end()
    }

    override fun dispose() {
        meteorTexture.dispose()
        returnTexture.dispose()
        meteorHighlighter.dispose()

        for (i in 1..gsm.backgroundsAvailable)
            meteorImages[i-1]?.dispose()
    }

    init {
        cam.position.x = Constants.WINDOWS_WIDTH/2.1f
        cam.update()

        for (i in 1..gsm.meteorsAvailable)
            meteorImages[i-1] = Texture(Gdx.files.internal("projectile/comet"+(i-1)+".png"))

        var indX = 0
        var indY = 0
        for (i in 1..gsm.meteorsAvailable) {
            meteorsBoundsHashMap[i] = Rectangle(meteorDisplayedPosition.x + (indX*distanceBetween2Images.x), meteorDisplayedPosition.y - (indY*distanceBetween2Images.y), meteorDisplayedSize.x, meteorDisplayedSize.y)
            indX++
            if (indX%2 == 0) {
                indX=0
                indY++
            }
        }
    }
}