package qubic.rendering

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import qubic.core.*
import kotlin.math.atan2

class GameRenderer(
    var selectable: Boolean = false,
    var onClick: (Int) -> Unit = {}
) {
    companion object {
        const val SENSITIVITY = 0.01f
        const val PERSPECTIVE = 40f
        const val UNSELECTED_RADIUS = 3f
        const val SELECTED_RADIUS = 10f
        const val SCALE = 40f
        const val SELECTION_RANGE = 10f
        const val SELECTION_OPACITY = 0.3f
        val POINTS = listOf(
            Vector(-3f, -3f, -3f),
            Vector(-3f, -3f, -1f),
            Vector(-3f, -3f, 1f),
            Vector(-3f, -3f, 3f),
            Vector(-3f, -1f, -3f),
            Vector(-3f, -1f, -1f),
            Vector(-3f, -1f, 1f),
            Vector(-3f, -1f, 3f),
            Vector(-3f, 1f, -3f),
            Vector(-3f, 1f, -1f),
            Vector(-3f, 1f, 1f),
            Vector(-3f, 1f, 3f),
            Vector(-3f, 3f, -3f),
            Vector(-3f, 3f, -1f),
            Vector(-3f, 3f, 1f),
            Vector(-3f, 3f, 3f),
            Vector(-1f, -3f, -3f),
            Vector(-1f, -3f, -1f),
            Vector(-1f, -3f, 1f),
            Vector(-1f, -3f, 3f),
            Vector(-1f, -1f, -3f),
            Vector(-1f, -1f, -1f),
            Vector(-1f, -1f, 1f),
            Vector(-1f, -1f, 3f),
            Vector(-1f, 1f, -3f),
            Vector(-1f, 1f, -1f),
            Vector(-1f, 1f, 1f),
            Vector(-1f, 1f, 3f),
            Vector(-1f, 3f, -3f),
            Vector(-1f, 3f, -1f),
            Vector(-1f, 3f, 1f),
            Vector(-1f, 3f, 3f),
            Vector(1f, -3f, -3f),
            Vector(1f, -3f, -1f),
            Vector(1f, -3f, 1f),
            Vector(1f, -3f, 3f),
            Vector(1f, -1f, -3f),
            Vector(1f, -1f, -1f),
            Vector(1f, -1f, 1f),
            Vector(1f, -1f, 3f),
            Vector(1f, 1f, -3f),
            Vector(1f, 1f, -1f),
            Vector(1f, 1f, 1f),
            Vector(1f, 1f, 3f),
            Vector(1f, 3f, -3f),
            Vector(1f, 3f, -1f),
            Vector(1f, 3f, 1f),
            Vector(1f, 3f, 3f),
            Vector(3f, -3f, -3f),
            Vector(3f, -3f, -1f),
            Vector(3f, -3f, 1f),
            Vector(3f, -3f, 3f),
            Vector(3f, -1f, -3f),
            Vector(3f, -1f, -1f),
            Vector(3f, -1f, 1f),
            Vector(3f, -1f, 3f),
            Vector(3f, 1f, -3f),
            Vector(3f, 1f, -1f),
            Vector(3f, 1f, 1f),
            Vector(3f, 1f, 3f),
            Vector(3f, 3f, -3f),
            Vector(3f, 3f, -1f),
            Vector(3f, 3f, 1f),
            Vector(3f, 3f, 3f),
        )
        val LINES = listOf(
            Vector(-3f, -3f, -3f) to Vector(-3f, -3f, 3f),
            Vector(-3f, -3f, -3f) to Vector(-3f, 3f, -3f),
            Vector(-3f, -3f, -3f) to Vector(3f, -3f, -3f),
            Vector(-3f, -1f, -3f) to Vector(-3f, -1f, 3f),
            Vector(-3f, -3f, -1f) to Vector(-3f, 3f, -1f),
            Vector(-3f, -3f, -1f) to Vector(3f, -3f, -1f),
            Vector(-3f, 1f, -3f) to Vector(-3f, 1f, 3f),
            Vector(-3f, -3f, 1f) to Vector(-3f, 3f, 1f),
            Vector(-3f, -3f, 1f) to Vector(3f, -3f, 1f),
            Vector(-3f, 3f, -3f) to Vector(-3f, 3f, 3f),
            Vector(-3f, -3f, 3f) to Vector(-3f, 3f, 3f),
            Vector(-3f, -3f, 3f) to Vector(3f, -3f, 3f),
            Vector(-1f, -3f, -3f) to Vector(-1f, -3f, 3f),
            Vector(-1f, -3f, -3f) to Vector(-1f, 3f, -3f),
            Vector(-3f, -1f, -3f) to Vector(3f, -1f, -3f),
            Vector(-1f, -1f, -3f) to Vector(-1f, -1f, 3f),
            Vector(-1f, -3f, -1f) to Vector(-1f, 3f, -1f),
            Vector(-3f, -1f, -1f) to Vector(3f, -1f, -1f),
            Vector(-1f, 1f, -3f) to Vector(-1f, 1f, 3f),
            Vector(-1f, -3f, 1f) to Vector(-1f, 3f, 1f),
            Vector(-3f, -1f, 1f) to Vector(3f, -1f, 1f),
            Vector(-1f, 3f, -3f) to Vector(-1f, 3f, 3f),
            Vector(-1f, -3f, 3f) to Vector(-1f, 3f, 3f),
            Vector(-3f, -1f, 3f) to Vector(3f, -1f, 3f),
            Vector(1f, -3f, -3f) to Vector(1f, -3f, 3f),
            Vector(1f, -3f, -3f) to Vector(1f, 3f, -3f),
            Vector(-3f, 1f, -3f) to Vector(3f, 1f, -3f),
            Vector(1f, -1f, -3f) to Vector(1f, -1f, 3f),
            Vector(1f, -3f, -1f) to Vector(1f, 3f, -1f),
            Vector(-3f, 1f, -1f) to Vector(3f, 1f, -1f),
            Vector(1f, 1f, -3f) to Vector(1f, 1f, 3f),
            Vector(1f, -3f, 1f) to Vector(1f, 3f, 1f),
            Vector(-3f, 1f, 1f) to Vector(3f, 1f, 1f),
            Vector(1f, 3f, -3f) to Vector(1f, 3f, 3f),
            Vector(1f, -3f, 3f) to Vector(1f, 3f, 3f),
            Vector(-3f, 1f, 3f) to Vector(3f, 1f, 3f),
            Vector(3f, -3f, -3f) to Vector(3f, -3f, 3f),
            Vector(3f, -3f, -3f) to Vector(3f, 3f, -3f),
            Vector(-3f, 3f, -3f) to Vector(3f, 3f, -3f),
            Vector(3f, -1f, -3f) to Vector(3f, -1f, 3f),
            Vector(3f, -3f, -1f) to Vector(3f, 3f, -1f),
            Vector(-3f, 3f, -1f) to Vector(3f, 3f, -1f),
            Vector(3f, 1f, -3f) to Vector(3f, 1f, 3f),
            Vector(3f, -3f, 1f) to Vector(3f, 3f, 1f),
            Vector(-3f, 3f, 1f) to Vector(3f, 3f, 1f),
            Vector(3f, 3f, -3f) to Vector(3f, 3f, 3f),
            Vector(3f, -3f, 3f) to Vector(3f, 3f, 3f),
            Vector(-3f, 3f, 3f) to Vector(3f, 3f, 3f),
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun render(
        modifier: Modifier,
        state: GameState? = null
    ) {
        var coordinateSystem by remember { mutableStateOf(CoordinateSystem()) }
        var lastOffset by remember { mutableStateOf(Offset.Unspecified) }
        var isMouseDown by remember { mutableStateOf(false) }
        var selectionIndex: Int? by remember { mutableStateOf(null) }

        Canvas(
            modifier = modifier
                .onPointerEvent(PointerEventType.Press) {
                    val curOffset = it.changes.first().position
                    lastOffset = curOffset
                    isMouseDown = true
                }
                .onPointerEvent(PointerEventType.Release) {
                    if (state != null) {
                        if (selectionIndex != null && state.isEmpty(selectionIndex!!)) {
                            onClick(selectionIndex!!)
                        }
                    }
                    isMouseDown = false
                }
                .onPointerEvent(PointerEventType.Move) { e ->
                    val curOffset = e.changes.first().position
                    if (isMouseDown) {
                        val difOffset = curOffset - lastOffset
                        val angle = atan2(difOffset.x, difOffset.y)
                        coordinateSystem = coordinateSystem
                            .rotateAboutZ(-angle)
                            .rotateAboutX(SENSITIVITY * difOffset.getDistance())
                            .rotateAboutZ(angle)
                        lastOffset = curOffset
                        selectionIndex = null
                    } else {
                        if (state != null) {
                            if (selectable) {
                                if (curOffset != Offset.Unspecified) {
                                    val withinRange = POINTS
                                        .map {
                                            val point = coordinateSystem.apply(it)
                                            val perspectiveConstant = PERSPECTIVE / (PERSPECTIVE - point.z)
                                            val centerOffset = Offset(
                                                size.width / 2 + SCALE * point.x * perspectiveConstant,
                                                size.height / 2 - SCALE * point.y * perspectiveConstant
                                            )
                                            (curOffset - centerOffset).getDistance()
                                        }
                                        .withIndex()
                                        .filter { it.value < SELECTION_RANGE }
                                    selectionIndex =
                                        if (withinRange.isNotEmpty()) {
                                            withinRange.minBy { it.value }.index
                                        } else {
                                            null
                                        }
                                }
                            }
                        }
                    }
                }
        ) {
            fun color(id: PlayerId) = when (id) {
                PlayerId.FIRST -> Color.Red
                PlayerId.SECOND -> Color.Blue
            }

            fun perspectiveConstant(point: Vector) = PERSPECTIVE / (PERSPECTIVE - coordinateSystem.apply(point).z)

            fun pointToOffset(point: Vector) = Offset(
                    size.width / 2 + SCALE * coordinateSystem.apply(point).x * perspectiveConstant(point),
                    size.height / 2 - SCALE * coordinateSystem.apply(point).y * perspectiveConstant(point)
                )

            LINES.forEach { (point1, point2) ->
                drawLine(
                    color = Color.LightGray,
                    start = pointToOffset(point1),
                    end = pointToOffset(point2)
                )
            }
            POINTS
                .withIndex()
                .sortedBy { coordinateSystem.apply(it.value).z }
                .forEach { (i, point) ->
                    if (state != null) {
                        when (state.cells[i]) {
                            null -> {
                                drawCircle(
                                    color = Color.Black,
                                    center = pointToOffset(point),
                                    radius = UNSELECTED_RADIUS * perspectiveConstant(point)
                                )
                                if (selectionIndex == i) {
                                    drawCircle(
                                        color = color(state.toMove),
                                        center = pointToOffset(point),
                                        radius = SELECTED_RADIUS * perspectiveConstant(point),
                                        alpha = SELECTION_OPACITY
                                    )
                                }
                            }

                            else ->
                                drawCircle(
                                    color = color(state.cells[i]!!),
                                    center = pointToOffset(point),
                                    radius = SELECTED_RADIUS * perspectiveConstant(point),
                                )
                        }
                    } else {
                        drawCircle(
                            color = Color.Black,
                            center = pointToOffset(point),
                            radius = UNSELECTED_RADIUS * perspectiveConstant(point)
                        )
                    }
                }
        }
    }
}