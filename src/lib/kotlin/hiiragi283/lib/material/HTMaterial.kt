package hiiragi283.lib.material

import com.mojang.serialization.Codec
import hiiragi283.lib.property.HTPropertyGetter
import hiiragi283.lib.property.HTPropertyManager
import hiiragi283.lib.resource.HTIdLike
import net.minecraft.resources.Identifier
import net.minecraft.util.ExtraCodecs

/**
 * [HTMaterial]を管理する[HTPropertyManager]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
typealias HTMaterialManager = HTPropertyManager<HTMaterialKey, HTMaterial>

/**
 * 素材のキーとプロパティを保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
class HTMaterial(override val key: HTMaterialKey, getter: HTPropertyGetter) :
    HTPropertyManager.Entry<HTMaterialKey>,
    HTIdLike,
    HTPropertyGetter by getter {
    companion object {
        @JvmStatic
        fun getManager(): HTMaterialManager = HTMaterialAccess.INSTANCE.getMaterialManager()

        @JvmField
        val CODEC: Codec<HTMaterial> = ExtraCodecs.idResolverCodec(HTMaterialKey.SIMPLE_CODEC, { getManager()[it] }, HTMaterial::key)
    }

    override fun getId(): Identifier = key.getId()

    override fun equals(other: Any?): Boolean = (other as? HTMaterial)?.key == this.key

    override fun hashCode(): Int = key.hashCode()

    override fun toString(): String = "HTMaterial(key=$key)"
}
