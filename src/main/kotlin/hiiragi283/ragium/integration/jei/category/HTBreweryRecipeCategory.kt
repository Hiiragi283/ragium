package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTBreweryRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import hiiragi283.ragium.integration.jei.addItemOutput
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import kotlin.jvm.optionals.getOrNull

class HTBreweryRecipeCategory(guiHelper: IGuiHelper) :
    HTMachineRecipeCategory<HTBreweryRecipe>(guiHelper, HTMachineType.ALCHEMICAL_BREWERY, 4.5) {
    override fun getRecipeType(): RecipeType<HTBreweryRecipe> = RagiumJEIRecipeTypes.BREWERY

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTBreweryRecipe, focuses: IFocusGroup) {
        // Water Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(HTBreweryRecipe.WATER_INGREDIENT)
        // Item Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.firstInput)

        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.secondInput)

        builder
            .addInputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.thirdInput.getOrNull())
        // Item Output
        builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .setStandardSlotBackground()
            .addItemOutput(recipe, 0)
    }

    override fun getWidth(): Int = 18 * 7 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTBreweryRecipe> = HTBreweryRecipe.CODEC.codec()
}
