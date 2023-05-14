package qubic.strategy

import qubic.core.GameState

interface GameBot {
    fun move(state: GameState): Int
}