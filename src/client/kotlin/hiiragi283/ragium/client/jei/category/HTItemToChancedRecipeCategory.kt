package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.ragium.api.integration.jei.HTItemToChancedRecipeCategoryExtension
import hiiragi283.ragium.api.recipe.HTItemToChancedRecipe
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeHolder

class HTItemToChancedRecipeCategory(guiHelper: IGuiHelper, recipeType: HTHolderRecipeViewerType<*, HTItemToChancedRecipe.Serializable>) :
    HTProcessingRecipeCategory<HTItemToChancedRecipe.Serializable>(guiHelper, recipeType) {
    companion object {
        @JvmStatic
        fun crushing(guiHelper: IGuiHelper): HTItemToChancedRecipeCategory =
            HTItemToChancedRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CRUSHING)

        @JvmStatic
        fun cutting(guiHelper: IGuiHelper): HTItemToChancedRecipeCategory =
            HTItemToChancedRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CUTTING)
    }

    private val extensions: MutableMap<Class<out HTItemToChancedRecipe>, HTItemToChancedRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTItemToChancedRecipe> addExtension(extension: HTItemToChancedRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTItemToChancedRecipe> addExtension(clazz: Class<RECIPE>, extension: HTItemToChancedRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTProcessingRecipeCategory    //

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemToChancedRecipe.Serializable, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.5), getPosition(1))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemToChancedRecipe.Serializable, focuses: IFocusGroup) {
        val (recipe1: HTItemToChancedRecipe, extension: HTItemToChancedRecipeCategoryExtension<HTItemToChancedRecipe>) =
            getExtension<HTItemToChancedRecipe>(recipe) ?: return
        // input
        extension.setInput(
            recipe1,
            builder.addInputSlot(getPosition(1.5), getPosition(0.5)).setSlotBackground(HTBackgroundType.INPUT),
        )
        // outputs
        extension.setOutput(
            recipe1,
            builder.addOutputSlot(getPosition(5.5), getPosition(0.5)).setSlotBackground(HTBackgroundType.OUTPUT),
        )
        extension.setExtraOutput(
            recipe1,
            builder.addOutputSlot(getPosition(5.5), getPosition(2)).setSlotBackground(HTBackgroundType.EXTRA_OUTPUT),
        )
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: RecipeHolder<HTItemToChancedRecipe.Serializable>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTItemToChancedRecipe, extension: HTItemToChancedRecipeCategoryExtension<HTItemToChancedRecipe>) =
            getExtension<HTItemToChancedRecipe>(recipe.value()) ?: return
        extension.onDisplayedIngredientsUpdate(recipe1, recipeSlots[0], recipeSlots[1], recipeSlots[2], focuses)
    }

    override fun isHandled(recipe: RecipeHolder<HTItemToChancedRecipe.Serializable>): Boolean =
        getExtension<HTItemToChancedRecipe>(recipe.value()) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTItemToChancedRecipe> getExtension(
        recipe: HTItemToChancedRecipe,
    ): Pair<RECIPE, HTItemToChancedRecipeCategoryExtension<RECIPE>>? {
        val extension: HTItemToChancedRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTItemToChancedRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTItemToChancedRecipe>, extension: HTItemToChancedRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTItemToChancedRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
