package hiiragi283.ragium.common.block

import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.block.HTHorizontalEntityBlock
import hiiragi283.core.common.registry.HTDeferredBlockEntityType

class HTMachineBlock(private val translation: HTTranslation, type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTHorizontalEntityBlock(type, properties),
    HTBlockWithDescription {
    override fun getDescription(): HTTranslation = translation
}
