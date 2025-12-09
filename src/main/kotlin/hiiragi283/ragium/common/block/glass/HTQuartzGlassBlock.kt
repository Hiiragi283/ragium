package hiiragi283.ragium.common.block.glass

import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.common.text.RagiumCommonTranslation

class HTQuartzGlassBlock(tinted: Boolean, properties: Properties) : HTGlassBlock(tinted, properties) {
    override fun getDescription(): HTTranslation = RagiumCommonTranslation.QUARTZ_GLASS
}
