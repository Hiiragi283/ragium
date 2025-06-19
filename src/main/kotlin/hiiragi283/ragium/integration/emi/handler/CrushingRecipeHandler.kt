package hiiragi283.ragium.integration.emi.handler

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler
import hiiragi283.ragium.common.inventory.HTCrusherMenu
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.world.inventory.Slot

object CrushingRecipeHandler : StandardRecipeHandler<HTCrusherMenu> {
    override fun getInputSources(handler: HTCrusherMenu): List<Slot> = buildList {
        // Crusher Input
        add(handler.getSlot(0))
        // Player Inventory
        for (i in (0 until 36)) {
            add(handler.getSlot(i + 9))
        }
    }

    override fun getCraftingSlots(handler: HTCrusherMenu): List<Slot> = listOf(handler.getSlot(0))

    override fun supportsRecipe(recipe: EmiRecipe): Boolean = recipe.category == RagiumEmiCategories.CRUSHING
}
