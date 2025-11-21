package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.recipe.HTClearComponentRecipe
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.impl.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition

/**
 * Ragiumがレシピ生成で使用するクラス
 * @see Direct
 * @see Integration
 */
sealed class HTRecipeProvider {
    protected lateinit var provider: HolderLookup.Provider
        private set
    protected lateinit var output: RecipeOutput
        private set

    val itemCreator: HTItemIngredientCreator by lazy { RagiumPlatform.INSTANCE.createItemCreator(provider) }

    val fluidCreator: HTFluidIngredientCreator by lazy { RagiumPlatform.INSTANCE.createFluidCreator(provider) }

    /**
     * [HTResultHelper]のインスタンス
     */
    val resultHelper: HTResultHelper = HTResultHelper

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

    /**
     * 指定された[ResourceLocation]を改変します。
     */
    protected abstract fun modifyId(id: ResourceLocation): ResourceLocation

    /**
     * 指定された[RecipeOutput]を改変します。
     */
    protected abstract fun modifyOutput(output: RecipeOutput): RecipeOutput

    /**
     * レシピを生成します。
     */
    protected abstract fun buildRecipeInternal()

    //    Direct    //

    /**
     * Ragium単体で使用するレシピ向けの拡張クラス
     */
    abstract class Direct : HTRecipeProvider() {
        override fun modifyId(id: ResourceLocation): ResourceLocation = RagiumAPI.id(id.path)

        override fun modifyOutput(output: RecipeOutput): RecipeOutput = output
    }

    //    Integration    //

    /**
     * 他Modとの連携レシピ向けの拡張クラス
     */
    abstract class Integration(protected val modid: String) : HTRecipeProvider() {
        protected fun id(path: String): ResourceLocation = modid.toId(path)

        override fun modifyId(id: ResourceLocation): ResourceLocation {
            val namespace: String = id.namespace
            return if (namespace in RagiumConst.BUILTIN_IDS) {
                val path: List<String> = id.path.split("/", limit = 2)
                return RagiumAPI.id(path[0] + "/$modid/" + path[1])
            } else {
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

    // recipe builders
    protected fun meltAndFreeze(
        catalyst: HTItemIngredient?,
        solid: HTItemHolderLike,
        fluid: HTFluidHolderLike,
        amount: Int,
    ) {
        // Melting
        HTItemToObjRecipeBuilder
            .melting(
                itemCreator.fromItem(solid),
                resultHelper.fluid(fluid.getFluid(), amount),
            ).saveSuffixed(output, "_from_${solid.getPath()}")
        // Solidifying
        HTComplexRecipeBuilder
            .solidifying(
                catalyst,
                fluidCreator.fromHolder(fluid, amount),
                resultHelper.item(solid),
            ).saveSuffixed(output, "_from_${fluid.getPath()}")
    }

    protected fun meltAndFreeze(data: HTRecipeData) {
        // Solidifying
        HTComplexRecipeBuilder
            .solidifying(
                data.catalyst?.let(itemCreator::fromItem),
                data.getFluidIngredients(fluidCreator)[0],
                data.getItemResults()[0].first,
            ).saveModified(output, data.operator)
        // Melting
        val data1: HTRecipeData = data.swap()
        HTItemToObjRecipeBuilder
            .melting(
                data1.getItemIngredients(itemCreator)[0],
                data1.getFluidResults()[0],
            ).saveModified(output, data.operator)
    }

    protected fun extractAndInfuse(
        empty: ItemLike,
        filled: HTItemHolderLike,
        fluid: HTFluidHolderLike,
        amount: Int = 250,
    ) {
        // Melting
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(filled),
                resultHelper.item(empty),
                null,
                resultHelper.fluid(fluid.getFluid(), amount),
            ).saveSuffixed(output, "_from_${filled.getPath()}")
        // Washing
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(empty),
                fluidCreator.fromHolder(fluid, amount),
            ).addResult(resultHelper.item(filled))
            .saveSuffixed(output, "_from_${fluid.getPath()}")
    }

    protected fun distillation(
        input: Pair<HTFluidHolderLike, Int>,
        itemResult: HTItemResult?,
        vararg results: Pair<HTFluidResult, HTItemIngredient?>,
    ) {
        val (holder: HTFluidHolderLike, amount: Int) = input
        val suffix = "_from_${holder.getPath()}"
        val ingredient: HTFluidIngredient = fluidCreator.fromHolder(holder, amount)
        // Refining
        for ((result: HTFluidResult, catalyst: HTItemIngredient?) in results) {
            HTComplexRecipeBuilder
                .refining(ingredient, result, catalyst, itemResult)
                .saveSuffixed(output, suffix)
        }
    }

    /**
     * 指定された引数から作業台でのリセットレシピを登録します。
     * @param item リセットを行うアイテム
     * @param targetTypes リセット対象のコンポーネントのキーの一覧
     */
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

    /**
     * 指定された引数から鍛冶台でのネザライト強化レシピのビルダーを返します。
     * @param output 強化後のアイテム
     * @param input 強化前のアイテム
     */
    protected fun createNetheriteUpgrade(output: ItemLike, input: ItemLike): HTSmithingRecipeBuilder = HTSmithingRecipeBuilder
        .create(output)
        .addIngredient(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
        .addIngredient(input)
        .addIngredient(CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.NETHERITE)

    /**
     * 指定された引数から鍛冶台での強化レシピのビルダーを返します。
     * @param tier アップグレードにつかう構造体のティア
     * @param output 強化後のアイテム
     * @param input 強化前のアイテム
     */
    protected fun createComponentUpgrade(tier: HTComponentTier, output: ItemLike, input: ItemLike): HTSmithingRecipeBuilder =
        HTSmithingRecipeBuilder
            .create(output)
            .addIngredient(RagiumItems.getComponent(tier))
            .addIngredient(input)

    /**
     * 指定された[HTWoodType]に基づいて，木材の製材レシピを追加します。
     */
    protected fun addWoodSawing(type: HTWoodType) {
        val planks: ItemLike = type.planks
        // Log -> 6x Planks
        HTItemToChancedItemRecipeBuilder
            .cutting(itemCreator.fromTagKey(type.log))
            .addResult(resultHelper.item(planks, 6))
            .modCondition(type.getModId())
            .save(output)
        // Planks -> 2x Slab
        type.getSlab().ifPresent { slab ->
            HTItemToChancedItemRecipeBuilder
                .cutting(itemCreator.fromItem(planks))
                .addResult(resultHelper.item(slab, 2))
                .modCondition(type.getModId())
                .save(output)
        }
        // Planks -> Stairs
        type.getStairs().ifPresent { stairs ->
            HTItemToChancedItemRecipeBuilder
                .cutting(itemCreator.fromItem(planks))
                .addResult(resultHelper.item(stairs))
                .modCondition(type.getModId())
                .save(output)
        }
    }

    protected fun pulverizeFromData(data: HTRecipeData) {
        HTItemToObjRecipeBuilder
            .pulverizing(
                data.getItemIngredients(itemCreator)[0],
                data.getItemResults()[0].first,
            ).saveModified(output, data.operator)
    }

    protected fun cropAndSeed(seed: ItemLike, crop: ItemLike, water: Int = 125) {
        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                itemCreator.fromItem(seed),
                fluidCreator.water(water),
            ).addResult(resultHelper.item(crop, 3))
            .addResult(resultHelper.item(seed), 1 / 3f)
            .save(output)
    }

    protected fun cropAndCrop(crop: ItemLike, water: Int = 125) {
        cropAndSeed(crop, crop, water)
    }

    protected fun tree(sapling: ItemLike, log: ItemLike, fluid: HTFluidIngredient = fluidCreator.water(250)) {
        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                itemCreator.fromItem(sapling),
                fluid,
            ).addResult(resultHelper.item(log, 6))
            .addResult(resultHelper.item(sapling), 1 / 6f)
            .save(output)
    }
}
