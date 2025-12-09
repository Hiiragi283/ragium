package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.HTIngredientCodec
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient

/**
 * [ImmutableFluidStack]向けの[HTIngredient]の実装クラス
 */
data class HTFluidIngredient(private val ingredient: FluidIngredient, private val amount: Int) : HTIngredient<Fluid, ImmutableFluidStack> {
    fun interface AmountGetter {
        fun getRequiredAmount(stack: ImmutableFluidStack): Int
    }

    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = BiCodec.composite(
            MapBiCodec.of(HTIngredientCodec.FLUID, FluidIngredient.STREAM_CODEC).forGetter(HTFluidIngredient::ingredient),
            BiCodecs.POSITIVE_INT.fieldOf(RagiumConst.AMOUNT).forGetter(HTFluidIngredient::amount),
            ::HTFluidIngredient,
        )
    }

    fun test(stack: FluidStack): Boolean = stack.toImmutable()?.let(this::test) ?: false

    fun testOnlyType(stack: FluidStack): Boolean = stack.toImmutable()?.let(this::testOnlyType) ?: false

    fun copyWithAmount(amount: Int): HTFluidIngredient = HTFluidIngredient(ingredient, amount)

    override fun test(stack: ImmutableFluidStack): Boolean = testOnlyType(stack) && stack.amount() >= this.amount

    override fun testOnlyType(stack: ImmutableFluidStack): Boolean = ingredient.test(stack.unwrap())

    override fun getRequiredAmount(stack: ImmutableFluidStack): Int = if (testOnlyType(stack)) this.amount else 0

    override fun hasNoMatchingStacks(): Boolean = ingredient.hasNoFluids()

    override fun unwrap(): Either<Pair<TagKey<Fluid>, Int>, List<ImmutableFluidStack>> = when (ingredient) {
        is TagFluidIngredient -> Either.left(ingredient.tag() to amount)
        else -> Either.right(ingredient.stacks.map { it.copyWithAmount(amount) }.mapNotNull(FluidStack::toImmutable))
    }
}
