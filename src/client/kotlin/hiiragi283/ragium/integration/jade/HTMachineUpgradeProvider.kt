package hiiragi283.ragium.integration.jade

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.machine.HTMachineUpgradeList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.StreamServerDataProvider
import snownee.jade.api.config.IPluginConfig

data object HTMachineUpgradeProvider {
    @JvmField
    val ID: Identifier = RagiumAPI.id("machine_upgrade")

    data object Server : StreamServerDataProvider<BlockAccessor, HTMachineUpgradeList> {
        override fun streamData(accessor: BlockAccessor): HTMachineUpgradeList? =
            accessor.blockEntity?.components()?.get(RagiumDataComponents.UPGRADES)

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, HTMachineUpgradeList> = HTMachineUpgradeList.STREAM_CODEC

        override fun getUid(): Identifier = ID
    }

    data object Client : IBlockComponentProvider {
        override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
            Server.decodeFromData(accessor).ifPresent { it.addToTooltip(tooltip::add) }
        }

        override fun getUid(): Identifier = ID
    }
}
