package hiiragi283.lib.integration.jei

import hiiragi283.lib.item.toTemplateOrNull
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookupContext
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

data object HTJeiRecipeHelper {
    //    Fluid    //

    @JvmStatic
    private val FAKE_FLUID_INGREDIENT: FluidIngredient = FluidIngredient.of(Fluids.WATER)

    @JvmStatic
    fun fakeFluid(stack: FluidStack, amount: Int): HTFluidIngredient = fakeFluid(FluidStackSlotDisplay(stack), amount)

    @JvmStatic
    fun fakeFluid(fluids: Iterable<FluidStack>, amount: Int): HTFluidIngredient = fakeFluid(fluids.map(::FluidStackSlotDisplay).let(SlotDisplay::Composite), amount)

    @JvmStatic
    fun fakeFluid(display: SlotDisplay, amount: Int): HTFluidIngredient = HTFluidIngredient(CustomDisplayFluidIngredient.of(FAKE_FLUID_INGREDIENT, display), amount)

    //    Item    //

    @JvmStatic
    private val FAKE_INGREDIENT: Ingredient = Ingredient.of(Items.BEDROCK)

    @JvmStatic
    fun fakeItem(stack: ItemStack, count: Int = 1): HTItemIngredient = fakeItem(stack.toTemplateOrNull()?.let(SlotDisplay::ItemStackSlotDisplay) ?: SlotDisplay.Empty.INSTANCE, count)

    @JvmStatic
    fun fakeItem(template: ItemStackTemplate, count: Int = 1): HTItemIngredient = fakeItem(SlotDisplay.ItemStackSlotDisplay(template), count)

    @JvmName("fakeItemStacks")
    @JvmStatic
    fun fakeItem(items: Iterable<ItemStack>, count: Int = 1): HTItemIngredient = fakeItem(items.mapNotNull { it.toTemplateOrNull() }, count)

    @JvmName("fakeItemTemplates")
    @JvmStatic
    fun fakeItem(items: Iterable<ItemStackTemplate>, count: Int = 1): HTItemIngredient = fakeItem(items.map(SlotDisplay::ItemStackSlotDisplay).let(SlotDisplay::Composite), count)

    @JvmStatic
    fun fakeItem(display: SlotDisplay, count: Int = 1): HTItemIngredient = HTItemIngredient(CustomDisplayIngredient.of(FAKE_INGREDIENT, display), count)

    //    Recipes    //

    @JvmStatic
    inline fun <RECIPE_A : Any, reified RECIPE_B : RECIPE_A> addRecipes(registration: IRecipeRegistration, recipeType: IRecipeType<HTRecipeHolder<RECIPE_B>>, lookup: HTRecipeLookup<RECIPE_A>) {
        registration.addRecipes(recipeType, getRecipes(lookup))
    }

    @JvmStatic
    inline fun <RECIPE_A : Any, reified RECIPE_B : RECIPE_A> getRecipes(lookup: HTRecipeLookup<RECIPE_A>): List<HTRecipeHolder<RECIPE_B>> {
        val recipes: MutableList<HTRecipeHolder<RECIPE_B>> = mutableListOf()
        for ((key: RecipeKey, recipe: RECIPE_A) in lookup.getAllRecipes(HTRecipeLookupContext.createOnClient())) {
            if (recipe is RECIPE_B) {
                recipes += HTRecipeHolder(key, recipe)
            }
        }
        return recipes
    }
}
