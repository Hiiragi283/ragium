package hiiragi283.ragium.client.integration.jade

import hiiragi283.ragium.common.block.entity.machine.HTMachineBlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTMachineProvider : IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    private const val PROGRESS = "MachineProgress"
    private const val IS_ACTIVE = "MachineActive"
    private const val SHOW_PREVIEW = "ShowPreview"

    //    IBlockComponentProvider    //

    override fun getUid(): Identifier = RagiumJadePlugin.MACHINE

    override fun appendTooltip(iTooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        val serverData: NbtCompound = accessor.serverData
        if (serverData.contains(PROGRESS)) {
            val progress: Int = serverData.getInt(PROGRESS)
        }
        if (serverData.contains(IS_ACTIVE)) {
            val isActive: Boolean = serverData.getBoolean(IS_ACTIVE)
        }
        if (serverData.contains(SHOW_PREVIEW)) {
            val showPreview: Boolean = serverData.getBoolean(SHOW_PREVIEW)
        }
    }

    //    IServerDataProvider    //

    override fun appendServerData(nbt: NbtCompound, accessor: BlockAccessor) {
        (accessor.blockEntity as HTMachineBlockEntity).let {
            nbt.putInt(PROGRESS, it.ticks)
            nbt.putBoolean(IS_ACTIVE, it.isActive)
            // nbt.putBoolean(SHOW_PREVIEW, it.showPreview)
        }
    }
}
