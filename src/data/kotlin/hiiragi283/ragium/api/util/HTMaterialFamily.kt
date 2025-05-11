package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.extension.asItemHolder
import net.minecraft.core.Holder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

/**
 * @see [net.minecraft.data.BlockFamily]
 */
class HTMaterialFamily private constructor(val baseVariant: Variant, val variantMap: Map<Variant, Entry>, val isVanilla: Boolean) {
    val baseEntry: Entry = variantMap[baseVariant]!!

    operator fun get(variant: Variant): Entry? = variantMap[variant]

    //    Builder    //

    class Builder private constructor(private val baseVariant: Variant, tagKey: TagKey<Item>, baseItem: ItemLike) {
        companion object {
            @JvmStatic
            fun gem(tagKey: TagKey<Item>, baseItem: ItemLike): Builder = Builder(Variant.GEM, tagKey, baseItem)

            @JvmStatic
            fun ingot(tagKey: TagKey<Item>, baseItem: ItemLike): Builder = Builder(Variant.INGOT, tagKey, baseItem)
        }

        private val variantMap: MutableMap<Variant, Entry> = mutableMapOf()
        private var isVanilla: Boolean = false

        init {
            setEntry(baseVariant, tagKey, baseItem)
        }

        fun setEntry(variant: Variant, tagKey: TagKey<Item>, item: ItemLike): Builder = apply {
            variantMap.put(variant, Entry(tagKey, item.asItemHolder()))
        }

        fun setVanilla(): Builder = apply {
            this.isVanilla = true
        }

        fun build(): HTMaterialFamily = HTMaterialFamily(baseVariant, variantMap, isVanilla)
    }

    //    Entry    //

    data class Entry(val tagKey: TagKey<Item>, val holder: Holder<Item>) : ItemLike {
        override fun asItem(): Item = holder.value()
    }

    //    Variant    //

    enum class Variant(val commonTag: TagKey<Item>) {
        DUST(Tags.Items.DUSTS),
        GEM(Tags.Items.GEMS),
        INGOT(Tags.Items.INGOTS),
        NUGGETS(Tags.Items.NUGGETS),
        ORES(Tags.Items.ORES),
        RAW_MATERIALS(Tags.Items.RAW_MATERIALS),
        RAW_STORAGE(Tags.Items.STORAGE_BLOCKS),
        STORAGE_BLOCK(Tags.Items.STORAGE_BLOCKS),
    }
}
