package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.item.HTSubCreativeTabContents
import hiiragi283.ragium.api.registry.HTItemHolderLike
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

data object HTCreativeTabHelper {
    @JvmStatic
    fun addToDisplay(output: CreativeModeTab.Output, items: Iterable<HTItemHolderLike>) {
        for (like: HTItemHolderLike in items) {
            addToDisplay(output, like)
        }
    }

    /**
     * @see mekanism.common.registration.impl.CreativeTabDeferredRegister.addToDisplay
     */
    @JvmStatic
    fun addToDisplay(output: CreativeModeTab.Output, vararg items: HTItemHolderLike) {
        val visibility: CreativeModeTab.TabVisibility = when (output) {
            is BuildCreativeModeTabContentsEvent -> CreativeModeTab.TabVisibility.PARENT_TAB_ONLY
            else -> CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
        }
        for (like: HTItemHolderLike in items) {
            val item: Item = like.asItem()
            if (item is HTSubCreativeTabContents) {
                if (item.shouldAddDefault()) {
                    output.accept(item, visibility)
                }
                item.addItems(like) { output.accept(it, visibility) }
            } else {
                output.accept(item, visibility)
            }
        }
    }
}
