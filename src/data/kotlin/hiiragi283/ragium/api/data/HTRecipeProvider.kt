package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.IConditionBuilder

abstract class HTRecipeProvider : IConditionBuilder {
    protected lateinit var provider: HolderLookup.Provider
        private set
    protected lateinit var output: RecipeOutput
        private set

    fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        provider = holderLookup
        this.output = object : RecipeOutput {
            override fun accept(
                id: ResourceLocation,
                recipe: Recipe<*>,
                advancement: AdvancementHolder?,
                vararg conditions: ICondition,
            ) {
                val fixedId: ResourceLocation = when (id.namespace) {
                    RagiumAPI.MOD_ID -> id
                    RagiumConst.COMMON -> RagiumAPI.id(id.path)
                    RagiumConst.MINECRAFT -> RagiumAPI.id(id.path)
                    else -> {
                        val path: List<String> = id.path.split("/", limit = 2)
                        RagiumAPI.id(path[0] + "/${id.namespace}/" + path[1])
                    }
                }
                output.accept(fixedId, recipe, advancement, *conditions)
            }

            override fun advancement(): Advancement.Builder = output.advancement()
        }
        buildRecipeInternal()
    }

    fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider, modid: String) {
        buildRecipes(output.withConditions(modLoaded(modid)), holderLookup)
    }

    protected abstract fun buildRecipeInternal()

    //    Extensions    //

    fun save(recipeId: ResourceLocation, recipe: Recipe<*>, vararg conditions: ICondition) {
        output.accept(recipeId, recipe, null, *conditions)
    }

    fun meltAndFreeze(
        output: RecipeOutput,
        catalyst: HTItemIngredient?,
        itemOut: ItemLike,
        fluidIn: TagKey<Fluid>,
        fluidOut: Fluid,
        amount: Int,
    ) {
        // Melting
        HTItemWithFluidToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(itemOut),
                HTResultHelper.fluid(fluidOut, amount),
            ).save(output)
        // Solidifying
        HTItemWithFluidToObjRecipeBuilder
            .solidifying(
                catalyst,
                HTIngredientHelper.fluid(fluidIn, amount),
                HTResultHelper.item(itemOut),
            ).saveSuffixed(output, "_from_${fluidIn.location.path}")
    }

    fun distillation(
        output: RecipeOutput,
        input: Pair<TagKey<Fluid>, Int>,
        itemResult: HTItemResult?,
        vararg fluidResults: HTFluidResult,
    ) {
        val (tagKey: TagKey<Fluid>, amount: Int) = input
        val suffix = "_from_${tagKey.location.path}"
        val ingredient: HTFluidIngredient = HTIngredientHelper.fluid(tagKey, amount)
        // Refining
        HTFluidToObjRecipeBuilder.refining(ingredient, itemResult, *fluidResults).save(output)
        // Solidifying
        itemResult?.let { result: HTItemResult ->
            HTItemWithFluidToObjRecipeBuilder
                .solidifying(null, ingredient, result)
                .saveSuffixed(output, suffix)
        }
    }

    fun createNetheriteUpgrade(output: ItemLike, input: ItemLike): HTSmithingRecipeBuilder = HTSmithingRecipeBuilder(output)
        .addIngredient(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
        .addIngredient(input)
        .addIngredient(Tags.Items.INGOTS_NETHERITE)
}
