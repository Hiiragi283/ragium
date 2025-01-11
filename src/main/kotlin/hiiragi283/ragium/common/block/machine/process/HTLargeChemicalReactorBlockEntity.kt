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

class HTLargeChemicalReactorBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.LARGE_CHEMICAL_REACTOR, pos, state) {
    override var machineKey: HTMachineKey = RagiumMachineKeys.LARGE_CHEMICAL_REACTOR

    override val inventory: HTMachineInventory = HTMachineInventory.ofLarge()

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.ofLarge(this)

    override val processor = HTMachineRecipeProcessor(machineKey, inventory, fluidStorage)
}
