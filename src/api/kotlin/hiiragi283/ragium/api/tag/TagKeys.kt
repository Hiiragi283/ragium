package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.registry.commonId
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.Tags

fun <T : Any> RegistryKey<T>.createTagKey(id: ResourceLocation): TagKey<T> = TagKey.create(this, id)

fun <T : Any> RegistryKey<T>.createCommonTag(path: String): TagKey<T> = createTagKey(commonId(path))

fun <T : Any> RegistryKey<T>.createCommonTag(prefix: String, value: String): TagKey<T> = createTagKey(commonId(prefix, value))

/**
 * [TagKey]の名前を返します。
 */
fun TagKey<*>.getName(): MutableComponent = Component.translatableWithFallback(Tags.getTagTranslationKey(this), "#${this.location}")
