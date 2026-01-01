package hiiragi283.ragium.common.block.storage

import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.block.HTBasicEntityBlock
import hiiragi283.core.common.registry.HTDeferredMenuType
import hiiragi283.ragium.common.text.RagiumTranslation
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumMenuTypes

class HTUniversalChestBlock(properties: Properties) :
    HTBasicEntityBlock(RagiumBlockEntityTypes.UNIVERSAL_CHEST, properties),
    HTBlockWithDescription {
    override fun getMenuType(): HTDeferredMenuType.WithContext<*, *> = RagiumMenuTypes.UNIVERSAL_CHEST

    override fun getDescription(): HTTranslation = RagiumTranslation.UNIVERSAL_CHEST
}
