package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.entry.HTDeviceOutputEntry
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class HTDeviceOutputCategory(guiHelper: IGuiHelper) : HTRecipeCategory<HTDeviceOutputEntry>(guiHelper) {
    override fun getRecipeType(): RecipeType<HTDeviceOutputEntry> = RagiumJEIRecipeTypes.DEVICE

    override fun getTitle(): Component = Component.literal("Device Output")

    override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(RagiumItems.RAGI_ALLOY_TOOLS.hammerItem)

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTDeviceOutputEntry, focuses: IFocusGroup) {
        // Device
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.device)
        // Output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .let(recipe::addOutput)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTDeviceOutputEntry, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.5), getPosition(0))
    }

    override fun getRegistryName(recipe: HTDeviceOutputEntry): ResourceLocation = recipe.id

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTDeviceOutputEntry> = HTDeviceOutputEntry.CODEC

    override fun getWidth(): Int = 18 * 4 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
