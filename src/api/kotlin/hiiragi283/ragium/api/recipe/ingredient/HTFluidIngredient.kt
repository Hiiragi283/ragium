package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaMapBiCodecs
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import kotlin.to

/**
 * [ImmutableFluidStack]向けの[HTIngredient]の拡張インターフェース
 */
sealed class HTFluidIngredient(protected val amount: Int) : HTIngredient<Fluid, ImmutableFluidStack> {
    fun interface AmountGetter {
        fun getRequiredAmount(stack: ImmutableFluidStack): Int
    }

    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = BiCodecs
            .xor(HolderBased.CODEC, IngredientBased.CODEC)
            .xmap(Either<HolderBased, IngredientBased>::unwrap) { ingredient: HTFluidIngredient ->
                when (ingredient) {
                    is HolderBased -> Either.left(ingredient)
                    is IngredientBased -> Either.right(ingredient)
                }
            }

        @JvmStatic
        fun of(holderSet: HolderSet<Fluid>, amount: Int): HTFluidIngredient = HolderBased(holderSet, amount)

        @JvmStatic
        fun of(ingredient: FluidIngredient, amount: Int): HTFluidIngredient = IngredientBased(ingredient, amount)
    }

    fun test(stack: FluidStack): Boolean = stack.toImmutable()?.let(this::test) ?: false

    fun testOnlyType(stack: FluidStack): Boolean = stack.toImmutable()?.let(this::testOnlyType) ?: false

    override fun test(stack: ImmutableFluidStack): Boolean = testOnlyType(stack) && stack.amount() >= this.amount

    final override fun getRequiredAmount(stack: ImmutableFluidStack): Int = if (test(stack)) this.amount else 0

    abstract fun copyWithAmount(amount: Int): HTFluidIngredient

    //    HolderBased    //

    private class HolderBased(private val holderSet: HolderSet<Fluid>, amount: Int) : HTFluidIngredient(amount) {
        companion object {
            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, HolderBased> = BiCodec.composite(
                VanillaBiCodecs.holderSet(Registries.FLUID).fieldOf("fluids"),
                HolderBased::holderSet,
                BiCodecs.POSITIVE_INT.fieldOf("amount"),
                HolderBased::amount,
                ::HolderBased,
            )
        }

        override fun testOnlyType(stack: ImmutableFluidStack): Boolean = stack.isOf(holderSet)

        override fun hasNoMatchingStacks(): Boolean = holderSet.none()

        override fun unwrap(): Either<Pair<TagKey<Fluid>, Int>, List<ImmutableFluidStack>> = holderSet.unwrap().map(
            { Either.left(it to amount) },
            { holders: List<Holder<Fluid>> ->
                Either.right(holders.map { holder: Holder<Fluid> -> ImmutableFluidStack.of(holder.value(), amount) })
            },
        )

        override fun copyWithAmount(amount: Int): HTFluidIngredient = HolderBased(holderSet, amount)
    }

    //    IngredientBased    //

    private class IngredientBased(private val ingredient: FluidIngredient, amount: Int) : HTFluidIngredient(amount) {
        companion object {
            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, IngredientBased> = BiCodec.composite(
                VanillaMapBiCodecs.FLUID_INGREDIENT,
                IngredientBased::ingredient,
                BiCodecs.POSITIVE_INT.fieldOf("amount"),
                IngredientBased::amount,
                ::IngredientBased,
            )
        }

        override fun testOnlyType(stack: ImmutableFluidStack): Boolean = ingredient.test(stack.unwrap())

        override fun hasNoMatchingStacks(): Boolean = ingredient.hasNoFluids()

        override fun unwrap(): Either<Pair<TagKey<Fluid>, Int>, List<ImmutableFluidStack>> =
            Either.right(ingredient.stacks.mapNotNull(FluidStack::toImmutable))

        override fun copyWithAmount(amount: Int): HTFluidIngredient = IngredientBased(ingredient, amount)
    }
}
