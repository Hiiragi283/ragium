package hiiragi283.lib.data.advancement

import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.toLanguageKey
import net.minecraft.advancements.Advancement
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * [Advancement]に対する[ResourceKey]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
typealias AdvancementKey = ResourceKey<Advancement>

/**
 * 新しい[AdvancementKey]のインスタンスを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun AdvancementKey(namespace: String, path: String): AdvancementKey = Registries.ADVANCEMENT.createKey(namespace, path)

/**
 * 新しい[AdvancementKey]のインスタンスを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun AdvancementKey(id: Identifier): AdvancementKey = Registries.ADVANCEMENT.createKey(id)

/**
 * この[AdvancementKey][this]から進捗のタイトルの翻訳キーを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
val AdvancementKey.titleKey: String get() = this.toLanguageKey("title")

/**
 * この[AdvancementKey][this]から進捗の説明の翻訳キーを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
val AdvancementKey.descKey: String get() = this.toLanguageKey("desc")
