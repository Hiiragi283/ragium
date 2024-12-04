package hiiragi283.ragium.common.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.contains
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.entity.LivingEntity
import net.minecraft.fluid.Fluid
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*
import kotlin.jvm.optionals.getOrNull

object HTDrankFluidCriterion : AbstractCriterion<HTDrankFluidCriterion.Condition>() {
    @JvmStatic
    fun create(entryList: RegistryEntryList<Fluid>): AdvancementCriterion<Condition> = create(Condition(null, entryList))

    @JvmStatic
    fun trigger(entity: LivingEntity?, fluid: Fluid) {
        (entity as? ServerPlayerEntity)?.let {
            trigger(it) { condition: Condition ->
                condition.matches(fluid)
            }
        }
    }

    override fun getConditionsCodec(): Codec<Condition> = Condition.CODEC

    //    Condition    //

    class Condition(private val predicate: LootContextPredicate?, private val entryList: RegistryEntryList<Fluid>) : Conditions {
        companion object {
            @JvmField
            val CODEC: Codec<Condition> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC
                            .optionalFieldOf("player")
                            .forGetter(Condition::player),
                        RegistryCodecs
                            .entryList(RegistryKeys.FLUID)
                            .fieldOf("fluids")
                            .forGetter(Condition::entryList),
                    ).apply(instance, ::Condition)
            }
        }

        constructor(
            predicate: Optional<LootContextPredicate>,
            entryList: RegistryEntryList<Fluid>,
        ) : this(predicate.getOrNull(), entryList)

        override fun player(): Optional<LootContextPredicate> = Optional.ofNullable(predicate)

        fun matches(fluid: Fluid): Boolean = entryList.contains(fluid)
    }
}
