@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.loot.builder

import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 * @see LootPool.Builder
 */
@HTBuilderMarker
class HTLootPoolBuilder {
    companion object {
        @JvmStatic
        inline fun build(builderAction: HTLootPoolBuilder.() -> Unit): LootPool.Builder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTLootPoolBuilder().apply(builderAction).build()
        }
    }

    // Rolls
    var rolls: NumberProvider by HTDelegates.onceInitialize { ConstantValue.exactly(1f) }
    var bonusRolls: NumberProvider by HTDelegates.onceInitialize { ConstantValue.exactly(0f) }

    // Entries
    @PublishedApi internal val entries: MutableList<LootPoolEntryContainer.Builder<*>> = ObjectArrayList()

    operator fun LootPoolEntryContainer.Builder<*>.unaryPlus() {
        entries += this
    }

    // Conditions
    @PublishedApi internal val conditions: MutableList<LootItemCondition.Builder> = ObjectArrayList()

    operator fun LootItemCondition.Builder.unaryPlus() {
        conditions += this
    }

    // Functions
    @PublishedApi internal val functions: MutableList<LootItemFunction.Builder> = ObjectArrayList()

    operator fun LootItemFunction.Builder.unaryPlus() {
        functions += this
    }

    fun build(): LootPool.Builder {
        val builder: LootPool.Builder = LootPool.lootPool()
        builder.setRolls(rolls)
        builder.setBonusRolls(bonusRolls)
        entries.forEach(builder::add)
        conditions.forEach(builder::`when`)
        functions.forEach(builder::apply)
        return builder
    }
}
