package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.entry.HTSoapEntry
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component

class HTSoapCategory(val guiHelper: IGuiHelper) : HTRecipeCategory<HTSoapEntry> {
    override fun getRecipeType(): RecipeType<HTSoapEntry> = RagiumJEIRecipeTypes.SOAP

    override fun getTitle(): Component = Component.literal("Soap Washing")

    override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(RagiumItems.SOAP)

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTSoapEntry, focuses: IFocusGroup) {
        // Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addItemLike(recipe.input.value())
        // Item Output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addItemLike(recipe.output.block)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTSoapEntry, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.5), getPosition(0))
    }

    override fun getWidth(): Int = 18 * 4 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTSoapEntry> = HTSoapEntry.CODEC
}
