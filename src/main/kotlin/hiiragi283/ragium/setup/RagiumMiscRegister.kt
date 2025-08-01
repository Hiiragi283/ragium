package hiiragi283.ragium.setup

import net.minecraft.advancements.CriterionTrigger
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.RegisterEvent

object RagiumMiscRegister {
    @JvmStatic
    fun onRegister(event: RegisterEvent) {
        event.register(Registries.TRIGGER_TYPE, ::advancementTriggers)
    }

    @JvmStatic
    private fun advancementTriggers(helper: RegisterEvent.RegisterHelper<CriterionTrigger<*>>) {
        // helper.register(RagiumAPI.id("block_interaction"), HTBlockInteractionTrigger)
    }
}
