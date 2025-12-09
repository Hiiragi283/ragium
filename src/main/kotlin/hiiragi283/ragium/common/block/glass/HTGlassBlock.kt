package hiiragi283.ragium.common.block.glass

import hiiragi283.ragium.api.block.HTBlockWithDescription
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.state.BlockState

abstract class HTGlassBlock(private val tinted: Boolean, properties: Properties) :
    TransparentBlock(properties),
    HTBlockWithDescription {
    final override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = !tinted

    final override fun getLightBlock(state: BlockState, level: BlockGetter, pos: BlockPos): Int =
        if (tinted) level.maxLightLevel else super.getLightBlock(state, level, pos)
}
