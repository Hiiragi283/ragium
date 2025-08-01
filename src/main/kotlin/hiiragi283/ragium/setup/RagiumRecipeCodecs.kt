package hiiragi283.ragium.setup

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.recipe.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithFluidToItemRecipeBuilder
import hiiragi283.ragium.api.extension.listOrElement
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.HTItemWithFluidToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult

object RagiumRecipeCodecs {
    @JvmStatic
    fun <R : HTItemToItemRecipe> itemToItem(factory: HTItemToItemRecipeBuilder.Factory<R>): MapCodec<R> =
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf("ingredient").forGetter(HTItemToItemRecipe::ingredient),
                    HTItemResult.CODEC.fieldOf("result").forGetter(HTItemToItemRecipe::result),
                ).apply(instance, factory::create)
        }

    @JvmStatic
    fun <R : HTItemToChancedItemRecipe> itemToChancedItem(factory: HTItemToChancedItemRecipeBuilder.Factory<R>): MapCodec<R> =
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
                    ).apply(instance, factory::create)
            }.validate { recipe: R ->
                if (recipe.chances.isNotEmpty()) {
                    if (recipe.chances.size != recipe.results.size) {
                        return@validate DataResult.error { "Requires the same count of results and its chances!" }
                    }
                }
                DataResult.success(recipe)
            }

    @JvmStatic
    fun <R : HTCombineItemToItemRecipe> combineItemToItem(factory: HTCombineItemToItemRecipeBuilder.Factory<R>): MapCodec<R> =
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC
                        .listOf(2, 2)
                        .fieldOf("ingredients")
                        .forGetter(HTCombineItemToItemRecipe::ingredients),
                    HTItemResult.CODEC
                        .fieldOf("result")
                        .forGetter(HTCombineItemToItemRecipe::result),
                ).apply(instance, factory::create)
        }

    @JvmStatic
    fun <R : HTItemWithCatalystToItemRecipe> itemWithCatalystToItem(
        factory: HTItemWithCatalystToItemRecipeBuilder.Factory<R>,
    ): MapCodec<R> = RecordCodecBuilder.mapCodec { instance ->
        instance
            .group(
                HTItemIngredient.CODEC.fieldOf("ingredient").forGetter(HTItemWithCatalystToItemRecipe::ingredient),
                HTItemIngredient.CODEC.optionalFieldOf("catalyst").forGetter(HTItemWithCatalystToItemRecipe::catalyst),
                HTItemResult.CODEC.fieldOf("result").forGetter(HTItemWithCatalystToItemRecipe::result),
            ).apply(instance, factory::create)
    }

    @JvmStatic
    fun <R : HTItemWithFluidToItemRecipe> itemWithFluidToItem(factory: HTItemWithFluidToItemRecipeBuilder.Factory<R>): MapCodec<R> =
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.optionalFieldOf("item_ingredient").forGetter(HTItemWithFluidToItemRecipe::itemIngredient),
                    HTFluidIngredient.CODEC.optionalFieldOf("fluid_ingredient").forGetter(HTItemWithFluidToItemRecipe::fluidIngredient),
                    HTItemResult.CODEC.fieldOf("result").forGetter(HTItemWithFluidToItemRecipe::result),
                ).apply(instance, factory::create)
        }
}
