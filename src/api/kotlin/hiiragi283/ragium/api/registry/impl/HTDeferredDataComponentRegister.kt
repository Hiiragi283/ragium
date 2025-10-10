package hiiragi283.ragium.api.registry.impl

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.RegistryKey
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.serialization.codec.BiCodec
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation

class HTDeferredDataComponentRegister(registryKey: RegistryKey<DataComponentType<*>>, namespace: String) :
    HTDeferredRegister<DataComponentType<*>>(
        registryKey,
        namespace,
    ) {
    fun <DATA : Any> registerType(name: String, builderAction: (DataComponentType.Builder<DATA>) -> Unit): DataComponentType<DATA> {
        val type: DataComponentType<DATA> = DataComponentType
            .builder<DATA>()
            .apply(builderAction)
            .build()
        register(name) { _: ResourceLocation -> type }
        return type
    }

    fun <DATA : Any> registerType(
        name: String,
        codec: Codec<DATA>,
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, DATA>?,
    ): DataComponentType<DATA> = registerType(name) { builder: DataComponentType.Builder<DATA> ->
        builder.persistent(codec)
        streamCodec?.let(builder::networkSynchronized)
    }

    fun <DATA : Any> registerType(name: String, codec: BiCodec<in RegistryFriendlyByteBuf, DATA>): DataComponentType<DATA> =
        registerType(name, codec.codec, codec.streamCodec)
}
