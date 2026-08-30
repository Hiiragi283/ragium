package hiiragi283.ragium.common.material

import hiiragi283.lib.material.HTMaterialAddon
import hiiragi283.lib.material.part.CommonParts
import hiiragi283.ragium.common.item.RagiumItems

data object RagiumMaterialAddon : HTMaterialAddon {
    override val priority: Int = 1000

    override fun registerExistingItem(consumer: HTMaterialAddon.ItemConsumer) {
        consumer.accept(CommonParts.FUEL, RagiumMaterialKeys.COAL_COKE, RagiumItems.COAL_COKE)
    }
}
