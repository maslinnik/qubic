package qubic.core

val LINES = listOf(
    listOf(0, 1, 2, 3),
    listOf(0, 4, 8, 12),
    listOf(0, 16, 32, 48),
    listOf(4, 5, 6, 7),
    listOf(1, 5, 9, 13),
    listOf(1, 17, 33, 49),
    listOf(8, 9, 10, 11),
    listOf(2, 6, 10, 14),
    listOf(2, 18, 34, 50),
    listOf(12, 13, 14, 15),
    listOf(3, 7, 11, 15),
    listOf(3, 19, 35, 51),
    listOf(16, 17, 18, 19),
    listOf(16, 20, 24, 28),
    listOf(4, 20, 36, 52),
    listOf(20, 21, 22, 23),
    listOf(17, 21, 25, 29),
    listOf(5, 21, 37, 53),
    listOf(24, 25, 26, 27),
    listOf(18, 22, 26, 30),
    listOf(6, 22, 38, 54),
    listOf(28, 29, 30, 31),
    listOf(19, 23, 27, 31),
    listOf(7, 23, 39, 55),
    listOf(32, 33, 34, 35),
    listOf(32, 36, 40, 44),
    listOf(8, 24, 40, 56),
    listOf(36, 37, 38, 39),
    listOf(33, 37, 41, 45),
    listOf(9, 25, 41, 57),
    listOf(40, 41, 42, 43),
    listOf(34, 38, 42, 46),
    listOf(10, 26, 42, 58),
    listOf(44, 45, 46, 47),
    listOf(35, 39, 43, 47),
    listOf(11, 27, 43, 59),
    listOf(48, 49, 50, 51),
    listOf(48, 52, 56, 60),
    listOf(12, 28, 44, 60),
    listOf(52, 53, 54, 55),
    listOf(49, 53, 57, 61),
    listOf(13, 29, 45, 61),
    listOf(56, 57, 58, 59),
    listOf(50, 54, 58, 62),
    listOf(14, 30, 46, 62),
    listOf(60, 61, 62, 63),
    listOf(51, 55, 59, 63),
    listOf(15, 31, 47, 63),
    listOf(0, 5, 10, 15),
    listOf(3, 6, 9, 12),
    listOf(0, 17, 34, 51),
    listOf(3, 18, 33, 48),
    listOf(0, 20, 40, 60),
    listOf(12, 24, 36, 48),
    listOf(16, 21, 26, 31),
    listOf(19, 22, 25, 28),
    listOf(4, 21, 38, 55),
    listOf(7, 22, 37, 52),
    listOf(1, 21, 41, 61),
    listOf(13, 25, 37, 49),
    listOf(32, 37, 42, 47),
    listOf(35, 38, 41, 44),
    listOf(8, 25, 42, 59),
    listOf(11, 26, 41, 56),
    listOf(2, 22, 42, 62),
    listOf(14, 26, 38, 50),
    listOf(48, 53, 58, 63),
    listOf(51, 54, 57, 60),
    listOf(12, 29, 46, 63),
    listOf(15, 30, 45, 60),
    listOf(3, 23, 43, 63),
    listOf(15, 27, 39, 51),
    listOf(0, 21, 42, 63),
    listOf(3, 22, 41, 60),
    listOf(12, 25, 38, 51),
    listOf(15, 26, 37, 48),
)

class GameState(
    val cells: List<PlayerId?> = List(64) { null },
    val toMove: PlayerId = PlayerId.FIRST
) {
    fun isEmpty(cellIndex: Int): Boolean {
        assert(cellIndex in cells.indices)
        return cells[cellIndex] == null
    }

    fun move(cellIndex: Int): GameState {
        assert(isEmpty(cellIndex))
        assert(!isLost())
        return GameState(
            cells.mapIndexed { index, value -> if (index == cellIndex) toMove else value },
            if (toMove == PlayerId.FIRST) PlayerId.SECOND else PlayerId.FIRST
        )
    }

    fun isLost(): Boolean {
        return LINES.any {
            cells[it[0]] != null && it.all { i -> cells[i] == cells[it[0]] }
        }
    }

    fun isDrawn(): Boolean {
        return !isLost() && cells.none { it == null }
    }

    fun isEnded(): Boolean {
        return isLost() || isDrawn()
    }

    fun winner(): PlayerId? {
        return if (isLost()) {
            if (toMove == PlayerId.FIRST) PlayerId.SECOND else PlayerId.FIRST
        } else {
            null
        }
    }
}