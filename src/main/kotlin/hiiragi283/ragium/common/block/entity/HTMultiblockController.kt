package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockValidator
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.util.getMachineEntity
import hiiragi283.ragium.common.util.getOrDefault
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

interface HTMultiblockController {
    var showPreview: Boolean

    fun onUseController(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ): ActionResult {
        val blockEntity: HTMultiblockController =
            world.getMachineEntity(pos) as? HTMultiblockController ?: return ActionResult.FAIL
        if (player.isSneaking) {
            blockEntity.showPreview = !blockEntity.showPreview
            return ActionResult.success(world.isClient)
        }
        return when (world.isClient) {
            true -> ActionResult.SUCCESS
            false -> {
                if (isValid(state, world, pos, player)) {
                    onSucceeded(state, world, pos, player)
                    showPreview = false
                } else {
                    onFailed(state, world, pos, player)
                }
                ActionResult.CONSUME
            }
        }
    }

    fun buildMultiblock(builder: HTMultiblockBuilder)

    fun isValid(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity? = null,
    ): Boolean {
        val direction: Direction = state.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        val validator = HTMultiblockValidator(world, pos, player)
        buildMultiblock(validator.rotate(direction))
        return validator.isValid
    }

    fun onSucceeded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {
        player.sendMessage(Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS), false)
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
    }

    fun onFailed(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {}
}
