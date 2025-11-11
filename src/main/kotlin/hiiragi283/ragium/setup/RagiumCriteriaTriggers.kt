package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredRegister
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.advancements.critereon.PlayerTrigger
import net.minecraft.core.registries.Registries
import java.util.Optional

/**
 * @see net.minecraft.advancements.CriteriaTriggers
 */
object RagiumCriteriaTriggers {
    @JvmField
    val REGISTER: HTDeferredRegister<CriterionTrigger<*>> =
        HTDeferredRegister(Registries.TRIGGER_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : CriterionTrigger<*>> register(name: String, trigger: T): T {
        REGISTER.register(name) { _ -> trigger }
        return trigger
    }

    @JvmField
    val BEHEAD_MOB: PlayerTrigger = register("behead_mob", PlayerTrigger())

    //    Extensions    //

    @JvmStatic
    fun beheadMob(): Criterion<PlayerTrigger.TriggerInstance> = BEHEAD_MOB.createCriterion(PlayerTrigger.TriggerInstance(Optional.empty()))
}
