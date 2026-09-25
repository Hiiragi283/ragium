package hiiragi283.lib.tag

import net.minecraft.resources.Identifier

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
@JvmRecord
data class HTMaterialKey(override val materialName: String) : HTMaterialLike {
    init {
        require(Identifier.isValidPath(materialName)) {
            "Material name $materialName is not valid for identifier path"
        }
    }
}
