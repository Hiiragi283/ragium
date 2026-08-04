@file:Suppress("NOTHING_TO_INLINE")

package hiiragi283.lib.resource

import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

//    Identifier    //

/**
 * この[文字列][this]を[名前空間][Identifier.getNamespace]とした[ID][Identifier]を作成します。
 * @param path IDの[パス][Identifier.getPath]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun String.toId(path: String): Identifier = Identifier.fromNamespaceAndPath(this, path)

/**
 * この[文字列][this]を[名前空間][Identifier.getNamespace]とした[ID][Identifier]を作成します。
 * @param path IDの[パス][Identifier.getPath]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun String.toId(vararg path: String): Identifier = this.toId(path.joinToString(separator = "/"))

/**
 * 名前空間が`minecraft`となる[ID][Identifier]を作成します。
 * @param path IDの[パス][Identifier.getPath]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun vanillaId(path: String): Identifier = Identifier.withDefaultNamespace(path)

/**
 * 名前空間が`minecraft`となる[ID][Identifier]を作成します。
 * @param path IDの[パス][Identifier.getPath]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun vanillaId(vararg path: String): Identifier = Identifier.withDefaultNamespace(path.joinToString(separator = "/"))

//    ResourceKey    //

/**
 * この[ResourceKey]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
inline fun ResourceKey<*>.toLanguageKey(): String = this.identifier().toLanguageKey(this.registryKey().identifier().path)

/**
 * この[ResourceKey]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
inline fun ResourceKey<*>.toLanguageKey(suffix: String): String = this.identifier().toLanguageKey(this.registryKey().identifier().path, suffix)

//    HTIdLike    //

/**
 * この[HTIdLike]から，`block/`で前置された[ID][HTIdLike.getId]を返します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val HTIdLike.blockId: Identifier get() = when {
    this.path.startsWith("block/") -> getId()
    else -> getId().withPrefix("block/")
}

/**
 * この[HTIdLike]から，`item/`で前置された[ID][HTIdLike.getId]を返します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val HTIdLike.itemId: Identifier get() = when {
    this.path.startsWith("item/") -> getId()
    else -> getId().withPrefix("item/")
}
