package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.extension.getHTBlockEntity
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineProvider
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.storage.EmptyWorldlyContainer
import hiiragi283.ragium.common.init.RagiumBlockProperties
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.WorldlyContainerHolder
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class HTMachineBlock(override val machineKey: HTMachineKey, properties: Properties) :
    HTEntityBlock(properties),
    WorldlyContainerHolder,
    HTMachineProvider {
    init {
        registerDefaultState(
            stateDefinition
                .any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(RagiumBlockProperties.ACTIVE, false)
                .setValue(HTMachineTier.PROPERTY, HTMachineTier.PRIMITIVE),
        )
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        machineKey.appendTooltip(tooltipComponents::add, stack.machineTier)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, RagiumBlockProperties.ACTIVE, HTMachineTier.PROPERTY)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? = defaultBlockState()
        .setValue(BlockStateProperties.HORIZONTAL_FACING, context.horizontalDirection.opposite)
        .setValue(HTMachineTier.PROPERTY, context.itemInHand.machineTier)

    override fun rotate(
        state: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        direction: Rotation,
    ): BlockState = state.setValue(
        BlockStateProperties.HORIZONTAL_FACING,
        direction.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)),
    )

    override fun mirror(state: BlockState, mirror: Mirror): BlockState = state.setValue(
        BlockStateProperties.HORIZONTAL_FACING,
        mirror.mirror(state.getValue(BlockStateProperties.HORIZONTAL_FACING)),
    )

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = machineKey
        .getEntryOrNull()
        ?.get(HTMachinePropertyKeys.MACHINE_FACTORY)
        ?.create(pos, state, machineKey)

    override fun getContainer(state: BlockState, level: LevelAccessor, pos: BlockPos): WorldlyContainer =
        level.getHTBlockEntity(pos)?.getContainer() ?: EmptyWorldlyContainer
}
