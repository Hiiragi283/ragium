package hiiragi283.ragium.common.init

import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumItemTags {
    @JvmField
    val STEEL_INGOTS: TagKey<Item> = create("ingots/steel")

    @JvmStatic
    fun create(id: Identifier): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, id)

    @JvmStatic
    fun create(path: String): TagKey<Item> = create(Identifier.of(TagUtil.C_TAG_NAMESPACE, path))
}
