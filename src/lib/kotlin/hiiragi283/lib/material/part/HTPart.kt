package hiiragi283.lib.material.part

import com.mojang.serialization.Codec
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.property.HTPropertyGetter
import hiiragi283.lib.property.HTPropertyManager
import hiiragi283.lib.resource.modifyPath
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

/**
 * [HTPart]を管理する[HTPropertyManager]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
typealias HTPartManager = HTPropertyManager<HTPartKey, HTPart>

/**
 * 部品のキーとプロパティを管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
class HTPart internal constructor(override val key: HTPartKey, private val idPattern: String, getter: HTPropertyGetter) :
    HTPropertyManager.Entry<HTPartKey>,
    HTPropertyGetter by getter {
    companion object {
        @JvmStatic
        fun getManager(): HTPartManager = TODO()

        private fun errorMessage(key: HTPartKey): String = "Missing part: $key"

        @JvmField
        val CODEC: Codec<HTPart> = HTPropertyManager.codec(HTPartKey.CODEC, ::getManager, ::errorMessage)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTPart> = HTPropertyManager.streamCodec(HTPartKey.STREAM_CODEC, ::getManager, ::errorMessage)
    }

    fun createId(key: HTMaterialKey) = key.getId().modifyPath { idPattern.replace("%s", it) }

    override fun equals(other: Any?): Boolean = (other as? HTPart)?.key == this.key

    override fun hashCode(): Int = key.hashCode()

    override fun toString(): String = "HTPart(key=$key)"
}
