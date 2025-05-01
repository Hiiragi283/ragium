package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Supplier

@ConsistentCopyVisibility
data class HTFluidOutput private constructor(private val holder: Holder<Fluid>, val amount: Int, val components: DataComponentPatch) :
    Supplier<FluidStack> {
        companion object {
            @JvmField
            val CODEC: Codec<HTFluidOutput> =
                FluidStack.CODEC.comapFlatMap(HTFluidOutput::fromStack, HTFluidOutput::get)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidOutput> =
                FluidStack.STREAM_CODEC.map(HTFluidOutput::of, HTFluidOutput::get)

            @JvmStatic
            fun of(fluid: Supplier<out Fluid>, amount: Int): HTFluidOutput = of(fluid.get(), amount)

            @JvmStatic
            fun of(fluid: Fluid, amount: Int): HTFluidOutput = of(FluidStack(fluid, amount))

            @JvmStatic
            fun of(stack: FluidStack): HTFluidOutput = fromStack(stack).orThrow

            @JvmStatic
            fun fromStack(stack: FluidStack): DataResult<HTFluidOutput> {
                if (stack.isEmpty) {
                    return DataResult.error { "Empty ItemStack is not allowed for HTFluidOutput!" }
                }
                return DataResult.success(HTFluidOutput(stack.fluidHolder, stack.amount, stack.componentsPatch))
            }
        }

        val id: ResourceLocation = holder.idOrThrow

        override fun get(): FluidStack = FluidStack(holder, amount, components)
    }
