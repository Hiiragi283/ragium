@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.advancement.builder

import hiiragi283.lib.util.HTDelegates
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.advancements.criterion.InventoryChangeTrigger
import net.minecraft.advancements.criterion.ItemPredicate
import net.minecraft.advancements.criterion.MinMaxBounds

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
class HTInventoryChangeBuilder {
    @PublishedApi internal val predicates: MutableList<ItemPredicate> = mutableListOf()

    @PublishedApi internal var slots: InventoryChangeTrigger.TriggerInstance.Slots by HTDelegates.onceInitialize(InventoryChangeTrigger.TriggerInstance.Slots::ANY)

    operator fun ItemPredicate.unaryPlus() {
        predicates += this
    }

    inline fun predicate(builderAction: HTItemPredicateBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemPredicateBuilder().apply(builderAction).build()
    }

    operator fun InventoryChangeTrigger.TriggerInstance.Slots.unaryPlus() {
        slots = this
    }

    inline fun slots(builderAction: SlotsBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +SlotsBuilder().apply(builderAction).build()
    }

    class SlotsBuilder {
        var occupied: MinMaxBounds.Ints = MinMaxBounds.Ints.ANY
        var full: MinMaxBounds.Ints = MinMaxBounds.Ints.ANY
        var empty: MinMaxBounds.Ints = MinMaxBounds.Ints.ANY

        fun build(): InventoryChangeTrigger.TriggerInstance.Slots = InventoryChangeTrigger.TriggerInstance.Slots(occupied, full, empty)
    }

    fun build(): InventoryChangeTrigger.TriggerInstance = InventoryChangeTrigger.TriggerInstance(Optional.empty(), slots, predicates)
}
