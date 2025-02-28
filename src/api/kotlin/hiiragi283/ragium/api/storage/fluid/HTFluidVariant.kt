package hiiragi283.ragium.api.storage.fluid

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.storage.HTVariant
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack

@ConsistentCopyVisibility
data class HTFluidVariant private constructor(override val holder: Holder<Fluid>, override val components: DataComponentPatch) :
    HTVariant<Fluid> {
        @Suppress("DEPRECATION")
        companion object {
            @JvmField
            val CODEC: Codec<HTFluidVariant> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        FluidStack.FLUID_NON_EMPTY_CODEC.fieldOf("id").forGetter(HTFluidVariant::holder),
                        DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(HTFluidVariant::components),
                    ).apply(instance, ::HTFluidVariant)
            }

            @JvmField
            val EMPTY: HTFluidVariant = HTFluidVariant(Fluids.EMPTY.builtInRegistryHolder(), DataComponentPatch.EMPTY)

            @JvmStatic
            fun of(fluid: Fluid, components: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidVariant =
                of(FluidStack(fluid.builtInRegistryHolder(), 1, components))

            @JvmStatic
            fun of(stack: FluidStack): HTFluidVariant =
                if (stack.isEmpty) EMPTY else HTFluidVariant(stack.fluidHolder, stack.componentsPatch)
        }

        override val isEmpty: Boolean
            get() = toStack(1).isEmpty

        fun isIn(tagKey: TagKey<Fluid>): Boolean = holder.`is`(tagKey)

        fun toStack(count: Int): FluidStack = FluidStack(holder, count, components)
    }
