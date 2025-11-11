package hiiragi283.ragium.common.advancements

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.critereon.ContextAwarePredicate
import net.minecraft.advancements.critereon.DamageSourcePredicate
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import java.util.Optional

class HTInvulnerableTrigger : SimpleCriterionTrigger<HTInvulnerableTrigger.TriggerInstance>() {
    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer, source: DamageSource) {
        trigger(player) { instance: TriggerInstance -> instance.matches(player, source) }
    }
    
    @JvmRecord
    data class TriggerInstance(val playerIn: Optional<ContextAwarePredicate>, val damage: Optional<DamageSourcePredicate>) : SimpleInstance {
        companion object {
            @JvmField
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::playerIn),
                        DamageSourcePredicate.CODEC.optionalFieldOf("damage").forGetter(TriggerInstance::damage),
                    ).apply(instance, ::TriggerInstance)
            }
        }

        override fun player(): Optional<ContextAwarePredicate> = playerIn

        fun matches(player: ServerPlayer, source: DamageSource): Boolean =
            this.damage.map { predicate: DamageSourcePredicate -> predicate.matches(player, source) }.orElse(false)
    }
}
