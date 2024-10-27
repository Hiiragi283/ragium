package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.extension.resourceAmount
import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.inventory.HTStorageBuilder
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.entity.HTProcessorMachineEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.init.RagiumAdvancementCriteria
import hiiragi283.ragium.common.screen.HTLargeMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTLargeProcessorMachineEntity(type: HTMachineConvertible, tier: HTMachineTier) :
    HTProcessorMachineEntityBase(type, tier),
    HTMultiblockController {
    final override val parent: HTSimpleInventory = HTStorageBuilder(7)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSimple()

    final override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.Large()

    final override val processor: HTMachineRecipeProcessor = HTMachineRecipeProcessor.of(parent, fluidStorage)

    override fun createInput(): HTMachineInput = HTMachineInput.Large(
        definition,
        parent.getStack(0),
        parent.getStack(1),
        parent.getStack(2),
        fluidStorage.getSlot(0).resourceAmount,
        fluidStorage.getSlot(1).resourceAmount,
    )

    final override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTLargeMachineScreenHandler(
            syncId,
            playerInventory,
            packet,
            createContext(),
        )

    //    HTMultiblockController    //

    final override var showPreview: Boolean = false

    override fun onSucceeded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {
        super.onSucceeded(state, world, pos, player)
        RagiumAdvancementCriteria.BUILT_MACHINE.trigger(player, machineType, tier)
    }
}
