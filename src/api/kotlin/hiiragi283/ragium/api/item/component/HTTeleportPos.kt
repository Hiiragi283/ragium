package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.item.HTTooltipProvider
import hiiragi283.ragium.api.network.blockPosText
import hiiragi283.ragium.api.network.bracketText
import hiiragi283.ragium.api.network.joinedText
import hiiragi283.ragium.api.network.levelText
import hiiragi283.ragium.api.serialization.codec.BiCodec
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

data class HTTeleportPos(val dimension: ResourceKey<Level>, val pos: BlockPos) : HTTooltipProvider {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTTeleportPos> = BiCodec
            .of(GlobalPos.CODEC, GlobalPos.STREAM_CODEC)
            .xmap(::HTTeleportPos, HTTeleportPos::globalPos)
    }
    constructor(globalPos: GlobalPos) : this(globalPos.dimension, globalPos.pos)

    constructor(dimension: ResourceKey<Level>, x: Int, y: Int, z: Int) : this(dimension, BlockPos(x, y, z))

    val globalPos = GlobalPos(dimension, pos)
    val x: Int = pos.x
    val y: Int = pos.y
    val z: Int = pos.z

    fun getDescription(): MutableComponent = bracketText(joinedText(levelText(dimension), blockPosText(pos)))

    override fun addToTooltip(context: Item.TooltipContext, consumer: (Component) -> Unit, flag: TooltipFlag) {
        getDescription().let(consumer)
    }
}
