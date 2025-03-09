package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.idOrNull
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.entry.HTHammerDropEntry
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

class HTHammerDropCategory(val guiHelper: IGuiHelper) : HTRecipeCategory<HTHammerDropEntry> {
    override fun getRecipeType(): RecipeType<HTHammerDropEntry> = RagiumJEIRecipeTypes.HAMMER_DROP

    override fun getTitle(): Component = Component.literal("Hammer Drops")

    override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(RagiumItems.RAGI_ALLOY_TOOLS.hammerItem)

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTHammerDropEntry, focuses: IFocusGroup) {
        // Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addItemLike(recipe.input.value())
        // Item Output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(recipe.output.output.get())
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTHammerDropEntry, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.5), getPosition(0))
    }

    override fun getWidth(): Int = 18 * 5 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getRegistryName(recipe: HTHammerDropEntry): ResourceLocation? = recipe.input.idOrNull

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTHammerDropEntry> = HTHammerDropEntry.CODEC
}
