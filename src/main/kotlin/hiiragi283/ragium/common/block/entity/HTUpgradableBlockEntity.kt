package hiiragi283.ragium.common.block.entity

import hiiragi283.core.api.stack.ImmutableItemStack
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

    override fun initializeVariables() {
        super.initializeVariables()
        this.machineUpgrade = HTMachineUpgradeComponent(this)
    }

    lateinit var machineUpgrade: HTMachineUpgradeComponent
        private set

    final override fun getUpgradeSlots(): List<HTItemSlot> = machineUpgrade.getUpgradeSlots()

    override fun isValidUpgrade(upgrade: ImmutableItemStack, existing: List<ImmutableItemStack>): Boolean =
        machineUpgrade.isValidUpgrade(upgrade, existing)
}
