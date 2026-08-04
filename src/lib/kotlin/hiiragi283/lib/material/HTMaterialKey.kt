package hiiragi283.lib.material

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier

/**
 * 素材の種類を表すクラスです。
 *
 * まさに伝統的な設計
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTMaterialKey(private val id: Identifier) :
    HTIdLike.Translatable,
    Comparable<HTMaterialKey> {
    companion object {
        /**
         * 指定した[id]から[HTMaterialKey]のインスタンスを返します。
         * @return キャッシュから取得した[HTMaterialKey]のインスタンス
         */
        @JvmStatic
        fun of(id: Identifier): HTMaterialKey = HTMaterialKey(id)

        /**
         * [HTMaterialKey]の[Codec]
         */
        @JvmField
        val CODEC: Codec<HTMaterialKey> = Identifier.CODEC.xmap(::HTMaterialKey, HTMaterialKey::getId)

        /**
         * [HTMaterialKey]の[StreamCodec]
         */
        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterialKey> = Identifier.STREAM_CODEC.map(::HTMaterialKey, HTMaterialKey::getId)
    }

    override fun getId(): Identifier = id

    override val translationKey: String get() = getId().toLanguageKey(HTConstants.MATERIAL)

    override fun getText(): Text = translatableText(translationKey)

    override fun compareTo(other: HTMaterialKey): Int = this.id.compareNamespaced(other.id)
}
