@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.registry.HTDeferredHolder
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.createKey
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * [HTFluidResult]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
class HTFluidResultBuilder @PublishedApi internal constructor() {
    companion object {
        @JvmStatic
        inline fun build(builderAction: HTFluidResultBuilder.() -> Unit): HTFluidResult {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTFluidResultBuilder().apply(builderAction).build()
        }
    }

    @PublishedApi internal var entry: HTFluidResult.Entry by HTDelegates.onceInitialize()
    var amount: Int by HTDelegates.onceInitialize { FluidType.BUCKET_VOLUME }

    operator fun HTFluidResult.Entry.unaryPlus() {
        entry = this
    }

    // Simple
    operator fun Identifier.unaryPlus() {
        +Registries.FLUID.createKey(this)
    }

    operator fun ResourceKey<Fluid>.unaryPlus() {
        +HTDeferredHolder(this).delegate
    }

    @JvmName("unaryPlusFluid")
    operator fun Holder<Fluid>.unaryPlus() {
        +HTFluidResult.SimpleEntry(this)
    }

    operator fun Fluid.unaryPlus() {
        +FluidStackTemplate(this, FluidType.BUCKET_VOLUME)
    }

    operator fun FluidStackTemplate.unaryPlus() {
        +HTFluidResult.SimpleEntry(this)
    }

    operator fun FluidStack.unaryPlus() {
        +HTFluidResult.SimpleEntry(this)
    }

    operator fun HTFluidContent.unaryPlus() {
        +this.sourceHolder
    }

    fun water() {
        +Fluids.WATER
    }

    fun lava() {
        +Fluids.LAVA
    }

    fun milk() {
        +NeoForgeMod.MILK
    }

    // Potion
    @JvmName("unaryPlusPotion")
    operator fun Holder<Potion>.unaryPlus() {
        +BottledPotionContents(this)
    }

    operator fun PotionContents.unaryPlus() {
        +BottledPotionContents(this)
    }

    operator fun BottledPotionContents.unaryPlus() {
        +HTFluidResult.PotionEntry(this)
    }

    fun build(): HTFluidResult = HTFluidResult(entry, amount)
}
