package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTBlockWithDescription
import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.world.level.ItemLike

class HTExpBerriesBushBlock(properties: Properties) :
    HTCropBlock(properties),
    HTBlockWithDescription {
    override fun getDescription(): HTTranslation = RagiumCommonTranslation.EXP_BERRIES

    override fun getBaseSeedId(): ItemLike = RagiumBlocks.EXP_BERRIES
}
