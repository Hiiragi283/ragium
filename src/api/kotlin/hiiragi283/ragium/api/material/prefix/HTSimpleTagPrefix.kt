package hiiragi283.ragium.api.material.prefix

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.material.HTMaterial
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

open class HTSimpleTagPrefix(override val name: String) : HTTagPrefix {
    override fun createPath(material: HTMaterial): String = "${material.materialName}_$name"

    private val prefixName = "${name}s"

    override fun <T : Any> createCommonTag(registryKey: ResourceKey<out Registry<T>>): TagKey<T> =
        TagKey.create(registryKey, commonId(prefixName))

    override fun <T : Any> createTag(registryKey: ResourceKey<out Registry<T>>, material: HTMaterial): TagKey<T> =
        TagKey.create(registryKey, commonId("$prefixName/${material.materialName}"))
}
