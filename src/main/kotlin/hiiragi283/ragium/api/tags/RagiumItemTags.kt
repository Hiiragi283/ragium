package hiiragi283.ragium.api.tags

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.item.Item
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

    @JvmStatic
    fun create(namespace: String, path: String): TagKey<Item> = itemTagKey(Identifier.of(namespace, path))

    @JvmStatic
    fun create(path: String): TagKey<Item> = itemTagKey(commonId(path))
}
