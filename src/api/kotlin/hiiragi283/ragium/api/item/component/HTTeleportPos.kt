package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.item.HTTooltipProvider
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.text.intText
import hiiragi283.ragium.api.text.levelText
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

@JvmInline
value class HTTeleportPos(val globalPos: GlobalPos) : HTTooltipProvider {
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

    override fun addToTooltip(context: Item.TooltipContext, consumer: (Component) -> Unit, flag: TooltipFlag) {
        consumer(
            RagiumTranslation.TOOLTIP_DIMENSION.translate(
                ChatFormatting.GRAY,
                ChatFormatting.WHITE,
                levelText(dimension),
            ),
        )
        consumer(
            RagiumTranslation.TOOLTIP_BLOCK_POS.translate(
                ChatFormatting.GRAY,
                ChatFormatting.WHITE,
                intText(pos.x),
                ChatFormatting.WHITE,
                intText(pos.y),
                ChatFormatting.WHITE,
                intText(pos.z),
            ),
        )
    }

    operator fun component1(): ResourceKey<Level> = dimension

    operator fun component2(): BlockPos = pos
}
