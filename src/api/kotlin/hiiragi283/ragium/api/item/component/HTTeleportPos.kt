package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.extension.blockPosText
import hiiragi283.ragium.api.extension.bracketText
import hiiragi283.ragium.api.extension.joinedText
import hiiragi283.ragium.api.extension.levelText
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

data class HTTeleportPos(val globalPos: GlobalPos) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTTeleportPos> = BiCodec
            .of(GlobalPos.CODEC, GlobalPos.STREAM_CODEC)
            .xmap(::HTTeleportPos, HTTeleportPos::globalPos)
    }
    constructor(dimension: ResourceKey<Level>, pos: BlockPos) : this(GlobalPos(dimension, pos))

    constructor(dimension: ResourceKey<Level>, x: Int, y: Int, z: Int) : this(dimension, BlockPos(x, y, z))

    val dimension: ResourceKey<Level> = globalPos.dimension
    val pos: BlockPos = globalPos.pos
    val x: Int = pos.x
    val y: Int = pos.y
    val z: Int = pos.z

    fun getDescription(): MutableComponent = bracketText(joinedText(levelText(dimension), blockPosText(pos)))
}
