package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.ragium.api.integration.jei.HTTankInteractingRecipeCategoryExtension
import hiiragi283.ragium.api.recipe.HTTankInteractingRecipe
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import net.minecraft.world.item.crafting.RecipeHolder

class HTTankInteractionRecipeCategory(guiHelper: IGuiHelper) :
    HTLookupRecipeCategory.Managed<HTTankInteractingRecipe>(guiHelper, RagiumJeiRecipeTypes.TANK_INTERACTION) {
    private val extensions: MutableMap<Class<out HTTankInteractingRecipe>, HTTankInteractingRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTTankInteractingRecipe> addExtension(extension: HTTankInteractingRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTTankInteractingRecipe> addExtension(clazz: Class<RECIPE>, extension: HTTankInteractingRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTLookupRecipeCategory    //

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTTankInteractingRecipe, focuses: IFocusGroup) {
        val (recipe1: HTTankInteractingRecipe, extension: HTTankInteractingRecipeCategoryExtension<HTTankInteractingRecipe>) =
            getExtension<HTTankInteractingRecipe>(recipe) ?: return
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
        recipe: RecipeHolder<HTTankInteractingRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTTankInteractingRecipe, extension: HTTankInteractingRecipeCategoryExtension<HTTankInteractingRecipe>) =
            getExtension<HTTankInteractingRecipe>(recipe.value()) ?: return
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

    override fun isHandled(recipe: RecipeHolder<HTTankInteractingRecipe>): Boolean =
        getExtension<HTTankInteractingRecipe>(recipe.value()) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTTankInteractingRecipe> getExtension(
        recipe: HTTankInteractingRecipe,
    ): Pair<RECIPE, HTTankInteractingRecipeCategoryExtension<RECIPE>>? {
        val extension: HTTankInteractingRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTTankInteractingRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTTankInteractingRecipe>, extension: HTTankInteractingRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTTankInteractingRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
