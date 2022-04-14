package com.mygdx.myandroidgame.sprite

import com.badlogic.gdx.physics.box2d.*
import com.mygdx.myandroidgame.utils.Constants


class PlayerBodyBuilder(world: World, id: String, x: Float, y: Float, width: Int, height: Int, isStatic: Boolean,
                  fixedRotation: Boolean, isOfType: Short, collideWith: Short) {

    lateinit var body: Body
    lateinit var id: String

    fun BodyBuilder(world: World, id: String, x: Float, y: Float, width: Int, height: Int, isStatic: Boolean,
    fixedRotation: Boolean) {
        this.id = id
        createBox(world, id, x, y, width, height, isStatic,
                fixedRotation)
    }

    fun BodyBuilder(world: World, id: String, x: Float, y: Float, width: Int, height: Int, isStatic: Boolean,
                    fixedRotation: Boolean, isOfType: Short, collideWith: Short) {
        this.id = id
        createBoxWithMask(world, id, x, y, width, height, isStatic,
                fixedRotation, isOfType, collideWith)
    }

    fun BodyBuilder(world: World, id: String, x: Float, y: Float, width: Int, height: Int) {
        this.id = id
        createBoxKinematic(world, id, x, y, width, height)
    }

    fun createBox(world: World, id: String, x: Float, y: Float, width: Int, height: Int, isStatic: Boolean,
                  fixedRotation: Boolean) {
        this.id = id
        val pBody: Body
        val def = BodyDef()
        if (isStatic) 
            def.type = BodyDef.BodyType.StaticBody 
        else 
            def.type = BodyDef.BodyType.DynamicBody

        def.position.set(x / Constants.PPM, y / Constants.PPM)
        def.fixedRotation = fixedRotation
        pBody = world.createBody(def)
        pBody.userData = this

        val shape: PolygonShape = PolygonShape()
        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM)

        val fd: FixtureDef = FixtureDef()
        fd.shape = shape
        fd.density = 1.0f

        pBody.createFixture(fd).userData = this
        shape.dispose()
    }

    fun createBoxWithMask(world: World, id: String, x: Float, y: Float, width: Int, height: Int, isStatic: Boolean,
                          fixedRotation: Boolean, isOfType: Short, collideWith: Short) {
        this.id = id
        val pBody: Body
        val def = BodyDef()
        if (isStatic)
            def.type = BodyDef.BodyType.StaticBody
        else
            def.type = BodyDef.BodyType.DynamicBody

        def.position.set(x / Constants.PPM, y / Constants.PPM)
        def.fixedRotation = fixedRotation
        pBody = world.createBody(def)

        val shape = PolygonShape()
        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM)

        val fd = FixtureDef()
        fd.shape = shape
        fd.density = 1.0f
        fd.filter.categoryBits = isOfType
        fd.filter.maskBits = collideWith

        pBody.createFixture(fd).userData = this
        shape.dispose()
    }

    fun createBoxKinematic(world: World, id: String, x: Float, y: Float, width: Int, height: Int) {
        val id = id
        val pBody: Body
        val def = BodyDef()

        def.type = BodyDef.BodyType.KinematicBody

        def.position.set(x / Constants.PPM, y / Constants.PPM)
        def.fixedRotation = true
        pBody = world.createBody(def)

        val shape = PolygonShape()
        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM)

        val fd = FixtureDef()
        fd.shape = shape
        fd.density = 1.0f

        pBody.createFixture(fd).userData = this
        shape.dispose()
    }

    fun hit(){
        print("$id : I've been hit !")
    }

}