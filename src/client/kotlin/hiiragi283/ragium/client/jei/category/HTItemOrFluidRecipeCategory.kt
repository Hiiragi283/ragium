package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.ragium.api.integration.jei.HTItemOrFluidRecipeCategoryExtension
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * @see mezz.jei.api.recipe.category.extensions.vanilla.smithing.IExtendableSmithingRecipeCategory
 */
class HTItemOrFluidRecipeCategory(guiHelper: IGuiHelper, recipeType: HTHolderRecipeViewerType<*, HTItemOrFluidRecipe.Serializable>) :
    HTProcessingRecipeCategory<HTItemOrFluidRecipe.Serializable>(guiHelper, recipeType) {
    private val extensions: MutableMap<Class<out HTItemOrFluidRecipe>, HTItemOrFluidRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTItemOrFluidRecipe> addExtension(extension: HTItemOrFluidRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTItemOrFluidRecipe> addExtension(clazz: Class<RECIPE>, extension: HTItemOrFluidRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTProcessingRecipeCategory    //

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemOrFluidRecipe.Serializable, focuses: IFocusGroup) {
        builder.addRecipePlusSign().setPosition(getPosition(1) + 2, getPosition(0) + 2)
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlusSign().setPosition(getPosition(7) + 2, getPosition(0) + 2)
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemOrFluidRecipe.Serializable, focuses: IFocusGroup) {
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

    override fun onDisplayedIngredientsUpdate(
        recipe: RecipeHolder<HTItemOrFluidRecipe.Serializable>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTItemOrFluidRecipe, extension: HTItemOrFluidRecipeCategoryExtension<HTItemOrFluidRecipe>) =
            getExtension<HTItemOrFluidRecipe>(recipe.value())
                ?: return
        extension.onDisplayedIngredientsUpdate(
            recipe1,
            recipeSlots[0],
            recipeSlots[1],
            recipeSlots[2],
            recipeSlots[3],
            focuses,
        )
    }

    override fun isHandled(recipe: RecipeHolder<HTItemOrFluidRecipe.Serializable>): Boolean =
        getExtension<HTItemOrFluidRecipe>(recipe.value()) != null

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
