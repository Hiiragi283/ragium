package hiiragi283.ragium.common.item.block

import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.support.item.HTDescriptionBlockItem
import hiiragi283.ragium.api.tag.RagiumTags
import net.minecraft.network.chat.TextColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

abstract class HTStorageBlockItem(block: Block, properties: Properties) : HTDescriptionBlockItem(block, properties) {
    protected fun isCreative(stack: ItemStack): Boolean = stack.`is`(RagiumTags.BlockItems.STORAGES_CREATIVE.item)

    final override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || isCreative(stack)

    override fun getNameColor(stack: ItemStack): TextColor? = when {
        isCreative(stack) -> HTDefaultColor.RED.textColor
        else -> super.getNameColor(stack)
    }
}
