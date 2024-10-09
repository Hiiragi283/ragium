package hiiragi283.ragium.api.tags

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumEnchantmentTags {
    //    Custom    //
    @JvmField
    val MODIFYING_EXCLUSIVE_SET: TagKey<Enchantment> = create(RagiumAPI.MOD_ID, "exclusive_set/modify")

    @JvmStatic
    fun create(namespace: String, path: String): TagKey<Enchantment> = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(namespace, path))

    @JvmStatic
    fun create(path: String): TagKey<Enchantment> = create(TagUtil.C_TAG_NAMESPACE, path)
}
