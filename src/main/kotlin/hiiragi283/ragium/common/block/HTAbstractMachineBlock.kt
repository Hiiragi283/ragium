package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.recipe.HTMachineType
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTAbstractMachineBlock(protected val machineType: HTMachineType) : HTBlockWithEntity(Settings.create()) {

    init {
        defaultState = stateManager.defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
    }

    override fun getTranslationKey(): String = machineType.translationKey

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        defaultState.with(Properties.HORIZONTAL_FACING, ctx.horizontalPlayerFacing.opposite)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.HORIZONTAL_FACING)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.with(Properties.HORIZONTAL_FACING, mirror.apply(state.get(Properties.HORIZONTAL_FACING)))

    //    BlockEntityProvider    //

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        HTMachineBlockEntity(pos, state, machineType)

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>,
    ): BlockEntityTicker<T>? = HTBlockEntityTicker.validateTicker(
        type,
        RagiumBlockEntityTypes.MACHINE,
        HTMachineBlockEntity.TICKER
    )

}