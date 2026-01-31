package hiiragi283.ragium.common.block.storage

import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.text.RagiumTranslation

class HTBatteryBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTStorageBlock(type, properties) {
    override fun getDescription(): HTTranslation = RagiumTranslation.BATTERY
}
