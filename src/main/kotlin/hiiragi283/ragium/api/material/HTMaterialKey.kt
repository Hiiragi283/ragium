package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toDataResult
import io.netty.buffer.ByteBuf
import net.minecraft.component.ComponentType
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.text.MutableText
import net.minecraft.text.Text

/**
 * 素材の種類を管理するキー
 *
 * すべてのキーは[HTMaterialRegistry]に登録される必要があります。
 *
 * @see [hiiragi283.ragium.api.RagiumPlugin.registerMaterial]
 */
class HTMaterialKey private constructor(val name: String) : Comparable<HTMaterialKey> {
    companion object {
        private val instances: MutableMap<String, HTMaterialKey> = mutableMapOf()

        @JvmField
        val CODEC: Codec<HTMaterialKey> =
            Codec.STRING.xmap(Companion::of, HTMaterialKey::name).validate { key: HTMaterialKey ->
                key
                    .takeIf { it in RagiumAPI.getInstance().materialRegistry }
                    .toDataResult { "Unknown material key: $key" }
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMaterialKey> =
            PacketCodecs.STRING.xmap(Companion::of, HTMaterialKey::name)

        @JvmField
        val COMPONENT_TYPE: ComponentType<HTMaterialKey> =
            ComponentType
                .builder<HTMaterialKey>()
                .codec(CODEC)
                .packetCodec(PACKET_CODEC)
                .build()

        /**
         * 指定された[name]から単一のインスタンスを返します。
         */
        @JvmStatic
        fun of(name: String): HTMaterialKey = instances.computeIfAbsent(name, ::HTMaterialKey)
    }

    val translationKey: String = "material.${RagiumAPI.MOD_ID}.$name"
    val text: MutableText
        get() = Text.translatable(translationKey)

    /**
     * [HTMaterialRegistry.Entry]を返します。
     * @return このキーが登録されていない場合はnullを返す
     */
    fun getEntryOrNull(): HTMaterialRegistry.Entry? = RagiumAPI.getInstance().materialRegistry.getEntryOrNull(this)

    /**
     * [getEntryOrNull]がnullでない場合に[action]を実行します。
     * @return [action]の戻り値を[DataResult]で包みます
     */
    fun <T : Any> useEntry(action: (HTMaterialRegistry.Entry) -> T): DataResult<T> =
        getEntryOrNull()?.let(action).toDataResult { "Unknown machine key: $this" }

    //    Comparable    //

    override fun compareTo(other: HTMaterialKey): Int = name.compareTo(other.name)

    override fun toString(): String = "HTMaterialKey[$name]"
}
