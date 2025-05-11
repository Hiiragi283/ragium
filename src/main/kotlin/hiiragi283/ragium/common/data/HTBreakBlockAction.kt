package hiiragi283.ragium.common.data

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import net.minecraft.world.item.context.UseOnContext

class HTBreakBlockAction(private val dropBlock: Boolean) : HTBlockAction {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBreakBlockAction> =
            Codec.BOOL.fieldOf("drop_block").xmap(::HTBreakBlockAction, HTBreakBlockAction::dropBlock)
    }

    override val codec: MapCodec<out HTBlockAction> = CODEC

    override fun applyAction(context: UseOnContext) {
        context.level.destroyBlock(context.clickedPos, dropBlock)
    }
}
