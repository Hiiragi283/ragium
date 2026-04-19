package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.api.integration.jei.setTankRenderer
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.common.recipe.viewer.HTViewerEnchantingRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.config.RagiumConfig
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTEnchantingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<HTViewerEnchantingRecipe>(guiHelper, RagiumRecipeViewerTypes.ENCHANTING, HTViewerEnchantingRecipe.CODEC.codec) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTViewerEnchantingRecipe, focuses: IFocusGroup) {
        // inputs
        val requiredExpAmount: Int = recipe.requiredExpAmount
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidIngredient(HTIngredientCreator.create(HCFluids.EXPERIENCE, requiredExpAmount))
            .setTankRenderer(RagiumConfig.COMMON.machine.tankCapacity)
            .setTankBackground(HTBackgroundType.INPUT)

        builder
            .addInputSlot(getPosition(2), getPosition(1))
            .addItemStacks(recipe.supportedItems)
            .setSlotBackground(HTBackgroundType.INPUT)

        builder
            .addInputSlot(getPosition(4), getPosition(1))
            .addItemIngredient(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        // output
        builder
            .addOutputSlot(getPosition(7), getPosition(1))
            .addItemStack(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTViewerEnchantingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(5.25), getPosition(1))
        builder.addRecipePlus(getPosition(1), getPosition(1))
        builder.addRecipePlus(getPosition(3), getPosition(1))
    }
}
