package hiiragi283.ragium.common.item.block

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.item.HTDescriptionBlockItem
import hiiragi283.ragium.common.block.storage.HTStorageBlock
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.item.ItemStack

abstract class HTStorageBlockItem<BLOCK : HTStorageBlock>(block: BLOCK, properties: Properties) :
    HTDescriptionBlockItem<BLOCK>(block, properties) {
    protected fun isCreative(stack: ItemStack): Boolean = stack.has(RagiumDataComponents.CREATIVE_STORAGE)

    final override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || isCreative(stack)

    final override fun getNameColor(stack: ItemStack): HTDefaultColor? = when {
        isCreative(stack) -> HTDefaultColor.RED
        else -> super.getNameColor(stack)
    }
}
