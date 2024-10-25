package hiiragi283.ragium.api.fluid

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.longRangeCodec
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.component.ComponentChanges
import net.minecraft.component.ComponentMap
import net.minecraft.component.ComponentMapImpl
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.dynamic.Codecs
import java.util.Optional

class HTFluidStack {
    companion object {
        @JvmField
        val EMPTY = HTFluidStack(Fluids.EMPTY, 0)

        @JvmField
        val FLUID_CODEC: Codec<RegistryEntry<Fluid>> = Registries.FLUID.entryCodec
            .validate { entry: RegistryEntry<Fluid> ->
                when (entry.matches(Fluids.EMPTY.registryEntry)) {
                    true -> DataResult.success(entry)
                    false -> DataResult.error { "Fluid must not be minecraft:empty!" }
                }
            }

        @JvmField
        val CODEC: Codec<HTFluidStack> = Codec.lazyInitialized {
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        FLUID_CODEC
                            .fieldOf("id")
                            .forGetter { it.fluid.registryEntry },
                        longRangeCodec(1, Long.MAX_VALUE)
                            .optionalFieldOf("amount", FluidConstants.BUCKET)
                            .forGetter(HTFluidStack::amount),
                        ComponentChanges.CODEC
                            .optionalFieldOf("components", ComponentChanges.EMPTY)
                            .forGetter { it.components.changes },
                    ).apply(instance, ::HTFluidStack)
            }
        }

        @JvmField
        val OPTIONAL_CODEC: Codec<HTFluidStack> = Codecs
            .optional(CODEC)
            .xmap(
                { it.orElse(EMPTY) },
                {
                    when (it.isEmpty) {
                        true -> Optional.empty()
                        false -> Optional.of(it)
                    }
                },
            )
    }

    val fluid: Fluid
        get() = if (isEmpty) Fluids.EMPTY else field
    val amount: Long
        get() = if (isEmpty) 0 else field
    private val components: ComponentMapImpl

    constructor(
        entry: RegistryEntry<Fluid>,
        amount: Long = FluidConstants.BUCKET,
        changes: ComponentChanges = ComponentChanges.EMPTY,
    ) : this(entry.value(), amount, changes)

    constructor(
        fluid: Fluid,
        amount: Long = FluidConstants.BUCKET,
        changes: ComponentChanges = ComponentChanges.EMPTY,
    ) : this(fluid, amount, ComponentMapImpl.create(ComponentMap.EMPTY, changes))

    private constructor(fluid: Fluid, amount: Long, components: ComponentMapImpl) {
        this.fluid = fluid
        this.amount = amount
        this.components = components
    }

    val isEmpty: Boolean
        get() = this == EMPTY || fluid == Fluids.EMPTY || amount <= 0

    fun getComponents(): ComponentMap = if (isEmpty) ComponentMap.EMPTY else components

    fun getComponentChanges(): ComponentChanges = if (isEmpty) ComponentChanges.EMPTY else components.changes
}
