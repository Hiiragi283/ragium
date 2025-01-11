package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineProvider
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.processor.HTMachineRecipeProcessor
import net.minecraft.block.BlockState
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

    override val processor = HTMachineRecipeProcessor(machineKey, inventory, fluidStorage)
}
