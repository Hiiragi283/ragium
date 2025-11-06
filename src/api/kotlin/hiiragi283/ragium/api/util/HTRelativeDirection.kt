package hiiragi283.ragium.api.util

import net.minecraft.core.Direction
import net.minecraft.util.StringRepresentable

/**
 * 相対的な方角を管理するクラス
 * @see Direction
 */
enum class HTRelativeDirection : StringRepresentable {
    DOWN,
    UP,
    FRONT,
    RIGHT,
    BACK,
    LEFT,
    ;

    companion object {
        /**
         * 指定した値から[HTRelativeDirection]を返します。
         * @param front 正面となる方角
         * @param target 変換したい方角
         * @throws IllegalArgumentException [front]が[Direction.UP]か[Direction.DOWN]の場合
         */
        @JvmStatic
        fun fromDirection(front: Direction, target: Direction): HTRelativeDirection = when (front) {
            Direction.NORTH -> getHorizontalSide(target)
            Direction.SOUTH -> getHorizontalSide(target.opposite)
            Direction.WEST -> getHorizontalSide(
                when (target) {
                    Direction.UP -> Direction.UP
                    Direction.DOWN -> Direction.DOWN
                    else -> target.clockWise
                },
            )

            Direction.EAST -> getHorizontalSide(
                when (target) {
                    Direction.UP -> Direction.UP
                    Direction.DOWN -> Direction.DOWN
                    else -> target.counterClockWise
                },
            )
            else -> throw IllegalArgumentException("Unsupported front direction: $target!")
        }

        private fun getHorizontalSide(target: Direction): HTRelativeDirection = when (target) {
            Direction.DOWN -> DOWN
            Direction.UP -> UP
            Direction.NORTH -> FRONT
            Direction.SOUTH -> BACK
            Direction.WEST -> LEFT
            Direction.EAST -> RIGHT
        }
    }

    /**
     * この方角を[Direction]に変換します。
     * @param front 正面となる方角
     */
    fun toDirection(front: Direction): Direction = when (this) {
        DOWN -> Direction.DOWN
        UP -> Direction.UP
        FRONT -> front
        RIGHT -> front.clockWise
        BACK -> front.opposite
        LEFT -> front.counterClockWise
    }

    override fun getSerializedName(): String = name.lowercase()
}
