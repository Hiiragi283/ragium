package hiiragi283.ragium.common.item

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class HTDynamicLanternItem(properties: Properties) : HTRangedItem(properties) {
    override fun inventoryTick(
        stack: ItemStack,
        level: Level,
        entity: Entity,
        slotId: Int,
        isSelected: Boolean,
    ) {
        if (!isActive(stack)) return
        val range: Int = getRange(stack, level)
        for (pos: BlockPos in BlockPos.betweenClosed(-range, -range, -range, range, range, range)) {
            val posIn: BlockPos = entity.blockPosition().offset(pos)
            val state: BlockState = level.getBlockState(posIn)
            if (state.isAir && level.getBrightness(LightLayer.BLOCK, posIn) == 0) {
                level.setBlockAndUpdate(posIn, Blocks.LIGHT.defaultBlockState())
                break
            }
        }
    }
}
