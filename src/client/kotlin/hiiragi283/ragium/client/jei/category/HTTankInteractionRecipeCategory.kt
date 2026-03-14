package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.common.recipe.HTTankInteractingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

class HTTankInteractionRecipeCategory(guiHelper: IGuiHelper) :
    HTLookupRecipeCategory.Managed<HTTankInteractingRecipe>(guiHelper, RagiumJeiRecipeTypes.TANK_INTERACTION) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTTankInteractingRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addOutputSlot()
            .setPosition(getPosition(0), getPosition(0))
            .addItemStack(recipe.emptyContainer.toStack())
            .setSlotBackground(HTBackgroundType.INPUT)

        builder
            .addOutputSlot()
            .setPosition(getPosition(4), getPosition(0))
            .addItemStack(recipe.filledContainer.toStack())
            .setSlotBackground(HTBackgroundType.INPUT)
        // fluid
        val fluids: MutableSet<Holder<Fluid>> = recipe.fluidTag
            .map(BuiltInRegistries.FLUID::getTagOrEmpty)
            .orElseGet { mutableListOf() }
            .toMutableSet()
        fluids += recipe.fluid.getHolder(BuiltInRegistries.FLUID::getHolderOrThrow)
        builder
            .addSlot(RecipeIngredientRole.RENDER_ONLY)
            .setPosition(getPosition(2), getPosition(0))
            .addIngredients(NeoForgeTypes.FLUID_STACK, fluids.map { FluidStack(it, recipe.amount) })
            .setFluidRenderer(recipe.amount.toLong(), false, 16, 18 * 3 - 2)
            .setTankBackground(HTBackgroundType.NONE)
        // output
        builder
            .addOutputSlot()
            .setPosition(getPosition(0), getPosition(2))
            .addItemStack(recipe.filledContainer.toStack())
            .setSlotBackground(HTBackgroundType.OUTPUT)

        builder
            .addOutputSlot()
            .setPosition(getPosition(4), getPosition(2))
            .addItemStack(recipe.emptyContainer.toStack())
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }
}
