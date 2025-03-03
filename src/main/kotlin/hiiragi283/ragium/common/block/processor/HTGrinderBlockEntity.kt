package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.HTSingleItemMenu
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState

class HTGrinderBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemMachineBlockEntity(
        RagiumBlockEntityTypes.GRINDER,
        pos,
        state,
        HTMachineType.GRINDER,
        HTRecipeTypes.GRINDER,
    ) {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.DEFAULT

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTSingleItemMenu(containerId, playerInventory, blockPos, inputSlot, catalystSlot, outputSlot)
}
