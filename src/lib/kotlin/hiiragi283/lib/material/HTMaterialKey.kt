package hiiragi283.lib.material

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.toId
import hiiragi283.lib.tag.HTTagMaterial
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
 * @since 26.1.2
 */
@JvmRecord
data class HTMaterialKey(private val id: Identifier) :
    HTIdLike.Translatable,
    HTTagMaterial,
    Comparable<HTMaterialKey> {
    companion object {
        @JvmField
        val SIMPLE_CODEC: Codec<HTMaterialKey> = Identifier.CODEC.xmap(::HTMaterialKey, HTMaterialKey::getId)

        @JvmField
        val CODEC: Codec<HTMaterialKey> = SIMPLE_CODEC.validate { key: HTMaterialKey ->
            when (key) {
                !in HTMaterial.getManager() -> DataResult.error { "Unregistered material $key" }
                else -> DataResult.success(key)
            }
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterialKey> = Identifier.STREAM_CODEC.map(::HTMaterialKey, HTMaterialKey::getId)
    }

    constructor(namespace: String, path: String) : this(namespace.toId(path))

    override val materialName: String get() = this.path

    override fun getId(): Identifier = id

    override val translationKey: String get() = getId().toLanguageKey("material")

    override fun getText(): Text = translatableText(translationKey)

    override fun compareTo(other: HTMaterialKey): Int = this.id.compareNamespaced(other.id)
}
