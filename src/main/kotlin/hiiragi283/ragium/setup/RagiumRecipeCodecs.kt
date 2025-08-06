package hiiragi283.ragium.setup

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.recipe.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTFluidWithCatalystToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.extension.listOrElement
import hiiragi283.ragium.api.recipe.HTFluidToObjRecipe
import hiiragi283.ragium.api.recipe.HTFluidWithCatalystToObjRecipe
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.HTItemWithFluidToObjRecipe
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult

object RagiumRecipeCodecs {
    @JvmStatic
    fun <R1 : HTRecipeResult<*, *>, R2 : HTItemToObjRecipe<R1>> itemToObj(
        codec: Codec<R1>,
        factory: HTItemToObjRecipeBuilder.Factory<R1, R2>,
    ): MapCodec<R2> = RecordCodecBuilder.mapCodec { instance ->
        instance
            .group(
                HTItemIngredient.CODEC.fieldOf("ingredient").forGetter(HTItemToObjRecipe<R1>::ingredient),
                codec.fieldOf("result").forGetter(HTItemToObjRecipe<R1>::result),
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
    fun <R1 : HTRecipeResult<*, *>, R2 : HTFluidWithCatalystToObjRecipe<R1>> fluidWithCatalystToObj(
        codec: Codec<R1>,
        factory: HTFluidWithCatalystToObjRecipeBuilder.Factory<R1, R2>,
    ): MapCodec<R2> = RecordCodecBuilder.mapCodec { instance ->
        instance
            .group(
                HTFluidIngredient.CODEC.fieldOf("ingredient").forGetter(HTFluidWithCatalystToObjRecipe<R1>::ingredient),
                HTItemIngredient.CODEC.optionalFieldOf("catalyst").forGetter(HTFluidWithCatalystToObjRecipe<R1>::catalyst),
                codec.fieldOf("result").forGetter(HTFluidWithCatalystToObjRecipe<R1>::result),
            ).apply(instance, factory::create)
    }

    @JvmStatic
    fun <R1 : HTRecipeResult<*, *>, R2 : HTItemWithFluidToObjRecipe<R1>> itemWithFluidToObj(
        codec: Codec<R1>,
        factory: HTItemWithFluidToObjRecipeBuilder.Factory<R1, R2>,
    ): MapCodec<R2> = RecordCodecBuilder.mapCodec { instance ->
        instance
            .group(
                HTItemIngredient.CODEC.optionalFieldOf("item_ingredient").forGetter(HTItemWithFluidToObjRecipe<R1>::itemIngredient),
                HTFluidIngredient.CODEC.optionalFieldOf("fluid_ingredient").forGetter(HTItemWithFluidToObjRecipe<R1>::fluidIngredient),
                codec.fieldOf("result").forGetter(HTItemWithFluidToObjRecipe<R1>::result),
            ).apply(instance, factory::create)
    }

    @JvmStatic
    fun <R : HTFluidToObjRecipe> fluidToObj(factory: HTFluidToObjRecipeBuilder.Factory<R>): MapCodec<R> =
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTFluidIngredient.CODEC.fieldOf("ingredient").forGetter(HTFluidToObjRecipe::ingredient),
                    HTItemResult.CODEC.optionalFieldOf("item_result").forGetter(HTFluidToObjRecipe::itemResult),
                    HTFluidResult.CODEC
                        .listOrElement(1, 4)
                        .fieldOf("fluid_result")
                        .forGetter(HTFluidToObjRecipe::fluidResults),
                ).apply(instance, factory::create)
        }
}
