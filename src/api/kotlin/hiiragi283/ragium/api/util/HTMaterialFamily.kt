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

            @JvmStatic
            fun ingot(baseItem: ItemSup?): Builder = Builder(Variant.INGOTS, baseItem)
        }

        private val itemMap: MutableMap<Variant, ItemSup?> = mutableMapOf()
        private var entryType: EntryType = EntryType.RAGIUM

        init {
            setEntry(baseVariant, baseItem)
        }

        fun setEntry(variant: Variant, item: ItemSup?): Builder = apply {
            check(itemMap.put(variant, item) == null) { "Duplicated entry found!" }
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
