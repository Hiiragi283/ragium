package hiiragi283.ragium.client.integration.jade.provider

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.serialization.codec.BiCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.StreamServerDataProvider
import snownee.jade.api.config.IPluginConfig

abstract class HTBlockDataProvider<T : Any>(
    private val id: ResourceLocation,
    private val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>,
) : StreamServerDataProvider<BlockAccessor, T>,
    IBlockComponentProvider {
    constructor(id: ResourceLocation, codec: BiCodec<RegistryFriendlyByteBuf, T>) : this(id, codec.streamCodec)

    constructor(path: String, streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>) : this(RagiumAPI.id(path), streamCodec)

    constructor(path: String, codec: BiCodec<RegistryFriendlyByteBuf, T>) : this(path, codec.streamCodec)

    abstract override fun streamData(accessor: BlockAccessor): T?

    final override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec

    final override fun getUid(): ResourceLocation = id

    final override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        decodeFromData(accessor).ifPresent { data: T ->
            appendTooltip(tooltip, accessor, config, data)
        }
    }

    protected abstract fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: T,
    )
}
