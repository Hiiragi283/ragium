package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.result.HTItemResultCreator
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.material.VanillaMaterialItems
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.setup.RagiumItems

object RagiumMaterialResultHelper {
    @JvmStatic
    fun item(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): HTItemResult {
        val holder: HTItemHolderLike<*> = RagiumItems.MATERIALS[prefix, material]
            ?: HCItems.MATERIALS[prefix, material]
            ?: VanillaMaterialItems.MATERIALS[prefix, material]
            ?: return HTItemResultCreator.create(prefix, material, count)
        return HTItemResultCreator.create(holder, prefix, material, count)
    }
}
