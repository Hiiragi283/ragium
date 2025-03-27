package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.property.HTPropertyMap
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
 * @see [RagiumAddon.onMaterialRegister]
 */
class HTMaterialKey private constructor(val name: String) : Comparable<HTMaterialKey> {
    companion object {
        private val instances: MutableMap<String, HTMaterialKey> = mutableMapOf()

        @JvmField
        val CODEC: Codec<HTMaterialKey> =
            Codec.STRING.xmap(::of, HTMaterialKey::name).validate { key: HTMaterialKey ->
                if (key !in RagiumAPI.getInstance().getMaterialRegistry()) {
                    return@validate DataResult.error { "Unknown material key: $key" }
                }
                DataResult.success(key)
            }

        @JvmField
        val FIELD_CODEC: MapCodec<HTMaterialKey> = CODEC.fieldOf("material")

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterialKey> =
            ByteBufCodecs.STRING_UTF8.map(::of, HTMaterialKey::name)

        /**
         * 指定された[name]から単一のインスタンスを返します。
         */
        @JvmStatic
        fun of(name: String): HTMaterialKey = instances.computeIfAbsent(name, ::HTMaterialKey)
    }

    /**
     * 素材の名前の翻訳キー
     */
    @JvmField
    val translationKey: String = "material.${RagiumAPI.MOD_ID}.$name"

    /**
     * 素材の名前の[MutableComponent]
     */
    val text: MutableComponent
        get() = Component.translatable(translationKey)

    private lateinit var propertyCache: HTPropertyMap

    fun getPropertyMap(): HTPropertyMap {
        if (!::propertyCache.isInitialized) {
            propertyCache = RagiumAPI.getInstance().getMaterialRegistry().getPropertyMap(this)
        }
        return propertyCache
    }

    //    Comparable    //

    override fun compareTo(other: HTMaterialKey): Int = name.compareTo(other.name)

    override fun equals(other: Any?): Boolean = (other as? HTMaterialKey)?.name == name

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = "HTMaterialKey[$name]"
}
