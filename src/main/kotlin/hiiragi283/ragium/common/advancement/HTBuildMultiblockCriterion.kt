package hiiragi283.ragium.common.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*
import kotlin.jvm.optionals.getOrNull

object HTBuildMultiblockCriterion : AbstractCriterion<HTBuildMultiblockCriterion.Condition>() {
    @JvmStatic
    fun create(machineType: HTMachineType.Multi): AdvancementCriterion<Condition> = create(Condition(null, machineType))

    override fun getConditionsCodec(): Codec<Condition> = Condition.CODEC

    fun trigger(player: PlayerEntity?, machineType: HTMachineType.Multi) {
        (player as? ServerPlayerEntity)?.let { trigger(it) { condition: Condition -> condition.matches(machineType) } }
    }

    //    Condition    //

    class Condition(private val predicate: LootContextPredicate?, private val machineType: HTMachineType.Multi) : Conditions {
        companion object {
            @JvmField
            val CODEC: Codec<Condition> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Condition::player),
                        HTMachineType.Multi.CODEC
                            .fieldOf("machine_type")
                            .forGetter(Condition::machineType),
                    ).apply(instance, ::Condition)
            }
        }

        constructor(
            predicate: Optional<LootContextPredicate>,
            machineType: HTMachineType.Multi,
        ) : this(predicate.getOrNull(), machineType)

        override fun player(): Optional<LootContextPredicate> = Optional.ofNullable(predicate)

        fun matches(machineType: HTMachineType.Multi) = this.machineType == machineType
    }
}
