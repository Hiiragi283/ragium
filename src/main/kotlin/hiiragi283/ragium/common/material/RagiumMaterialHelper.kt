package hiiragi283.ragium.common.material

import hiiragi283.lib.material.CommonMaterials
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialCategory
import hiiragi283.lib.material.HTMaterialManager
import hiiragi283.lib.material.VanillaMaterials
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import hiiragi283.ragium.common.item.RagiumItems

data object RagiumMaterialHelper {
    @JvmField
    val MANAGER: HTMaterialManager = HTMaterialManager.create(
        buildList {
            addAll(VanillaMaterials.entries)
            addAll(CommonMaterials.entries)
            addAll(RagiumMaterials.entries)
        },
    )

    @JvmStatic
    fun getFuelBase(fuel: HTMaterial): HTSimpleDeferredItem? {
        if (fuel.category != HTMaterialCategory.FUEL) return null
        return when (fuel) {
            VanillaMaterials.COAL -> HTSimpleDeferredItem(vanillaId("coal"))
            VanillaMaterials.CHARCOAL -> HTSimpleDeferredItem(vanillaId("charcoal"))
            CommonMaterials.COAL_COKE -> RagiumItems.COAL_COKE
            else -> null
        }
    }
}
