package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.extension.createContext
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineProvider
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.processor.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
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

    override val processor = HTMachineRecipeProcessor(
        machineKey,
        inventory,
        intArrayOf(0, 1),
        intArrayOf(3, 4),
        2,
        fluidStorage,
        intArrayOf(0),
        intArrayOf(1),
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSimpleMachineScreenHandler(syncId, playerInventory, createContext())
}
