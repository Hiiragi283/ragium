package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.RagiumConst
import net.minecraft.Util
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

//    ResourceLocation    //

fun String.toId(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(this, path)

/**
 * 名前空間が`minecraft`となる[ResourceLocation]を返します。
 */

fun vanillaId(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)

/**
 * 名前空間が`c`となる[ResourceLocation]を返します。
 */
fun commonId(path: String): ResourceLocation = RagiumConst.COMMON.toId(path)

fun commonId(prefix: String, value: String): ResourceLocation = commonId("$prefix/$value")

fun ResourceKey<*>.toDescriptionKey(prefix: String, suffix: String? = null): String = location().toDescriptionKey(prefix, suffix)

fun ResourceLocation.toDescriptionKey(prefix: String, suffix: String? = null): String = buildString {
    append(Util.makeDescriptionId(prefix, this@toDescriptionKey))
    if (suffix != null) {
        append('.')
        append(suffix)
    }
}
