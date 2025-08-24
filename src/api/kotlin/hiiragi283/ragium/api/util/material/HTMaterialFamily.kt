package hiiragi283.ragium.api.util.material

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

private typealias ItemSup = Supplier<out ItemLike>

/**
 * @see [net.minecraft.data.BlockFamily]
 */
class HTMaterialFamily(
    val entryType: EntryType,
    private val baseVariant: HTMaterialVariant.ItemTag,
    val variantMap: Map<HTMaterialVariant.ItemTag, Pair<TagKey<Item>, ItemSup?>>,
) : Iterable<Triple<HTMaterialVariant.ItemTag, TagKey<Item>, ItemLike?>> {
    companion object {
        @JvmStatic
        val instances: Map<String, HTMaterialFamily> get() = _instances

        @JvmStatic
        private val _instances: MutableMap<String, HTMaterialFamily> = mutableMapOf()
    }

    private fun getRawEntry(variant: HTMaterialVariant.ItemTag): Pair<TagKey<Item>, ItemSup?>? = variantMap[variant]

    fun getTagKey(variant: HTMaterialVariant.ItemTag): TagKey<Item>? = getRawEntry(variant)?.first

    fun getBaseTagKey(): TagKey<Item> = getTagKey(baseVariant)!!

    fun getItem(variant: HTMaterialVariant.ItemTag): ItemLike? = getRawEntry(variant)?.second?.get()

    fun getBaseItem(): ItemLike? = getItem(baseVariant)

    override fun iterator(): Iterator<Triple<HTMaterialVariant.ItemTag, TagKey<Item>, ItemLike?>> = variantMap
        .map { (variant: HTMaterialVariant.ItemTag, pair: Pair<TagKey<Item>, ItemSup?>) ->
            Triple(variant, pair.first, pair.second?.get())
        }.iterator()

    //    Builder    //

    class Builder(private val baseVariant: HTMaterialVariant.ItemTag, baseItem: ItemSup?) {
        companion object {
            @JvmStatic
            fun fuel(baseItem: ItemSup?): Builder = Builder(HTItemMaterialVariant.FUEL, baseItem)
                .setEmptyEntry(HTItemMaterialVariant.DUST)
                .setEmptyEntry(HTBlockMaterialVariant.ORE)
                .setEmptyEntry(HTBlockMaterialVariant.STORAGE_BLOCK)

            @JvmStatic
            fun gem(baseItem: ItemSup?): Builder = Builder(HTItemMaterialVariant.GEM, baseItem)
                .setEmptyEntry(HTItemMaterialVariant.DUST)
                .setEmptyEntry(HTBlockMaterialVariant.ORE)
                .setEmptyEntry(HTBlockMaterialVariant.STORAGE_BLOCK)

            @JvmStatic
            fun ingot(baseItem: ItemSup?): Builder = Builder(HTItemMaterialVariant.INGOT, baseItem)
                .setEmptyEntry(HTItemMaterialVariant.DUST)
                .setEmptyEntry(HTItemMaterialVariant.NUGGET)
                .setEmptyEntry(HTBlockMaterialVariant.ORE)
                .setEmptyEntry(HTItemMaterialVariant.RAW_MATERIAL)
                .setEmptyEntry(HTBlockMaterialVariant.STORAGE_BLOCK)

            @JvmStatic
            fun ingotAlloy(baseItem: ItemSup?): Builder = ingot(baseItem)
                .removeEntry(HTBlockMaterialVariant.ORE)
                .removeEntry(HTItemMaterialVariant.RAW_MATERIAL)
        }

        private val itemMap: MutableMap<HTMaterialVariant.ItemTag, ItemSup?> = mutableMapOf()
        private var entryType: EntryType = EntryType.RAGIUM

        init {
            if (baseItem == null) {
                setEmptyEntry(baseVariant)
            } else {
                setDefaultedEntry(baseVariant, baseItem)
            }
        }

        fun setEmptyEntry(variant: HTMaterialVariant.ItemTag): Builder = apply {
            check(itemMap.put(variant, null) == null) { "Duplicated entry found!" }
        }

        fun setDefaultedEntry(variant: HTMaterialVariant.ItemTag, item: ItemSup?): Builder = apply {
            check(itemMap.put(variant, item) == null) { "Duplicated entry found!" }
        }

        fun removeEntry(variant: HTMaterialVariant.ItemTag): Builder = apply {
            itemMap.remove(variant)
        }

        fun setVanilla(): Builder = apply {
            this.entryType = EntryType.VANILLA
        }

        fun setMod(): Builder = apply {
            this.entryType = EntryType.MOD
        }

        fun build(material: HTMaterialType): HTMaterialFamily {
            val variantMap: Map<HTMaterialVariant.ItemTag, Pair<TagKey<Item>, ItemSup?>> =
                itemMap.mapValues { (variant: HTMaterialVariant.ItemTag, item: ItemSup?) ->
                    variant.itemTagKey(material) to item
                }
            val family = HTMaterialFamily(entryType, baseVariant, variantMap)
            _instances[material.serializedName] = family
            return family
        }
    }

    //    Entry    //

    enum class EntryType {
        VANILLA,
        RAGIUM,
        MOD,
    }
}
