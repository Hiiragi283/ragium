package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.extension.toId
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTRawStorageMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.recipe.HTClearComponentRecipe
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTIngredientHelperImpl
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition

sealed class HTRecipeProvider {
    protected lateinit var provider: HolderLookup.Provider
        private set
    protected lateinit var output: RecipeOutput
        private set

    val ingredientHelper: HTIngredientHelper by lazy { HTIngredientHelperImpl(provider) }
    val resultHelper: HTResultHelper = HTResultHelper.INSTANCE

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
        protected fun id(path: String): ResourceLocation = modid.toId(path)

        override fun modifyId(id: ResourceLocation): ResourceLocation = when (val namespace: String = id.namespace) {
            in RagiumConst.BUILTIN_IDS -> RagiumAPI.wrapId(id)
            else -> {
                val path: List<String> = id.path.split("/", limit = 2)
                RagiumAPI.id(path[0] + "/$namespace/" + path[1])
            }
        }

        override fun modifyOutput(output: RecipeOutput): RecipeOutput = output.withConditions(ModLoadedCondition(modid))
    }

    //    Extensions    //

    protected fun save(recipeId: ResourceLocation, recipe: Recipe<*>, vararg conditions: ICondition) {
        output.accept(recipeId, recipe, null, *conditions)
    }

    // ingredient
    protected fun fuelOrDust(material: HTMaterialType): Ingredient =
        multiVariants(material, HTItemMaterialVariant.DUST, HTItemMaterialVariant.FUEL)

    protected fun gemOrDust(material: HTMaterialType): Ingredient =
        multiVariants(material, HTItemMaterialVariant.DUST, HTItemMaterialVariant.GEM)

    protected fun ingotOrDust(material: HTMaterialType): Ingredient =
        multiVariants(material, HTItemMaterialVariant.DUST, HTItemMaterialVariant.INGOT)

    protected fun ingotOrRod(material: HTMaterialType): Ingredient =
        multiVariants(material, HTItemMaterialVariant.INGOT, HTItemMaterialVariant.ROD)

    protected fun multiVariants(material: HTMaterialType, vararg variants: HTMaterialVariant.ItemTag): Ingredient = variants
        .map { it.itemTagKey(material) }
        .map(Ingredient::TagValue)
        .stream()
        .let(Ingredient::fromValues)

    // recipe builders
    protected fun meltAndFreeze(
        catalyst: HTItemIngredient?,
        solid: HTItemHolderLike,
        fluid: HTFluidContent<*, *, *>,
        amount: Int,
    ) {
        // Melting
        HTItemToObjRecipeBuilder
            .melting(
                ingredientHelper.item(solid),
                resultHelper.fluid(fluid, amount),
            ).saveSuffixed(output, "_from_${solid.getPath()}")
        // Solidifying
        HTFluidTransformRecipeBuilder
            .solidifying(
                catalyst,
                ingredientHelper.fluid(fluid, amount),
                resultHelper.item(solid),
            ).saveSuffixed(output, "_from_${fluid.getPath()}")
    }

    protected fun meltAndFreeze(
        catalyst: HTItemIngredient?,
        solid: TagKey<Item>,
        fluid: HTFluidContent<*, *, *>,
        amount: Int,
    ) {
        // Melting
        HTItemToObjRecipeBuilder
            .melting(
                ingredientHelper.item(solid),
                resultHelper.fluid(fluid, amount),
            ).saveSuffixed(output, "_from_${solid.location.path}")
        // Solidifying
        HTFluidTransformRecipeBuilder
            .solidifying(
                catalyst,
                ingredientHelper.fluid(fluid, amount),
                resultHelper.item(solid),
            ).saveSuffixed(output, "_from_${fluid.getPath()}")
    }

    protected fun extractAndInfuse(
        empty: HTItemIngredient,
        filled: HTItemHolderLike,
        fluid: HTFluidContent<*, *, *>,
        amount: Int,
    ) {
        // Melting
        HTItemToObjRecipeBuilder
            .melting(
                ingredientHelper.item(filled),
                resultHelper.fluid(fluid, amount),
            ).saveSuffixed(output, "_from_${filled.getPath()}")
        // Washing
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                empty,
                ingredientHelper.fluid(fluid, amount),
            ).addResult(resultHelper.item(filled))
            .saveSuffixed(output, "_from_${fluid.getPath()}")
    }

    protected fun distillation(
        input: Pair<HTFluidContent<*, *, *>, Int>,
        itemResult: HTItemResult?,
        vararg results: Pair<HTFluidResult, HTItemIngredient?>,
    ) {
        val (content: HTFluidContent<*, *, *>, amount: Int) = input
        val suffix = "_from_${content.getPath()}"
        val ingredient: HTFluidIngredient = ingredientHelper.fluid(content, amount)
        // Refining
        for ((result: HTFluidResult, catalyst: HTItemIngredient?) in results) {
            HTFluidTransformRecipeBuilder
                .refining(ingredient, result, catalyst, itemResult)
                .saveSuffixed(output, suffix)
        }
    }

    protected fun rawToIngot(material: HTMaterialType) {
        val ingot: TagKey<Item> = HTItemMaterialVariant.INGOT.itemTagKey(material)
        // Basic
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(ingot, 3),
                ingredientHelper.item(HTItemMaterialVariant.RAW_MATERIAL, material, 2),
                ingredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).tagCondition(ingot)
            .saveSuffixed(output, "_with_basic_flux")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(ingot, 27),
                ingredientHelper.item(HTRawStorageMaterialVariant, material, 2),
                ingredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC, 6),
            ).tagCondition(ingot)
            .saveSuffixed(output, "_from_block_with_basic_flux")
        // Advanced
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(ingot, 2),
                ingredientHelper.item(HTItemMaterialVariant.RAW_MATERIAL, material),
                ingredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED),
            ).tagCondition(ingot)
            .saveSuffixed(output, "_with_advanced_flux")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(ingot, 18),
                ingredientHelper.item(HTRawStorageMaterialVariant, material),
                ingredientHelper.item(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, 6),
            ).tagCondition(ingot)
            .saveSuffixed(output, "_from_block_with_advanced_flux")
    }

    protected fun resetComponent(item: HTItemHolderLike, vararg targetTypes: DataComponentType<*>) {
        save(
            item.getId().withPath { "shapeless/${it}_clear" },
            HTClearComponentRecipe(
                "",
                CraftingBookCategory.MISC,
                item,
                listOf(*targetTypes),
            ),
        )
    }

    protected fun createNetheriteUpgrade(output: ItemLike, input: ItemLike): HTSmithingRecipeBuilder = HTSmithingRecipeBuilder(output)
        .addIngredient(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
        .addIngredient(input)
        .addIngredient(HTItemMaterialVariant.INGOT, HTVanillaMaterialType.NETHERITE)

    protected fun createComponentUpgrade(tier: HTComponentTier, output: ItemLike, ingredient: ItemLike): HTSmithingRecipeBuilder =
        HTSmithingRecipeBuilder(output)
            .addIngredient(RagiumItems.getComponent(tier))
            .addIngredient(ingredient)

    protected fun addWoodSawing(type: HTWoodType) {
        val planks: ItemLike = type.planks
        // Log -> 6x Planks
        HTItemToObjRecipeBuilder
            .sawmill(
                ingredientHelper.item(type.log),
                resultHelper.item(planks, 6),
            ).modCondition(type.getModId())
            .save(output)
        // Planks -> 2x Slab
        type.getSlab().ifPresent { slab ->
            HTItemToObjRecipeBuilder
                .sawmill(
                    ingredientHelper.item(planks),
                    resultHelper.item(slab, 2),
                ).modCondition(type.getModId())
                .save(output)
        }
        // Planks -> Stairs
        type.getStairs().ifPresent { stairs ->
            HTItemToObjRecipeBuilder
                .sawmill(
                    ingredientHelper.item(planks),
                    resultHelper.item(stairs),
                ).modCondition(type.getModId())
                .save(output)
        }
    }
}
