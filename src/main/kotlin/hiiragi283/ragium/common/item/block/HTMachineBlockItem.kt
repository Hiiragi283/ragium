package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.item.HTDescriptionBlockItem
import hiiragi283.ragium.common.block.HTTypedEntityBlock
import hiiragi283.ragium.common.tier.HTMachineTier
import net.minecraft.ChatFormatting
import net.minecraft.world.item.ItemStack

class HTMachineBlockItem(block: HTTypedEntityBlock<*>, properties: Properties) :
    HTDescriptionBlockItem<HTTypedEntityBlock<*>>(block, properties) {
    override fun getNameColor(stack: ItemStack): ChatFormatting? = block.getAttributeTier<HTMachineTier>()?.getBaseTier()?.color
}
