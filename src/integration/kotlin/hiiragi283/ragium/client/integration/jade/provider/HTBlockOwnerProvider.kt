package hiiragi283.ragium.client.integration.jade.provider

import hiiragi283.ragium.api.block.entity.HTOwnedBlockEntity
import hiiragi283.ragium.api.serialization.codec.BiCodec
import net.minecraft.network.chat.Component
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTBlockOwnerProvider : HTBlockDataProvider<String>("block_owner", BiCodec.STRING.cast()) {
    override fun streamData(accessor: BlockAccessor): String? = (accessor.blockEntity as? HTOwnedBlockEntity)?.getOwnerName()

    override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: String,
    ) {
        tooltip.add(Component.translatable("jade.owner", data))
    }
}
