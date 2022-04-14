package com.mygdx.myandroidgame.sprite

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.mygdx.myandroidgame.utils.Constants

class MeteorQueue(world: World, id: String, x: Float, y: Float) {

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

        val vec: Array<Vector2?> = arrayOfNulls<Vector2>(3)
        vec[0] = Vector2(0f, 0f)
        vec[1] = Vector2(1.3f, 2f)
        vec[2] = Vector2(1.2f, 0f)
        shape.set(vec)

        fd.shape = shape
        fd.density = 1.0f

        body.createFixture(fd).userData = this
        shape.dispose()
    }

    fun hit(){
        print("$id : I've been hit !")
    }

}