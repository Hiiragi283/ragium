package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.processor.HTMachineRecipeProcessor
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTDistillationTowerBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.DISTILLATION_TOWER, pos, state) {
    override var machineKey: HTMachineKey = RagiumMachineKeys.DISTILLATION_TOWER

    override val inventory: HTMachineInventory = HTMachineInventory(2, intArrayOf(), 0, intArrayOf(1))

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage
        .Builder(4)
        .input(0)
        .output(1, 2, 3)
        .build(this)

    override val processor = HTMachineRecipeProcessor(machineKey, inventory, fluidStorage)
}
