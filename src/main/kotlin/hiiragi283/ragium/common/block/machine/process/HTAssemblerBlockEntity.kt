package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTAssemblerBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.ASSEMBLER, pos, state) {
    override var machineKey: HTMachineKey = RagiumMachineKeys.ASSEMBLER

    override val inventory: HTMachineInventory = HTMachineInventory(6, intArrayOf(0, 1, 2, 3), 4, intArrayOf(5))

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.EMPTY
}
