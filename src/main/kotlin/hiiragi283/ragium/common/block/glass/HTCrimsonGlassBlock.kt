package hiiragi283.ragium.common.block.glass

import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class HTCrimsonGlassBlock(tinted: Boolean, properties: Properties) : HTGlassBlock(tinted, properties) {
    override fun getDescription(): HTTranslation = RagiumCommonTranslation.CRIMSON_GLASS

    override fun stepOn(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        entity: Entity,
    ) {
        Blocks.MAGMA_BLOCK.stepOn(level, pos, state, entity)
    }
}
