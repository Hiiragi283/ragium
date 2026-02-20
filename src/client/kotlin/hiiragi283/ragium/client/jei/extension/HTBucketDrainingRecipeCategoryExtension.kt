package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.integration.jei.addFluidStack
import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.ragium.api.integration.jei.HTItemOrFluidRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.special.HTBucketDrainingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.core.RegistryAccess
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTBucketDrainingRecipeCategoryExtension(val manager: IIngredientManager) :
    HTItemOrFluidRecipeCategoryExtension<HTBucketDrainingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setInputFluid(recipe: HTBucketDrainingRecipe, accessor: T) {}

    override fun <T : IIngredientAcceptor<T>> setInputItem(recipe: HTBucketDrainingRecipe, accessor: T) {
        accessor
            .addItemStacks(
                manager.allItemStacks
                    .asSequence()
                    .filter(recipe::isFilledBucket)
                    .toList(),
            )
    }

    override fun <T : IIngredientAcceptor<T>> setOutputFluid(recipe: HTBucketDrainingRecipe, accessor: T) {
        accessor.addFluidStacks(true, listOf())
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTBucketDrainingRecipe,
        inputFluid: IRecipeSlotDrawable,
        inputItem: IRecipeSlotDrawable,
        outputItem: IRecipeSlotDrawable,
        outputFluid: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {
        val access: RegistryAccess = HiiragiCoreAPI.getActiveAccess() ?: return
        val input = HTItemAndFluidRecipeInput(
            inputItem.displayedItemStack.orElse(ItemStack.EMPTY),
            inputFluid.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).orElse(FluidStack.EMPTY),
        )

        outputItem
            .createDisplayOverrides()
            .addItemStack(recipe.assemble(input, access))
        outputFluid
            .createDisplayOverrides()
            .addFluidStack(recipe.assembleFluid(input, access))
    }
}
