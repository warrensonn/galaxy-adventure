package com.mygdx.myandroidgame.managers

import com.mygdx.myandroidgame.states.GameState
import java.util.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.myandroidgame.states.levels.StateLevel1
import com.mygdx.myandroidgame.states.levels.StateLevel2
import com.mygdx.myandroidgame.states.levels.StateLevel3

class GameStateManager {

    private val gameStates: Stack<GameState> = Stack()

    var stateLevelNumber: Int = 1   // level sélectionné
    var levelsAvailable: Int = 1

    var selectedBackground: Int = 0
    var backgroundsAvailable: Int = 1

    var meteorsAvailable: Int = 1
    var selectedMeteor: Int = 0

    var charactersAvailable: Int = 1
    var selectedCharacter: Int = 0

    var platformsAvailable: Int = 1
    var selectedAirPlatform: Int = 0
    var selectedGroundPlatform: Int = 0

    var newUnlock: Boolean = false


    fun push(gameState: GameState) {  // place en haut de la pile stack gameStates l'objet gameState en paramètre
        gameStates.push(gameState)
    }

    fun set(gameState: GameState) {   // récupère le gameState en haut de la pile, on execute sa méthode dispose puis l'objet est détruit avec pop()
        gameStates.pop().dispose()
        gameStates.push(gameState)
    }

    fun update(dt: Float) {           // récupère l'objet au dessus de la pile, l'utilise puis le replace au dessus de la pile
        gameStates.peek().update(dt)
    }

    fun render(sb: SpriteBatch) {     //
        gameStates.peek().render(sb)
    }

    fun switchGameState(level_Number: Int): GameState {
        return when (level_Number) {
            1 -> StateLevel1(this)
            2 -> StateLevel2(this)
            3 -> StateLevel3(this)
            else -> {
                StateLevel2(this)
            }
        }
    }
}