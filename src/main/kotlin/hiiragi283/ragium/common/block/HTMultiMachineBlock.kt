package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.recipe.HTMachineType
import hiiragi283.ragium.common.shape.HTMultiMachineShape
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTMultiMachineBlock(machineType: HTMachineType.Multi) : HTAbstractMachineBlock(machineType) {

    private val multiShape: HTMultiMachineShape = machineType.multiShape

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        if (player.isSneaking && player.activeHand == Hand.MAIN_HAND) {
            (world.getBlockEntity(pos) as? HTMachineBlockEntity)?.let { blockEntity: HTMachineBlockEntity ->
                blockEntity.showPreview = !blockEntity.showPreview
            }
            return ActionResult.success(world.isClient)
        }
        return when (world.isClient) {
            true -> ActionResult.SUCCESS
            false -> {
                if (multiShape.test(world, pos, player)) {
                    player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
                }
                ActionResult.CONSUME
            }
        }
    }
}