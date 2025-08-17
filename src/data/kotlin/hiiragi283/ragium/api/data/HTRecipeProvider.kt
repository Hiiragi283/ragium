package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidWithCatalystToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumTierType
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.IConditionBuilder
import net.neoforged.neoforge.common.crafting.CompoundIngredient

sealed class HTRecipeProvider : IConditionBuilder {
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
                output.accept(modifyId(id), recipe, advancement, *conditions)
            }

            override fun advancement(): Advancement.Builder = output.advancement()
        }.let(::modifyOutput)
        buildRecipeInternal()
    }

    protected abstract fun modifyId(id: ResourceLocation): ResourceLocation

    protected abstract fun modifyOutput(output: RecipeOutput): RecipeOutput

    protected abstract fun buildRecipeInternal()

    //    Direct    //

    abstract class Direct : HTRecipeProvider() {
        override fun modifyId(id: ResourceLocation): ResourceLocation = RagiumAPI.id(id.path)

        override fun modifyOutput(output: RecipeOutput): RecipeOutput = output
    }

    //    Integration    //

    abstract class Integration(protected val modid: String) : HTRecipeProvider() {
        protected fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(modid, path)

        override fun modifyId(id: ResourceLocation): ResourceLocation = when (val namespace: String = id.namespace) {
            RagiumAPI.MOD_ID -> id
            RagiumConst.COMMON -> RagiumAPI.id(id.path)
            RagiumConst.MINECRAFT -> RagiumAPI.id(id.path)
            else -> {
                val path: List<String> = id.path.split("/", limit = 2)
                RagiumAPI.id(path[0] + "/$namespace/" + path[1])
            }
        }

        override fun modifyOutput(output: RecipeOutput): RecipeOutput = output.withConditions(modLoaded(modid))
    }

    //    Extensions    //

    protected fun save(recipeId: ResourceLocation, recipe: Recipe<*>, vararg conditions: ICondition) {
        output.accept(recipeId, recipe, null, *conditions)
    }

    protected fun fuelOrDust(material: HTMaterialType): Ingredient = multiVariants(material, HTMaterialVariant.DUST, HTMaterialVariant.FUEL)

    protected fun gemOrDust(material: HTMaterialType): Ingredient = multiVariants(material, HTMaterialVariant.DUST, HTMaterialVariant.GEM)

    protected fun ingotOrDust(material: HTMaterialType): Ingredient =
        multiVariants(material, HTMaterialVariant.DUST, HTMaterialVariant.INGOT)

    protected fun ingotOrRod(material: HTMaterialType): Ingredient = multiVariants(material, HTMaterialVariant.INGOT, HTMaterialVariant.ROD)

    protected fun multiVariants(material: HTMaterialType, vararg variants: HTMaterialVariant): Ingredient =
        CompoundIngredient(variants.map { it.itemTagKey(material) }.map(Ingredient::of)).toVanilla()

    protected fun meltAndFreeze(
        catalyst: HTItemIngredient?,
        solid: ItemLike,
        fluid: HTFluidContent<*, *, *>,
        amount: Int,
    ) {
        // Melting
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(solid),
                HTResultHelper.fluid(fluid, amount),
            ).saveSuffixed(output, "_from_${solid.asItemHolder().idOrThrow.path}")
        // Solidifying
        HTFluidWithCatalystToObjRecipeBuilder
            .solidifying(
                catalyst,
                HTIngredientHelper.fluid(fluid, amount),
                HTResultHelper.item(solid),
            ).saveSuffixed(output, "_from_${fluid.id.path}")
    }

    protected fun extractAndInfuse(
        empty: HTItemIngredient,
        filled: ItemLike,
        fluid: HTFluidContent<*, *, *>,
        amount: Int,
    ) {
        // Melting
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(filled),
                HTResultHelper.fluid(fluid, amount),
            ).saveSuffixed(output, "_from_${filled.asItemHolder().idOrThrow.path}")
        // Infusing
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                empty,
                HTIngredientHelper.fluid(fluid, amount),
                HTResultHelper.item(filled),
            ).saveSuffixed(output, "_from_${fluid.id.path}")
    }

    protected fun distillation(input: Pair<TagKey<Fluid>, Int>, itemResult: HTItemResult?, vararg fluidResults: HTFluidResult) {
        val (tagKey: TagKey<Fluid>, amount: Int) = input
        val suffix = "_from_${tagKey.location.path}"
        val ingredient: HTFluidIngredient = HTIngredientHelper.fluid(tagKey, amount)
        // Refining
        HTFluidToObjRecipeBuilder.refining(ingredient, itemResult, *fluidResults).save(output)
        // Solidifying
        itemResult?.let { result: HTItemResult ->
            HTFluidWithCatalystToObjRecipeBuilder
                .solidifying(null, ingredient, result)
                .saveSuffixed(output, suffix)
        }
    }

    protected fun createNetheriteUpgrade(output: ItemLike, input: ItemLike): HTSmithingRecipeBuilder = HTSmithingRecipeBuilder(output)
        .addIngredient(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
        .addIngredient(input)
        .addIngredient(Tags.Items.INGOTS_NETHERITE)

    protected fun createComponentUpgrade(tier: RagiumTierType, output: ItemLike, ingredient: ItemLike): HTSmithingRecipeBuilder =
        HTSmithingRecipeBuilder(output).addIngredient(RagiumItems.getComponent(tier)).addIngredient(ingredient)
}
