package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.serialization.codec.BiCodec
import io.netty.buffer.ByteBuf
import net.minecraft.resources.ResourceLocation

/**
 * 素材の種類を表すクラス
 *
 * まさに伝統的な設計
 */
@JvmInline
value class HTMaterialKey private constructor(val name: String) : HTMaterialLike {
    companion object {
        @JvmStatic
        private val instances: MutableMap<String, HTMaterialKey> = hashMapOf()

        @JvmStatic
        fun of(name: String): HTMaterialKey {
            check(ResourceLocation.isValidPath(name)) { "Material name $name is not valid" }
            return instances.computeIfAbsent(name, ::HTMaterialKey)
        }

        @JvmField
        val CODEC: BiCodec<ByteBuf, HTMaterialKey> = BiCodec.STRING.comapFlatMap(
            { name: String -> runCatching { of(name) } },
            HTMaterialKey::name,
        )
    }

    override fun asMaterialKey(): HTMaterialKey = this
}
