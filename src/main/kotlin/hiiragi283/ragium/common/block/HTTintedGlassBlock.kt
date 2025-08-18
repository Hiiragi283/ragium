package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState

class HTTintedGlassBlock(canPlayerThrough: Boolean, properties: Properties) : HTGlassBlock(canPlayerThrough, properties) {
    override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = false

    override fun getLightBlock(state: BlockState, level: BlockGetter, pos: BlockPos): Int = level.maxLightLevel
}
