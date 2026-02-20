package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.type.HTJeiRecipeType
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import hiiragi283.ragium.common.recipe.HTBendingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTLathingRecipe
import hiiragi283.ragium.common.recipe.HTWiringRecipe
import hiiragi283.ragium.common.recipe.base.HTSingleProcessingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeHolder

class HTItemToItemRecipeCategory<RECIPE : HTSingleProcessingRecipe.ItemToItem>(
    guiHelper: IGuiHelper,
    recipeType: HTJeiRecipeType<RecipeHolder<RECIPE>>,
) : HTProcessingRecipeCategory<RECIPE>(guiHelper, recipeType) {
    companion object {
        @JvmStatic
        fun bending(guiHelper: IGuiHelper): HTItemToItemRecipeCategory<HTBendingRecipe> =
            HTItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.BENDING)

        @JvmStatic
        fun compressing(guiHelper: IGuiHelper): HTItemToItemRecipeCategory<HTCompressingRecipe> =
            HTItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.COMPRESSING)

        @JvmStatic
        fun lathing(guiHelper: IGuiHelper): HTItemToItemRecipeCategory<HTLathingRecipe> =
            HTItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.LATHING)

        @JvmStatic
        fun wiring(guiHelper: IGuiHelper): HTItemToItemRecipeCategory<HTWiringRecipe> =
            HTItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.WIRING)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.5), getPosition(1))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(1.5), getPosition(0.5))
            .addItemIngredient(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(5.5), getPosition(1))
            .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }
}
