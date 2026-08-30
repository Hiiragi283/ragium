package hiiragi283.lib.material.part

import com.mojang.serialization.Codec
import hiiragi283.lib.material.HTMaterialAccess
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.property.HTPropertyGetter
import hiiragi283.lib.property.HTPropertyManager
import hiiragi283.lib.resource.modifyPath
import net.minecraft.resources.Identifier
import net.minecraft.util.ExtraCodecs

/**
 * [HTPart]を管理する[HTPropertyManager]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
typealias HTPartManager = HTPropertyManager<HTPartKey, HTPart>

/**
 * 部品のキーとプロパティを管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
class HTPart(override val key: HTPartKey, private val idPattern: String, getter: HTPropertyGetter) :
    HTPropertyManager.Entry<HTPartKey>,
    HTPropertyGetter by getter {
    companion object {
        @JvmStatic
        fun getManager(): HTPartManager = HTMaterialAccess.INSTANCE.getPartManager()

        @JvmField
        val CODEC: Codec<HTPart> = ExtraCodecs.idResolverCodec(HTPartKey.CODEC, { getManager()[it] }, HTPart::key)
    }

    fun createId(key: HTMaterialKey): Identifier = key.getId().modifyPath { idPattern.replace("%s", it) }

    override fun equals(other: Any?): Boolean = (other as? HTPart)?.key == this.key

    override fun hashCode(): Int = key.hashCode()

    override fun toString(): String = "HTPart(key=$key)"
}
