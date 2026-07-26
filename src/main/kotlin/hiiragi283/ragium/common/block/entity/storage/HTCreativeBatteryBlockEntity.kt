package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.support.storage.energy.HTInfiniteEnergyHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTCreativeBatteryBlockEntity(pos: BlockPos, state: BlockState) : HTBatteryBlockEntity<HTInfiniteEnergyHandler>(RagiumBlockEntityTypes.CREATIVE_BATTERY.get(), pos, state) {
    override val handler: HTInfiniteEnergyHandler get() = HTInfiniteEnergyHandler

    override fun isCreative(): Boolean = true

    override fun getSlotSyncType(): HTSyncType? = null
}
