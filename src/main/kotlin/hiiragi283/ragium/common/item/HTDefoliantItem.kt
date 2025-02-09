package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.data.HTDefoliant
import hiiragi283.ragium.api.data.RagiumDataMaps
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

class HTDefoliantItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        BlockPos.betweenClosedStream(-4, -4, -4, 4, 4, 4).map(pos::offset).forEach { posIn: BlockPos ->
            val defoliant: HTDefoliant =
                level.getBlockState(posIn).blockHolder.getData(RagiumDataMaps.DEFOLIANT) ?: return@forEach
            level.setBlockAndUpdate(posIn, defoliant.state)
        }
        context.itemInHand.consume(1, context.player)
        return InteractionResult.sidedSuccess(level.isClientSide)
    }
}
