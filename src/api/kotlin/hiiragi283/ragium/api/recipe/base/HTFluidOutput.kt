package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Supplier

class HTFluidOutput private constructor(private val holder: Holder<Fluid>, val amount: Int, val components: DataComponentPatch) :
    Supplier<FluidStack> {
        companion object {
            @JvmField
            val CODEC: Codec<HTFluidOutput> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        FluidStack.FLUID_NON_EMPTY_CODEC
                            .fieldOf("fluid")
                            .forGetter(HTFluidOutput::holder),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(HTFluidOutput::amount),
                        DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(HTFluidOutput::components),
                    ).apply(instance, ::HTFluidOutput)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidOutput> = StreamCodec.composite(
                ByteBufCodecs.holderRegistry(Registries.FLUID),
                HTFluidOutput::holder,
                ByteBufCodecs.VAR_INT,
                HTFluidOutput::amount,
                DataComponentPatch.STREAM_CODEC,
                HTFluidOutput::components,
                ::HTFluidOutput,
            )
        }

        constructor(fluid: Fluid, amount: Int, components: DataComponentPatch) : this(
            FluidStack(fluid, 1).fluidHolder,
            amount,
            components,
        )

        val id: ResourceLocation = holder.idOrThrow

        override fun get(): FluidStack = FluidStack(holder, amount, components)
    }
