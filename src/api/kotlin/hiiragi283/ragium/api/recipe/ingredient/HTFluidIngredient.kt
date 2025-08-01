package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.serialization.Codec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

@ConsistentCopyVisibility
data class HTFluidIngredient private constructor(private val delegate: SizedFluidIngredient) : HTIngredient<FluidStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidIngredient> =
            SizedFluidIngredient.FLAT_CODEC.xmap(::HTFluidIngredient, HTFluidIngredient::delegate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidIngredient> =
            SizedFluidIngredient.STREAM_CODEC.map(::HTFluidIngredient, HTFluidIngredient::delegate)

        @JvmStatic
        fun of(ingredient: SizedFluidIngredient): HTFluidIngredient {
            check(!ingredient.ingredient().isEmpty)
            return HTFluidIngredient(ingredient)
        }
    }

    override fun test(stack: FluidStack): Boolean = delegate.test(stack)

    override fun testOnlyType(stack: FluidStack): Boolean = delegate.ingredient().test(stack)

    override fun getMatchingStack(stack: FluidStack): FluidStack =
        if (test(stack)) stack.copyWithAmount(delegate.amount()) else FluidStack.EMPTY

    override fun getRequiredAmount(stack: FluidStack): Int = if (test(stack)) delegate.amount() else 0

    override fun hasNoMatchingStacks(): Boolean = delegate.ingredient().hasNoFluids()

    override fun getMatchingStacks(): List<FluidStack> = listOf(*delegate.fluids)
}
