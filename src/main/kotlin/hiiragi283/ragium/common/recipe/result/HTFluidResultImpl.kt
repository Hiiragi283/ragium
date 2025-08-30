package hiiragi283.ragium.common.recipe.result

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.tag.HTKeyOrTagEntry
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

class HTFluidResultImpl(entry: HTKeyOrTagEntry<Fluid>, amount: Int, components: DataComponentPatch) :
    HTRecipeResultBase<Fluid, FluidStack>(entry, amount, components),
    HTFluidResult {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidResult> = createCodec(
            Registries.FLUID,
            BiCodec.INT.fieldOf("amount"),
            ::HTFluidResultImpl,
        ).let(BiCodec.Companion::downCast)
    }

    override fun createStack(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch): DataResult<FluidStack> {
        val stack = FluidStack(holder, amount, components)
        return when {
            stack.isEmpty -> DataResult.error { "Empty Fluid Stack is not valid for recipe result!" }
            else -> DataResult.success(stack)
        }
    }
}
