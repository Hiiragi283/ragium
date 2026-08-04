package hiiragi283.lib.material

import com.mojang.serialization.Codec
import hiiragi283.lib.property.HTPropertyGetter
import hiiragi283.lib.property.HTPropertyManager
import hiiragi283.lib.resource.HTIdLike
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier

/**
 * [HTMaterial]を管理する[HTPropertyManager]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
typealias HTMaterialManager = HTPropertyManager<HTMaterialKey, HTMaterial>

/**
 * 素材のキーとプロパティを保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
class HTMaterial internal constructor(override val key: HTMaterialKey, getter: HTPropertyGetter) :
    HTPropertyManager.Entry<HTMaterialKey>,
    HTIdLike,
    HTPropertyGetter by getter {
    companion object {
        @JvmStatic
        fun getManager(): HTMaterialManager = TODO()

        private fun errorMessage(key: HTMaterialKey): String = "Missing material: $key"

        @JvmField
        val CODEC: Codec<HTMaterial> = HTPropertyManager.codec(HTMaterialKey.CODEC, ::getManager, ::errorMessage)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterial> = HTPropertyManager.streamCodec(HTMaterialKey.STREAM_CODEC, ::getManager, ::errorMessage)
    }

    override fun getId(): Identifier = key.getId()

    override fun equals(other: Any?): Boolean = (other as? HTMaterial)?.key == this.key

    override fun hashCode(): Int = key.hashCode()

    override fun toString(): String = "HTMaterial(key=$key)"
}
