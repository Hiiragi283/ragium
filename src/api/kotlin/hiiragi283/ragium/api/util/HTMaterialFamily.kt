package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.util.HTMaterialFamily.Variant
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import java.util.function.Supplier

private typealias ItemSup = Supplier<out ItemLike>

class HTMaterialFamily(
    val entryType: EntryType,
    private val baseVariant: Variant,
    val variantMap: Map<Variant, Pair<TagKey<Item>, ItemSup?>>,
) : Iterable<Triple<Variant, TagKey<Item>, ItemLike?>> {
    companion object {
        @JvmStatic
        val instances: Map<String, HTMaterialFamily> get() = _instances

        @JvmStatic
        private val _instances: MutableMap<String, HTMaterialFamily> = mutableMapOf()
    }

    private fun getRawEntry(variant: Variant): Pair<TagKey<Item>, ItemSup?>? = variantMap[variant]

    fun getTagKey(variant: Variant): TagKey<Item>? = getRawEntry(variant)?.first

    fun getBaseTagKey(): TagKey<Item> = getTagKey(baseVariant)!!

    fun getItem(variant: Variant): ItemLike? = getRawEntry(variant)?.second?.get()

    fun getBaseItem(): ItemLike? = getItem(baseVariant)

    override fun iterator(): Iterator<Triple<Variant, TagKey<Item>, ItemLike?>> = variantMap
        .map { (variant: Variant, pair: Pair<TagKey<Item>, ItemSup?>) ->
            Triple(variant, pair.first, pair.second?.get())
        }.iterator()

    //    Builder    //

    class Builder private constructor(private val baseVariant: Variant, baseItem: ItemSup?) {
        companion object {
            @JvmStatic
            fun gem(baseItem: ItemSup?): Builder = Builder(Variant.GEMS, baseItem)
                .setEmptyEntry(Variant.DUSTS)
                .setEmptyEntry(Variant.ORES)
                .setEmptyEntry(Variant.STORAGE_BLOCKS)

            @JvmStatic
            fun ingot(baseItem: ItemSup?): Builder = Builder(Variant.INGOTS, baseItem)
                .setEmptyEntry(Variant.DUSTS)
                .setEmptyEntry(Variant.NUGGETS)
                .setEmptyEntry(Variant.ORES)
                .setEmptyEntry(Variant.RAW_MATERIALS)
                .setEmptyEntry(Variant.STORAGE_BLOCKS)

            @JvmStatic
            fun ingotAlloy(baseItem: ItemSup?): Builder = ingot(baseItem)
                .removeEntry(Variant.ORES)
                .removeEntry(Variant.RAW_MATERIALS)
        }

        private val itemMap: MutableMap<Variant, ItemSup?> = mutableMapOf()
        private var entryType: EntryType = EntryType.RAGIUM

        init {
            if (baseItem == null) {
                setEmptyEntry(baseVariant)
            } else {
                setDefaultedEntry(baseVariant, baseItem)
            }
        }

        fun setEmptyEntry(variant: Variant): Builder = apply {
            check(itemMap.put(variant, null) == null) { "Duplicated entry found!" }
        }

        fun setDefaultedEntry(variant: Variant, item: ItemSup): Builder = apply {
            check(itemMap.put(variant, item) == null) { "Duplicated entry found!" }
        }

        fun removeEntry(variant: Variant): Builder = apply {
            itemMap.remove(variant)
        }

        fun setVanilla(): Builder = apply {
            this.entryType = EntryType.VANILLA
        }

        fun setMod(): Builder = apply {
            this.entryType = EntryType.MOD
        }

        fun build(key: String): HTMaterialFamily {
            val variantMap: Map<Variant, Pair<TagKey<Item>, ItemSup?>> =
                itemMap.mapValues { (variant: Variant, item: ItemSup?) ->
                    itemTagKey(commonId(variant.tagFormat.replace("%s", key))) to item
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

    enum class Variant(val commonTag: TagKey<Item>, val tagFormat: String) {
        DUSTS(Tags.Items.DUSTS, "dusts/%s"),
        GEMS(Tags.Items.GEMS, "gems/%s"),
        INGOTS(Tags.Items.INGOTS, "ingots/%s"),
        NUGGETS(Tags.Items.NUGGETS, "nuggets/%s"),
        ORES(Tags.Items.ORES, "ores/%s"),
        RAW_MATERIALS(Tags.Items.RAW_MATERIALS, "raw_materials/%s"),
        STORAGE_BLOCKS(Tags.Items.STORAGE_BLOCKS, "storage_blocks/%s"),
    }
}
