package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler
import hiiragi283.ragium.api.inventory.container.HTContainerMenu
import net.minecraft.world.inventory.Slot

open class HTRecipeHandler<T : HTContainerMenu>(private val categories: List<EmiRecipeCategory>) : StandardRecipeHandler<T> {
    constructor(vararg categories: EmiRecipeCategory) : this(categories.toList())

    override fun getInputSources(handler: T): List<Slot> = when {
        handler.inputSlots.isEmpty() -> listOf()
        else -> buildList {
            // Inputs
            addAll(getCraftingSlots(handler))
            // Player Inventory
            for (i: Int in (0 until 36)) {
                add(handler.getSlot(i + handler.playerStartIndex))
            }
        }
    }

    override fun getCraftingSlots(handler: T): List<Slot> = handler.inputSlots.mapNotNull(handler::getSlot)

    override fun supportsRecipe(recipe: EmiRecipe): Boolean = recipe.category in categories
}
