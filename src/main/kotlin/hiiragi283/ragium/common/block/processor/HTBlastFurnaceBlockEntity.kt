package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import hiiragi283.ragium.common.inventory.HTBlastFurnaceMenu
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState

class HTBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiItemMachineBlockEntity(
        RagiumBlockEntityTypes.BLAST_FURNACE,
        pos,
        state,
        HTMachineType.BLAST_FURNACE,
        HTRecipeTypes.BLAST_FURNACE,
    ),
    HTMultiblockController {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.DEFAULT

    override fun process(level: ServerLevel, pos: BlockPos) {
        validateMultiblock(this, null).getOrThrow()
        super.process(level, pos)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        if (validateMultiblock(this, player).isSuccess) {
            HTBlastFurnaceMenu(
                containerId,
                playerInventory,
                blockPos,
                firstInputSlot,
                secondInputSlot,
                thirdInputSlot,
                outputSlot,
            )
        } else {
            null
        }

    //    HTMultiblockController    //

    override fun getMultiblockMap(): HTMultiblockMap.Relative = RagiumMultiblockMaps.BLAST_FURNACE

    override fun getDefinition(): HTControllerDefinition? = level?.let { HTControllerDefinition(it, pos, front) }
}
