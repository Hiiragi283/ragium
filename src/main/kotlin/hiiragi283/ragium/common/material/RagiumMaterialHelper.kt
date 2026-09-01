package hiiragi283.ragium.common.material

import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.item.RagiumItems

data object RagiumMaterialHelper {
    @JvmStatic
    fun getFuelBase(fuel: RagiumMaterial.Fuel): HTSimpleDeferredItem = when (fuel) {
        RagiumMaterial.Fuel.COAL -> HTSimpleDeferredItem(vanillaId("coal"))
        RagiumMaterial.Fuel.CHARCOAL -> HTSimpleDeferredItem(vanillaId("charcoal"))
        RagiumMaterial.Fuel.COAL_COKE -> RagiumItems.COAL_COKE
    }
}
