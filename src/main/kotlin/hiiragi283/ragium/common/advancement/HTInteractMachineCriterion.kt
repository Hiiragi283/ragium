package hiiragi283.ragium.common.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.asServerPlayer
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.predicate.entity.LootContextPredicate
import java.util.*
import kotlin.jvm.optionals.getOrNull

object HTInteractMachineCriterion : AbstractCriterion<HTInteractMachineCriterion.Condition>() {
    @JvmStatic
    fun create(key: HTMachineKey, minTier: HTMachineTier): AdvancementCriterion<Condition> = create(Condition(null, key, minTier))

    @JvmStatic
    fun trigger(player: PlayerEntity?, key: HTMachineKey, tier: HTMachineTier) {
        player?.asServerPlayer()?.let {
            trigger(it) { condition: Condition ->
                condition.matches(key, tier)
            }
        }
    }

    override fun getConditionsCodec(): Codec<Condition> = Condition.CODEC

    //    Condition    //

    class Condition(private val predicate: LootContextPredicate?, private val key: HTMachineKey, private val minTier: HTMachineTier) :
        Conditions {
        companion object {
            @JvmField
            val CODEC: Codec<Condition> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC
                            .optionalFieldOf("player")
                            .forGetter(Condition::player),
                        HTMachineKey.CODEC
                            .fieldOf("machine_type")
                            .forGetter(Condition::key),
                        HTMachineTier.CODEC
                            .optionalFieldOf("min_tier", HTMachineTier.PRIMITIVE)
                            .forGetter(Condition::minTier),
                    ).apply(instance, ::Condition)
            }
        }

        constructor(
            predicate: Optional<LootContextPredicate>,
            key: HTMachineKey,
            minTier: HTMachineTier,
        ) : this(predicate.getOrNull(), key, minTier)

        override fun player(): Optional<LootContextPredicate> = Optional.ofNullable(predicate)

        fun matches(key: HTMachineKey, tier: HTMachineTier): Boolean = key == this.key && tier >= minTier
    }
}
