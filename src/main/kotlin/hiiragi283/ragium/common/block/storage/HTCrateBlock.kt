package hiiragi283.ragium.common.block.storage

import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.block.HTBasicEntityBlock
import hiiragi283.core.common.block.HTBlockWithModularUI
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.text.RagiumTranslation

class HTCrateBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTBasicEntityBlock(type, properties),
    HTBlockWithDescription,
    HTBlockWithModularUI {
    override fun getDescription(): HTTranslation = RagiumTranslation.CRATE
}
