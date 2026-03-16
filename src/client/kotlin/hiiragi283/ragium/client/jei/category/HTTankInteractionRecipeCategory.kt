package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.resource.IdToValue
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.ragium.api.data.tank.HTTankInteraction
import hiiragi283.ragium.api.integration.jei.HTTankInteractionCategoryExtension
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole

class HTTankInteractionRecipeCategory(guiHelper: IGuiHelper) :
    HTLookupRecipeCategory.Fake<HTTankInteraction>(guiHelper, RagiumJeiRecipeTypes.TANK_INTERACTION) {
    private val extensions: MutableMap<Class<out HTTankInteraction>, HTTankInteractionCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTTankInteraction> addExtension(extension: HTTankInteractionCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTTankInteraction> addExtension(clazz: Class<RECIPE>, extension: HTTankInteractionCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTLookupRecipeCategory    //

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTTankInteraction, focuses: IFocusGroup) {
        val (recipe1: HTTankInteraction, extension: HTTankInteractionCategoryExtension<HTTankInteraction>) =
            getExtension<HTTankInteraction>(recipe) ?: return
        // inputs
        extension.setEmptyContainer(
            recipe1,
            builder.addInputSlot().setPosition(getPosition(0), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT),
        )
        extension.setFilledContainer(
            recipe1,
            builder.addInputSlot().setPosition(getPosition(4), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT),
        )
        // fluid
        extension.setFluid(
            recipe1,
            builder
                .addSlot(RecipeIngredientRole.CATALYST)
                .setPosition(getPosition(2), getPosition(0))
                .setFluidRenderer(recipe1.amount.toLong(), false, 16, 18 * 3 - 2)
                .setTankBackground(HTBackgroundType.NONE),
        )
        // outputs
        extension.setFilledContainer(
            recipe1,
            builder.addOutputSlot().setPosition(getPosition(0), getPosition(2)).setSlotBackground(HTBackgroundType.OUTPUT),
        )
        extension.setEmptyContainer(
            recipe1,
            builder.addOutputSlot().setPosition(getPosition(4), getPosition(2)).setSlotBackground(HTBackgroundType.OUTPUT),
        )
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: IdToValue<HTTankInteraction>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTTankInteraction, extension: HTTankInteractionCategoryExtension<HTTankInteraction>) =
            getExtension<HTTankInteraction>(recipe.second) ?: return
        extension.onDisplayedIngredientsUpdate(
            recipe1,
            recipeSlots[0],
            recipeSlots[1],
            recipeSlots[2],
            focuses,
        )
        extension.onDisplayedIngredientsUpdate(
            recipe1,
            recipeSlots[4],
            recipeSlots[3],
            recipeSlots[2],
            focuses,
        )
    }

    override fun isHandled(recipe: IdToValue<HTTankInteraction>): Boolean = getExtension<HTTankInteraction>(recipe.second) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTTankInteraction> getExtension(
        recipe: HTTankInteraction,
    ): Pair<RECIPE, HTTankInteractionCategoryExtension<RECIPE>>? {
        val extension: HTTankInteractionCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTTankInteractionCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTTankInteraction>, extension: HTTankInteractionCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTTankInteractionCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
