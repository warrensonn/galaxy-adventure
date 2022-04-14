package com.mygdx.myandroidgame.managers

import com.badlogic.gdx.physics.box2d.*
import com.mygdx.myandroidgame.sprite.PlayerBodyBuilder
import com.mygdx.myandroidgame.sprite.CircleBody
import com.mygdx.myandroidgame.sprite.StaticPlatform


class MyContactListener : ContactListener {
    override fun beginContact(contact: Contact) {
        val fa: Fixture = contact.fixtureA
        val fb: Fixture = contact.fixtureB
        println("nouveau contact")

        if (fa == null || fb == null)
            return
        if(fa.userData == null && fb.userData == null)
            return

        if (fa.userData is PlayerBodyBuilder) {
            var tba: PlayerBodyBuilder = fa.userData as PlayerBodyBuilder
            if (fb.userData is CircleBody) {
                tba.hasBeenHitByMeteor = true
            } else if (fb.userData is StaticPlatform) {
                var tbb: StaticPlatform = fb.userData as StaticPlatform
                if (tbb.id == "ground4")
                    tba.hasHitGround4 = true
            }
        }


    }

    override fun endContact(p0: Contact?) {
        println("fin du contact")
    }

    override fun preSolve(p0: Contact?, p1: Manifold?) {
        val fa: Fixture? = p0?.fixtureA
        val fb: Fixture? = p0?.fixtureB
    }

    override fun postSolve(p0: Contact?, p1: ContactImpulse?) {
        val fa: Fixture? = p0?.fixtureA
        val fb: Fixture? = p0?.fixtureB
    }

}