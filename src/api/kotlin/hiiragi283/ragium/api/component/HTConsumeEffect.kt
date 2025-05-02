package hiiragi283.ragium.api.component

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

interface HTConsumeEffect {
    companion object {
        @JvmField
        val CODEC: Codec<HTConsumeEffect> =
            RagiumRegistries.CONSUME_EFFECT_TYPE.byNameCodec().dispatch(HTConsumeEffect::type, Type<*>::codec)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTConsumeEffect> =
            ByteBufCodecs
                .registry(RagiumRegistries.Keys.CONSUME_EFFECT_TYPE)
                .dispatch(HTConsumeEffect::type, Type<*>::streamCodec)
    }

    val type: Type<*>

    fun apply(level: Level, stack: ItemStack, user: LivingEntity): Boolean

    data class Type<T : HTConsumeEffect>(val codec: MapCodec<T>, val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>)
}
