package hiiragi283.ragium.api.recipe.result

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.data.BiCodec
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

class HTFluidResult(entry: Either<ResourceKey<Fluid>, TagKey<Fluid>>, amount: Int, components: DataComponentPatch) :
    HTRecipeResultBase<Fluid, FluidStack>(entry, amount, components) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidResult> = createCodec(
            Registries.FLUID,
            BiCodec.INT.fieldOf("amount"),
            ::HTFluidResult,
        )
    }

    override val lookup: HolderLookup.RegistryLookup<Fluid>
        get() = BuiltInRegistries.FLUID.asLookup()

    override fun createStack(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch): DataResult<FluidStack> {
        val stack = FluidStack(holder, amount, components)
        return when {
            stack.isEmpty -> DataResult.error { "Empty Fluid Stack is not valid for recipe result!" }
            else -> DataResult.success(stack)
        }
    }

    override val emptyStack: FluidStack get() = FluidStack.EMPTY
}
