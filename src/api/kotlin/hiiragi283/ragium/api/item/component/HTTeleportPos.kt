package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.serialization.codec.BiCodec
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

@JvmInline
value class HTTeleportPos(val globalPos: GlobalPos) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTTeleportPos> = BiCodec
            .of(GlobalPos.CODEC, GlobalPos.STREAM_CODEC)
            .xmap(::HTTeleportPos, HTTeleportPos::globalPos)
    }
    constructor(dimension: ResourceKey<Level>, pos: BlockPos) : this(GlobalPos(dimension, pos))

    constructor(dimension: ResourceKey<Level>, x: Int, y: Int, z: Int) : this(dimension, BlockPos(x, y, z))

    val dimension: ResourceKey<Level> get() = globalPos.dimension
    val pos: BlockPos get() = globalPos.pos

    operator fun component1(): ResourceKey<Level> = dimension

    operator fun component2(): BlockPos = pos
}
