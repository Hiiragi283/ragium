package hiiragi283.ragium.client.jei.extension

import hiiragi283.ragium.api.integration.jei.HTTankInteractingRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.HTSimpleTankInteractingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.neoforge.NeoForgeTypes
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

data object HTSimpleTankInteractionRecipeCategoryExtension : HTTankInteractingRecipeCategoryExtension<HTSimpleTankInteractingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setEmptyContainer(recipe: HTSimpleTankInteractingRecipe, accessor: T) {
        accessor.addItemStack(recipe.emptyContainer.toStack())
    }

    override fun <T : IIngredientAcceptor<T>> setFilledContainer(recipe: HTSimpleTankInteractingRecipe, accessor: T) {
        accessor.addItemStack(recipe.filledContainer.toStack())
    }

    override fun <T : IIngredientAcceptor<T>> setFluid(recipe: HTSimpleTankInteractingRecipe, accessor: T) {
        val fluids: MutableSet<Holder<Fluid>> = recipe.fluidTag
            .map(BuiltInRegistries.FLUID::getTagOrEmpty)
            .orElseGet { mutableListOf() }
            .toMutableSet()
        fluids += recipe.fluid.getHolder(BuiltInRegistries.FLUID::getHolderOrThrow)
        accessor.addIngredients(NeoForgeTypes.FLUID_STACK, fluids.map { FluidStack(it, recipe.amount) })
    }

    override fun getTankCapacity(recipe: HTSimpleTankInteractingRecipe): Long = recipe.amount.toLong()
}
