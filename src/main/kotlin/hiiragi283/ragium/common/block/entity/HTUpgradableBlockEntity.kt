package hiiragi283.ragium.common.block.entity

import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.upgrade.HTUpgradeHandler
import hiiragi283.ragium.common.block.entity.component.HTMachineUpgradeComponent
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

abstract class HTUpgradableBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state),
    HTUpgradeHandler {
    //    HTUpgradeHandler    //

    override fun initializeVariables() {
        super.initializeVariables()
        this.machineUpgrade = HTMachineUpgradeComponent(this)
    }

    lateinit var machineUpgrade: HTMachineUpgradeComponent
        private set

    final override fun getUpgrades(): List<HTItemResourceType> = machineUpgrade.getUpgrades()

    final override fun isValidUpgrade(upgrade: HTItemResourceType): Boolean = machineUpgrade.isValidUpgrade(upgrade)
}
