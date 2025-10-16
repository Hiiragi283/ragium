package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.RagiumConst
import net.minecraft.Util
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

//    ResourceLocation    //

/**
 * この文字列を名前空間として[ResourceLocation]を返します。
 */
fun String.toId(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(this, path)

/**
 * この文字列を名前空間として[ResourceLocation]を返します。
 */
fun String.toId(prefix: String, value: String): ResourceLocation = this.toId("$prefix/$value")

/**
 * 名前空間が`minecraft`となる[ResourceLocation]を返します。
 */
fun vanillaId(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)

/**
 * 名前空間が`minecraft`となる[ResourceLocation]を返します。
 */
fun vanillaId(prefix: String, value: String): ResourceLocation = ResourceLocation.withDefaultNamespace("$prefix/$value")

/**
 * 名前空間が`c`となる[ResourceLocation]を返します。
 */
fun commonId(path: String): ResourceLocation = RagiumConst.COMMON.toId(path)

/**
 * 名前空間が`c`となる[ResourceLocation]を返します。
 */
fun commonId(prefix: String, value: String): ResourceLocation = commonId("$prefix/$value")

/**
 * この[ResourceKey]から翻訳キーに変換します。
 */
fun ResourceKey<*>.toDescriptionKey(prefix: String, suffix: String? = null): String = location().toDescriptionKey(prefix, suffix)

/**
 * この[ResourceLocation]から翻訳キーに変換します。
 */
fun ResourceLocation.toDescriptionKey(prefix: String, suffix: String? = null): String = buildString {
    append(Util.makeDescriptionId(prefix, this@toDescriptionKey))
    if (suffix != null) {
        append('.')
        append(suffix)
    }
}
