package hiiragi283.ragium.common.integration

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.common.data.map.HTSoulVialEntityIngredient
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.RegisterEvent

@EventBusSubscriber
data object RagiumIntegrationMiscRegister {
    @SubscribeEvent
    fun register(event: RegisterEvent) {
        // Sub Entity Type Ingredient
        event.register(RagiumAPI.SUB_ENTITY_INGREDIENT_TYPE_KEY) { helper ->
            helper.register(RagiumConst.EIO_BASE.toId("soul_vial"), HTSoulVialEntityIngredient.CODEC)
        }
    }
}
