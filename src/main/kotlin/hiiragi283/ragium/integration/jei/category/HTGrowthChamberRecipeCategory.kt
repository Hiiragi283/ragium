package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.recipe.HTGrowthChamberRecipe
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addFluidStack
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack

class HTGrowthChamberRecipeCategory(val guiHelper: IGuiHelper) : HTRecipeCategory<HTGrowthChamberRecipe> {
    override fun getRecipeType(): RecipeType<HTGrowthChamberRecipe> = RagiumJEIRecipeTypes.GROWTH_CHAMBER

    override fun getTitle(): Component = RagiumMachineKeys.GROWTH_CHAMBER.text

    override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(RagiumMachineKeys.GROWTH_CHAMBER.getBlock())

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTGrowthChamberRecipe, focuses: IFocusGroup) {
        // Seed
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.seed)
        // Soil
        builder
            .addInputSlot(getPosition(0), getPosition(1))
            .setStandardSlotBackground()
            .addIngredients(recipe.soil)
        // Water
        if (recipe.waterAmount > 0) {
            builder
                .addInputSlot(getPosition(1), getPosition(1))
                .setStandardSlotBackground()
                .addFluidStack(FluidStack(Fluids.WATER, recipe.waterAmount))
        }
        // Crop
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(recipe.crop)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTGrowthChamberRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.5), getPosition(0))
    }

    override fun getWidth(): Int = 18 * 4 + 8

    override fun getHeight(): Int = 18 * 2 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTGrowthChamberRecipe> =
        HTGrowthChamberRecipe.CODEC.codec()
}
