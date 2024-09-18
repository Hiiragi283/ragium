package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.machine.HTMultiblockBuilder
import hiiragi283.ragium.common.machine.HTMultiblockValidator
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTMultiMachineBlockEntity(machineType: HTMachineType.Multi, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity<HTMachineType.Multi>(machineType, pos, state) {
    var showPreview: Boolean = false

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val blockEntity: HTMultiMachineBlockEntity =
            world.getBlockEntity(pos) as? HTMultiMachineBlockEntity ?: return ActionResult.FAIL
        if (player.isSneaking) {
            blockEntity.showPreview = true
            return ActionResult.success(world.isClient)
        }
        return when (world.isClient) {
            true -> ActionResult.SUCCESS
            false -> {
                val direction: Direction = state.get(Properties.HORIZONTAL_FACING)
                val validator = HTMultiblockValidator(world, pos, player)
                blockEntity.buildMultiblock(validator.rotate(direction))
                if (validator.isValid) {
                    player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
                    player.sendMessage(Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS), false)
                }
                ActionResult.CONSUME
            }
        }
    }

    abstract fun buildMultiblock(builder: HTMultiblockBuilder): HTMultiblockBuilder
}
