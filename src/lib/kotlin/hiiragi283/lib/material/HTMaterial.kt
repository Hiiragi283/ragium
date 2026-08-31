package hiiragi283.lib.material

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.tag.HTMaterialLike
import net.minecraft.resources.Identifier

interface HTMaterial :
    HTMaterialLike,
    HTLangName {
    val category: HTMaterialCategory

    fun getId(): Identifier

    override val materialName: String get() = this.getId().path
}
