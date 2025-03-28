package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ChorusFruitItem
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.FlowerBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.Tags

class HTEnderLilyBlock(properties: Properties) : FlowerBlock(MobEffects.CONFUSION, 8f, properties) {
    override fun mayPlaceOn(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = state.`is`(Tags.Blocks.END_STONES)

    /**
     * @see [ChorusFruitItem.finishUsingItem]
     */
    override fun entityInside(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        entity: Entity,
    ) {
        if (!level.isClientSide) {
            if (entity is LivingEntity) {
                // コーラスフルーツのようにランダムテレポートを起こす
            }
        }
    }
}
