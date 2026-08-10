package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike

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
        +HTItemResult.SimpleEntry(HTSimpleDeferredItem(this))
    }

    operator fun ResourceKey<Item>.unaryPlus() {
        +HTItemResult.SimpleEntry(HTSimpleDeferredItem(this))
    }

    operator fun ItemLike.unaryPlus() {
        +ItemStackTemplate(this.asItem())
    }

    operator fun ItemStackTemplate.unaryPlus() {
        +HTItemResult.SimpleEntry(this)
    }

    // Tag
    operator fun TagKey<Item>.unaryPlus() {
        +HTItemResult.TagEntry(this)
    }

    fun build(): HTItemResult = HTItemResult(entry, count)
}
