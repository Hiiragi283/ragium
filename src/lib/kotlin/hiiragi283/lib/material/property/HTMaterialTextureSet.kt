package hiiragi283.lib.material.property

import hiiragi283.lib.material.part.HTPartKey
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.resources.Identifier

/**
 * テクスチャを生成する際のプリセットを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
class HTMaterialTextureSet(val name: String, val parent: HTMaterialTextureSet?) {
    companion object {
        @JvmField
        val DEFAULT = HTMaterialTextureSet("default", null)

        @JvmField
        val DULL = HTMaterialTextureSet("dull", DEFAULT)

        @JvmField
        val SHINE = HTMaterialTextureSet("shine", DEFAULT)

        @JvmField
        val MYSTICAL = HTMaterialTextureSet("mystical", SHINE)
    }

    operator fun get(part: HTPartKey): Identifier = parent?.get(part) ?: RagiumAPI.id(name, part.name)
}
