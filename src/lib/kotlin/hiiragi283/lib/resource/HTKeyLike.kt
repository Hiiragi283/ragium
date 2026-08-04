package hiiragi283.lib.resource

import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * [キー][ResourceKey]を提供するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 * @see SupplierWithKey
 */
fun interface HTKeyLike<R : Any> : HTIdLike {
    fun getKey(): ResourceKey<R>

    fun getRegistryKey(): RegistryKey<R> = getKey().registryKey()

    override fun getId(): Identifier = getKey().identifier()

    interface Translatable<R : Any> :
        HTKeyLike<R>,
        HTIdLike.Translatable

    fun interface SimpleTranslatable<R : Any> : Translatable<R> {
        override val translationKey: String get() = getKey().toLanguageKey()

        override fun getText(): Text = translatableText(translationKey)
    }
}
