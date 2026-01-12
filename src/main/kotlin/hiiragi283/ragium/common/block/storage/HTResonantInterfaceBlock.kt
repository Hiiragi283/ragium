package hiiragi283.ragium.common.block.storage

import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.block.HTBasicEntityBlock
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.setup.RagiumBlockEntityTypes

class HTResonantInterfaceBlock(properties: Properties) :
    HTBasicEntityBlock(RagiumBlockEntityTypes.RESONANT_INTERFACE, properties),
    HTBlockWithDescription {
    override fun getDescription(): HTTranslation = RagiumTranslation.UNIVERSAL_CHEST
}
