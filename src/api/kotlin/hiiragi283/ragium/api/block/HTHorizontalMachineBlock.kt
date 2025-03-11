package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties

abstract class HTHorizontalMachineBlock(val machineType: HTMachineType, properties: Properties) : HTEntityBlock.Horizontal(properties) {
    final override fun initDefaultState(): BlockState = stateDefinition
        .any()
        .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
        .setValue(HTBlockStateProperties.IS_ACTIVE, false)

    override fun getDescriptionId(): String = machineType.translationKey

    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        tooltips.add(machineType.descriptionText)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(HTBlockStateProperties.IS_ACTIVE)
    }
}
