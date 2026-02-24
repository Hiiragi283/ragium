package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.ragium.api.integration.jei.HTItemToItemRecipeCategoryExtension
import hiiragi283.ragium.api.recipe.HTItemToItemRecipe
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeHolder

class HTItemToItemRecipeCategory(guiHelper: IGuiHelper, recipeType: HTHolderRecipeViewerType<*, HTItemToItemRecipe.Serializable>) :
    HTProcessingRecipeCategory<HTItemToItemRecipe.Serializable>(guiHelper, recipeType) {
    companion object {
        @JvmStatic
        fun compressing(guiHelper: IGuiHelper): HTItemToItemRecipeCategory =
            HTItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.COMPRESSING)

        @JvmStatic
        fun wiring(guiHelper: IGuiHelper): HTItemToItemRecipeCategory = HTItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.WIRING)
    }

    private val extensions: MutableMap<Class<out HTItemToItemRecipe>, HTItemToItemRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTItemToItemRecipe> addExtension(extension: HTItemToItemRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTItemToItemRecipe> addExtension(clazz: Class<RECIPE>, extension: HTItemToItemRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTProcessingRecipeCategory    //

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemToItemRecipe.Serializable, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.5), getPosition(1))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemToItemRecipe.Serializable, focuses: IFocusGroup) {
        val (recipe1: HTItemToItemRecipe, extension: HTItemToItemRecipeCategoryExtension<HTItemToItemRecipe>) =
            getExtension<HTItemToItemRecipe>(recipe) ?: return
        // input
        extension.setInput(
            recipe1,
            builder.addInputSlot(getPosition(1.5), getPosition(0.5)).setSlotBackground(HTBackgroundType.INPUT),
        )
        // output
        extension.setOutput(
            recipe1,
            builder.addOutputSlot(getPosition(5.5), getPosition(1)).setSlotBackground(HTBackgroundType.OUTPUT),
        )
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: RecipeHolder<HTItemToItemRecipe.Serializable>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTItemToItemRecipe, extension: HTItemToItemRecipeCategoryExtension<HTItemToItemRecipe>) =
            getExtension<HTItemToItemRecipe>(recipe.value()) ?: return
        extension.onDisplayedIngredientsUpdate(recipe1, recipeSlots[0], recipeSlots[1], focuses)
    }

    override fun isHandled(recipe: RecipeHolder<HTItemToItemRecipe.Serializable>): Boolean =
        getExtension<HTItemToItemRecipe>(recipe.value()) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTItemToItemRecipe> getExtension(
        recipe: HTItemToItemRecipe,
    ): Pair<RECIPE, HTItemToItemRecipeCategoryExtension<RECIPE>>? {
        val extension: HTItemToItemRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTItemToItemRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTItemToItemRecipe>, extension: HTItemToItemRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTItemToItemRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
