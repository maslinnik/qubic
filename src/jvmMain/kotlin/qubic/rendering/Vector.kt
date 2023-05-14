package qubic.rendering

import kotlin.math.cos
import kotlin.math.sin

data class Vector(val x: Float, val y: Float, val z: Float) {
    fun rotateAboutX(angle: Float) = Vector(x, cos(angle) * y - sin(angle) * z, sin(angle) * y + cos(angle) * z)
    fun rotateAboutY(angle: Float) = Vector(cos(angle) * x + sin(angle) * z, y, -sin(angle) * x + cos(angle) * z)
    fun rotateAboutZ(angle: Float) = Vector(cos(angle) * x - sin(angle) * y, sin(angle) * x + cos(angle) * y, z)
}