package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.common.entity.vehicle.HTMinecart
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseRailBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.RailShape

open class HTMinecartItem(private val factory: HTMinecart.Factory, properties: Properties) : Item(properties.stacksTo(1)) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val state: BlockState = level.getBlockState(pos)
        if (!state.`is`(BlockTags.RAILS)) return InteractionResult.FAIL

        val stack: ItemStack = context.itemInHand
        if (!level.isClientSide) {
            val railShape: RailShape = (state.block as? BaseRailBlock)?.getRailDirection(state, level, pos, null) ?: RailShape.NORTH_SOUTH
            val height: Double = if (railShape.isAscending) 0.5 else 0.0
            val minecart: HTMinecart<*> = factory.create(level, pos.x + 0.5, pos.y + 0.0625 + height, pos.z + 0.5)
            stack.get(DataComponents.CUSTOM_NAME)?.let(minecart::setCustomName)

            level.addFreshEntity(minecart)
        }
        stack.consume(1, context.player)
        return InteractionResult.SUCCESS
    }
}
