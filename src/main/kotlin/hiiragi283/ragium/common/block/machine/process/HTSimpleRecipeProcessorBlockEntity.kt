package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineProvider
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTSimpleRecipeProcessorBlockEntity(pos: BlockPos, state: BlockState, override val machineKey: HTMachineKey) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.SIMPLE_PROCESSOR, pos, state) {
    companion object {
        @JvmStatic
        fun fromState(pos: BlockPos, state: BlockState): HTSimpleRecipeProcessorBlockEntity {
            val machineKey: HTMachineKey =
                (state.block as? HTMachineProvider)?.machineKey ?: RagiumMachineKeys.ASSEMBLER
            return HTSimpleRecipeProcessorBlockEntity(pos, state, machineKey)
        }
    }

    override val inventory: HTMachineInventory = HTMachineInventory.ofSimple()

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.ofSmall(this)
}
