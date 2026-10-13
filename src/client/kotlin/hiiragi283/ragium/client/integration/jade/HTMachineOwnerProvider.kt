package hiiragi283.ragium.client.integration.jade

import hiiragi283.lib.block.entity.HTOwnedBlockEntity
import hiiragi283.lib.text.HTTextUtil
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.ChatFormatting
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.StreamServerDataProvider
import snownee.jade.api.config.IPluginConfig

data object HTMachineOwnerProvider {
    @JvmField
    val ID: Identifier = RagiumAPI.id("machine_owner")

    data object Server : StreamServerDataProvider<BlockAccessor, String> {
        override fun streamData(accessor: BlockAccessor): String? =
            (accessor.blockEntity as? HTOwnedBlockEntity)?.getOwnerName()

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, String> = ByteBufCodecs.STRING_UTF8.cast()

        override fun getUid(): Identifier = ID
    }

    data object Client : IBlockComponentProvider {
        override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
            Server.decodeFromData(accessor).ifPresent {
                tooltip.add(HTTextUtil.smartTranslate("jade.owner", ChatFormatting.WHITE, it))
            }
        }

        override fun getUid(): Identifier = ID
    }
}
