package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.item.HTTooltipProvider
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.text.blockPosText
import hiiragi283.ragium.api.text.bracketText
import hiiragi283.ragium.api.text.joinedText
import hiiragi283.ragium.api.text.levelText
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
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

    fun getDescription(): MutableComponent = bracketText(joinedText(levelText(dimension), blockPosText(pos)))

    override fun addToTooltip(context: Item.TooltipContext, consumer: (Component) -> Unit, flag: TooltipFlag) {
        getDescription().let(consumer)
    }

    operator fun component1(): ResourceKey<Level> = dimension

    operator fun component2(): BlockPos = pos
}
