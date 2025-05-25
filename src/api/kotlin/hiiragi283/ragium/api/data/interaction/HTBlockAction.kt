package hiiragi283.ragium.api.data.interaction

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.extension.toRegistryStream
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import java.util.function.Function

interface HTBlockAction {
    companion object {
        @JvmField
        val CODEC: Codec<HTBlockAction> =
            RagiumRegistries.BLOCK_ACTION_SERIALIZERS.byNameCodec().dispatch(HTBlockAction::codec, Function.identity())

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTBlockAction> = CODEC.toRegistryStream()
    }

    val codec: MapCodec<out HTBlockAction>

    fun applyAction(context: UseOnContext)

    interface ItemPreview : HTBlockAction {
        fun getPreviewStack(): ItemStack
    }
}
