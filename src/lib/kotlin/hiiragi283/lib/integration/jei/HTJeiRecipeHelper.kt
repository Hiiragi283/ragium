package hiiragi283.lib.integration.jei

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.item.toTemplateOrNull
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.display.SlotDisplay
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.crafting.CustomDisplayIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.CustomDisplayFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.display.FluidStackSlotDisplay

/**
 * JEI上でレシピの登録を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTJeiRecipeHelper {
    //    Fluid    //

    @JvmStatic
    private val FAKE_FLUID_INGREDIENT: FluidIngredient = FluidIngredient.of(Fluids.WATER)

    /**
     * 動的レシピ向けに，偽のプレビューをもつ[HTFluidIngredient]を作成します。
     * @param stack 偽のプレビュー
     * @param amount 必要な数量
     */
    @JvmStatic
    fun fakeFluid(stack: FluidStack, amount: Int): HTFluidIngredient = fakeFluid(FluidStackSlotDisplay(stack), amount)

    /**
     * 動的レシピ向けに，偽のプレビューをもつ[HTFluidIngredient]を作成します。
     * @param fluids 偽のプレビューの一覧
     * @param amount 必要な数量
     */
    @JvmStatic
    fun fakeFluid(fluids: Iterable<FluidStack>, amount: Int): HTFluidIngredient =
        fakeFluid(fluids.map(::FluidStackSlotDisplay).let(::SlotDisplay), amount)

    /**
     * 動的レシピ向けに，偽のプレビューをもつ[HTFluidIngredient]を作成します。
     * @param display 偽のプレビューの提供元
     * @param amount 必要な数量
     */
    @JvmStatic
    fun fakeFluid(display: SlotDisplay, amount: Int): HTFluidIngredient =
        HTFluidIngredient(CustomDisplayFluidIngredient.of(FAKE_FLUID_INGREDIENT, display), amount)

    //    Item    //

    @JvmStatic
    private val FAKE_INGREDIENT: Ingredient = Ingredient.of(Items.BEDROCK)

    /**
     * 動的レシピ向けに，偽のプレビューをもつ[HTItemIngredient]を作成します。
     * @param stack 偽のプレビュー
     * @param count 必要な個数
     */
    @JvmStatic
    fun fakeItem(stack: ItemStack, count: Int = 1): HTItemIngredient =
        fakeItem(stack.toTemplateOrNull()?.let(SlotDisplay::ItemStackSlotDisplay) ?: SlotDisplay.Empty.INSTANCE, count)

    /**
     * 動的レシピ向けに，偽のプレビューをもつ[HTItemIngredient]を作成します。
     * @param template 偽のプレビュー
     * @param count 必要な個数
     */
    @JvmStatic
    fun fakeItem(template: ItemStackTemplate, count: Int = 1): HTItemIngredient =
        fakeItem(SlotDisplay.ItemStackSlotDisplay(template), count)

    /**
     * 動的レシピ向けに，偽のプレビューをもつ[HTItemIngredient]を作成します。
     * @param items 偽のプレビューの一覧
     * @param count 必要な個数
     */
    @JvmName("fakeItemStacks")
    @JvmStatic
    fun fakeItem(items: Iterable<ItemStack>, count: Int = 1): HTItemIngredient =
        fakeItem(items.mapNotNull { it.toTemplateOrNull() }, count)

    /**
     * 動的レシピ向けに，偽のプレビューをもつ[HTItemIngredient]を作成します。
     * @param items 偽のプレビューの一覧
     * @param count 必要な個数
     */
    @JvmName("fakeItemTemplates")
    @JvmStatic
    fun fakeItem(items: Iterable<ItemStackTemplate>, count: Int = 1): HTItemIngredient =
        fakeItem(items.map(SlotDisplay::ItemStackSlotDisplay).let(::SlotDisplay), count)

    /**
     * 動的レシピ向けに，偽のプレビューをもつ[HTItemIngredient]を作成します。
     * @param display 偽のプレビューの提供元
     * @param count 必要な個数
     */
    @JvmStatic
    fun fakeItem(display: SlotDisplay, count: Int = 1): HTItemIngredient =
        HTItemIngredient(CustomDisplayIngredient.of(FAKE_INGREDIENT, display), count)

    //    Recipes    //

    /**
     * JEIにレシピを登録します。
     * @param RECIPE_A [lookup]が提供するレシピのクラス
     * @param RECIPE_B [recipeType]が要求するレシピのクラス
     * @param registration レシピの登録先
     * @param recipeType JEI上でのレシピの種類
     * @param lookup レシピの提供元
     */
    @JvmStatic
    inline fun <RECIPE_A : Any, reified RECIPE_B : RECIPE_A> addRecipes(
        registration: IRecipeRegistration,
        recipeType: IRecipeType<HTRecipeHolder<RECIPE_B>>,
        lookup: HTRecipeLookup<RECIPE_A>
    ) {
        val recipes: List<HTRecipeHolder<RECIPE_B>> = getRecipes(lookup)
        if (recipes.isEmpty()) return
        registration.addRecipes(recipeType, recipes)
    }

    /**
     * レシピを取得します。
     * @param RECIPE_A [lookup]が提供するレシピのクラス
     * @param RECIPE_B 変換後のレシピのクラス
     * @param lookup レシピの提供元
     * @return [RECIPE_B]に対する[HTRecipeHolder]の一覧
     */
    @JvmStatic
    inline fun <RECIPE_A : Any, reified RECIPE_B : RECIPE_A> getRecipes(
        lookup: HTRecipeLookup<RECIPE_A>
    ): List<HTRecipeHolder<RECIPE_B>> = lookup.getAllRecipesN(HTPhysicalSideHelper.createLookupContext())
        .mapNotNull { (key: RecipeKey, recipe: RECIPE_A) ->
            when (recipe) {
                is RECIPE_B -> HTRecipeHolder(key, recipe)
                else -> null
            }
        }.toList()
}
