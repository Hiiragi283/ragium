package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTListItemResult
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTImplodingRecipe(
    val ingredient: HTItemIngredient,
    val explosive: HTItemIngredient,
    val results: HTListItemResult,
    override val progressData: HTProgressData,
) : HTRecipePredicates.DoubleItem,
    HTRecipeFactories.DoubleItem<Iterable<ItemStack>>,
    HTProgressRecipe.Simple<RecipeInput>,
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTImplodingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTImplodingRecipe::ingredient),
                    HTItemIngredient.CODEC.fieldOf("explosive").forGetter(HTImplodingRecipe::explosive),
                    HTListItemResult
                        .codec(2)
                        .fieldOf(HTConst.RESULTS)
                        .forGetter(HTImplodingRecipe::results),
                    HTProgressData.CODEC.forGetter(HTImplodingRecipe::progressData),
                ).apply(instance, ::HTImplodingRecipe)
        }
    }

    override fun test(first: ItemStack, second: ItemStack): Boolean = ingredient.test(first) && explosive.test(second)

    override fun getRequiredAmount(first: ItemStack, second: ItemStack): Pair<Int, Int> =
        ingredient.getRequiredAmount(first) to explosive.getRequiredAmount(second)

    override fun assemble(firstInput: ItemStack, secondInput: ItemStack): Iterable<ItemStack> = results

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.IMPLODING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.IMPLODING.get()
}
