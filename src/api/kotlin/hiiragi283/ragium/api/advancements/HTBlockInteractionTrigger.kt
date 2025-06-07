package hiiragi283.ragium.api.advancements

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.blockHolderSet
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.ContextAwarePredicate
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.SimpleCriterionTrigger
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.*

object HTBlockInteractionTrigger : SimpleCriterionTrigger<HTBlockInteractionTrigger.TriggerInstance>() {
    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer, state: BlockState) {
        trigger(player) { instance: TriggerInstance -> instance.matches(state) }
    }

    //    TriggerInstance    //

    data class TriggerInstance(private val player1: Optional<ContextAwarePredicate>, val blocks: HolderSet<Block>) :
        SimpleInstance {
        companion object {
            @JvmField
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        RegistryCodecs
                            .homogeneousList(Registries.BLOCK)
                            .fieldOf("blocks")
                            .forGetter(TriggerInstance::blocks),
                    ).apply(instance, ::TriggerInstance)
            }

            @Suppress("DEPRECATION")
            @JvmStatic
            fun interactBlock(block: Block): Criterion<TriggerInstance> = interactBlock(blockHolderSet(block))

            @JvmStatic
            fun interactBlock(tagKey: TagKey<Block>): Criterion<TriggerInstance> =
                interactBlock(BuiltInRegistries.BLOCK.getOrCreateTag(tagKey))

            @JvmStatic
            fun interactBlock(blocks: HolderSet<Block>): Criterion<TriggerInstance> =
                createCriterion(TriggerInstance(Optional.empty(), blocks))
        }

        override fun player(): Optional<ContextAwarePredicate> = player1

        fun matches(state: BlockState): Boolean = state.`is`(blocks)
    }
}
