package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.HTJeiHolderRecipeType
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import hiiragi283.ragium.common.recipe.HTCanningRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.base.HTItemOrFluidRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTItemOrFluidRecipeCategory<RECIPE : HTItemOrFluidRecipe>(guiHelper: IGuiHelper, recipeType: HTJeiHolderRecipeType<RECIPE>) :
    HTProcessingRecipeCategory<RECIPE>(guiHelper, recipeType) {
    companion object {
        @JvmStatic
        fun canning(guiHelper: IGuiHelper): HTItemOrFluidRecipeCategory<HTCanningRecipe> =
            HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CANNING)

        @JvmStatic
        fun freezing(guiHelper: IGuiHelper): HTItemOrFluidRecipeCategory<HTFreezingRecipe> =
            HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.FREEZING)

        @JvmStatic
        fun melting(guiHelper: IGuiHelper): HTItemOrFluidRecipeCategory<HTMeltingRecipe> =
            HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.MELTING)

        @JvmStatic
        fun pyrolyzing(guiHelper: IGuiHelper): HTItemOrFluidRecipeCategory<HTPyrolyzingRecipe> =
            HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PYROLYZING)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(1))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // inputs
        val (itemIn: HTItemIngredient?, fluidIn: HTFluidIngredient?) = recipe.ingredient.toPair()
        builder
            .addFluidSlot(getPosition(0), getPosition(0), true, fluidIn)
            .setTankBackground(HTBackgroundType.EXTRA_INPUT)
        builder
            .addItemSlot(getPosition(1.5), getPosition(0.5), itemIn)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        val (itemOut: HTItemResult?, fluidOut: HTFluidResult?) = recipe.result.toPair()
        builder
            .addFluidSlot(getPosition(6.5), getPosition(0), true, fluidOut)
            .setTankBackground(HTBackgroundType.EXTRA_OUTPUT)
        builder
            .addItemSlot(getPosition(5), getPosition(1), itemOut)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }
}
