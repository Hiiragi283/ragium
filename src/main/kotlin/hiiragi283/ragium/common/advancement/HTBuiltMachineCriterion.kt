package hiiragi283.ragium.common.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.machine.HTMachineTypeRegistry
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*
import kotlin.jvm.optionals.getOrNull

object HTBuiltMachineCriterion : AbstractCriterion<HTBuiltMachineCriterion.Condition>() {
    @JvmStatic
    fun create(machineType: HTMachineType, minTier: HTMachineTier): AdvancementCriterion<Condition> =
        create(Condition(null, machineType, minTier))

    override fun getConditionsCodec(): Codec<Condition> = Condition.CODEC

    fun trigger(player: PlayerEntity?, machineType: HTMachineType, tier: HTMachineTier) {
        (player as? ServerPlayerEntity)?.let {
            trigger(it) { condition: Condition ->
                condition.matches(machineType, tier)
            }
        }
    }

    //    Condition    //

    class Condition(
        private val predicate: LootContextPredicate?,
        private val machineType: HTMachineType,
        private val minTier: HTMachineTier,
    ) : Conditions {
        companion object {
            @JvmField
            val CODEC: Codec<Condition> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC
                            .optionalFieldOf("player")
                            .forGetter(Condition::player),
                        HTMachineTypeRegistry.CODEC
                            .fieldOf("machine_type")
                            .forGetter(Condition::machineType),
                        HTMachineTier.CODEC
                            .optionalFieldOf("min_tier", HTMachineTier.PRIMITIVE)
                            .forGetter(Condition::minTier),
                    ).apply(instance, ::Condition)
            }
        }

        constructor(
            predicate: Optional<LootContextPredicate>,
            machineType: HTMachineType,
            minTier: HTMachineTier,
        ) : this(predicate.getOrNull(), machineType, minTier)

        override fun player(): Optional<LootContextPredicate> = Optional.ofNullable(predicate)

        fun matches(machineType: HTMachineType, tier: HTMachineTier): Boolean = this.machineType == machineType && tier >= minTier
    }
}
