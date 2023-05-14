package qubic.strategy

import qubic.core.GameState
import kotlin.random.Random
import qubic.core.LINES

class HeuristicBot : GameBot {
    fun heuristic(state: GameState): Float {
        var forcedLines = 0
        return LINES.map {
            val countToMove = it.count { i -> state.cells[i] == state.toMove }
            val countOther = it.count { i -> state.cells[i] != null && state.cells[i] != state.toMove }
            val countNull = 4 - countOther - countToMove
            if (countToMove == 3 && countOther == 0) {
                +1000000f
            } else if (countToMove == 2 && countOther == 0) {
                1f
            } else if (countOther == 2 && countToMove == 0) {
                -1f
            } else if (countOther == 3 && countToMove == 0) {
                forcedLines += 1
                if (forcedLines > 1) {
                    -1000f
                } else {
                    -1f
                }
            } else if (countOther == 4) {
                -1000000000f
            } else {
                0f
            }
        }.sum()
    }

    override fun move(state: GameState): Int {
        val possibleMoves = (0..63).filter {
            state.isEmpty(it)
        }
        val minimumValue = possibleMoves.minOf { heuristic(state.move(it)) }
        val bestMoves = possibleMoves.filter { heuristic(state.move(it)) == minimumValue }
        return bestMoves[Random.nextInt(bestMoves.size)]
    }
}