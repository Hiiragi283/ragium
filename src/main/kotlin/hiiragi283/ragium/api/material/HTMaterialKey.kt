package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toDataResult
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * 素材の種類を管理するキー
 *
 * すべてのキーは[HTMaterialRegistry]に登録される必要があります。
 *
 * @see [hiiragi283.ragium.api.event.HTRegisterMaterialEvent]
 */
class HTMaterialKey private constructor(val name: String) : Comparable<HTMaterialKey> {
    companion object {
        private val instances: MutableMap<String, HTMaterialKey> = mutableMapOf()

        @JvmField
        val CODEC: Codec<HTMaterialKey> =
            Codec.STRING.xmap(Companion::of, HTMaterialKey::name).validate { key: HTMaterialKey ->
                key
                    .takeIf(RagiumAPI.materialRegistry::contains)
                    .toDataResult { "Unknown material key: $key" }
            }

        @JvmField
        val FIELD_CODEC: MapCodec<HTMaterialKey> = CODEC.fieldOf("material")

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterialKey> =
            ByteBufCodecs.STRING_UTF8.map(Companion::of, HTMaterialKey::name)

        /**
         * 指定された[name]から単一のインスタンスを返します。
         */
        @JvmStatic
        fun of(name: String): HTMaterialKey = instances.computeIfAbsent(name, ::HTMaterialKey)
    }

    /**
     * 素材の名前の翻訳キー
     */
    val translationKey: String = "material.${RagiumAPI.MOD_ID}.$name"

    /**
     * 素材の名前の[MutableComponent]
     */
    val text: MutableComponent = Component.translatable(translationKey)

    //    Comparable    //

    override fun compareTo(other: HTMaterialKey): Int = name.compareTo(other.name)

    override fun equals(other: Any?): Boolean = (other as? HTMaterialKey)?.name == name

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = "HTMaterialKey[$name]"
}
