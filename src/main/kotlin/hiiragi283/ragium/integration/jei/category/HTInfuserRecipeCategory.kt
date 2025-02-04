package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.recipe.HTInfuserRecipe
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addFluidStack
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
import kotlin.jvm.optionals.getOrNull

class HTInfuserRecipeCategory(val guiHelper: IGuiHelper) : HTRecipeCategory<HTInfuserRecipe> {
    override fun getRecipeType(): RecipeType<HTInfuserRecipe> = RagiumJEIRecipeTypes.INFUSER

    override fun getTitle(): Component = RagiumMachineKeys.INFUSER.text

    override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(RagiumMachineKeys.INFUSER.getBlock())

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTInfuserRecipe, focuses: IFocusGroup) {
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.itemInput)
        // Fluid Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.fluidInput)
        // Item Output
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(recipe.itemOutput.orElse(ItemStack.EMPTY))
        // Fluid Output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setStandardSlotBackground()
            .addFluidStack(recipe.fluidOutput.getOrNull())
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTInfuserRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(2.5), getPosition(0))
    }

    override fun getWidth(): Int = 18 * 6 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTInfuserRecipe> = HTInfuserRecipe.CODEC.codec()
}
