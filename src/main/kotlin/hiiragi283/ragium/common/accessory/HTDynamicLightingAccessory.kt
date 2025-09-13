package hiiragi283.ragium.common.accessory

import hiiragi283.ragium.config.RagiumConfig
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.roundToInt

object HTDynamicLightingAccessory : Accessory {
    override fun tick(stack: ItemStack, reference: SlotReference) {
        super.tick(stack, reference)
        val entity: LivingEntity = reference.entity()
        val level: Level = entity.level()
        val range: Int = RagiumConfig.CONFIG.deviceCollectorEntityRange.asDouble
            .roundToInt()
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
