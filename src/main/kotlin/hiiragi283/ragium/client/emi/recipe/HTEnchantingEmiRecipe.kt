package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.item.createEnchantedBook
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder

class HTEnchantingEmiRecipe(holder: RecipeHolder<HTEnchantingRecipe>) :
    HTEmiHolderRecipe<HTEnchantingRecipe>(RagiumEmiRecipeCategories.ENCHANTING, holder) {
    init {
        addInput(recipe.expIngredient)
        addInput(Items.BOOK.toEmi())
        addInput(recipe.ingredient)

        addOutputs(createEnchantedBook(recipe.enchantments).toEmi())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(4), getPosition(1), recipe.time)
        widgets.addBurning(getPosition(2), getPosition(1), recipe.time)
        // inputs
        widgets.addTank(input(0), getPosition(0), HTBackgroundType.INPUT)
        widgets.addSlot(input(1), getPosition(2), getPosition(0), HTBackgroundType.EXTRA_INPUT)
        widgets.addSlot(input(2), getPosition(2), getPosition(2), HTBackgroundType.INPUT)
        // output
        widgets.addSlot(output(0), getPosition(6), getPosition(1), HTBackgroundType.OUTPUT)
    }
}
