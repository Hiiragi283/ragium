package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.api.extension.openBackpackScreen
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand
import net.minecraft.util.ItemActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object HTBackpackInterfaceBlock : Block(blockSettings(Blocks.SMOOTH_STONE)) {
    private val COLOR: EnumProperty<DyeColor>
        get() = RagiumBlockProperties.COLOR

    init {
        defaultState = stateManager.defaultState.with(COLOR, DyeColor.WHITE)
    }

    override fun onUseWithItem(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult,
    ): ItemActionResult = stack.get(RagiumComponentTypes.COLOR)?.let { color: DyeColor ->
        world.setBlockState(pos, state.with(COLOR, color))
        ItemActionResult.success(world.isClient)
    } ?: ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        openBackpackScreen(world, player, state.getOrDefault(COLOR, DyeColor.WHITE))
        return ActionResult.success(world.isClient)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? =
        defaultState.with(COLOR, ctx.stack.getOrDefault(RagiumComponentTypes.COLOR, DyeColor.WHITE))

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(COLOR)
    }
}
