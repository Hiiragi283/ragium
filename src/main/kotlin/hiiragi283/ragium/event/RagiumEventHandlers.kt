package hiiragi283.ragium.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.OnDatapackSyncEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
data object RagiumEventHandlers {
    @SubscribeEvent
    fun onDatapackSync(event: OnDatapackSyncEvent) {
        event.sendRecipes(RagiumRecipeTypes.allTypes)
    }
}
