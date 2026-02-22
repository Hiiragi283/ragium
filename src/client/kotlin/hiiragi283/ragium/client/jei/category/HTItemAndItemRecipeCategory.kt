package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.ragium.api.integration.jei.HTItemAndItemRecipeCategoryExtension
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeHolder

class HTItemAndItemRecipeCategory(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<RecipeHolder<HTItemAndItemRecipe>>) :
    HTProcessingRecipeCategory<HTItemAndItemRecipe>(guiHelper, recipeType) {
    companion object {
        @JvmStatic
        fun printing(guiHelper: IGuiHelper): HTItemAndItemRecipeCategory =
            HTItemAndItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PRINTING)
    }

    private val extensions: MutableMap<Class<out HTItemAndItemRecipe>, HTItemAndItemRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTItemAndItemRecipe> addExtension(extension: HTItemAndItemRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTItemAndItemRecipe> addExtension(clazz: Class<RECIPE>, extension: HTItemAndItemRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTProcessingRecipeCategory    //

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemAndItemRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.5), getPosition(1.5))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemAndItemRecipe, focuses: IFocusGroup) {
        val (recipe1: HTItemAndItemRecipe, extension: HTItemAndItemRecipeCategoryExtension<HTItemAndItemRecipe>) =
            getExtension<HTItemAndItemRecipe>(recipe) ?: return
        // inputs
        extension.setFirstInput(
            recipe1,
            builder.addInputSlot(getPosition(1.5), getPosition(1.5)).setSlotBackground(HTBackgroundType.INPUT),
        )
        extension.setSecondInput(
            recipe1,
            builder.addInputSlot(getPosition(3.5), getPosition(0.5)).setSlotBackground(HTBackgroundType.INPUT),
        )
        // output
        extension.setOutput(
            recipe1,
            builder.addOutputSlot(getPosition(5.5), getPosition(1.5)).setSlotBackground(HTBackgroundType.OUTPUT),
        )
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: RecipeHolder<HTItemAndItemRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTItemAndItemRecipe, extension: HTItemAndItemRecipeCategoryExtension<HTItemAndItemRecipe>) =
            getExtension<HTItemAndItemRecipe>(recipe.value()) ?: return
        extension.onDisplayedIngredientsUpdate(recipe1, recipeSlots[0], recipeSlots[1], recipeSlots[2], focuses)
    }

    override fun isHandled(recipe: RecipeHolder<HTItemAndItemRecipe>): Boolean = getExtension<HTItemAndItemRecipe>(recipe.value()) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTItemAndItemRecipe> getExtension(
        recipe: HTItemAndItemRecipe,
    ): Pair<RECIPE, HTItemAndItemRecipeCategoryExtension<RECIPE>>? {
        val recipe1: RECIPE = recipe as? RECIPE ?: return null
        val extension: HTItemAndItemRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTItemAndItemRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTItemAndItemRecipe>, extension: HTItemAndItemRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTItemAndItemRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return recipe1 to extension
    }
}
