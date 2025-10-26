package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.variant.HTMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.recipe.HTClearComponentRecipe
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.variant.HTItemMaterialVariant
import hiiragi283.ragium.common.variant.HTRawStorageMaterialVariant
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.ingredient.HTFluidIngredientCreatorImpl
import hiiragi283.ragium.impl.data.recipe.ingredient.HTItemIngredientCreatorImpl
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
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

    val itemCreator: HTItemIngredientCreator by lazy { HTItemIngredientCreatorImpl(provider.lookupOrThrow(Registries.ITEM)) }

    val fluidCreator: HTFluidIngredientCreator by lazy { HTFluidIngredientCreatorImpl(provider.lookupOrThrow(Registries.FLUID)) }

    /**
     * [HTResultHelper]のインスタンス
     */
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
                itemCreator.fromItem(solid),
                resultHelper.fluid(fluid, amount),
            ).saveSuffixed(output, "_from_${solid.getPath()}")
        // Solidifying
        HTFluidTransformRecipeBuilder
            .solidifying(
                catalyst,
                fluidCreator.fromContent(fluid, amount),
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
                itemCreator.fromTagKey(solid),
                resultHelper.fluid(fluid, amount),
            ).saveSuffixed(output, "_from_${solid.location.path}")
        // Solidifying
        HTFluidTransformRecipeBuilder
            .solidifying(
                catalyst,
                fluidCreator.fromContent(fluid, amount),
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
                itemCreator.fromItem(filled),
                resultHelper.fluid(fluid, amount),
            ).saveSuffixed(output, "_from_${filled.getPath()}")
        // Washing
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                empty,
                fluidCreator.fromContent(fluid, amount),
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
        val ingredient: HTFluidIngredient = fluidCreator.fromContent(content, amount)
        // Refining
        for ((result: HTFluidResult, catalyst: HTItemIngredient?) in results) {
            HTFluidTransformRecipeBuilder
                .refining(ingredient, result, catalyst, itemResult)
                .saveSuffixed(output, suffix)
        }
    }

    /**
     * 原石または原石ブロックをインゴットに製錬する合金レシピを登録します。
     * @param material 単体金属系の素材
     */
    protected fun rawToIngot(material: HTMaterialType) {
        val ingot: TagKey<Item> = HTItemMaterialVariant.INGOT.itemTagKey(material)
        // Basic
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(ingot, 3),
                itemCreator.fromTagKey(HTItemMaterialVariant.RAW_MATERIAL, material, 2),
                itemCreator.fromTagKey(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC),
            ).tagCondition(ingot)
            .saveSuffixed(output, "_with_basic_flux")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(ingot, 27),
                itemCreator.fromTagKey(HTRawStorageMaterialVariant, material, 2),
                itemCreator.fromTagKey(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC, 6),
            ).tagCondition(ingot)
            .saveSuffixed(output, "_from_block_with_basic_flux")
        // Advanced
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(ingot, 2),
                itemCreator.fromTagKey(HTItemMaterialVariant.RAW_MATERIAL, material),
                itemCreator.fromTagKey(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED),
            ).tagCondition(ingot)
            .saveSuffixed(output, "_with_advanced_flux")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(ingot, 18),
                itemCreator.fromTagKey(HTRawStorageMaterialVariant, material),
                itemCreator.fromTagKey(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, 6),
            ).tagCondition(ingot)
            .saveSuffixed(output, "_from_block_with_advanced_flux")
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
        .addIngredient(HTItemMaterialVariant.INGOT, HTVanillaMaterialType.NETHERITE)

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
        HTSingleItemRecipeBuilder
            .sawmill(planks, 6)
            .addIngredient(type.log)
            .modCondition(type.getModId())
            .save(output)
        // Planks -> 2x Slab
        type.getSlab().ifPresent { slab ->
            HTSingleItemRecipeBuilder
                .sawmill(slab, 2)
                .addIngredient(planks)
                .modCondition(type.getModId())
                .save(output)
        }
        // Planks -> Stairs
        type.getStairs().ifPresent { stairs ->
            HTSingleItemRecipeBuilder
                .sawmill(stairs)
                .addIngredient(planks)
                .modCondition(type.getModId())
                .save(output)
        }
    }
}
