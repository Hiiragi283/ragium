package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.type.HTJeiRecipeType
import hiiragi283.ragium.api.integration.jei.HTItemOrFluidRecipeCategoryExtension
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
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
class HTItemOrFluidRecipeCategory(guiHelper: IGuiHelper, recipeType: HTJeiRecipeType<RecipeHolder<HTItemOrFluidRecipe>>) :
    HTProcessingRecipeCategory<HTItemOrFluidRecipe>(guiHelper, recipeType) {
    companion object {
        @JvmStatic
        fun canning(guiHelper: IGuiHelper): HTItemOrFluidRecipeCategory =
            HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CANNING)

        @JvmStatic
        fun freezing(guiHelper: IGuiHelper): HTItemOrFluidRecipeCategory =
            HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.FREEZING)

        @JvmStatic
        fun melting(guiHelper: IGuiHelper): HTItemOrFluidRecipeCategory =
            HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.MELTING)

        @JvmStatic
        fun pyrolyzing(guiHelper: IGuiHelper): HTItemOrFluidRecipeCategory =
            HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PYROLYZING)
    }

    private val extensions: MutableMap<Class<out HTItemOrFluidRecipe>, HTItemOrFluidRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTItemOrFluidRecipe> addExtension(extension: HTItemOrFluidRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTItemOrFluidRecipe> addExtension(clazz: Class<RECIPE>, extension: HTItemOrFluidRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTProcessingRecipeCategory    //

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemOrFluidRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(1))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemOrFluidRecipe, focuses: IFocusGroup) {
        val (recipe1: HTItemOrFluidRecipe, extension: HTItemOrFluidRecipeCategoryExtension<HTItemOrFluidRecipe>) =
            getExtension<HTItemOrFluidRecipe>(recipe) ?: return

        extension.setInputFluid(
            recipe1,
            builder.addInputSlot(getPosition(0), getPosition(0)).setTankBackground(HTBackgroundType.EXTRA_INPUT),
        )
        extension.setInputItem(
            recipe1,
            builder.addInputSlot(getPosition(1.5), getPosition(0.5)).setSlotBackground(HTBackgroundType.INPUT),
        )
        extension.setOutputItem(
            recipe1,
            builder.addOutputSlot(getPosition(5), getPosition(1)).setSlotBackground(HTBackgroundType.OUTPUT),
        )
        extension.setOutputFluid(
            recipe1,
            builder.addOutputSlot(getPosition(6.5), getPosition(0)).setTankBackground(HTBackgroundType.EXTRA_OUTPUT),
        )
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: RecipeHolder<HTItemOrFluidRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTItemOrFluidRecipe, extension: HTItemOrFluidRecipeCategoryExtension<HTItemOrFluidRecipe>) = getExtension<HTItemOrFluidRecipe>(
            recipe.value(),
        )
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

    override fun isHandled(recipe: RecipeHolder<HTItemOrFluidRecipe>): Boolean = getExtension<HTItemOrFluidRecipe>(recipe.value()) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTItemOrFluidRecipe> getExtension(
        recipe: HTItemOrFluidRecipe,
    ): Pair<RECIPE, HTItemOrFluidRecipeCategoryExtension<RECIPE>>? {
        val recipe1: RECIPE = recipe as? RECIPE ?: return null
        val extension = (extensions[recipe::class.java] as? HTItemOrFluidRecipeCategoryExtension<RECIPE>) ?: run {
            for ((clazz: Class<out HTItemOrFluidRecipe>, extension: HTItemOrFluidRecipeCategoryExtension<*>) in extensions) {
                if (clazz.isInstance(recipe)) {
                    return@run extension as? HTItemOrFluidRecipeCategoryExtension<RECIPE>
                }
            }
            null
        } ?: return null
        return recipe1 to extension
    }
}
