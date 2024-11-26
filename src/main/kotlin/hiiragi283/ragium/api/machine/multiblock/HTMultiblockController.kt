package hiiragi283.ragium.api.machine.multiblock

import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

interface HTMultiblockController {
    var showPreview: Boolean

    fun buildMultiblock(builder: HTMultiblockBuilder)

    fun onUseController(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ): Boolean {
        if (player.isSneaking) {
            showPreview = !showPreview
            return true
        }
        if (!world.isClient) {
            val result: Boolean = updateValidation(state, world, pos, player)
            if (result) {
                player.sendMessage(Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS), true)
                showPreview = false
            }
            onUpdated(state, world, pos, player, result)
            return result
        }
        return false
    }

    fun beforeValidation(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity?,
    ) {}

    fun updateValidation(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity? = null,
    ): Boolean {
        val direction: Direction = state.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        val validator = HTMultiblockValidator(world, pos, player)
        beforeValidation(state, world, pos, player)
        buildMultiblock(validator.rotate(direction))
        return validator.isValid
    }

    fun onUpdated(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        result: Boolean,
    ) {}
}
