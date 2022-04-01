package com.mygdx.myandroidgame.sprite

import com.badlogic.gdx.physics.box2d.*
import com.mygdx.myandroidgame.utils.Constants

object BodyBuilder {

    fun createBox(world: World, x: Float, y: Float, width: Int, height: Int, isStatic: Boolean,
                  fixedRotation: Boolean): Body {
        val pBody: Body
        val def: BodyDef = BodyDef()
        if (isStatic) 
            def.type = BodyDef.BodyType.StaticBody 
        else 
            def.type = BodyDef.BodyType.DynamicBody

        def.position.set(x / Constants.PPM, y / Constants.PPM)
        def.fixedRotation = fixedRotation
        pBody = world.createBody(def)
        //pBody.userData = dataType

        val shape: PolygonShape = PolygonShape()
        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM)

        val fd: FixtureDef = FixtureDef()
        fd.shape = shape
        fd.density = 1.0f
        //fd.filter.categoryBits = Constants.BIT_WALL
        //fd.filter.maskBits = Constants.BIT_PLAYER or Constants.BIT_WALL or Constants.BIT_SENSOR
        //fd.filter.groupIndex = 0
        pBody.createFixture(fd)
        shape.dispose()

        return pBody
    }

    fun createBoxWithMask(world: World, x: Float, y: Float, width: Int, height: Int, isStatic: Boolean,
                  fixedRotation: Boolean, isOfType: Short, collideWith: Short): Body {
        val pBody: Body
        val def: BodyDef = BodyDef()
        if (isStatic)
            def.type = BodyDef.BodyType.StaticBody
        else
            def.type = BodyDef.BodyType.DynamicBody

        def.position.set(x / Constants.PPM, y / Constants.PPM)
        def.fixedRotation = fixedRotation
        pBody = world.createBody(def)
        //pBody.userData = dataType

        val shape: PolygonShape = PolygonShape()
        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM)

        val fd: FixtureDef = FixtureDef()
        fd.shape = shape
        fd.density = 1.0f
        fd.filter.categoryBits = isOfType
        fd.filter.maskBits = collideWith

        //fd.filter.groupIndex = 0
        pBody.createFixture(fd)
        shape.dispose()

        return pBody
    }

    fun createBoxKinematic(world: World, x: Float, y: Float, width: Int, height: Int): Body {
        val pBody: Body
        val def: BodyDef = BodyDef()

        def.type = BodyDef.BodyType.KinematicBody

        def.position.set(x / Constants.PPM, y / Constants.PPM)
        def.fixedRotation = true
        pBody = world.createBody(def)
        //pBody.userData = dataType

        val shape: PolygonShape = PolygonShape()
        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM)

        val fd: FixtureDef = FixtureDef()
        fd.shape = shape
        fd.density = 1.0f
        //fd.filter.categoryBits = Constants.BIT_WALL
        //fd.filter.maskBits = Constants.BIT_PLAYER or Constants.BIT_WALL or Constants.BIT_SENSOR
        //fd.filter.groupIndex = 0
        pBody.createFixture(fd)
        shape.dispose()

        return pBody
    }

}