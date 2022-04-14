package com.mygdx.myandroidgame.sprite

import com.badlogic.gdx.physics.box2d.*
import com.mygdx.myandroidgame.utils.Constants

class StaticPlatform(world: World, id: String, x: Float, y: Float, width: Int, height: Int, isStatic: Boolean,
                     fixedRotation: Boolean) {

    var id: String = id
    var body: Body
    private val def = BodyDef()
    private val shape = PolygonShape()
    private val fd = FixtureDef()

    init {
        if (isStatic)
            def.type = BodyDef.BodyType.StaticBody
        else
            def.type = BodyDef.BodyType.DynamicBody

        def.position.set(x / Constants.PPM, y / Constants.PPM)
        def.fixedRotation = fixedRotation
        body = world.createBody(def)
        body.userData = this

        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM)

        fd.shape = shape
        fd.density = 1.0f
        if (id=="ground3")
            fd.filter.categoryBits = Constants.BIT_INVISIBLE_WALL

        body.createFixture(fd).userData = this
        shape.dispose()
    }



    fun hit(){
        print("$id : I've been hit !")
    }
}