package qubic.strategy

import qubic.core.GameState
import kotlin.random.Random

class RandomBot : GameBot {
    override fun move(state: GameState): Int {
        val possibleMoves = (0..63).filter {
            state.isEmpty(it)
        }
        return possibleMoves[Random.nextInt(possibleMoves.size)]
    }
}