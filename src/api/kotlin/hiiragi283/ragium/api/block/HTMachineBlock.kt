package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition

abstract class HTMachineBlock(val machineType: HTMachineType, properties: Properties) : HTEntityBlock(properties) {
    override fun initDefaultState(): BlockState = stateDefinition
        .any()
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
