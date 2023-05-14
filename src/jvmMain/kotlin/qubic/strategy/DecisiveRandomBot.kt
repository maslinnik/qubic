package qubic.strategy

import qubic.core.GameState
import kotlin.random.Random

class DecisiveRandomBot : GameBot {
    override fun move(state: GameState): Int {
        val possibleMoves = (0..63).filter {
            state.isEmpty(it)
        }
        val winningMoves = possibleMoves.filter {
            state.move(it).isLost()
        }
        if (winningMoves.isNotEmpty()) {
            return winningMoves.first()
        } else {
            return possibleMoves[Random.nextInt(possibleMoves.size)]
        }
    }
}