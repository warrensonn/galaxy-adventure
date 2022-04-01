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

class CharacterPersonalization(gsm: GameStateManager) : GameState(gsm) {
    private val characterTexture: Texture = Texture(Gdx.files.internal("menu/characterTitle.png"))
    private val returnTexture: Texture = Texture(Gdx.files.internal("menu/return.png"))
    private val characterHighlighter: Texture = Texture(Gdx.files.internal("menu/selectHighlight.png"))
    private val characterImages: HashMap<Int, Texture> = HashMap()

    private val characterImageSize: Vector2 = Vector2(500f, 200f)
    private val characterImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/2.1f - (characterImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/2.5f-(characterImageSize.y/2))

    private val returnImageSize: Vector2 = Vector2(100f, 100f)
    private val returnImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/4.2f - (returnImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/2.3f-(returnImageSize.y/2))

    private val characterDisplayedSize: Vector2 = Vector2(137f, 300f)
    private val characterDisplayedPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/4.3f,
            Constants.WINDOWS_HEIGHT/11f)

    private val highlighterSize: Vector2 = Vector2(70f, 8f)
    private val highlighterPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH / 4f,
            Constants.WINDOWS_HEIGHT / 13f)

    private var charactersBoundsHashMap : HashMap<Int, Rectangle> = HashMap()
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

            for (i in 1..gsm.charactersAvailable) {
                when {
                    charactersBoundsHashMap[i]!!.contains(click.x, click.y) -> {
                        gsm.selectedCharacter = i-1
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

        sb.draw(characterTexture, characterImagePosition.x, characterImagePosition.y, characterImageSize.x, characterImageSize.y)
        sb.draw(returnTexture, returnImagePosition.x, returnImagePosition.y, returnImageSize.x, returnImageSize.y)

        var indexx = 0
        var indexy = 0
        for (i in 1..gsm.charactersAvailable) {
            sb.draw(characterImages[i-1], characterDisplayedPosition.x + (indexx*distanceBetween2Images.x), characterDisplayedPosition.y - (indexy*distanceBetween2Images.y), characterDisplayedSize.x, characterDisplayedSize.y)
            indexx++
            if (indexx % 2 == 0) {
                indexx = 0
                indexy++
            }
        }

        sb.draw(characterHighlighter, highlighterPosition.x + (((gsm.selectedCharacter)%2)*distanceBetween2Images.x), highlighterPosition.y - (gsm.selectedCharacter/2).toInt()*distanceBetween2Images.y, highlighterSize.x, highlighterSize.y)

        sb.end()
    }

    override fun dispose() {
        characterTexture.dispose()
        returnTexture.dispose()
        characterHighlighter.dispose()

        for (i in 1..gsm.charactersAvailable)
            characterImages[i-1]?.dispose()
    }

    init {
        cam.position.x = Constants.WINDOWS_WIDTH/2.1f
        cam.update()


        var indX = 0
        var indY = 0
        for (i in 1..gsm.charactersAvailable) {
            charactersBoundsHashMap[i] = Rectangle(characterDisplayedPosition.x + (indX*distanceBetween2Images.x), characterDisplayedPosition.y - (indY*distanceBetween2Images.y), characterDisplayedSize.x, characterDisplayedSize.y)
            characterImages[i-1] = Texture(Gdx.files.internal("characters/character${i-1}/0.png"))
            indX++
            if (indX%2 == 0) {
                indX=0
                indY++
            }
        }
    }
}