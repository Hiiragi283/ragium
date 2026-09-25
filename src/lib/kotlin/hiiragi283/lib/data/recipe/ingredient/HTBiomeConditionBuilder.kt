@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.ingredient

import hiiragi283.lib.data.HolderAcceptor
import hiiragi283.lib.recipe.ingredient.HTBiomeCondition
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import net.minecraft.core.HolderSet
import net.minecraft.world.level.biome.Biome
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * [HTBiomeCondition]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
@HTBuilderMarker
class HTBiomeConditionBuilder @PublishedApi internal constructor() {
    companion object {
        @JvmStatic
        inline fun build(builderAction: HTBiomeConditionBuilder.() -> Unit): HTBiomeCondition {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTBiomeConditionBuilder().apply(builderAction).build()
        }
    }

    @PublishedApi internal var condition: HTBiomeCondition by HTDelegates.onceInitialize()

    operator fun HTBiomeCondition.unaryPlus() {
        condition = this
    }

    operator fun HolderSet<Biome>.unaryPlus() {
        +HTBiomeCondition(this)
    }

    inline fun biomes(builderAction: HolderAcceptor.SetBuilder<Biome>.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HolderAcceptor.SetBuilder<Biome>().apply(builderAction).build()
    }

    fun build(): HTBiomeCondition = condition
}
