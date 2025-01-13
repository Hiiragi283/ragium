package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

abstract class HTEntityBlock(
    properties: Properties,
) : Block(properties),
    EntityBlock {
    final override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult =
        (level.getBlockEntity(pos) as? HTBlockEntity)
            ?.onRightClicked(state, level, pos, player, hitResult)
            ?: super.useWithoutItem(state, level, pos, player, hitResult)

    override fun attack(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ) {
        super.attack(state, level, pos, player)
        (level.getBlockEntity(pos) as? HTBlockEntity)?.onLeftClicked(state, level, pos, player)
    }

    override fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
        super.setPlacedBy(level, pos, state, placer, stack)
        (level.getBlockEntity(pos) as? HTBlockEntity)?.setPlacedBy(level, pos, state, placer, stack)
    }

    final override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
        (level.getBlockEntity(pos) as? HTBlockEntity)?.onRemove(state, level, pos, newState, movedByPiston)
    }

    final override fun hasAnalogOutputSignal(state: BlockState): Boolean = true

    final override fun getAnalogOutputSignal(
        state: BlockState,
        level: Level,
        pos: BlockPos,
    ): Int = super.getAnalogOutputSignal(state, level, pos)

    final override fun <T : BlockEntity> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>,
    ): BlockEntityTicker<T>? =
        BlockEntityTicker<T> { level: Level, pos: BlockPos, state: BlockState, blockEntity: T ->
            (blockEntity as? HTBlockEntity)?.tick(level, pos, state)
        }
}
