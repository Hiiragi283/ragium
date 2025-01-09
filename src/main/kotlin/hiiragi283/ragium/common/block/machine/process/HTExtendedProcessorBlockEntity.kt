package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.extension.createContext
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.multiblock.HTMultiblockData
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTLargeMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTExtendedProcessorBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.EXTENDED_PROCESSOR, pos, state) {
    companion object {
        private val DEFAULT_KEY: HTMachineKey = HTMachineKey.of(RagiumAPI.id("large_processor"))
    }

    override var machineKey: HTMachineKey = DEFAULT_KEY

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

    override fun processData(data: HTMultiblockData) {
        data.ifPresent(HTMachinePropertyKeys.EXTENDED_CHILD) { machineKey = it }
    }
}
