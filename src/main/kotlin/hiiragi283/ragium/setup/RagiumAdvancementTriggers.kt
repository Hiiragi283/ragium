package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.advancements.HTBlockInteractionTrigger
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object RagiumAdvancementTriggers {
    @JvmField
    val REGISTER: DeferredRegister<CriterionTrigger<*>> =
        DeferredRegister.create(Registries.TRIGGER_TYPE, RagiumAPI.MOD_ID)

    @JvmField
    val BLOCK_INTERACTION: Supplier<HTBlockInteractionTrigger> =
        REGISTER.register("block_interaction") { _: ResourceLocation -> HTBlockInteractionTrigger }
}
