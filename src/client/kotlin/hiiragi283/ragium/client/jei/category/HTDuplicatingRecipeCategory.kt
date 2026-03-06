package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.api.integration.jei.HTDuplicatingRecipeCategoryExtension
import hiiragi283.ragium.api.recipe.HTDuplicatingRecipe
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

class HTDuplicatingRecipeCategory(guiHelper: IGuiHelper) :
    HTProcessingRecipeCategory<HTDuplicatingRecipe>(guiHelper, RagiumJeiRecipeTypes.DUPLICATING) {
    private val extensions: MutableMap<Class<out HTDuplicatingRecipe>, HTDuplicatingRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTDuplicatingRecipe> addExtension(extension: HTDuplicatingRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTDuplicatingRecipe> addExtension(clazz: Class<RECIPE>, extension: HTDuplicatingRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTProcessingRecipeCategory    //

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTDuplicatingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlusSign().setPosition(getPosition(1) + 2, getPosition(0) + 2)
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTDuplicatingRecipe, focuses: IFocusGroup) {
        val (recipe1: HTDuplicatingRecipe, extension: HTDuplicatingRecipeCategoryExtension<HTDuplicatingRecipe>) =
            getExtension<HTDuplicatingRecipe>(recipe) ?: return
        // inputs
        extension.setInput(
            recipe1,
            builder.addInputSlot(getPosition(2), getPosition(0)).setSlotBackground(HTBackgroundType.NONE),
        )
        extension.setRequiredMatter(
            recipe1,
            builder.addInputSlot(getPosition(0), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT),
        )
        // outputs
        extension.setInput(
            recipe1,
            builder.addOutputSlot(getPosition(5), getPosition(0)).setSlotBackground(HTBackgroundType.OUTPUT),
        )
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: RecipeHolder<HTDuplicatingRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTDuplicatingRecipe, extension: HTDuplicatingRecipeCategoryExtension<HTDuplicatingRecipe>) =
            getExtension<HTDuplicatingRecipe>(recipe.value()) ?: return
        // input
        val inputSlot: IRecipeSlotDrawable = recipeSlots[0]
        extension.onDisplayedIngredientsUpdate(recipe1, inputSlot, recipeSlots[1], focuses)
        // output
        inputSlot.displayedItemStack.map(recipeSlots[2].createDisplayOverrides()::addItemStack)
    }

    override fun isHandled(recipe: RecipeHolder<HTDuplicatingRecipe>): Boolean = getExtension<HTDuplicatingRecipe>(recipe.value()) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTDuplicatingRecipe> getExtension(
        recipe: HTDuplicatingRecipe,
    ): Pair<RECIPE, HTDuplicatingRecipeCategoryExtension<RECIPE>>? {
        val extension: HTDuplicatingRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTDuplicatingRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTDuplicatingRecipe>, extension: HTDuplicatingRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTDuplicatingRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
