package hiiragi283.ragium.common.block.glass

import hiiragi283.ragium.api.block.HTBlockWithDescription
import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.state.BlockState

open class HTGlassBlock(private val tinted: Boolean, properties: Properties) :
    TransparentBlock(properties),
    HTBlockWithDescription {
    override fun getDescription(): HTTranslation = RagiumCommonTranslation.QUARTZ_GLASS

    final override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = !tinted

    final override fun getLightBlock(state: BlockState, level: BlockGetter, pos: BlockPos): Int =
        if (tinted) level.maxLightLevel else super.getLightBlock(state, level, pos)
}
