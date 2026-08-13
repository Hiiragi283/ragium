package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate

/**
 * [HTItemResult]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
class HTItemResultBuilder {
    @PublishedApi internal var entry: HTItemResult.Entry by HTDelegates.onceInitialize()
    var count: Int by HTDelegates.onceInitialize { 1 }

    operator fun HTItemResult.Entry.unaryPlus() {
        entry = this
    }

    // Simple
    operator fun Identifier.unaryPlus() {
        +HTSimpleDeferredItem(this).delegate
    }

    operator fun ResourceKey<Item>.unaryPlus() {
        +HTSimpleDeferredItem(this).delegate
    }

    operator fun Holder<Item>.unaryPlus() {
        +HTItemResult.SimpleEntry(this)
    }

    operator fun Item.unaryPlus() {
        +ItemStackTemplate(this)
    }

    operator fun ItemStackTemplate.unaryPlus() {
        +HTItemResult.SimpleEntry(this)
    }

    operator fun ItemStack.unaryPlus() {
        +HTItemResult.SimpleEntry(this)
    }

    // Tag
    operator fun HolderSet<Item>.unaryPlus() {
        +HTItemResult.TagEntry(this)
    }

    fun build(): HTItemResult = HTItemResult(entry, count)
}
