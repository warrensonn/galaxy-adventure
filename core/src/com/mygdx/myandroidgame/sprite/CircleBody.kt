package com.mygdx.myandroidgame.sprite

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.mygdx.myandroidgame.utils.Constants


class CircleBody(world: World, id: String, x: Float, y: Float, height: Int, r: Float) {

    var id: String = id
    var body: Body
    private val def = BodyDef()
    private val fd = FixtureDef()
    private val shape = CircleShape()

    init {
        def.type = BodyDef.BodyType.KinematicBody

        def.position.set(x/Constants.PPM, y/Constants.PPM)
        def.fixedRotation = true
        body = world.createBody(def)

        shape.radius = 0.65f
        shape.position = Vector2(0f, -height/2f)

        fd.shape = shape
        fd.density = 1.0f

        body.createFixture(fd).userData = this
        shape.dispose()
    }
}