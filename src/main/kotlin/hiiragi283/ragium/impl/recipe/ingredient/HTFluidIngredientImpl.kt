package hiiragi283.ragium.impl.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.serialization.codec.downCast
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

internal class HTFluidIngredientImpl(holderSet: HolderSet<Fluid>, amount: Int) :
    HTIngredientBase<Fluid, FluidStack>(holderSet, amount),
    HTFluidIngredient {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = BiCodec
            .composite(
                VanillaBiCodecs.holderSet(Registries.FLUID).fieldOf("fluids"),
                HTFluidIngredientImpl::holderSet,
                BiCodecs.POSITIVE_INT.fieldOf("amount"),
                HTFluidIngredientImpl::amount,
                ::HTFluidIngredientImpl,
            ).downCast()

            /*fun of(ingredient: FluidIngredient, amount: Int = 1): HTFluidIngredientImpl {
                check(amount >= 1)
                return HTFluidIngredientImpl(Either.right(ingredient), amount)
            }*/
    }

    override fun unwrap(): Either<Pair<TagKey<Fluid>, Int>, List<FluidStack>> = holderSet.unwrap().map(
        { Either.left(it to amount) },
        { holders: List<Holder<Fluid>> ->
            Either.right(holders.map { holder: Holder<Fluid> -> FluidStack(holder, amount) })
        },
    )

    override fun test(stack: FluidStack): Boolean = testOnlyType(stack) && stack.amount >= this.amount

    override fun testOnlyType(stack: FluidStack): Boolean = stack.`is`(holderSet)
}
