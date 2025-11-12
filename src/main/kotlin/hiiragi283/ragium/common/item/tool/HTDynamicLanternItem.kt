package hiiragi283.ragium.common.item.tool

import hiiragi283.ragium.common.item.base.HTActivatableItem
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.roundToInt

class HTDynamicLanternItem(properties: Properties) : HTActivatableItem(properties) {
    override fun onTick(
        stack: ItemStack,
        level: Level,
        player: Player,
        slotId: Int,
        isSelected: Boolean,
    ) {
        val range: Int = RagiumConfig.COMMON.deviceCollectorEntityRange.asDouble
            .roundToInt()
        for (pos: BlockPos in BlockPos.betweenClosed(-range, -range, -range, range, range, range)) {
            val posIn: BlockPos = player.blockPosition().offset(pos)
            val state: BlockState = level.getBlockState(posIn)
            if (state.isAir && level.getBrightness(LightLayer.BLOCK, posIn) == 0) {
                level.setBlockAndUpdate(posIn, Blocks.LIGHT.defaultBlockState())
                break
            }
        }
    }
}
