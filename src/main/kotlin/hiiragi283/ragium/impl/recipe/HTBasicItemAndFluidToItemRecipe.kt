package hiiragi283.ragium.impl.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.base.HTItemAndFluidToItemRecipe
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

open class HTBasicItemAndFluidToItemRecipe(
    val itemIngredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    val result: HTItemResult,
    override val progressData: HTProgressData,
) : HTItemAndFluidToItemRecipe,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmStatic
        fun <T : HTBasicItemAndFluidToItemRecipe> codec(
            factory: (HTItemIngredient, HTFluidIngredient, HTItemResult, HTProgressData) -> T,
        ): MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HTBasicItemAndFluidToItemRecipe::itemIngredient),
                    HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HTBasicItemAndFluidToItemRecipe::fluidIngredient),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTBasicItemAndFluidToItemRecipe::result),
                    HTProgressData.CODEC.forGetter(HTBasicItemAndFluidToItemRecipe::progressData),
                ).apply(instance, factory)
        }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTBasicItemAndFluidToItemRecipe> = codec(::HTBasicItemAndFluidToItemRecipe)
    }

    override fun test(first: ItemStack, second: FluidStack): Boolean = itemIngredient.test(first) && fluidIngredient.test(second)

    override fun getRequiredAmount(first: ItemStack, second: FluidStack): Pair<Int, Int> = itemIngredient.getRequiredAmount(first) to fluidIngredient.getRequiredAmount(second)

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): ItemStack = result.getOrEmpty()
}
