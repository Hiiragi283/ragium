package hiiragi283.ragium.setup

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.listOrElement
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult

object RagiumRecipeCodecs {
    @JvmStatic
    fun <R : HTItemToItemRecipe> itemToItem(factory: (HTItemIngredient, HTItemResult) -> R): MapCodec<R> =
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf("ingredient").forGetter(HTItemToItemRecipe::ingredient),
                    HTItemResult.CODEC.fieldOf("result").forGetter(HTItemToItemRecipe::result),
                ).apply(instance, factory)
        }

    @JvmStatic
    fun <R : HTItemToChancedItemRecipe> itemToChancedItem(factory: (HTItemIngredient, List<HTItemResult>, List<Float>) -> R): MapCodec<R> =
        RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTItemIngredient.CODEC.fieldOf("ingredient").forGetter(HTItemToChancedItemRecipe::ingredient),
                        HTItemResult.CODEC
                            .listOrElement(1, 4)
                            .fieldOf("results")
                            .forGetter(HTItemToChancedItemRecipe::results),
                        Codec
                            .floatRange(0f, 1f)
                            .listOrElement(0, 4)
                            .optionalFieldOf("chances", listOf(1f))
                            .forGetter(HTItemToChancedItemRecipe::chances),
                    ).apply(instance, factory)
            }.validate { recipe: R ->
                if (recipe.chances.isNotEmpty()) {
                    if (recipe.chances.size != recipe.results.size) {
                        return@validate DataResult.error { "Requires the same count of results and its chances!" }
                    }
                }
                DataResult.success(recipe)
            }
}
