package hiiragi283.lib.material

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.tag.HTMaterialLike

interface HTMaterial :
    HTMaterialLike,
    HTIdLike,
    HTLangName {
    val category: HTMaterialCategory

    override val materialName: String get() = this.path
}
