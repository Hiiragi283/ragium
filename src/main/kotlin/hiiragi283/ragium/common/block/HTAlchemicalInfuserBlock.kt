package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.HTAlchemicalInfuserBlockEntity
import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.ItemActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

object HTAlchemicalInfuserBlock :
    HTBlockWithEntity(blockSettings(Blocks.CRYING_OBSIDIAN)) {
    @JvmField
    val SHAPE: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0)

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = SHAPE

    override fun onUseWithItem(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult,
    ): ItemActionResult = (world.getBlockEntity(pos) as? HTAlchemicalInfuserBlockEntity)
        ?.processRecipe(stack, world)
        ?: ItemActionResult.SUCCESS

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTAlchemicalInfuserBlockEntity(pos, state)
}
