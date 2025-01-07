package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.extension.createContext
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineProvider
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockManager
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTLargeMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTLargeRecipeProcessorBlockEntity(pos: BlockPos, state: BlockState, override val machineKey: HTMachineKey) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.LARGE_PROCESSOR, pos, state),
    HTMultiblockProvider.Machine {
    companion object {
        @JvmStatic
        fun fromState(pos: BlockPos, state: BlockState): HTLargeRecipeProcessorBlockEntity {
            val machineKey: HTMachineKey =
                (state.block as? HTMachineProvider)?.machineKey ?: RagiumMachineKeys.BLAST_FURNACE
            return HTLargeRecipeProcessorBlockEntity(pos, state, machineKey)
        }
    }

    override val inventory: HTMachineInventory = HTMachineInventory.ofLarge()

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.ofSimple(this)

    override val processor = HTMachineRecipeProcessor(
        inventory,
        intArrayOf(0, 1, 2),
        intArrayOf(4, 5, 6),
        3,
        fluidStorage,
        intArrayOf(0, 1),
        intArrayOf(2, 3),
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTLargeMachineScreenHandler(syncId, playerInventory, createContext())

    //    HTMultiblockProvider    //

    override val multiblockManager = HTMultiblockManager(::getWorld, pos, this)
}
