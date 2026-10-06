@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.loot.builder

import hiiragi283.lib.data.ConditionBuilder
import hiiragi283.lib.util.HTBuilderMarker
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.neoforged.neoforge.common.conditions.ICondition
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 * @see LootTable.Builder
 */
@HTBuilderMarker
class HTLootTableBuilder {
    companion object {
        @JvmStatic
        inline fun build(builderAction: HTLootTableBuilder.() -> Unit): LootTable.Builder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTLootTableBuilder().apply(builderAction).build()
        }
    }

    // Pools
    @PublishedApi internal val pools: MutableList<LootPool.Builder> = ObjectArrayList()

    operator fun LootPool.Builder.unaryPlus() {
        pools += this
    }

    inline fun pool(builderAction: HTLootPoolBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTLootPoolBuilder.build(builderAction)
    }

    // Functions
    @PublishedApi internal val functions: MutableList<LootItemFunction.Builder> = ObjectArrayList()

    operator fun LootItemFunction.Builder.unaryPlus() {
        functions += this
    }

    // Conditions
    @PublishedApi
    internal val conditions: MutableList<ICondition> = ObjectArrayList()

    inline fun condition(builderAction: ConditionBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ConditionBuilder(conditions).apply(builderAction)
    }

    fun build(): LootTable.Builder {
        val builder: LootTable.Builder = LootTable.lootTable()
        pools.forEach(builder::withPool)
        functions.forEach(builder::apply)
        builder.withConditions(conditions)
        return builder
    }
}
