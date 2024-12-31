package hiiragi283.ragium.api.tags

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumItemTags {
    @JvmField
    val ALKALI: TagKey<Item> = create("alkali")

    @JvmField
    val PROTEIN_FOODS: TagKey<Item> = create("foods/protein")

    @JvmField
    val SILICON: TagKey<Item> = create("silicon")

    @JvmField
    val SILICON_PLATES: TagKey<Item> = create("plates/silicon")

    @JvmField
    val REFINED_SILICON_PLATES: TagKey<Item> = create("plates/refined_silicon")

    /**
     * [hiiragi283.ragium.api.block.HTMachineBlockEntityBase]を強化できるアイテムの一覧
     */
    @JvmField
    val ADVANCED_UPGRADES: TagKey<Item> = create(RagiumAPI.MOD_ID, "upgrades/advanced")

    /**
     * [hiiragi283.ragium.api.block.HTMachineBlockEntityBase]を強化できるアイテムの一覧
     */
    @JvmField
    val BASIC_UPGRADES: TagKey<Item> = create(RagiumAPI.MOD_ID, "upgrades/basic")

    /**
     * 液体フィルタとして機能するアイテムのタグ
     */
    @JvmField
    val FLUID_EXPORTER_FILTERS: TagKey<Item> = create(RagiumAPI.MOD_ID, "exporter_filters/fluid")

    /**
     * アイテムフィルタとして機能するアイテムのタグ
     */
    @JvmField
    val ITEM_EXPORTER_FILTERS: TagKey<Item> = create(RagiumAPI.MOD_ID, "exporter_filters/item")

    @JvmStatic
    fun create(namespace: String, path: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.of(namespace, path))

    @JvmStatic
    fun create(path: String): TagKey<Item> = create(TagUtil.C_TAG_NAMESPACE, path)
}
