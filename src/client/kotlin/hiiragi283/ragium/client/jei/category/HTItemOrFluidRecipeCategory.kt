package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.ragium.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.ragium.client.jei.extension.HTItemOrFluidRecipeCategoryExtension
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

/**
 * @see mezz.jei.api.recipe.category.extensions.vanilla.smithing.IExtendableSmithingRecipeCategory
 */
class HTItemOrFluidRecipeCategory(guiHelper: IGuiHelper, recipeType: HTLookupRecipeViewerType<*, HTItemOrFluidRecipe>) :
    HTLookupRecipeCategory<HTItemOrFluidRecipe>(guiHelper, recipeType) {
    private val extensions: MutableMap<Class<out HTItemOrFluidRecipe>, HTItemOrFluidRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTItemOrFluidRecipe> addExtension(extension: HTItemOrFluidRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTItemOrFluidRecipe> addExtension(clazz: Class<RECIPE>, extension: HTItemOrFluidRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTProcessingRecipeCategory    //

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemOrFluidRecipe, focuses: IFocusGroup) {
        val (recipe1: HTItemOrFluidRecipe, extension: HTItemOrFluidRecipeCategoryExtension<HTItemOrFluidRecipe>) =
            getExtension<HTItemOrFluidRecipe>(recipe) ?: return

        extension.setInputFluid(
            recipe1,
            builder.addInputSlot(getPosition(0), getPosition(0)).setSlotBackground(HTBackgroundType.EXTRA_INPUT),
        )
        extension.setInputItem(
            recipe1,
            builder.addInputSlot(getPosition(2), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT),
        )
        extension.setOutputItem(
            recipe1,
            builder.addOutputSlot(getPosition(5), getPosition(0)).setSlotBackground(HTBackgroundType.OUTPUT),
        )
        extension.setOutputFluid(
            recipe1,
            builder.addOutputSlot(getPosition(7), getPosition(0)).setSlotBackground(HTBackgroundType.EXTRA_OUTPUT),
        )
    }

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HTItemOrFluidRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(6))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HTItemOrFluidRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTItemOrFluidRecipe, extension: HTItemOrFluidRecipeCategoryExtension<HTItemOrFluidRecipe>) =
            getExtension<HTItemOrFluidRecipe>(recipe.recipe) ?: return
        extension.onDisplayedIngredientsUpdate(
            recipe1,
            recipeSlots[0],
            recipeSlots[1],
            recipeSlots[2],
            recipeSlots[3],
            focuses,
        )
    }

    override fun isHandled(recipe: HTRecipeHolder<HTItemOrFluidRecipe>): Boolean = getExtension<HTItemOrFluidRecipe>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTItemOrFluidRecipe> getExtension(
        recipe: HTItemOrFluidRecipe,
    ): Pair<RECIPE, HTItemOrFluidRecipeCategoryExtension<RECIPE>>? {
        val extension: HTItemOrFluidRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTItemOrFluidRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTItemOrFluidRecipe>, extension: HTItemOrFluidRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTItemOrFluidRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
