package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.registry.isOf
import hiiragi283.core.api.registry.toStack
import hiiragi283.ragium.api.integration.jei.HTTankInteractingRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.special.HTBucketInteractingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

data class HTBucketInteractionRecipeCategoryExtension(private val manager: IIngredientManager) :
    HTTankInteractingRecipeCategoryExtension<HTBucketInteractingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setEmptyContainer(recipe: HTBucketInteractingRecipe, accessor: T) {
        accessor.addItemLike(Items.BUCKET)
    }

    override fun <T : IIngredientAcceptor<T>> setFilledContainer(recipe: HTBucketInteractingRecipe, accessor: T) {
        recipe.fluid
            .toStack(recipe.amount)
            .let(FluidUtil::getFilledBucket)
            .let(accessor::addItemStack)
    }

    override fun <T : IIngredientAcceptor<T>> setFluid(recipe: HTBucketInteractingRecipe, accessor: T) {
        accessor.addFluidStacks(manager.getAllIngredients(NeoForgeTypes.FLUID_STACK).filter(recipe.fluid::isOf), false)
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTBucketInteractingRecipe,
        emptySlot: IRecipeSlotDrawable,
        filledSlot: IRecipeSlotDrawable,
        fluidSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {
        val fluidStack: FluidStack = fluidSlot.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).orElse(FluidStack.EMPTY)
        if (fluidStack.isEmpty) return
        val bucket: ItemStack = FluidUtil.getFilledBucket(fluidStack)
        if (bucket.isEmpty) return
        filledSlot.createDisplayOverrides().addItemStack(bucket)
    }
}
