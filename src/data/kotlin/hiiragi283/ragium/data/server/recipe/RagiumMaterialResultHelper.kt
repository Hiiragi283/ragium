package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.result.HTFluidResultCreator
import hiiragi283.core.api.data.recipe.result.HTItemResultCreator
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems

object RagiumMaterialResultHelper {
    @JvmStatic
    fun item(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): HTItemResult {
        val holder: HTItemHolderLike<*> = RagiumItems.MATERIALS[prefix, material]
            ?: HCItems.MATERIALS[prefix, material]
            ?: VanillaMaterialKeys.INGREDIENTS[prefix, material]
            ?: return HTItemResultCreator.create(prefix, material, count)
        return HTItemResultCreator.create(holder, prefix, material, count)
    }

    @JvmStatic
    fun fluid(prefix: HTPrefixLike, material: HTMaterialLike, amount: Int): HTFluidResult {
        val holder: HTSimpleFluidContent = RagiumFluids.MATERIALS[prefix, material]
            ?: HCFluids.MATERIALS[prefix, material]
            ?: return HTFluidResultCreator.create(prefix.fluidTagKey(material), amount)
        return HTFluidResultCreator.create(holder, amount)
    }
}
