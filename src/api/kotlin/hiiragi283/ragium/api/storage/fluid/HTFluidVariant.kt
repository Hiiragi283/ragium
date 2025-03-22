package hiiragi283.ragium.api.storage.fluid

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.HTRegistryCodecs
import hiiragi283.ragium.api.extension.asFluidHolder
import hiiragi283.ragium.api.extension.asReference
import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.api.storage.HTVariant
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack

@ConsistentCopyVisibility
data class HTFluidVariant private constructor(override val holder: Holder.Reference<Fluid>, override val components: DataComponentPatch) :
    HTVariant<Fluid> {
        @Suppress("DEPRECATION")
        companion object {
            @JvmField
            val CODEC: Codec<HTFluidVariant> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        HTRegistryCodecs.FLUID_HOLDER.fieldOf("id").forGetter(HTFluidVariant::holder),
                        DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(HTFluidVariant::components),
                    ).apply(instance, ::of)
            }

            @JvmStatic
            private val simpleCache: MutableMap<Holder.Reference<Fluid>, HTFluidVariant> = mutableMapOf()

            @JvmField
            val EMPTY: HTFluidVariant = of(Fluids.EMPTY)

            @JvmStatic
            fun of(stack: FluidStack): HTFluidVariant = of(stack.fluid, stack.componentsPatch)

            @JvmStatic
            fun of(fluid: Fluid, components: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidVariant =
                of(fluid.asFluidHolder(), components)

            @JvmStatic
            fun of(holder: Holder<Fluid>, components: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidVariant {
                val reference: Holder.Reference<Fluid> =
                    holder as? Holder.Reference<Fluid> ?: holder.asReference(BuiltInRegistries.FLUID)
                return when {
                    components.isEmpty -> simpleCache.computeIfAbsent(reference) { holder: Holder.Reference<Fluid> ->
                        HTFluidVariant(holder, DataComponentPatch.EMPTY)
                    }

                    else -> HTFluidVariant(reference, components)
                }
            }
        }

        override val isEmpty: Boolean
            get() = holder.isOf(Fluids.EMPTY)

        fun isIn(tagKey: TagKey<Fluid>): Boolean = holder.`is`(tagKey)

        fun toStack(count: Int): FluidStack = FluidStack(holder, count, components)
    }
