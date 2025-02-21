package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTBreweryRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTBreweryRecipeCategory(guiHelper: IGuiHelper) :
    HTMachineRecipeCategory<HTBreweryRecipe>(guiHelper, HTMachineType.ALCHEMICAL_BREWERY, 4.5) {
    override fun getRecipeType(): RecipeType<RecipeHolder<HTBreweryRecipe>> = RagiumJEIRecipeTypes.BREWERY

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTBreweryRecipe) {
        // Water Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(HTBreweryRecipe.WATER_INGREDIENT)
        // Item Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.firstInput)

        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.secondInput)

        builder
            .addInputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.thirdInput.getOrNull())
        // Item Output
        builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(
                ItemStack(Items.POTION).apply {
                    set(
                        DataComponents.POTION_CONTENTS,
                        PotionContents(recipe.potion),
                    )
                },
            )
    }

    override fun getWidth(): Int = 18 * 7 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
