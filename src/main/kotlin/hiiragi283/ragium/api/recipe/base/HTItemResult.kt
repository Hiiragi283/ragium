package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.extension.toRegistryStream
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.ItemEnchantments
import java.util.function.Function

interface HTItemResult {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemResult> = RagiumRegistries.ITEM_RESULT
            .byNameCodec()
            .dispatch(HTItemResult::getCodec, Function.identity())

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> =
            ByteBufCodecs
                .registry(RagiumRegistries.Keys.ITEM_RESULT)
                .dispatch(HTItemResult::getCodec, MapCodec<out HTItemResult>::toRegistryStream)
    }

    fun getCodec(): MapCodec<out HTItemResult>

    fun getResultId(): ResourceLocation

    fun getItem(enchantments: ItemEnchantments): ItemStack
}
