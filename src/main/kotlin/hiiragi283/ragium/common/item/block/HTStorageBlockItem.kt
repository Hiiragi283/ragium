package hiiragi283.ragium.common.item.block

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.item.HTDescriptionBlockItem
import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import java.util.function.Consumer

abstract class HTStorageBlockItem<BLOCK>(block: BLOCK, properties: Properties) :
    HTDescriptionBlockItem<BLOCK>(block, properties),
    HTSubCreativeTabContents
    where BLOCK : Block, BLOCK : HTBlockWithDescription {
    final override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || HTUpgradeHelper.isCreative(stack)

    final override fun getNameColor(stack: ItemStack): HTDefaultColor? = when {
        HTUpgradeHelper.isCreative(stack) -> HTDefaultColor.RED
        else -> super.getNameColor(stack)
    }

    final override fun addItems(
        baseItem: HTItemHolderLike<*>,
        parameters: CreativeModeTab.ItemDisplayParameters,
        consumer: Consumer<ItemStack>,
    ) {
        // Creative Version
        createItemStack(
            baseItem,
            RagiumDataComponents.MACHINE_UPGRADES,
            HTAttachedItems(listOf(createItemStack(RagiumUpgradeType.CREATIVE))),
        ).let(consumer::accept)
    }
}
