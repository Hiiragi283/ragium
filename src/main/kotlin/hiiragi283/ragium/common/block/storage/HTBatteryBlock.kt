package hiiragi283.ragium.common.block.storage

import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.block.HTBasicEntityBlock
import hiiragi283.core.common.block.HTBlockWithModularUI
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.setup.RagiumBlockEntityTypes

class HTBatteryBlock(properties: Properties) :
    HTBasicEntityBlock(RagiumBlockEntityTypes.BATTERY, properties),
    HTBlockWithDescription,
    HTBlockWithModularUI {
    override fun getDescription(): HTTranslation = RagiumTranslation.BATTERY
}
