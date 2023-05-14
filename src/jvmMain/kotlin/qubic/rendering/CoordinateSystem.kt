package qubic.rendering

data class CoordinateSystem(
    val basis: List<Vector> =
        listOf(
            Vector(1f, 0f, 0f),
            Vector(0f, 1f, 0f),
            Vector(0f, 0f, 1f)
        )
) {
    fun rotateAboutX(angle: Float) = CoordinateSystem(basis.map { it.rotateAboutX(angle) })
    fun rotateAboutY(angle: Float) = CoordinateSystem(basis.map { it.rotateAboutY(angle) })
    fun rotateAboutZ(angle: Float) = CoordinateSystem(basis.map { it.rotateAboutZ(angle) })
    fun apply(v: Vector): Vector {
        val (i, j, k) = basis
        return Vector(
            v.x * i.x + v.y * j.x + v.z * k.x,
            v.x * i.y + v.y * j.y + v.z * k.y,
            v.x * i.z + v.y * j.z + v.z * k.z,
        )
    }
}