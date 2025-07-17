package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

class HTMaterialFamily(val entryType: EntryType, baseVariant: Variant, val variantMap: Map<Variant, Pair<TagKey<Item>, Item?>>) {
    companion object {
        @JvmStatic
        val instances: Map<String, HTMaterialFamily> get() = _instances

        @JvmStatic
        private val _instances: MutableMap<String, HTMaterialFamily> = mutableMapOf()
    }

    val baseEntry: Pair<TagKey<Item>, Item?> = getRawEntry(baseVariant)!!

    fun getRawEntry(variant: Variant): Pair<TagKey<Item>, Item?>? = variantMap[variant]

    fun getTagKey(variant: Variant): TagKey<Item>? = getRawEntry(variant)?.first

    fun getItem(variant: Variant): Item? = getRawEntry(variant)?.second

    //    Builder    //

    class Builder private constructor(private val baseVariant: Variant, baseItem: ItemLike?) {
        companion object {
            @JvmStatic
            fun gem(baseItem: ItemLike?): Builder = Builder(Variant.GEMS, baseItem)

            @JvmStatic
            fun ingot(baseItem: ItemLike?): Builder = Builder(Variant.INGOTS, baseItem)
        }

        private val itemMap: MutableMap<Variant, Item?> = mutableMapOf()
        private var entryType: EntryType = EntryType.RAGIUM

        init {
            setEntry(baseVariant, baseItem)
        }

        fun setEntry(variant: Variant, item: ItemLike?): Builder = apply {
            check(itemMap.put(variant, item?.asItem()) == null) { "Duplicated entry found!" }
        }

        fun setVanilla(): Builder = apply {
            this.entryType = EntryType.VANILLA
        }

        fun setMod(): Builder = apply {
            this.entryType = EntryType.MOD
        }

        fun build(key: String): HTMaterialFamily {
            val variantMap: Map<Variant, Pair<TagKey<Item>, Item?>> = itemMap.mapValues { (variant: Variant, item: Item?) ->
                itemTagKey(commonId("${variant.name.lowercase()}/$key")) to item
            }
            val family = HTMaterialFamily(entryType, baseVariant, variantMap)
            _instances[key] = family
            return family
        }
    }

    //    Entry    //

    enum class EntryType(val isVanilla: Boolean, val isMod: Boolean) {
        VANILLA(true, false),
        RAGIUM(false, false),
        MOD(false, true),
    }

    //    Variant    //

    enum class Variant(val commonTag: TagKey<Item>) {
        DUSTS(Tags.Items.DUSTS),
        GEMS(Tags.Items.GEMS),
        INGOTS(Tags.Items.INGOTS),
        NUGGETS(Tags.Items.NUGGETS),
        ORES(Tags.Items.ORES),
        RAW_MATERIALS(Tags.Items.RAW_MATERIALS),
        STORAGE_BLOCKS(Tags.Items.STORAGE_BLOCKS),
    }
}
