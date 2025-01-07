package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.extension.createContext
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockManager
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTDistillationTowerScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTDistillationTowerBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.DISTILLATION_TOWER, pos, state),
    HTMultiblockProvider.Machine {
    override var machineKey: HTMachineKey = RagiumMachineKeys.DISTILLATION_TOWER

    override val inventory: HTMachineInventory = HTMachineInventory.Builder(2).output(1).build()

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage
        .Builder(4)
        .input(0)
        .output(1, 2, 3)
        .build(this)

    override val processor = HTMachineRecipeProcessor(
        inventory,
        intArrayOf(),
        intArrayOf(1),
        0,
        fluidStorage,
        intArrayOf(0),
        intArrayOf(1, 2, 3),
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTDistillationTowerScreenHandler(syncId, playerInventory, createContext())

    //    HTMultiblockPatternProvider    //

    override val multiblockManager: HTMultiblockManager = HTMultiblockManager(::getWorld, pos, this)
}
