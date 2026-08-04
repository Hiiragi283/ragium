package hiiragi283.lib.item

import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTPotionHelper
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * ポーションに基づいた[Item]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTPotionBasedItem(properties: Properties) :
    Item(properties),
    HTSubCreativeTabContents {

    override fun getCreatorModId(registries: HolderLookup.Provider, itemStack: ItemStack): String? = HTPotionHelper.getPotionModId(itemStack) ?: super.getCreatorModId(registries, itemStack)

    //    HTSubCreativeTabContents    //

    override fun addItems(baseItem: Holder<Item>, parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output) {
        parameters.holders()
            .lookupOrThrow(Registries.POTION)
            .filterFeatures(parameters.enabledFeatures())
            .listElements()
            .map(::BottledPotionContents)
            .map(HTPotionHelper::createItemPatch)
            .map { ItemStack(baseItem, 1, it) }
            .forEach(output::accept)
    }

    override fun shouldAddDefault(): Boolean = false
}
