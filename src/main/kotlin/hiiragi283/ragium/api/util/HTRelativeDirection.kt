package hiiragi283.ragium.api.util

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.identifiedCodec
import hiiragi283.ragium.api.extension.identifiedPacketCodec
import hiiragi283.ragium.api.util.HTRelativeDirection.entries
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.Direction

/**
 * Represent relative direction
 * @see [Direction]
 */
enum class HTRelativeDirection : StringIdentifiable {
    DOWN,
    UP,
    FRONT,
    RIGHT,
    BACK,
    LEFT,
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTRelativeDirection> = identifiedCodec(entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTRelativeDirection> = identifiedPacketCodec(entries)

        /**
         * Transform [target] into [HTRelativeDirection]
         * @throws IllegalArgumentException if [front] is [Direction.UP] or [Direction.DOWN]
         */
        @JvmStatic
        fun fromDirection(front: Direction, target: Direction): HTRelativeDirection = when (front) {
            Direction.NORTH -> getHorizontalSide(target)
            Direction.SOUTH -> getHorizontalSide(target.opposite)
            Direction.WEST -> getHorizontalSide(target.rotateYClockwise())
            Direction.EAST -> getHorizontalSide(target.rotateYCounterclockwise())
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
     * Transform relative direction into [Direction]
     */
    fun toDirection(front: Direction): Direction = when (this) {
        DOWN -> Direction.DOWN
        UP -> Direction.UP
        FRONT -> front
        RIGHT -> front.rotateYClockwise()
        BACK -> front.opposite
        LEFT -> front.rotateYCounterclockwise()
    }

    override fun asString(): String = name.lowercase()
}
