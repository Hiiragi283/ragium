package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.extension.resourceAmount
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.entity.HTProcessorMachineEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.common.init.RagiumAdvancementCriteria
import hiiragi283.ragium.common.screen.HTLargeMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTLargeProcessorMachineEntity(type: HTMachineConvertible, tier: HTMachineTier) :
    HTProcessorMachineEntityBase(HTMachineType.Size.LARGE, type, tier),
    HTMultiblockController {
    override fun createInput(): HTMachineInput = HTMachineInput.create(machineType, tier) {
        add(parent.getStack(0))
        add(parent.getStack(1))
        add(parent.getStack(2))
        add(fluidStorage[0].resourceAmount)
        add(fluidStorage[1].resourceAmount)
    }

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
