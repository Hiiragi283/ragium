package hiiragi283.ragium.common.block.glass

import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState

class HTObsidianGlass(tinted: Boolean, properties: Properties) : HTGlassBlock(tinted, properties) {
    override fun getDescription(): HTTranslation = RagiumCommonTranslation.OBSIDIAN_GLASS

    override fun isPortalFrame(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = true
}
