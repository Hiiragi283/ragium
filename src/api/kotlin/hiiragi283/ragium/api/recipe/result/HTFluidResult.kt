package hiiragi283.ragium.api.recipe.result

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

class HTFluidResult(entry: Either<ResourceLocation, TagKey<Fluid>>, amount: Int, components: DataComponentPatch) :
    HTRecipeResult<Fluid, FluidStack>(entry, amount, components) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidResult> = BiCodec.composite(
            BiCodecs.idOrTag(Registries.FLUID).fieldOf("id"),
            HTFluidResult::entry,
            BiCodec.INT.fieldOf("amount"),
            HTFluidResult::amount,
            BiCodecs.COMPONENT_PATCH.optionalFieldOf("components", DataComponentPatch.EMPTY),
            HTFluidResult::components,
            ::HTFluidResult,
        )
    }

    override val registry: Registry<Fluid>
        get() = BuiltInRegistries.FLUID

    override fun createStack(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch): DataResult<FluidStack> {
        val stack = FluidStack(holder, amount, components)
        return when {
            stack.isEmpty -> DataResult.error { "Empty Fluid Stack is not valid for recipe result!" }
            else -> DataResult.success(stack)
        }
    }

    override val emptyStack: FluidStack get() = FluidStack.EMPTY
}
