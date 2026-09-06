@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.advancement.criterion

import hiiragi283.lib.util.HTDelegates
import net.minecraft.advancements.criterion.ConsumeItemTrigger
import net.minecraft.advancements.criterion.ItemPredicate
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
class HTConsumeItemBuilder {
    companion object {
        @JvmStatic
        inline fun build(builderAction: HTConsumeItemBuilder.() -> Unit): ConsumeItemTrigger.TriggerInstance {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTConsumeItemBuilder().apply(builderAction).build()
        }
    }

    @PublishedApi internal var predicate: Optional<ItemPredicate> by HTDelegates.onceInitialize { Optional.empty() }

    operator fun ItemPredicate.unaryPlus() {
        predicate = Optional.of(this)
    }

    inline fun predicate(builderAction: HTItemPredicateBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemPredicateBuilder().apply(builderAction).build()
    }

    fun build(): ConsumeItemTrigger.TriggerInstance = ConsumeItemTrigger.TriggerInstance(Optional.empty(), predicate)
}
