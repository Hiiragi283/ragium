package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.HTViewProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.toLike
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.core.Holder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment

class HTSingleRecipeBuilder<ING : Any, RES : Any>(
    prefix: String,
    private val factory: Factory<ING, RES, *>,
    private val idFactory: (RES) -> ResourceLocation,
) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun enchanting(
            output: RecipeOutput,
            builderAction: HTSingleRecipeBuilder<HTItemIngredient, Holder<Enchantment>>.() -> Unit,
        ) {
            HTSingleRecipeBuilder(
                RagiumConst.ENCHANTING,
                ::HTEnchantingRecipe,
            ) { holder: Holder<Enchantment> -> holder.toLike().getId() }.apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun melting(output: RecipeOutput, builderAction: HTSingleRecipeBuilder<HTItemIngredient, HTFluidResult>.() -> Unit) {
            HTSingleRecipeBuilder(RagiumConst.MELTING, ::HTMeltingRecipe, HTFluidResult::getId).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun pressing(
            output: RecipeOutput,
            builderAction: HTSingleRecipeBuilder<Pair<HTItemIngredient, HTItemIngredient>, HTItemResult>.() -> Unit,
        ) {
            HTSingleRecipeBuilder(RagiumConst.PRESSING, ::HTPressingRecipe, HTItemResult::getId).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun refining(output: RecipeOutput, builderAction: HTSingleRecipeBuilder<HTFluidIngredient, HTFluidResult>.() -> Unit) {
            HTSingleRecipeBuilder(RagiumConst.REFINING, ::HTRefiningRecipe, HTFluidResult::getId).apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: ING
    lateinit var result: RES

    override fun getPrimalId(): ResourceLocation = idFactory(result)

    override fun createRecipe(): HTViewProcessingRecipe = factory.create(ingredient, result, subParameters())

    //    Factory    //

    fun interface Factory<ING : Any, RES : Any, RECIPE : HTViewProcessingRecipe> {
        fun create(ingredient: ING, result: RES, parameters: HTProcessingRecipe.SubParameters): RECIPE
    }
}
