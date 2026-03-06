package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.util.getOrEmpty
import hiiragi283.ragium.api.integration.jei.HTItemOrFluidRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.special.HTPotionFillingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack

class HTPotionFillingRecipeCategoryExtension(val manager: IIngredientManager) :
    HTItemOrFluidRecipeCategoryExtension<HTPotionFillingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setInputFluid(recipe: HTPotionFillingRecipe, accessor: T) {
        accessor
            .addFluidStacks(
                true,
                manager
                    .getAllIngredients(NeoForgeTypes.FLUID_STACK)
                    .filter { HTPotionHelper.getContents(it) != null },
            )
    }

    override fun <T : IIngredientAcceptor<T>> setInputItem(recipe: HTPotionFillingRecipe, accessor: T) {
        accessor.addItemLike(Items.GLASS_BOTTLE)
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTPotionFillingRecipe,
        inputFluid: IRecipeSlotDrawable,
        inputItem: IRecipeSlotDrawable,
        outputItem: IRecipeSlotDrawable,
        outputFluid: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {
        val fluidStack: FluidStack = inputFluid.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).getOrEmpty().copy()
        if (fluidStack.isEmpty) return

        HTPotionHelper
            .getContents(fluidStack)
            ?.let(HTPotionHelper::createPotion)
            ?.let(outputItem.createDisplayOverrides()::addItemStack)
    }
}
