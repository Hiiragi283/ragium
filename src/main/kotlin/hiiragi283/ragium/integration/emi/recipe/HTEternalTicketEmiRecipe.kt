package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.SlotWidget
import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.Unbreakable

class HTEternalTicketEmiRecipe(private val equipment: Item, id: ResourceLocation) :
    EmiPatternCraftingRecipe(
        listOf(EmiStack.of(equipment), EmiStack.of(RagiumItems.ETERNAL_COMPONENT)),
        EmiStack.of(equipment),
        id,
    ) {
    override fun getInputWidget(slot: Int, x: Int, y: Int): SlotWidget = SlotWidget(
        when (slot) {
            0 -> EmiStack.of(equipment)
            1 -> EmiStack.of(RagiumItems.ETERNAL_COMPONENT)
            else -> EmiStack.EMPTY
        },
        x,
        y,
    )

    override fun getOutputWidget(x: Int, y: Int): SlotWidget = SlotWidget(
        createItemStack(equipment, DataComponents.UNBREAKABLE, Unbreakable(true)).let(EmiStack::of),
        x,
        y,
    )
}
