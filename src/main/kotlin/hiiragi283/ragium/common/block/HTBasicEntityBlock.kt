package hiiragi283.ragium.common.block

import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.world.getTypedBlockEntity
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTExtendedBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.redstone.Orientation

open class HTBasicEntityBlock(val type: HTDeferredBlockEntityType<*>, properties: Properties) :
    Block(properties),
    EntityBlock {
    /*override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        val blockEntity: HTExtendedBlockEntity = level.getTypedBlockEntity(pos) ?: return InteractionResult.PASS
        val menuType: HTDeferredMenuType.WithContext<*, *>? = null
        if (level.isClientSide) {
            return when {
                menuType == null -> InteractionResult.PASS
                else -> InteractionResult.SUCCESS
            }
        }
        val name: Component = when (blockEntity) {
            is Nameable -> blockEntity.name
            else -> state.block.name
        }
        return menuType
            ?.openMenu(player, name, blockEntity, blockEntity::writeExtraContainerData)
            ?: InteractionResult.PASS
    }*/

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        super.setPlacedBy(level, pos, state, placer, stack)
        level.getTypedBlockEntity<HTBlockEntity>(pos)?.ownerId = placer?.uuid
    }

    final override fun triggerEvent(state: BlockState, level: Level, pos: BlockPos, id: Int, param: Int): Boolean {
        super.triggerEvent(state, level, pos, id, param)
        return level.getBlockEntity(pos)?.triggerEvent(id, param) ?: false
    }

    final override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        block: Block,
        orientation: Orientation?,
        movedByPiston: Boolean
    ) {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston)
        level.getTypedBlockEntity<HTExtendedBlockEntity>(
            pos
        )?.neighborChanged(state, level, pos, block, orientation, movedByPiston)
    }

    final override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = type.create(pos, state)

    @Suppress("UNCHECKED_CAST")
    final override fun <T : BlockEntity> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? = when (blockEntityType) {
        type.get() -> type.getTicker(level.isClientSide) as? BlockEntityTicker<T>
        else -> null
    }
}
