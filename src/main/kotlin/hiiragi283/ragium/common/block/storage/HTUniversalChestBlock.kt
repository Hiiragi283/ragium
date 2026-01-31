package hiiragi283.ragium.common.block.storage

import hiiragi283.core.api.text.HTTranslation
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.setup.RagiumBlockEntityTypes

class HTUniversalChestBlock(properties: Properties) : HTStorageBlock(RagiumBlockEntityTypes.UNIVERSAL_CHEST, properties) {
    override fun getDescription(): HTTranslation = RagiumTranslation.UNIVERSAL_CHEST
}
