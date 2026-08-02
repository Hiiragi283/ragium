package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.FluidIngredientBuilder
import hiiragi283.core.api.recipe.HTRecipeResultHelper
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HTWashingRecipe(val ingredient: HTItemIngredient, val results: List<HTChancedItemResult>, override val progressData: HTProgressData) :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.SingleItemTo<Iterable<ItemStack>>,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput>,
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTWashingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTWashingRecipe::ingredient),
                    HTChancedItemResult.CODEC.listOrElement(1..4).fieldOf(HTConst.RESULTS).forGetter(HTWashingRecipe::results),
                    HTProgressData.CODEC.forGetter(HTWashingRecipe::progressData),
                ).apply(instance, ::HTWashingRecipe)
        }

        @JvmField
        val WATER_INGREDIENT: HTFluidIngredient = FluidIngredientBuilder().apply {
            +VanillaFluidContents.WATER
            amount = 250
        }.buildSized()
    }

    override fun test(first: ItemStack, second: FluidStack): Boolean = ingredient.test(first) && WATER_INGREDIENT.test(second)

    override fun getMatchingStacks(first: ItemStack, second: FluidStack): Pair<ItemStack, FluidStack> = ingredient.getMatchingStack(first) to WATER_INGREDIENT.getMatchingStack(second)

    override fun assemble(input: ItemStack): Iterable<ItemStack> = results.map(HTChancedItemResult::createOrEmpty).let(HTRecipeResultHelper::mergeStacks)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.WASHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.WASHING

    override fun isIncomplete(): Boolean = ingredient.isIncomplete() || results.any(HTChancedItemResult::isIncomplete)
}
