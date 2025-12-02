package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import net.neoforged.neoforge.registries.RegisterEvent

object RagiumIntegrationMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Runtime Recipe Type
        event.register(RagiumAPI.RUNTIME_RECIPE_TYPE_KEY) {}
    }
}
