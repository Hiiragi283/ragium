package hiiragi283.ragium.api.tags

import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable

class HTTagPrefix(private val prefix: String) {
    companion object {
        @JvmStatic
        val registry: Map<String, HTTagPrefix>
            get() = registry1

        @JvmStatic
        private val registry1: MutableMap<String, HTTagPrefix> = mutableMapOf()

        @JvmStatic
        fun of(prefix: String): HTTagPrefix = registry1.computeIfAbsent(prefix, ::HTTagPrefix)
    }

    private fun commonId(path: String): Identifier = Identifier.of(TagUtil.C_TAG_NAMESPACE, path)

    val commonTagKey: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, commonId(prefix))

    fun createTag(value: StringIdentifiable): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, commonId("$prefix/${value.asString()}"))
}
