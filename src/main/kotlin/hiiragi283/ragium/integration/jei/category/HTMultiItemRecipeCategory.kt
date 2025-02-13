package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.integration.jei.addIngredients
import hiiragi283.ragium.integration.jei.addItemResult
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeSerializer
import kotlin.jvm.optionals.getOrNull

class HTMultiItemRecipeCategory<T : HTMultiItemRecipe>(
    guiHelper: IGuiHelper,
    machine: HTMachineType,
    private val recipeType: RecipeType<T>,
    private val serializer: RecipeSerializer<T>,
) : HTMachineRecipeCategory<T>(guiHelper, machine, 3.5) {
    override fun getRecipeType(): RecipeType<T> = recipeType

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: T, focuses: IFocusGroup) {
        // First Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.firstInput)
        // Second Item Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.secondInput)
        // Third Item Input
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.thirdInput.getOrNull())
        // Item Output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setStandardSlotBackground()
            .addItemResult(recipe, 0)
    }

    override fun getWidth(): Int = 18 * 6 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<T> = serializer.codec().codec()
}
