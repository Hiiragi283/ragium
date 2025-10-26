package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.common.block.HTTypedEntityBlock
import hiiragi283.ragium.common.tier.HTMachineTier

class HTMachineBlockItem(block: HTTypedEntityBlock<*>, properties: Properties) : HTBlockItem<HTTypedEntityBlock<*>>(block, properties) {
    override fun getTier(): HTMachineTier = block.getAttributeTier<HTMachineTier>()
}
