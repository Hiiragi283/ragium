package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import hiiragi283.ragium.integration.jei.entry.HTGeneratorFuelEntry
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTGeneratorFuelCategory(val guiHelper: IGuiHelper) : HTRecipeCategory<HTGeneratorFuelEntry> {
    override fun getRecipeType(): RecipeType<HTGeneratorFuelEntry> = RagiumJEIRecipeTypes.GENERATOR

    override fun getTitle(): Component = Component.literal("Generator Fuels")

    override fun getIcon(): IDrawable? = guiHelper.recipeFlameFilled

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTGeneratorFuelEntry, focuses: IFocusGroup) {
        // Generator Icon
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addItemLike(recipe.machine.getBlock())
        // Fluid Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(SizedFluidIngredient.of(recipe.fuelTag, recipe.amount))
        // Item Output
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTGeneratorFuelEntry, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(2.5), getPosition(0))
    }

    override fun getWidth(): Int = 18 * 5 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTGeneratorFuelEntry> =
        HTGeneratorFuelEntry.CODEC
}
