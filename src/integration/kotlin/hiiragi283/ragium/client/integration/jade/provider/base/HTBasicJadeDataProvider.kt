package hiiragi283.ragium.client.integration.jade.provider.base

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.serialization.codec.BiCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.Accessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

abstract class HTBasicJadeDataProvider<ACCESSOR : Accessor<*>, DATA : Any>(
    private val id: ResourceLocation,
    private val streamCodec: StreamCodec<RegistryFriendlyByteBuf, DATA>,
) : HTJadeDataProvider<ACCESSOR, DATA> {
    constructor(id: ResourceLocation, codec: BiCodec<RegistryFriendlyByteBuf, DATA>) : this(id, codec.streamCodec)

    constructor(path: String, streamCodec: StreamCodec<RegistryFriendlyByteBuf, DATA>) : this(RagiumAPI.id(path), streamCodec)

    constructor(path: String, codec: BiCodec<RegistryFriendlyByteBuf, DATA>) : this(path, codec.streamCodec)

    abstract override fun streamData(accessor: ACCESSOR): DATA?

    final override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, DATA> = streamCodec

    final override fun getUid(): ResourceLocation = id

    final override fun appendTooltip(tooltip: ITooltip, accessor: ACCESSOR, config: IPluginConfig) {
        decodeFromData(accessor).ifPresent { data: DATA ->
            appendTooltip(tooltip, accessor, config, data)
        }
    }
}
