package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.extension.createContext
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.processor.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTAssemblerScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTAssemblerBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.ASSEMBLER, pos, state) {
    override var machineKey: HTMachineKey = RagiumMachineKeys.ASSEMBLER

    override val inventory: HTMachineInventory = HTMachineInventory
        .Builder(6)
        .input(0, 1, 2, 3)
        .output(5)
        .build()

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.EMPTY

    override val processor = HTMachineRecipeProcessor(
        machineKey,
        inventory,
        intArrayOf(0, 1, 2, 3),
        intArrayOf(5),
        4,
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTAssemblerScreenHandler(syncId, playerInventory, createContext())
}
