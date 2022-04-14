package com.mygdx.myandroidgame.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.myandroidgame.managers.GameStateManager
import com.mygdx.myandroidgame.utils.Constants


class EndOfLevelState(gsm: GameStateManager) : GameState(gsm) {

    // Textures
    private val background: Texture = Texture(Gdx.files.internal("backgrounds/background"+gsm.selectedBackground+".jpg"))
    private val nextTexture: Texture = Texture(Gdx.files.internal("menu/next.png"))
    private val menuTexture: Texture = Texture(Gdx.files.internal("menu/menu.png"))
    private val unlockTexture: Texture = Texture(Gdx.files.internal("menu/unlock.png"))

    private val nextImageSize: Vector2 = Vector2(250f, 120f)
    private val nextImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/1.6f - (nextImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/9f)

    private val menuImageSize: Vector2 = Vector2(250f, 120f)
    private val menuImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/3f - (menuImageSize.x/2.1f),
            Constants.WINDOWS_HEIGHT/9f)

    private var decalage = 2f
    private val unlockImageSize: Vector2 = Vector2(250f, 50f)
    private var unlockImagePosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/3.7f,
            Constants.WINDOWS_HEIGHT/decalage)

    private val nextTextureBounds = Rectangle(nextImagePosition.x, nextImagePosition.y, nextImageSize.x, nextImageSize.y);
    private val menuTextureBounds = Rectangle(menuImagePosition.x, menuImagePosition.y, menuImageSize.x, menuImageSize.y)

    private var new:Boolean = gsm.newUnlock || gsm.goldMedal

    // FA55E9 -> couleur écriture
    // 85 -> taille ecriture


    override fun handleInput() {
        if(Gdx.input.isTouched) {
            val click  = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            cam.unproject(click);

            when {
                nextTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(gsm.switchGameState(gsm.stateLevelNumber))
                }
                menuTextureBounds.contains(click.x, click.y) -> {
                    gsm.set(MainMenu(gsm))
                }
            }
        }
    }

    override fun update(dt: Float) {
        handleInput()

       if(gsm.newUnlock || gsm.goldMedal){
           decalage += 0.05f
           if (decalage < 17f){
               unlockImagePosition.y = Constants.WINDOWS_HEIGHT/decalage
           } else {
               gsm.newUnlock = false
               gsm.goldMedal = false
           }
       }
    }

    override fun render(sb: SpriteBatch) {
        sb.projectionMatrix = cam.combined
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.begin()
        sb.draw(background, -45f, -150f, Constants.WINDOWS_WIDTH.toFloat(), Constants.WINDOWS_HEIGHT.toFloat());
        sb.draw(nextTexture, nextImagePosition.x, nextImagePosition.y, nextImageSize.x, nextImageSize.y)
        sb.draw(menuTexture, menuImagePosition.x, menuImagePosition.y, menuImageSize.x, menuImageSize.y)

        if (new)
            sb.draw(unlockTexture, unlockImagePosition.x, unlockImagePosition.y, unlockImageSize.x, unlockImageSize.y)

        sb.end()
    }

    override fun dispose() {
        background.dispose()
        nextTexture.dispose()
        menuTexture.dispose()
    }

    init {
        cam.position.x = Constants.WINDOWS_WIDTH/2.1f
        cam.update()
    }
}