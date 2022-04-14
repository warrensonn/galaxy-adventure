package com.mygdx.myandroidgame.states.levels

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.mygdx.myandroidgame.managers.GameStateManager
import com.mygdx.myandroidgame.managers.MyContactListener
import com.mygdx.myandroidgame.sprite.*
import com.mygdx.myandroidgame.states.EndOfLevelState
import com.mygdx.myandroidgame.states.GameState
import com.mygdx.myandroidgame.utils.Constants
import javax.swing.KeyStroke


class StateLevel1(gsm: GameStateManager) : GameState(gsm) {
    private var background: Texture = Texture(Gdx.files.internal("backgrounds/background" + gsm.selectedBackground + ".jpg"))
    private var lostTexture: Texture = Texture(Gdx.files.internal("lost.png"))
    private var world: World = World(Vector2(0f, -19.8f), false)
    private val b2dr: Box2DDebugRenderer = Box2DDebugRenderer()
    private val ppm = Constants.PPM
    private val groundPlatformHeight = Constants.WINDOWS_HEIGHT/12.7f

    // Player
    private var imageControl: Int = 0
    private var currentImage: Int = 0
    private var playerTexture: Texture = Texture(Gdx.files.internal("characters/character${gsm.selectedCharacter}/Front/${currentImage}.png"))
    private val playerPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/12.5f,
            Constants.WINDOWS_HEIGHT/8f)
    private val player: PlayerBodyBuilder = PlayerBodyBuilder(world, "player", playerPosition.x, playerPosition.y, Constants.playerWidth, Constants.playerHeight,
            false, fixedRotation = true, isOfType = Constants.BIT_PLAYER, collideWith = Constants.BIT_WALL)

    // Plaftorms
    private var ground: Texture = Texture(Gdx.files.internal("grounds/ground" + gsm.selectedGroundPlatform + ".png"))

    private val groundPlatform1Size: Vector2 = Vector2(380f, 15f)
    private val groundPlatform1Position: Vector2 = Vector2(Constants.WINDOWS_WIDTH/12.5f,
            groundPlatformHeight)
    private var groundPlatform1: StaticPlatform = StaticPlatform(world, "ground1", groundPlatform1Position.x, groundPlatform1Position.y,
            groundPlatform1Size.x.toInt(), groundPlatform1Size.y.toInt(), isStatic = true, fixedRotation = true) // platform gauche

    private val groundPlatform2Size: Vector2 = Vector2(4f, 12f)
    private val groundPlatform2Position: Vector2 = Vector2(Constants.WINDOWS_WIDTH/2f,
            groundPlatformHeight)
    // small platforms right
    private var groundPlatform2: StaticPlatform = StaticPlatform(world, "ground2", groundPlatform2Position.x, groundPlatform2Position.y,
            groundPlatform2Size.x.toInt(), groundPlatform2Size.y.toInt(), isStatic = true, fixedRotation = false)
    private var groundPlatform3: StaticPlatform = StaticPlatform(world, "ground3", groundPlatform2Position.x+170, groundPlatform2Position.y,
            groundPlatform2Size.x.toInt(), groundPlatform2Size.y.toInt(), isStatic = true, fixedRotation = true)
    private var groundPlatform4: StaticPlatform = StaticPlatform(world, "ground4", groundPlatform2Position.x+340, groundPlatform2Position.y,
            groundPlatform2Size.x.toInt(), groundPlatform2Size.y.toInt(), isStatic = true, fixedRotation = true)

    private var hiddenPlatform: StaticPlatform = StaticPlatform(world, "hidden",-60f, 200f, 30, 30,
            isStatic = true, fixedRotation = true)

    // air platform
    private var air: Texture = Texture(Gdx.files.internal("grounds/air" + gsm.selectedAirPlatform + ".png"))

    private val movingPlatformSize: Vector2 = Vector2(30f, 30f)
    private val movingPlatformPosition: Vector2 = Vector2(20f, 350f)
    private var movingPlatform: KinematicPlatform = KinematicPlatform(world, "moving", movingPlatformPosition.x, movingPlatformPosition.y, movingPlatformSize.x.toInt(), movingPlatformSize.y.toInt())

    private val staticPlatformSize: Vector2 = Vector2(Constants.WINDOWS_WIDTH/8f, 1f)
    private val staticPlatformPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/3.2f, 300f)
    private var airPlatform1: KinematicPlatform = KinematicPlatform(world, "air", staticPlatformPosition.x, staticPlatformPosition.y,
            staticPlatformSize.x.toInt(), staticPlatformSize.y.toInt())

    // meteor
    private var meteorTexture: Texture = Texture(Gdx.files.internal("projectile/meteor" + gsm.selectedMeteor + ".png"))
    private val meteorSize: Float = 0.65f
    private val meteorPosition: Vector2 = Vector2(Constants.WINDOWS_WIDTH/3.2f, 600f)
    private val meteor2Position: Vector2 = Vector2(groundPlatform2Position.x+380, 600f)
    private var meteorCircleBody: CircleBody? = null
    private var meteorQueueBody: MeteorQueue? = null



    // counter display on screen
    private var timeCount: Float = 0f
    private var time2Count: Float = 0f
    private var timerIsOn: Boolean = false
    private var timer2IsOn: Boolean = false
    private var timeToBeat: Int = 15
    private var font: BitmapFont = BitmapFont()
    private var font2: BitmapFont = BitmapFont()

    private var position: Vector3 = Vector3()
    private var movingRight: Boolean = true
    private var meteorHasSpawn: Boolean = false
    private var meteor2HasSpawn: Boolean = false
    private var hasLost: Boolean = false
    private var frame: Int = 0


    override fun handleInput() {  // voir si on garde inertie ou non

        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY))  // press a key launches the timer
            timerIsOn = true

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.body.linearVelocity.x < 10) {
            player.body.setLinearVelocity(8f, player.body.linearVelocity.y)
            currentImage += 1
            playerTexture = Texture(Gdx.files.internal("characters/character${gsm.selectedCharacter}/Right/${currentImage}.png"))
            if (currentImage == 7)
                currentImage = 0

        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.body.linearVelocity.x > -10) {
            player.body.setLinearVelocity(-8f, player.body.linearVelocity.y)
        } else if (Gdx.input.isTouched) {
            if (Gdx.input.x > Constants.WINDOWS_WIDTH / 1.5f) {
                player.body.setLinearVelocity(8f, player.body.linearVelocity.y)
            } else if (Gdx.input.x < Constants.WINDOWS_WIDTH / 3) {
                player.body.setLinearVelocity(-8f, player.body.linearVelocity.y)
            }
            if (Gdx.input.y < Constants.WINDOWS_HEIGHT / 2 && player.body.linearVelocity.y == 0f) {
                player.body.applyForceToCenter(0f, 1900f, false)
            }
        } else {
            player.body.setLinearVelocity(0f, player.body.linearVelocity.y)
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && player.body.linearVelocity.y == 0f) {
            player.body.applyForceToCenter(0f, 1900f, true) // 300 up object against gravity, gravity slows it
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }


    }

    override fun update(dt: Float) {
        world.step(1 / 66f, 6, 2) // 6 and 2 bonne pratique, change how smooth it is

        cameraUpdate()
        airPlatformMoving()
        playerTextureAnimation()

        if (player.body.position.x > airPlatform1.body.position.x-staticPlatformSize.x/2/ppm && player.body.position.y > airPlatform1.body.position.y && !meteorHasSpawn) {
            meteorCircleBody = creatingMeteorBall(meteorPosition.x, meteorPosition.y, meteorSize)
            meteorQueueBody = creatingMeteorQueue(meteorPosition.x-20, meteorPosition.y-10)
            meteorHasSpawn = true
            timer2IsOn = true
        }
        if (meteorHasSpawn && meteorCircleBody!=null) {
            fallingMeteor(16f)
        }

        if (player.hasHitGround4 && !meteor2HasSpawn) {
            meteorCircleBody = creatingMeteorBall(meteor2Position.x, meteor2Position.y, meteorSize)
            meteorQueueBody = creatingMeteorQueue(meteor2Position.x-20, meteor2Position.y-10)
            meteor2HasSpawn = true
        }
        if (meteor2HasSpawn && meteorCircleBody!=null)
            fallingMeteor(45f)

        if (timerIsOn)
            timeCount += dt
        if (timer2IsOn)
            time2Count+=dt

        if (time2Count > 1.8f) {
            airPlatform1.body.setLinearVelocity(0f, airPlatform1.body.linearVelocity.y - 2)
        }


        if (timeCount > timeToBeat-4) {
            font.color = Color.ORANGE
        }
        if (timeCount > timeToBeat)
            font.color = Color.RED

        if (player.body.position.y < -7 || player.hasBeenHitByMeteor) {
            hasLost = true
            frame++
            if (frame==10) {
                gsm.set(StateLevel1(gsm))
            }
        } else if (player.body.position.x > 53) {
            gsm.stateLevelNumber++
//
            if (gsm.backgroundsAvailable<2) {
                gsm.backgroundsAvailable++
                gsm.levelsAvailable++
                gsm.platformsAvailable++
                gsm.meteorsAvailable++
                gsm.newUnlock = true
            }

            if (timeCount < timeToBeat) {
                if (gsm.goldMedalHasBeenEarned[gsm.stateLevelNumber-1] == false) {
                    gsm.charactersAvailable++
                    gsm.goldMedal = true
                    gsm.goldMedalHasBeenEarned[gsm.stateLevelNumber -1] = true
                }
            }
            gsm.set(EndOfLevelState(gsm))
        } else {
            handleInput()
        }
    }

    override fun render(sb: SpriteBatch) { // Permet de dessiner les textures
        sb.projectionMatrix = cam.combined
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        b2dr.render(world, cam.combined.scl(ppm)) // scale with pixel per meter

        sb.begin()
        // decors
        sb.draw(background, -45f, -150f, Constants.WINDOWS_WIDTH.toFloat(), Constants.WINDOWS_HEIGHT.toFloat())
        sb.draw(air, airPlatform1.body.position.x*ppm-staticPlatformSize.x/2, airPlatform1.body.position.y*ppm+5, staticPlatformSize.x, 5f) // static platform
        sb.draw(air, movingPlatform.body.position.x*ppm-movingPlatformSize.x/2, movingPlatform.body.position.y*ppm-movingPlatformSize.y/3.5f, movingPlatformSize.x, movingPlatformSize.y)  // moving air platform
        sb.draw(ground, groundPlatform1.body.position.x*ppm-groundPlatform1Size.x/2, groundPlatform1.body.position.y*ppm+5, groundPlatform1Size.x, groundPlatform1Size.y)
        // player
        sb.draw(playerTexture, player.body.position.x * ppm - 25, player.body.position.y * ppm - 35, 50f, 109.5f)

        sb.draw(ground, groundPlatform2.body.position.x*ppm-groundPlatform2Size.x/2, groundPlatform2.body.position.y*ppm-groundPlatform2Size.y/2+4, groundPlatform2Size.x, groundPlatform2Size.y)
        sb.draw(ground, groundPlatform3.body.position.x*ppm-groundPlatform2Size.x/2, groundPlatform3.body.position.y*ppm-groundPlatform2Size.y/2+4, groundPlatform2Size.x, groundPlatform2Size.y)
        sb.draw(ground, groundPlatform4.body.position.x*ppm-groundPlatform2Size.x/2, groundPlatform4.body.position.y*ppm-groundPlatform2Size.y/2+4, groundPlatform2Size.x, groundPlatform2Size.y)

        if (meteorCircleBody != null) {
            sb.draw(TextureRegion(meteorTexture), meteorCircleBody!!.body.position.x*ppm-55, meteorCircleBody!!.body.position.y*ppm-23, 0f, 0f, 100f, 100f, 1f, 1f, 340f)
        }
        if (hasLost)
            sb.draw(lostTexture, cam.position.x-150f, cam.position.y-90f, 300f, 200f)
//
        font.draw(sb, "Time : "+String.format("%.2f", timeCount), cam.position.x-Constants.WINDOWS_WIDTH/4, 520f)
        font2.draw(sb, "Time to beat : "+String.format("%.2f", timeToBeat.toFloat()), cam.position.x-Constants.WINDOWS_WIDTH/6.5f, 520f)
//
        sb.end()

    }

    override fun dispose() {
        world.dispose()
        b2dr.dispose()

        playerTexture.dispose()
        background.dispose()
        air.dispose()
        ground.dispose()
        meteorTexture.dispose()

        font.dispose()
        font2.dispose()
    }

    private fun cameraUpdate() {
        val position = cam.position
        if (player.body.position.x > 15 && player.body.position.x<41)
            position.x = player.body.position.x*ppm

        cam.position.set(position)
        cam.update()
    }

    private fun airPlatformMoving() {
        if (movingRight) {
            movingPlatform.body.setLinearVelocity(2f, movingPlatform.body.linearVelocity.y)
            if (movingPlatform.body.position.x>3f)
                movingRight = false

        } else if (!movingRight) {
            movingPlatform.body.setLinearVelocity(-2f, movingPlatform.body.linearVelocity.y)
            if (movingPlatform.body.position.x<-1f)
                movingRight = true
        }
    }

    private fun creatingMeteorBall(x: Float, y: Float, r: Float): CircleBody {
        return CircleBody(world, "meteor", x, y, 1, r)
    }

    private fun creatingMeteorQueue(x: Float, y: Float): MeteorQueue {
        return MeteorQueue(world, "meteor", x, y)
    }

    private fun fallingMeteor(yFall: Float) {
        meteorCircleBody!!.body.setLinearVelocity(-2f, -yFall)
        meteorQueueBody!!.body.setLinearVelocity(-2f, -yFall)
        if (meteorCircleBody!!.body.position.y < -7f) {
            meteorCircleBody = null
            meteorQueueBody = null
        }
    }

    private fun playerTextureAnimation() {
        if(player.body.linearVelocity.x > 0) {
            currentImage += 1
            playerTexture = Texture(Gdx.files.internal("characters/character${gsm.selectedCharacter}/Right/${currentImage}.png"))
            if (currentImage == 7)
                currentImage = 0

        } else if(player.body.linearVelocity.x < 0){
            currentImage+=1
            playerTexture = Texture(Gdx.files.internal("characters/character${gsm.selectedCharacter}/Left/${currentImage}.png"))
            if(currentImage == 7)
                currentImage = 0

        } else if (player.body.linearVelocity.y != 0f) {
            imageControl += 1
            if (imageControl==3) {
                currentImage += 1
                imageControl = 0
            }
            playerTexture = Texture(Gdx.files.internal("characters/character${gsm.selectedCharacter}/Front/${currentImage}.png"))
            if(currentImage == 7)
                currentImage = 0

        } else {
            currentImage = 0
            playerTexture = Texture(Gdx.files.internal("characters/character${gsm.selectedCharacter}/Front/${currentImage}.png"))
        }
    }


    init {
        position.x = (Constants.WINDOWS_WIDTH / 4).toFloat()
        position.y = (Constants.WINDOWS_HEIGHT / 4).toFloat()
        cam.position.set(position)

        font.color = Color.CYAN
        font.data.scaleX = 2f
        font.data.scaleY = 2f

        font2.color = Color.WHITE
        font2.data.scaleX = 2f
        font2.data.scaleY = 2f

        world.setContactListener(MyContactListener())

    }
}