package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.COMPRESSOR, pos, state) {
    override val machineKey: HTMachineKey = RagiumMachineKeys.COMPRESSOR

    override val inventory: HTMachineInventory = HTMachineInventory(3, intArrayOf(0), 1, intArrayOf(2))

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.EMPTY
}
