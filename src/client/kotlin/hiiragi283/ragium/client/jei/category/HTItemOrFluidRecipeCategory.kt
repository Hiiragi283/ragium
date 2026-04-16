package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.impl.recipe.HTBasicItemOrFluidRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeSerializer

open class HTItemOrFluidRecipeCategory<RECIPE : HTBasicItemOrFluidRecipe>(
    guiHelper: IGuiHelper,
    recipeType: HTHolderRecipeViewerType<RECIPE>,
    serializer: RecipeSerializer<RECIPE>,
) : HTHolderRecipeCategory.Registered<RECIPE>(guiHelper, recipeType, serializer) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidIngredient(recipe.ingredient.getRight())
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addItemIngredient(recipe.ingredient.getLeft())
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addItemResult(recipe.result.getLeft())
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(7), getPosition(0))
            .addFluidResult(recipe.result.getRight())
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(6))
    }
}
