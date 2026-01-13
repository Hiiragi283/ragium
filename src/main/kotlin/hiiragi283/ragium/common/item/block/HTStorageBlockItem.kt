package hiiragi283.ragium.common.item.block

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.item.HTDescriptionBlockItem
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

abstract class HTStorageBlockItem<BLOCK>(block: BLOCK, properties: Properties) :
    HTDescriptionBlockItem<BLOCK>(block, properties)
    where BLOCK : Block, BLOCK : HTBlockWithDescription {
    final override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || HTUpgradeHelper.isCreative(stack)

    final override fun getNameColor(stack: ItemStack): HTDefaultColor? = when {
        HTUpgradeHelper.isCreative(stack) -> HTDefaultColor.RED
        else -> super.getNameColor(stack)
    }
}
