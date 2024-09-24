package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumItemTags {
    @JvmField
    val ORGANIC_OILS: TagKey<Item> = create(Ragium.MOD_ID, "organic_oils")

    @JvmField
    val RAGINITE_ORES: TagKey<Item> = create("ores/raginite")

    @JvmField
    val STEEL_INGOTS: TagKey<Item> = create("ingots/steel")

    @JvmStatic
    fun create(namespace: String, path: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.of(namespace, path))

    @JvmStatic
    fun create(path: String): TagKey<Item> = create(TagUtil.C_TAG_NAMESPACE, path)
}
