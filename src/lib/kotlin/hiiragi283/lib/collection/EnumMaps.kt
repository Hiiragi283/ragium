package hiiragi283.lib.collection

import java.util.EnumMap

/**
 * 新しい[EnumMap]のインスタンスを作成します。
 * @param K [Enum]を継承したクラス
 * @param V 値のクラス
 * @return 読み取り専用の[Map]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified K : Enum<K>, V> enumMapOf(): Map<K, V> = EnumMap<K, V>(K::class.java)

/**
 * 新しい[EnumMap]のインスタンスを作成します。
 * @param K [Enum]を継承したクラス
 * @param V 値のクラス
 * @return 読み取り専用の[Map]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified K : Enum<K>, V> enumMapOf(vararg pairs: Pair<K, V>): Map<K, V> = mutableMapOf(*pairs)

/**
 * 新しい[EnumMap]のインスタンスを作成します。
 * @param K [Enum]を継承したクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified K : Enum<K>, V> mutableEnumMapOf(): MutableMap<K, V> = EnumMap<K, V>(K::class.java)

/**
 * 新しい[EnumMap]のインスタンスを作成します。
 * @param K [Enum]を継承したクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <reified K : Enum<K>, V> mutableEnumMapOf(vararg pairs: Pair<K, V>): MutableMap<K, V> = pairs.toMap(mutableEnumMapOf())
