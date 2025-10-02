package hiiragi283.ragium.impl.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTFluidIngredientImpl private constructor(either: Either<HolderSet<Fluid>, FluidIngredient>, amount: Int) :
    HTIngredientBase<Fluid, FluidStack, FluidIngredient>(either, amount),
    HTFluidIngredient {
        companion object {
            @JvmField
            val INGREDIENT_CODEC: BiCodec<RegistryFriendlyByteBuf, FluidIngredient> = BiCodecs
                .registryBased(
                    NeoForgeRegistries.FLUID_INGREDIENT_TYPES,
                ).dispatch(FluidIngredient::getType, FluidIngredientType<*>::codec, FluidIngredientType<*>::streamCodec)

            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = BiCodec
                .composite(
                    eitherCodec(Registries.FLUID, INGREDIENT_CODEC).fieldOf("fluids"),
                    HTFluidIngredientImpl::either,
                    BiCodecs.POSITIVE_INT.fieldOf("amount"),
                    HTFluidIngredientImpl::amount,
                    ::HTFluidIngredientImpl,
                ).let(BiCodec.Companion::downCast)

            @JvmStatic
            fun of(vararg fluids: Fluid, amount: Int = 1): HTFluidIngredientImpl {
                check(fluids.isNotEmpty())
                check(amount >= 1)
                return of(HolderSet.direct(BuiltInRegistries.FLUID::wrapAsHolder, *fluids), amount)
            }

            @JvmStatic
            fun of(holderSet: HolderSet<Fluid>, amount: Int = 1): HTFluidIngredientImpl {
                check(amount >= 1)
                return HTFluidIngredientImpl(Either.left(holderSet), amount)
            }

            @JvmStatic
            fun of(ingredient: FluidIngredient, amount: Int = 1): HTFluidIngredientImpl {
                check(amount >= 1)
                return HTFluidIngredientImpl(Either.right(ingredient), amount)
            }
        }

        override fun unwrap(): Either<Pair<TagKey<Fluid>, Int>, List<FluidStack>> = either.map(
            { holderSet: HolderSet<Fluid> ->
                holderSet.unwrap().map(
                    { Either.left(it to amount) },
                    { holders: List<Holder<Fluid>> ->
                        Either.right(holders.map { holder: Holder<Fluid> -> FluidStack(holder, amount) })
                    },
                )
            },
            { ingredient: FluidIngredient ->
                Either.right(
                    ingredient.stacks.toList().onEach { stack: FluidStack ->
                        stack.amount = this.amount
                    },
                )
            },
        )

        override fun test(stack: FluidStack): Boolean = testOnlyType(stack) && stack.amount >= this.amount

        override fun testOnlyType(stack: FluidStack): Boolean =
            either.map(stack::`is`) { ingredient: FluidIngredient -> ingredient.test(stack) }

        override fun getMatchingStack(stack: FluidStack): FluidStack =
            if (test(stack)) stack.copyWithAmount(this.amount) else FluidStack.EMPTY

        override fun getRequiredAmount(stack: FluidStack): Int = if (test(stack)) this.amount else 0

        override fun hasNoMatchingStacks(): Boolean = either.map(
            { holderSet: HolderSet<Fluid> -> holderSet.toList().isEmpty() },
            { ingredient: FluidIngredient -> ingredient.hasNoFluids() },
        )

        override fun getMatchingStacks(): List<FluidStack> = either
            .map(
                { holderSet: HolderSet<Fluid> -> holderSet.stream().map { holder: Holder<Fluid> -> FluidStack(holder, amount) }.toList() },
                { ingredient: FluidIngredient -> ingredient.stacks.onEach { stack: FluidStack -> stack.amount = this.amount }.toList() },
            )
    }
