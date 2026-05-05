package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.getRequiredAmount
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.HTListFluidResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.ragium.api.recipe.result.HTChemicalResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

class HTRefiningRecipe(
    val ingredient: HTFluidIngredient,
    val catalyst: Optional<Ingredient>,
    val fluidResults: HTListFluidResult,
    val itemResult: Optional<HTItemResult>,
    override val progressData: HTProgressData,
) : HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<HTChemicalResult>,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput>,
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRefiningRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTFluidIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTRefiningRecipe::ingredient),
                    HTCodecs.INGREDIENT.optionalFieldOf(HTConst.CATALYST).forGetter(HTRefiningRecipe::catalyst),
                    HTListFluidResult.codec(2).fieldOf(HTConst.FLUID_RESULT).forGetter(HTRefiningRecipe::fluidResults),
                    HTItemResult.CODEC.optionalFieldOf(HTConst.ITEM_RESULT).forGetter(HTRefiningRecipe::itemResult),
                    HTProgressData.CODEC.forGetter(HTRefiningRecipe::progressData),
                ).apply(instance, ::HTRefiningRecipe)
        }
    }

    override fun test(first: ItemStack, second: FluidStack): Boolean =
        catalyst.map { it.test(first) }.orElseGet(first::isEmpty) && ingredient.test(second)

    override fun getRequiredAmount(first: ItemStack, second: FluidStack): Pair<Int, Int> =
        catalyst.map { it.getRequiredAmount(first) }.orElseGet { 0 } to ingredient.getRequiredAmount(second)

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): HTChemicalResult =
        HTChemicalResult.create(fluidResults, itemResult)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REFINING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.REFINING.get()
}
