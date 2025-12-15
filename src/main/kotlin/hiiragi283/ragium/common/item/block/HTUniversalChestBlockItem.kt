package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.item.HTDescriptionBlockItem
import hiiragi283.ragium.api.item.HTSubCreativeTabContents
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.common.block.storage.HTUniversalChestBlock
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import java.util.function.Consumer

class HTUniversalChestBlockItem(block: HTUniversalChestBlock, properties: Properties) :
    HTDescriptionBlockItem<HTUniversalChestBlock>(block, properties),
    HTSubCreativeTabContents {
    override fun addItems(baseItem: HTItemHolderLike, parameters: CreativeModeTab.ItemDisplayParameters, consumer: Consumer<ItemStack>) {
        for (color: DyeColor in DyeColor.entries) {
            consumer.accept(createItemStack(baseItem, RagiumDataComponents.COLOR, color))
        }
    }

    override fun shouldAddDefault(): Boolean = false
}
