package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTBlockWithDescription
import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class HTWarpedWartBlock(properties: Properties) :
    HTCropBlock(properties),
    HTBlockWithDescription {
    override fun getDescription(): HTTranslation = RagiumCommonTranslation.WARPED_WART

    override fun getBaseSeedId(): ItemLike = RagiumBlocks.WARPED_WART

    override fun mayPlaceOn(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = state.`is`(Blocks.SOUL_SAND)
}
