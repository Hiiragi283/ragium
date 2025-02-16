package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTSolidifierRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import hiiragi283.ragium.integration.jei.addItemOutput
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.Ingredient

class HTSolidifierRecipeCategory(guiHelper: IGuiHelper) :
    HTMachineRecipeCategory<HTSolidifierRecipe>(guiHelper, HTMachineType.SOLIDIFIER, 2.5) {
    override fun getRecipeType(): RecipeType<HTSolidifierRecipe> = RagiumJEIRecipeTypes.SOLIDIFIER

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTSolidifierRecipe, focuses: IFocusGroup) {
        // Fluid Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.input)
        // Catalyst
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.catalyst.orElse(Ingredient.of()))
        // Item Output
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addItemOutput(recipe, 0)
    }

    override fun getWidth(): Int = 18 * 5 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTSolidifierRecipe> =
        HTSolidifierRecipe.CODEC.codec()
}
