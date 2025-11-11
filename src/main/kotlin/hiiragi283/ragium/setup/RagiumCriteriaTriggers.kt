package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.common.advancements.HTInvulnerableTrigger
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.advancements.critereon.DamageSourcePredicate
import net.minecraft.core.registries.Registries
import java.util.Optional

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
    val INVULNERABLE_TO: HTInvulnerableTrigger = register("invulnerable_to", HTInvulnerableTrigger())

    //    Extensions    //
    
    @JvmStatic
    fun invulnerableTo(): Criterion<HTInvulnerableTrigger.TriggerInstance> =
        INVULNERABLE_TO.createCriterion(HTInvulnerableTrigger.TriggerInstance(Optional.empty(), Optional.empty()))

    @JvmStatic
    fun invulnerableTo(damage: DamageSourcePredicate.Builder): Criterion<HTInvulnerableTrigger.TriggerInstance> =
        INVULNERABLE_TO.createCriterion(HTInvulnerableTrigger.TriggerInstance(Optional.empty(), Optional.of(damage.build())))
}
