package hiiragi283.ragium.api.material.prefix

import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.RegistryKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

fun interface HTPrefixLike {
    fun asMaterialPrefix(): HTMaterialPrefix

    fun asPrefixName(): String = asMaterialPrefix().name

    fun isOf(other: HTPrefixLike): Boolean = this.asMaterialPrefix() == other.asMaterialPrefix()

    fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> = asMaterialPrefix().createCommonTagKey(key)

    fun <T : Any> createTagKey(key: RegistryKey<T>, material: HTMaterialLike): TagKey<T> = createTagKey(key, material.asMaterialName())

    fun <T : Any> createTagKey(key: RegistryKey<T>, name: String): TagKey<T> = asMaterialPrefix().createTagKey(key, name)

    fun itemTagKey(material: HTMaterialLike): TagKey<Item> = createTagKey(Registries.ITEM, material)
}
