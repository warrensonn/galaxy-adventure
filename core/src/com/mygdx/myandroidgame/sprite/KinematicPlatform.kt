package com.mygdx.myandroidgame.sprite

import com.badlogic.gdx.physics.box2d.*
import com.mygdx.myandroidgame.utils.Constants

class KinematicPlatform(world: World, id: String, x: Float, y: Float, width: Int, height: Int) {

    var id: String = id
    var body: Body
    private val def = BodyDef()
    private val fd = FixtureDef()
    private val shape = PolygonShape()

    init {
        def.type = BodyDef.BodyType.KinematicBody

        def.position.set(x / Constants.PPM, y / Constants.PPM)
        def.fixedRotation = true
        body = world.createBody(def)

        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM)

        fd.shape = shape
        fd.density = 1.0f

        body.createFixture(fd).userData = this
        shape.dispose()
    }

    fun hit(){
        print("$id : I've been hit !")
    }
}