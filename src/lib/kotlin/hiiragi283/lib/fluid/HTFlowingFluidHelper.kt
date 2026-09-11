package hiiragi283.lib.fluid

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.core.Holder
import net.minecraft.core.TypedInstance
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
data object HTFlowingFluidHelper {
    @JvmField
    val SOURCE_FLUID_HOLDER_CODEC: Codec<Holder<Fluid>> = FluidInstance.FLUID_HOLDER_CODEC.validate { holder ->
        when {
            isSource(holder) -> DataResult.success(holder)
            else -> DataResult.error { "Fluid must be source" }
        }
    }

    @JvmStatic
    fun isSource(fluid: Fluid): Boolean = when {
        fluid !is FlowingFluid -> true
        else -> fluid.source == fluid
    }

    @JvmStatic
    fun isSource(fluid: Holder<Fluid>): Boolean = isSource(fluid.value())

    @JvmStatic
    fun isSource(instance: TypedInstance<Fluid>): Boolean = isSource(instance.typeHolder())
}
