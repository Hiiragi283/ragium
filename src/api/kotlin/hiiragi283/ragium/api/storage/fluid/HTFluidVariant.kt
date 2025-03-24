package hiiragi283.ragium.api.storage.fluid

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.asFluidHolder
import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.api.storage.HTVariant
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

@ConsistentCopyVisibility
data class HTFluidVariant private constructor(val fluid: Fluid, override val components: DataComponentPatch = DataComponentPatch.EMPTY) :
    HTVariant<Fluid> {
        @Suppress("DEPRECATION")
        companion object {
            @JvmStatic
            private val RAW_CODEC: Codec<HTFluidVariant> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        FluidStack.FLUID_NON_EMPTY_CODEC.fieldOf("id").forGetter(HTFluidVariant::holder),
                        DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(HTFluidVariant::components),
                    ).apply(instance, ::of)
            }

            @JvmField
            val CODEC: Codec<HTFluidVariant> = ExtraCodecs.optionalEmptyMap(RAW_CODEC).xmap(
                { optional: Optional<HTFluidVariant> -> optional.orElse(EMPTY) },
                { variant: HTFluidVariant -> if (variant.isEmpty) Optional.empty() else Optional.of(variant) },
            )

            @JvmStatic
            private val simpleCache: MutableMap<Fluid, HTFluidVariant> = mutableMapOf()

            @JvmField
            val EMPTY: HTFluidVariant = of(Fluids.EMPTY)

            @JvmStatic
            fun of(stack: FluidStack): HTFluidVariant = of(stack.fluid, stack.componentsPatch)

            @JvmStatic
            fun of(holder: Holder<Fluid>, components: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidVariant =
                of(holder.value(), components)

            @JvmStatic
            fun of(fluid: Fluid, components: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidVariant {
                if (components.isEmpty) {
                    return simpleCache.computeIfAbsent(fluid, ::HTFluidVariant)
                }
                return HTFluidVariant(fluid, components)
            }
        }

        override val holder: Holder.Reference<Fluid>
            get() = fluid.asFluidHolder()

        override val isEmpty: Boolean
            get() = holder.isOf(Fluids.EMPTY)

        fun isIn(tagKey: TagKey<Fluid>): Boolean = holder.`is`(tagKey)

        fun toStack(count: Int): FluidStack = FluidStack(holder, count, components)
    }
