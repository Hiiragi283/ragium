package hiiragi283.ragium.common.block.entity

import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.upgrade.HTSlotUpgradeHandler
import hiiragi283.ragium.common.block.entity.component.HTMachineUpgradeComponent
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

abstract class HTUpgradableBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state),
    HTSlotUpgradeHandler {
    //    HTSlotUpgradeHandler    //

    @DescSynced
    @Persisted(subPersisted = true)
    val machineUpgrade: HTMachineUpgradeComponent = HTMachineUpgradeComponent(this)

    final override fun getUpgradeSlots(): List<HTItemSlot> = machineUpgrade.getUpgradeSlots()

    override fun isValidUpgrade(upgrade: HTItemResourceType, existing: List<HTItemResourceType>): Boolean =
        machineUpgrade.isValidUpgrade(upgrade, existing)
}
