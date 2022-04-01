package com.mygdx.myandroidgame.states

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.myandroidgame.managers.GameStateManager
import com.mygdx.myandroidgame.states.levels.StateLevel1
import com.mygdx.myandroidgame.states.levels.StateLevel2
import com.mygdx.myandroidgame.states.levels.StateLevel3
import com.mygdx.myandroidgame.states.levels.StateLevel4
import com.mygdx.myandroidgame.states.personalization.PersonalizationMenu
import com.mygdx.myandroidgame.utils.Constants

class LevelSelect(gsm: GameStateManager) : GameState(gsm) {

    // Textures
    private val background: Texture = Texture(Gdx.files.internal("backgrounds/background"+gsm.selectedBackground+".jpg"))
    private val levelSelectTexture: Texture = Texture(Gdx.files.internal("menu/levelSelect.jpg"))
    private val returnTexture: Texture = Texture(Gdx.files.internal("menu/return.png"))
    private val levelHighlighter: Texture = Texture(Gdx.files.internal("menu/selectHighlight.png"))
    private val playTexture: Texture = Texture(Gdx.files.internal("menu/Play.jpg"))

    private val playImageSize: Vector2 = Vector2(200f, 60f)
    private val playImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/1.48f - (playImageSize.x/2.1f),
            Constants.WINDOWS_WIDTH/4.5f-(playImageSize.y/2))

    private val levelImageSize: Vector2 = Vector2(500f, 200f)
    private val levelImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/2.1f - (levelImageSize.x/2.1f),
                                                Constants.WINDOWS_HEIGHT/2.5f-(levelImageSize.y/2))

    private val returnImageSize: Vector2 = Vector2(100f, 100f)
    private val returnImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/4.2f - (returnImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/2.3f-(returnImageSize.y/2))

    private val levelDisplayedSize: Vector2 = Vector2(100f, 100f)
    private val levelDisplayedPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/4.1f,
            Constants.WINDOWS_HEIGHT/6.5f)

    private val backgroundSize: Vector2 = Vector2(Constants.WINDOWS_WIDTH.toFloat(), Constants.WINDOWS_HEIGHT.toFloat())
    private val backgroundPosition: Vector2 = Vector2(-45f, -150f)

    private val highlighterSize: Vector2 = Vector2(100f, 10f)
    private val highlighterPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/4.1f,
            Constants.WINDOWS_HEIGHT/7.1f)

    private var levelHashMap : HashMap<Int, Texture> = HashMap<Int, Texture> ()
    private var levelBoundsHashMap : HashMap<Int, Rectangle> = HashMap<Int, Rectangle>()

    private var distanceBetween2Images : Vector2 = Vector2(200f, 150f)

    private val returnTextureBounds = Rectangle(returnImagePosition.x, returnImagePosition.y, returnImageSize.x, returnImageSize.y)
    private val playTextureBounds = Rectangle(playImagePosition.x, playImagePosition.y, playImageSize.x, playImageSize.y)


    override fun handleInput() {
        if(Gdx.input.isTouched) {
            val click  = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            cam.unproject(click)

            when {
                returnTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(MainMenu(gsm))
                } playTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(gsm.switchGameState(gsm.stateLevelNumber))
                }
            }
            for (i in 1..gsm.levelsAvailable) {
                when {
                    levelBoundsHashMap[i]!!.contains(click.x, click.y) -> {
                        gsm.stateLevelNumber = i
                    }
                }
            }
        }

    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render(sb: SpriteBatch) {
        sb.projectionMatrix = cam.combined
        //Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.begin()

        sb.draw(background, backgroundPosition.x, backgroundPosition.y, backgroundSize.x, backgroundSize.y)
        sb.draw(levelSelectTexture, levelImagePosition.x, levelImagePosition.y, levelImageSize.x, levelImageSize.y)
        sb.draw(returnTexture, returnImagePosition.x, returnImagePosition.y, returnImageSize.x, returnImageSize.y)
        sb.draw(levelHighlighter, highlighterPosition.x + (((gsm.stateLevelNumber-1)%5)*distanceBetween2Images.x), highlighterPosition.y-gsm.stateLevelNumber/6*distanceBetween2Images.y, highlighterSize.x, highlighterSize.y)
        sb.draw(playTexture, playImagePosition.x, playImagePosition.y, playImageSize.x, playImageSize.y)

        var indexx=0
        var indexy=0
        for (i in 1..gsm.levelsAvailable) {
            sb.draw(levelHashMap[i], levelDisplayedPosition.x + (indexx*distanceBetween2Images.x), levelDisplayedPosition.y - (indexy*distanceBetween2Images.y), levelDisplayedSize.x, levelDisplayedSize.y)
            indexx++
            if (indexx %5==0) {
                indexx=0
                indexy++
            }
        }

        sb.end()
    }

    override fun dispose() {
        background.dispose()
        levelSelectTexture.dispose()
        returnTexture.dispose()
        levelHighlighter.dispose()
        playTexture.dispose()

        for (i in 1..gsm.levelsAvailable) {
            levelHashMap[i]?.dispose()
        }
    }

    init {
        for (i in 1..gsm.levelsAvailable)
            levelHashMap[i] = Texture(Gdx.files.internal("menu/levelNumber/$i.png"))

        cam.position.x = Constants.WINDOWS_WIDTH/2.1f
        cam.update()

        var indX = 0
        var indY = 0
        for (i in 1..gsm.levelsAvailable) {
            levelBoundsHashMap[i] = Rectangle(levelDisplayedPosition.x+(indX*distanceBetween2Images.x), levelDisplayedPosition.y-(indY*distanceBetween2Images.y), levelDisplayedSize.x, levelDisplayedSize.y)
            indX++
            if (indX%5 == 0) {
                indX=0
                indY++
            }
        }
    }

}

