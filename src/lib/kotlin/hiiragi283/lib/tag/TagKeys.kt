package hiiragi283.lib.tag

import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.text.MutableText
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.Tags

/**
 * 指定した[レジストリキー][RegistryKey]と[ID][Identifier]から[TagKey]を作成します。
 * @param T レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : Any> RegistryKey<T>.createTagKey(id: Identifier): TagKey<T> = TagKey.create(this, id)

/**
 * この[TagKey]の名前を取得します。
 * @return 翻訳がない場合は`#`を先頭につけた[ID][TagKey.location]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun TagKey<*>.getName(): MutableText = Component.translatableWithFallback(Tags.getTagTranslationKey(this), "#${this.location}")
