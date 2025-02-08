package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.recipe.HTGrinderRecipe
import hiiragi283.ragium.api.recipe.base.HTChancedItemStack
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class HTGrinderRecipeCategory(val guiHelper: IGuiHelper) : HTRecipeCategory<HTGrinderRecipe> {
    override fun getRecipeType(): RecipeType<HTGrinderRecipe> = RagiumJEIRecipeTypes.GRINDER

    override fun getTitle(): Component = RagiumMachineKeys.GRINDER.text

    override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(RagiumMachineKeys.GRINDER.getBlock())

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTGrinderRecipe, focuses: IFocusGroup) {
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.input)
        // Item Output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(recipe.getItemOutput())
        // Second Item Output
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(recipe.secondOutput.map(HTChancedItemStack::toStack).orElse(ItemStack.EMPTY))
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTGrinderRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.5), getPosition(0))
    }

    override fun getWidth(): Int = 18 * 5 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTGrinderRecipe> = HTGrinderRecipe.CODEC.codec()
}
