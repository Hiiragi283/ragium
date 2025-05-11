package hiiragi283.ragium.api.data.interaction

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import java.util.function.Function

interface HTBlockAction {
    companion object {
        @JvmField
        val CODEC: Codec<HTBlockAction> =
            RagiumRegistries.BLOCK_ACTION_SERIALIZERS.byNameCodec().dispatch(HTBlockAction::codec, Function.identity())
    }

    val codec: MapCodec<out HTBlockAction>

    fun applyAction(context: UseOnContext)

    interface ItemPreview : HTBlockAction {
        fun getPreviewStack(): ItemStack
    }
}
