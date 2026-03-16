package hiiragi283.ragium.client.jei.extension

import hiiragi283.ragium.api.integration.jei.HTTankInteractionCategoryExtension
import hiiragi283.ragium.common.data.tank.HTSimpleTankInteraction
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.neoforge.NeoForgeTypes
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

data object HTSimpleTankInteractionCategoryExtension : HTTankInteractionCategoryExtension<HTSimpleTankInteraction> {
    override fun <T : IIngredientAcceptor<T>> setEmptyContainer(recipe: HTSimpleTankInteraction, accessor: T) {
        accessor.addItemStack(recipe.emptyContainer.toStack())
    }

    override fun <T : IIngredientAcceptor<T>> setFilledContainer(recipe: HTSimpleTankInteraction, accessor: T) {
        accessor.addItemStack(recipe.filledContainer.toStack())
    }

    override fun <T : IIngredientAcceptor<T>> setFluid(recipe: HTSimpleTankInteraction, accessor: T) {
        val fluids: MutableSet<Holder<Fluid>> = recipe.fluidTag
            .map(BuiltInRegistries.FLUID::getTagOrEmpty)
            .orElseGet { mutableListOf() }
            .toMutableSet()
        fluids += recipe.fluid.getHolder(BuiltInRegistries.FLUID::getHolderOrThrow)
        accessor.addIngredients(NeoForgeTypes.FLUID_STACK, fluids.map { FluidStack(it, recipe.amount) })
    }
}
