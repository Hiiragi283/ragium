package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.item.HTDescriptionBlockItem
import hiiragi283.ragium.common.block.HTTypedEntityBlock
import hiiragi283.ragium.common.tier.HTMachineTier

class HTMachineBlockItem(block: HTTypedEntityBlock<*>, properties: Properties) :
    HTDescriptionBlockItem<HTTypedEntityBlock<*>>(block, properties) {
    override fun getTier(): HTMachineTier? = block.getAttributeTier<HTMachineTier>()
}
