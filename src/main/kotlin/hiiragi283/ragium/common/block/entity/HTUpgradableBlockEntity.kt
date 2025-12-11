package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.block.entity.HTBlockEntityWithUpgrade
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.common.block.entity.component.HTMachineUpgradeComponent
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTUpgradableBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state),
    HTBlockEntityWithUpgrade {
    //    HTBlockEntityWithUpgrade    //

    override fun initializeVariables() {
        super.initializeVariables()
        this.machineUpgrade = HTMachineUpgradeComponent(this)
    }

    lateinit var machineUpgrade: HTMachineUpgradeComponent
        private set

    final override fun hasUpgrade(item: ItemLike): Boolean = machineUpgrade.hasUpgrade(item)

    final override fun getMachineUpgrades(): List<Pair<HTMachineUpgrade, Int>> = machineUpgrade.getMachineUpgrades()

    final override fun canApplyUpgrade(stack: ItemStack): Boolean = machineUpgrade.canApplyUpgrade(stack)
}
