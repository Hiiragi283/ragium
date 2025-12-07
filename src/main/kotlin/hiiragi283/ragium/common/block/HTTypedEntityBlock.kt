package hiiragi283.ragium.common.block

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.block.HTTypedBlock
import hiiragi283.ragium.api.block.attribute.HTMenuBlockAttribute
import hiiragi283.ragium.api.block.attribute.getAttribute
import hiiragi283.ragium.api.block.attribute.hasAttribute
import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.world.getTypedBlockEntity
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.util.HTItemDropHelper
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.Tags
import java.util.function.UnaryOperator

typealias HTSimpleTypedEntityBlock = HTTypedEntityBlock<HTEntityBlockType>

/**
 * @see mekanism.common.block.prefab.BlockTile
 */
open class HTTypedEntityBlock<TYPE : HTEntityBlockType>(type: TYPE, properties: Properties) :
    HTTypedBlock<TYPE>(type, properties),
    HTBlockWithEntity {
    constructor(
        type: TYPE,
        operator: UnaryOperator<Properties>,
    ) : this(type, operator.apply(Properties.of().strength(3.5f, 16f).requiresCorrectToolForDrops()))

    final override fun getBlockEntityType(): HTDeferredBlockEntityType<*> = type.getBlockEntityType()

    //    Block    //

    override fun codec(): MapCodec<out BaseEntityBlock> = throw UnsupportedOperationException()

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        val result: ItemInteractionResult = super.useItemOn(stack, state, level, pos, player, hand, hitResult)
        if (stack.isEmpty) return result
        if (stack.`is`(Tags.Items.TOOLS_WRENCH)) {
            val blockEntity: HTBlockEntity = level.getTypedBlockEntity(pos) ?: return result
            RagiumMenuTypes.ACCESS_CONFIG.openMenu(player, blockEntity.name, blockEntity, blockEntity::writeExtraContainerData)
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return result
    }

    /**
     * @see mekanism.common.block.prefab.BlockTile.useWithoutItem
     */
    final override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        val blockEntity: HTBlockEntity = level.getTypedBlockEntity(pos) ?: return InteractionResult.PASS
        if (level.isClientSide) {
            return when (this.hasAttribute<HTMenuBlockAttribute<*>>()) {
                true -> InteractionResult.SUCCESS
                false -> InteractionResult.PASS
            }
        }
        return this
            .getAttribute<HTMenuBlockAttribute<*>>()
            ?.openMenu(player, blockEntity.name, blockEntity, blockEntity::writeExtraContainerData)
            ?: InteractionResult.PASS
    }

    override fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
        super.setPlacedBy(level, pos, state, placer, stack)
        level.getTypedBlockEntity<HTBlockEntity>(pos)?.ownerId = placer?.uuid
    }

    final override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        if (!state.`is`(newState.block)) {
            level.getTypedBlockEntity<HTBlockEntity>(pos)?.let { blockEntity: HTBlockEntity ->
                blockEntity.collectDrops { stack: ImmutableItemStack ->
                    HTItemDropHelper.dropStackAt(level, pos, stack)
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    override fun triggerEvent(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        id: Int,
        param: Int,
    ): Boolean {
        super.triggerEvent(state, level, pos, id, param)
        return level.getTypedBlockEntity<HTBlockEntity>(pos)?.triggerEvent(id, param) ?: false
    }

    final override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean,
    ) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
        level.getTypedBlockEntity<HTBlockEntity>(pos)?.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
    }
}
