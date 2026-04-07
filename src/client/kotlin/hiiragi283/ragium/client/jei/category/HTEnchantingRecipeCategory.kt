package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.ragium.api.integration.jei.HTEnchantingRecipeCategoryExtension
import hiiragi283.ragium.api.recipe.HTEnchantingRecipe
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTEnchantingRecipeCategory(guiHelper: IGuiHelper) :
    HTLookupRecipeCategory<HTEnchantingRecipe>(guiHelper, RagiumJeiRecipeTypes.ENCHANTING) {
    private val extensions: MutableMap<Class<out HTEnchantingRecipe>, HTEnchantingRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTEnchantingRecipe> addExtension(extension: HTEnchantingRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTEnchantingRecipe> addExtension(clazz: Class<RECIPE>, extension: HTEnchantingRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTLookupRecipeCategory    //

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTEnchantingRecipe, focuses: IFocusGroup) {
        val (recipe1: HTEnchantingRecipe, extension: HTEnchantingRecipeCategoryExtension<HTEnchantingRecipe>) =
            getExtension<HTEnchantingRecipe>(recipe) ?: return
        // inputs
        extension.setExpInput(
            recipe1,
            builder.addInputSlot(getPosition(0), getPosition(0)).setSlotBackground(HTBackgroundType.EXTRA_INPUT),
        )
        extension.setBookInput(
            recipe1,
            builder.addInputSlot(getPosition(1.5), getPosition(0)).setSlotBackground(HTBackgroundType.EXTRA_INPUT),
        )
        extension.setItemInput(
            recipe1,
            builder.addInputSlot(getPosition(3.5), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT),
        )
        // output
        extension.setOutput(
            recipe1,
            builder.addOutputSlot(getPosition(6.5), getPosition(0)).setSlotBackground(HTBackgroundType.OUTPUT),
        )
    }

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HTEnchantingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(4.75), getPosition(0))
        builder.addRecipePlus(getPosition(2.5))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HTEnchantingRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTEnchantingRecipe, extension: HTEnchantingRecipeCategoryExtension<HTEnchantingRecipe>) =
            getExtension<HTEnchantingRecipe>(recipe.recipe) ?: return
        extension.onDisplayedIngredientsUpdate(recipe1, recipeSlots[0], recipeSlots[1], recipeSlots[2], recipeSlots[3], focuses)
    }

    override fun isHandled(recipe: HTRecipeHolder<HTEnchantingRecipe>): Boolean = getExtension<HTEnchantingRecipe>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTEnchantingRecipe> getExtension(
        recipe: HTEnchantingRecipe,
    ): Pair<RECIPE, HTEnchantingRecipeCategoryExtension<RECIPE>>? {
        val extension: HTEnchantingRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTEnchantingRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTEnchantingRecipe>, extension: HTEnchantingRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTEnchantingRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
