package com.mygdx.myandroidgame.states.levels

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.mygdx.myandroidgame.managers.GameStateManager
import com.mygdx.myandroidgame.sprite.BodyBuilder
import com.mygdx.myandroidgame.states.GameState
import com.mygdx.myandroidgame.states.MainMenu
import com.mygdx.myandroidgame.utils.Constants

class StateLevel4(gsm: GameStateManager) : GameState(gsm) {
    //private val DEBUG = false
    private val world: World = World(Vector2(0f, -19.8f), false)
    private val b2dr: Box2DDebugRenderer = Box2DDebugRenderer()
    private val ppm = Constants.PPM
    private val groundPlatformHeight = Constants.WINDOWS_HEIGHT/12.7f
    // Player
    private val player: Body = BodyBuilder.createBox(world, 300f, 200f, 32, 70,
            isStatic = false, fixedRotation = true)

    // ground platforms
    private var platform: Body = BodyBuilder.createBox(world, Constants.WINDOWS_WIDTH/12.5f, groundPlatformHeight,
            380, 5, isStatic = true, fixedRotation = true) // platform gauche
    private var platform2: Body = BodyBuilder.createBox(world, Constants.WINDOWS_WIDTH / 1.75f, groundPlatformHeight,
            Constants.WINDOWS_WIDTH / 3, 5, isStatic = true, fixedRotation = true) // platform right

    // hidden air platforms
    private var platform3: Body = BodyBuilder.createBox(world, -60f, 350f, 30, 30,
            isStatic = true, fixedRotation = true)
    private var platform4: Body = BodyBuilder.createBox(world, -60f, 200f, 30, 30,
            isStatic = true, fixedRotation = true)

    // air platform
    private var platform5: Body = BodyBuilder.createBox(world, (Constants.WINDOWS_WIDTH / 3.5).toFloat(), 300f,
            Constants.WINDOWS_WIDTH / 5, 5, isStatic = true, fixedRotation = true)

    // Texture
    private val background: Texture = Texture(Gdx.files.internal("backgrounds/background"+gsm.selectedBackground+".jpg"))
    private var playerTexture: Texture = Texture(Gdx.files.internal("characters/character"+gsm.selectedCharacter+"/0.png"))
    private var ground: Texture? = null
    private var cyanPlatform: Texture? = null

    private var position: Vector3 = Vector3()


    override fun handleInput() {  // voir si on garde inertie ou non
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.linearVelocity.x < 10) {
            player.setLinearVelocity(8f, player.linearVelocity.y)
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.linearVelocity.x > -10) {
            player.setLinearVelocity(-8f, player.linearVelocity.y)
        } else if (Gdx.input.isTouched) {
            if (Gdx.input.x > Constants.WINDOWS_WIDTH / 1.5f) {
                player.setLinearVelocity(8f, player.linearVelocity.y)
            } else if (Gdx.input.x < Constants.WINDOWS_WIDTH / 3) {
                player.setLinearVelocity(-8f, player.linearVelocity.y)
            }
            if (Gdx.input.y < Constants.WINDOWS_HEIGHT / 2 && player.linearVelocity.y == 0f) {
                player.applyForceToCenter(0f, 1900f, false)
            }
        } else {
            player.setLinearVelocity(0f, player.linearVelocity.y)
        }

        /*
    } else if (player.linearVelocity.x < 4 && player.linearVelocity.x > -4 && player.linearVelocity.y == 0f) {
        player.setLinearVelocity(0f, player.linearVelocity.y)
    } else if (player.linearVelocity.x > 0) {
        val v = Vector2(-15f, 0f)
        player.applyForceToCenter(v, false)
    } else if (player.linearVelocity.x < 0) {
        val v = Vector2(15f, 0f)
        player.applyForceToCenter(v, false)
    }*/


        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && player.linearVelocity.y == 0f) {
            player.applyForceToCenter(0f, 1900f, true) // 300 up object against gravity, gravity slows it
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

    }

    override fun update(dt: Float) {
        world.step(1 / 66f, 6, 2) // 6 and 2 bonne pratique, change how smooth it is
        handleInput()
        //cam.position.x = Constants.WINDOWS_WIDTH/2.1f
        cameraUpdate()

        if (player.position.y < -7) {
            gsm.set(StateLevel2(gsm))
        } else if (player.position.x > 53) {
            gsm.set(MainMenu(gsm))
        }

        // si contact supérieur platform haute de gauche, platform.position.x + = 2
    }

    override fun render(sb: SpriteBatch) { // Permet de dessiner les textures
        sb.projectionMatrix = cam.combined
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.begin()
        // decors
        sb.draw(background, -45f, -150f, Constants.WINDOWS_WIDTH.toFloat(), Constants.WINDOWS_HEIGHT.toFloat());
        // sb.draw(cyanPlatform, (Constants.WINDOWS_WIDTH / 1.5f)/2 -
        // ((Constants.WINDOWS_WIDTH / 5)/2), 350, Constants.WINDOWS_WIDTH / 5, 10);
        // sb.draw(ground, 0, 75, Constants.WINDOWS_WIDTH / 5, 12);
        // player
        sb.draw(playerTexture, player.position.x * ppm - 25, player.position.y * ppm - 35, 50f, 70f)
        sb.end()
        b2dr.render(world, cam.combined.scl(ppm)) // scale with pixel per meter
    }

    override fun dispose() {
        world.dispose()
        b2dr.dispose()
        playerTexture.dispose()
        background.dispose()
        // cyanPlatform.dispose();
        // ground.dispose();
    }

    private fun cameraUpdate() {
        val position = cam.position
        if (player.position.x > 15 && player.position.x<41) {
            position.x = player.position.x*ppm
        }
        cam.position.set(position)
        cam.update()
    }

    init {
        position.x = (Constants.WINDOWS_WIDTH / 4).toFloat()
        position.y = (Constants.WINDOWS_HEIGHT / 4).toFloat()
        cam.position.set(position)
    }
}