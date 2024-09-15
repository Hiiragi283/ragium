package hiiragi283.ragium.integration.jade

import hiiragi283.ragium.common.block.entity.HTBurningBoxBlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTBurningBoxProvider : IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    private const val BURN_TIME: String = "BurnTime"

    //    IBlockComponentProvider    //

    override fun getUid(): Identifier = RagiumJadePlugin.BURNING_BOX

    override fun appendTooltip(iTooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        accessor.ifPresent(BURN_TIME) {
            iTooltip.add(Text.literal("Burn Time ${accessor.serverData.getInt(BURN_TIME)}"))
        }
    }

    //    IServerDataProvider    //

    override fun appendServerData(nbt: NbtCompound, accessor: BlockAccessor) {
        (accessor.blockEntity as HTBurningBoxBlockEntity).let {
            nbt.putInt(BURN_TIME, it.burningTime)
        }
    }
}
