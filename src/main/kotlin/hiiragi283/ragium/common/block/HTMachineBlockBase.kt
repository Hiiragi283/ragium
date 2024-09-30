package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.machine.HTMachineConvertible
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.Direction

abstract class HTMachineBlockBase(val machineType: HTMachineType, val tier: HTMachineTier) :
    HTBlockWithEntity(blockSettings(tier.getBaseBlock())) {
    init {
        defaultState = stateManager.defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
    }

    constructor(convertible: HTMachineConvertible, tier: HTMachineTier) : this(convertible.asMachine(), tier)

    override fun getTranslationKey(): String = machineType.translationKey

    override fun getName(): MutableText = tier.createPrefixedText(machineType)

    override fun appendTooltip(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltip: MutableList<Text>,
        options: TooltipType,
    ) {
        machineType.appendTooltip(stack, context.registryLookup, tooltip::add, tier)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        defaultState.with(Properties.HORIZONTAL_FACING, ctx.horizontalPlayerFacing.opposite)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.HORIZONTAL_FACING)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.with(Properties.HORIZONTAL_FACING, mirror.apply(state.get(Properties.HORIZONTAL_FACING)))
}
