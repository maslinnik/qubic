package qubic.strategy

import qubic.core.GameState
import kotlin.random.Random
import qubic.core.LINES

class ForcedSequenceBot : GameBot {
    private fun heuristic(state: GameState): Float {
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

    private fun GameState.winningMove(): Int? {
        val winningLine = LINES
            .firstOrNull {
                it.count { i -> cells[i] == toMove } == 3 && it.count { i -> cells[i] == null } == 1
            }
        if (winningLine != null) {
            return winningLine.first { cells[it] == null }
        } else {
            return null
        }
    }

    private fun GameState.forcedMove(): Int? {
        val forcingLine = LINES
            .firstOrNull {
                it.count { i -> cells[i] != null && cells[i] != toMove } == 3 && it.count { i -> cells[i] == null } == 1
            }
        if (forcingLine != null) {
            return forcingLine.first { cells[it] == null }
        } else {
            return null
        }
    }

    private fun findForcedSequence(state: GameState, depth: Int = 0): Int? {
        val possibleMoves = (0..63).filter {
            state.isEmpty(it)
        }
        val winningMove = state.winningMove()
        if (winningMove != null) {
            return winningMove
        }
        if (depth == 4) return null
        val forcingMoves = possibleMoves.filter {
            state.move(it).forcedMove() != null
        }
        return forcingMoves
            .filter {
                val nextState = state.move(it)
                if (nextState.winningMove() != null) {
                    false
                } else {
                    val nextNextState = nextState.move(nextState.forcedMove()!!)
                    findForcedSequence(nextNextState, depth + 1) != null
                }
            }.firstOrNull()
    }

    override fun move(state: GameState): Int {
        val forcedSequenceMove = findForcedSequence(state)
        if (forcedSequenceMove != null) {
            return forcedSequenceMove
        }
        val possibleMoves = (0..63).filter {
            state.isEmpty(it)
        }
        val minimumValue = possibleMoves.minOf { heuristic(state.move(it)) }
        val bestMoves = possibleMoves.filter { heuristic(state.move(it)) == minimumValue }
        return bestMoves[Random.nextInt(bestMoves.size)]
    }
}