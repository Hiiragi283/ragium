package hiiragi283.lib.item

import hiiragi283.lib.item.alchemy.HTPotionHelper
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents

/**
 * ポーションに基づいた[Item]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTPotionBasedItem(properties: Properties) :
    Item(properties),
    HTSubCreativeTabContents {

    override fun getDefaultInstance(): ItemStack {
        val stack: ItemStack = super.getDefaultInstance()
        stack[DataComponents.POTION_CONTENTS] = PotionContents.EMPTY
        return stack
    }

    override fun getCreatorModId(registries: HolderLookup.Provider, itemStack: ItemStack): String? =
        HTPotionHelper.getPotionId(itemStack)
            ?.namespace
            ?: super.getCreatorModId(registries, itemStack)

    //    HTSubCreativeTabContents    //

    override fun addItems(
        baseItem: Holder<Item>,
        parameters: CreativeModeTab.ItemDisplayParameters,
        output: CreativeModeTab.Output
    ) {
        parameters.holders()
            .lookupOrThrow(Registries.POTION)
            .filterFeatures(parameters.enabledFeatures())
            .listElements()
            .map { ItemStack(baseItem, 1, HTPotionHelper.createPotionPatch(it)) }
            .forEach(output::accept)
    }

    override fun shouldAddDefault(): Boolean = false
}
