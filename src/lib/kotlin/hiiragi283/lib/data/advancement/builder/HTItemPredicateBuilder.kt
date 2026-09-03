@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.advancement.builder

import hiiragi283.lib.data.HolderAcceptor
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import net.minecraft.advancements.criterion.DataComponentMatchers
import net.minecraft.advancements.criterion.ItemPredicate
import net.minecraft.advancements.criterion.MinMaxBounds
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
@HTBuilderMarker
class HTItemPredicateBuilder {
    @PublishedApi internal var items: Optional<HolderSet<Item>> by HTDelegates.onceInitialize { Optional.empty() }

    @PublishedApi internal var count: MinMaxBounds.Ints by HTDelegates.onceInitialize { MinMaxBounds.Ints.ANY }

    @PublishedApi internal var components: DataComponentMatchers by HTDelegates.onceInitialize {
        DataComponentMatchers.ANY
    }

    operator fun HolderSet<Item>.unaryPlus() {
        items = Optional.of(this)
    }

    inline fun items(builderAction: HolderAcceptor.ItemSetBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HolderAcceptor.ItemSetBuilder().apply(builderAction).build()
    }

    operator fun MinMaxBounds.Ints.unaryPlus() {
        count = this
    }

    operator fun DataComponentMatchers.unaryPlus() {
        components = this
    }

    inline fun components(builderAction: DataComponentMatchers.Builder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +DataComponentMatchers.Builder.components().apply(builderAction).build()
    }

    fun build(): ItemPredicate = ItemPredicate(items, count, components)
}
