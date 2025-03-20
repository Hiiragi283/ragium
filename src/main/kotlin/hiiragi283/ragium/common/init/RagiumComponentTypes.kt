package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumComponentTypes {
    @JvmField
    val REGISTER: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Any> register(
        name: String,
        codec: Codec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>,
    ): DeferredHolder<DataComponentType<*>, DataComponentType<T>> =
        REGISTER.registerComponentType<T>(name) { builder: DataComponentType.Builder<T> ->
            builder.persistent(codec).networkSynchronized(streamCodec)
        }

    @JvmField
    val FLUID_CONTENT: DeferredHolder<DataComponentType<*>, DataComponentType<SimpleFluidContent>> =
        register("fluid_content", SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC)
}
