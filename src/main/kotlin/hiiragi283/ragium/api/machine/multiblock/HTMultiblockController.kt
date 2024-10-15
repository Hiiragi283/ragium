package hiiragi283.ragium.api.machine.multiblock

import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.RegistryKey
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

interface HTMultiblockController {
    var showPreview: Boolean
    val pattern: RegistryKey<HTMultiblockPattern>

    fun buildMultiblock(world: World, builder: HTMultiblockBuilder): Unit? = world.registryManager
        .get(HTMultiblockPattern.REGISTRY_KEY)
        .get(pattern)
        ?.buildMultiblock(builder)

    fun onUseController(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        controller: HTMultiblockController?,
    ): ActionResult {
        if (controller == null) return ActionResult.PASS
        if (player.isSneaking) {
            controller.showPreview = !controller.showPreview
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

    fun isValid(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity? = null,
    ): Boolean {
        val direction: Direction = state.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        val validator = HTMultiblockValidator(world, pos, player)
        return buildMultiblock(world, validator.rotate(direction))?.let { validator.isValid } ?: false
    }

    fun onSucceeded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {
        player.sendMessage(Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS), true)
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
    }

    fun onFailed(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {}
}
