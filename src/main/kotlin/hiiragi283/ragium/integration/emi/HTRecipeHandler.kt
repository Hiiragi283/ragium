package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler
import hiiragi283.ragium.api.inventory.HTContainerMenu
import net.minecraft.world.inventory.Slot

open class HTRecipeHandler<T : HTContainerMenu>(private val category: EmiRecipeCategory) : StandardRecipeHandler<T> {
    override fun getInputSources(handler: T): List<Slot> = buildList {
        // Inputs
        addAll(getCraftingSlots(handler))
        // Player Inventory
        for (i in (0 until 36)) {
            add(handler.getSlot(i + handler.playerStartIndex))
        }
    }

    override fun getCraftingSlots(handler: T): List<Slot> = handler.inputSlots.map(handler::getSlot)

    override fun supportsRecipe(recipe: EmiRecipe): Boolean = recipe.category == category
}
