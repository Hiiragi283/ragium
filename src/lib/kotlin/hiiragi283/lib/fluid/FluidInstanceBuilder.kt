@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.fluid

import hiiragi283.lib.data.HolderAcceptor
import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.isEmpty
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.right
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

/**
 * [FluidStackTemplate]や[FluidStack]向けのビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.1
 */
@HTBuilderMarker
class FluidInstanceBuilder : HolderAcceptor.FluidAcceptor {
    companion object {
        /**
         * [FluidStackTemplate]を作成します。
         * @throws IllegalStateException 液体が空の場合，または量が`0`以下の場合
         */
        @JvmStatic
        inline fun buildTemplate(builderAction: FluidInstanceBuilder.() -> Unit): FluidStackTemplate {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return FluidInstanceBuilder().apply(builderAction).run { FluidStackTemplate(fluid, amount, patch) }
        }

        /**
         * [FluidStackTemplate]を作成します。
         */
        @Suppress("DEPRECATION")
        @JvmStatic
        inline fun buildSafeTemplate(builderAction: FluidInstanceBuilder.() -> Unit): HTTextResult<FluidStackTemplate> {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return FluidInstanceBuilder().apply(builderAction).run {
                if (fluid.isEmpty) HTTextResult("Fluid must be non-empty")
                if (amount <= 0) HTTextResult("Amount must be positive")
                FluidStackTemplate(fluid, amount, patch).right()
            }
        }

        /**
         * [FluidStack]を作成します。
         */
        @JvmStatic
        inline fun buildStack(builderAction: FluidInstanceBuilder.() -> Unit): FluidStack {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return FluidInstanceBuilder().apply(builderAction).run { FluidStack(fluid, amount, patch) }
        }
    }

    @PublishedApi internal var fluid: Holder<Fluid> by HTDelegates.onceInitialize()
    var amount: Int = FluidType.BUCKET_VOLUME

    @PublishedApi internal var patch: DataComponentPatch = DataComponentPatch.EMPTY

    override operator fun Holder<Fluid>.unaryPlus() {
        fluid = this
    }

    operator fun HTFluidContent.unaryPlus() {
        +this.sourceHolder
    }

    operator fun DataComponentPatch.unaryPlus() {
        patch = this
    }

    inline fun components(builderAction: DataComponentPatch.Builder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        patch = buildDataPatch(builderAction)
    }
}
