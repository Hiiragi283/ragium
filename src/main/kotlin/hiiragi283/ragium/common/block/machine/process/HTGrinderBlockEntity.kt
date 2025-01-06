package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.extension.createContext
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTGrinderScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTGrinderBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.GRINDER, pos, state) {
    override val machineKey: HTMachineKey = RagiumMachineKeys.GRINDER

    override val inventory: HTMachineInventory = HTMachineInventory
        .Builder(5)
        .input(0)
        .output(2, 3, 4)
        .build()

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.ofSmall(this)

    override val processor = HTMachineRecipeProcessor(
        inventory,
        intArrayOf(0),
        intArrayOf(2, 3, 4),
        1,
        fluidStorage,
        intArrayOf(0),
        intArrayOf(1),
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTGrinderScreenHandler(syncId, playerInventory, createContext())
}
