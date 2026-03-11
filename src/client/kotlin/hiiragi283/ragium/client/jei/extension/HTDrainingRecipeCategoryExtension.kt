package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.integration.jei.addFluidStack
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.util.getOrEmpty
import hiiragi283.ragium.api.integration.jei.HTItemOrFluidRecipeCategoryExtension
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.core.RegistryAccess
import net.minecraft.world.item.ItemStack

class HTDrainingRecipeCategoryExtension<RECIPE : HTItemOrFluidRecipe>(
    private val manager: IIngredientManager,
    private val outputAmount: Long,
    private val inputFilter: (ItemStack) -> Boolean,
) : HTItemOrFluidRecipeCategoryExtension<RECIPE> {
    override fun <T : IIngredientAcceptor<T>> setInputFluid(recipe: RECIPE, accessor: T) {}

    override fun <T : IIngredientAcceptor<T>> setInputItem(recipe: RECIPE, accessor: T) {
        accessor
            .addItemStacks(
                manager.allItemStacks
                    .asSequence()
                    .filter(inputFilter)
                    .toList(),
            )
    }

    override fun <T : IIngredientAcceptor<T>> setOutputFluid(recipe: RECIPE, accessor: T) {
        if (accessor is IRecipeSlotBuilder) {
            accessor.setFluidRenderer(outputAmount, false, 16, 16)
        }
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        inputFluid: IRecipeSlotDrawable,
        inputItem: IRecipeSlotDrawable,
        outputItem: IRecipeSlotDrawable,
        outputFluid: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {
        val access: RegistryAccess = HiiragiCoreAPI.getActiveAccess() ?: return
        val input = HTItemAndFluidRecipeInput(
            inputItem.displayedItemStack.getOrEmpty(),
            inputFluid.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).getOrEmpty(),
        )
        if (input.isEmpty) return

        outputItem
            .createDisplayOverrides()
            .addItemStack(recipe.assemble(input, access))
        outputFluid
            .createDisplayOverrides()
            .addFluidStack(recipe.assembleFluid(input, access))
    }
}
