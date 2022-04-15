package com.mygdx.myandroidgame.sprite

import com.badlogic.gdx.physics.box2d.*
import com.mygdx.myandroidgame.utils.Constants


class PlayerBodyBuilder(world: World, id: String, x: Float, y: Float, width: Int, height: Int, isStatic: Boolean,
                  fixedRotation: Boolean, isOfType: Short, collideWith: Short) {

    var id: String = id
    var body: Body
    var hasBeenHitByMeteor = false
    var hasHitMovingTiny = false
    var hasHitGround4: Boolean = false
    private val def = BodyDef()
    private val shape = PolygonShape()
    private val fd = FixtureDef()


    fun hit(){
        print("$id : I've been hit !")
    }

    init {
        if (isStatic)
            def.type = BodyDef.BodyType.StaticBody
        else
            def.type = BodyDef.BodyType.DynamicBody

        def.position.set(x / Constants.PPM, y / Constants.PPM)
        def.fixedRotation = fixedRotation
        body = world.createBody(def)

        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM)

        fd.shape = shape
        fd.density = 1.0f
        fd.filter.categoryBits = isOfType
        fd.filter.maskBits = collideWith

        body.createFixture(fd).userData = this
        shape.dispose()
    }

}