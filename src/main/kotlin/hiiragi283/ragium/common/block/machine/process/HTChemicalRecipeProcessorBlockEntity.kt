package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.extension.createContext
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineProvider
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTChemicalMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTChemicalRecipeProcessorBlockEntity(pos: BlockPos, state: BlockState, override val machineKey: HTMachineKey) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.CHEMICAL_PROCESSOR, pos, state) {
    companion object {
        @JvmStatic
        fun fromState(pos: BlockPos, state: BlockState): HTChemicalRecipeProcessorBlockEntity {
            val machineKey: HTMachineKey =
                (state.block as? HTMachineProvider)?.machineKey ?: RagiumMachineKeys.BLAST_FURNACE
            return HTChemicalRecipeProcessorBlockEntity(pos, state, machineKey)
        }
    }

    override val inventory: HTMachineInventory = HTMachineInventory.ofSimple()

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.ofSimple(this)

    override val processor = HTMachineRecipeProcessor(
        inventory,
        intArrayOf(0, 1),
        intArrayOf(3, 4),
        2,
        fluidStorage,
        intArrayOf(0, 1),
        intArrayOf(2, 3),
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTChemicalMachineScreenHandler(syncId, playerInventory, createContext())
}
