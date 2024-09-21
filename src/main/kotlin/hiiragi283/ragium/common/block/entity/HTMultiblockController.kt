package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.machine.HTMultiblockBuilder
import hiiragi283.ragium.common.machine.HTMultiblockValidator
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
            world.getBlockEntity(pos) as? HTMultiblockController ?: return ActionResult.FAIL
        if (player.isSneaking) {
            blockEntity.showPreview = !blockEntity.showPreview
            return ActionResult.success(world.isClient)
        }
        return when (world.isClient) {
            true -> ActionResult.SUCCESS
            false -> {
                val direction: Direction = state.get(Properties.HORIZONTAL_FACING)
                val validator = HTMultiblockValidator(world, pos, player)
                blockEntity.buildMultiblock(validator.rotate(direction))
                if (validator.isValid) {
                    player.sendMessage(Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS), false)
                    onValid(state, world, pos, player)
                    player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
                    showPreview = false
                }
                ActionResult.CONSUME
            }
        }
    }

    fun onValid(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    )

    fun buildMultiblock(builder: HTMultiblockBuilder): HTMultiblockBuilder
}
