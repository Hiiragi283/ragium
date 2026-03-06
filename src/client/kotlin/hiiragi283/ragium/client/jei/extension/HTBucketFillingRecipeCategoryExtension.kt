package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.util.getOrEmpty
import hiiragi283.ragium.api.integration.jei.HTItemOrFluidRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.special.HTBucketFillingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

class HTBucketFillingRecipeCategoryExtension(val manager: IIngredientManager) :
    HTItemOrFluidRecipeCategoryExtension<HTBucketFillingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setInputFluid(recipe: HTBucketFillingRecipe, accessor: T) {
        accessor.addFluidStacks(true, manager.getAllIngredients(NeoForgeTypes.FLUID_STACK))
    }

    override fun <T : IIngredientAcceptor<T>> setInputItem(recipe: HTBucketFillingRecipe, accessor: T) {
        accessor.addIngredients(Ingredient.of(Tags.Items.BUCKETS_EMPTY))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTBucketFillingRecipe,
        inputFluid: IRecipeSlotDrawable,
        inputItem: IRecipeSlotDrawable,
        outputItem: IRecipeSlotDrawable,
        outputFluid: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {
        val fluidStack: FluidStack = inputFluid.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).getOrEmpty().copy()
        if (fluidStack.isEmpty) return

        outputItem
            .createDisplayOverrides()
            .addItemStack(FluidUtil.getFilledBucket(fluidStack))
    }
}
