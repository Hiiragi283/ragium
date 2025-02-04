package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack

data class HTChancedItemStack(val stack: ItemStack, val chance: Float) {
    companion object {
        @JvmField
        val CODEC: Codec<HTChancedItemStack> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ItemStack.STRICT_CODEC.fieldOf("stack").forGetter(HTChancedItemStack::stack),
                    Codec.floatRange(0f, 1f).fieldOf("chance").forGetter(HTChancedItemStack::chance),
                ).apply(instance, ::HTChancedItemStack)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTChancedItemStack> = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            HTChancedItemStack::stack,
            ByteBufCodecs.FLOAT,
            HTChancedItemStack::chance,
            ::HTChancedItemStack,
        )
    }
}
