@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.advancement.criterion

import hiiragi283.lib.data.HolderAcceptor
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import net.minecraft.advancements.criterion.BlockPredicate
import net.minecraft.advancements.criterion.DataComponentMatchers
import net.minecraft.advancements.criterion.NbtPredicate
import net.minecraft.advancements.criterion.StatePropertiesPredicate
import net.minecraft.core.HolderSet
import net.minecraft.world.level.block.Block
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@HTBuilderMarker
class HTBlockPredicateBuilder {
    @PublishedApi internal var blocks: Optional<HolderSet<Block>> by HTDelegates.optionalInitialize()

    @PublishedApi internal var properties: Optional<StatePropertiesPredicate> by HTDelegates.optionalInitialize()

    @PublishedApi internal var nbt: Optional<NbtPredicate> by HTDelegates.optionalInitialize()

    @PublishedApi internal var components: DataComponentMatchers by HTDelegates.onceInitialize {
        DataComponentMatchers.ANY
    }

    // blocks
    operator fun HolderSet<Block>.unaryPlus() {
        blocks = Optional.of(this)
    }

    inline fun blocks(builderAction: HolderAcceptor.BlockSetBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HolderAcceptor.buildBlockSet(builderAction)
    }

    // properties
    inline fun properties(builderAction: StatePropertiesPredicate.Builder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        properties = StatePropertiesPredicate.Builder.properties().apply(builderAction).build()
    }

    // nbt
    operator fun NbtPredicate.unaryPlus() {
        nbt = Optional.of(this)
    }

    // components
    operator fun DataComponentMatchers.unaryPlus() {
        components = this
    }

    inline fun components(builderAction: DataComponentMatchers.Builder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +DataComponentMatchers.Builder.components().apply(builderAction).build()
    }

    fun build(): BlockPredicate = BlockPredicate(blocks, properties, nbt, components)
}
