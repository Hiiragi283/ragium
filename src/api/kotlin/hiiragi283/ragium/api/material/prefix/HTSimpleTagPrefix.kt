package hiiragi283.ragium.api.material.prefix

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.material.HTMaterialKey
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

open class HTSimpleTagPrefix(override val name: String) : HTTagPrefix {
    override fun createPath(key: HTMaterialKey): String = "${key.name}_$name"

    private val prefixName = "${name}s"

    override fun <T : Any> createCommonTag(registryKey: ResourceKey<out Registry<T>>): TagKey<T> =
        TagKey.create(registryKey, commonId(prefixName))

    override fun <T : Any> createTag(registryKey: ResourceKey<out Registry<T>>, key: HTMaterialKey): TagKey<T> =
        TagKey.create(registryKey, commonId("$prefixName/${key.name}"))
}
