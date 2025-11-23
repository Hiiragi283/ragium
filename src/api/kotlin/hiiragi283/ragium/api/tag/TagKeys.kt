package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.registry.commonId
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.Tags

/**
 * [RegistryKey]に基づいて[ResourceLocation]を[TagKey]に変換します。
 */
fun <T : Any> RegistryKey<T>.createTagKey(id: ResourceLocation): TagKey<T> = TagKey.create(this, id)

/**
 * [RegistryKey]に基づいて名前空間が`c`となる[TagKey]に変換します。
 */
fun <T : Any> RegistryKey<T>.createCommonTag(path: String): TagKey<T> = createTagKey(commonId(path))

/**
 * [RegistryKey]に基づいて名前空間が`c`となる[TagKey]に変換します。
 */
fun <T : Any> RegistryKey<T>.createCommonTag(vararg path: String): TagKey<T> = createTagKey(commonId(*path))

/**
 * [TagKey]の名前を返します。
 */
fun TagKey<*>.getName(): MutableComponent = Component.translatableWithFallback(Tags.getTagTranslationKey(this), "#${this.location}")
