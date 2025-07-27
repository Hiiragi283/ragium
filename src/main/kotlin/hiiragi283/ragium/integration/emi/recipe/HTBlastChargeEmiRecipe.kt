package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.GeneratedSlotWidget
import dev.emi.emi.api.widget.SlotWidget
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.Tags
import java.util.*
import kotlin.math.min

class HTBlastChargeEmiRecipe(id: ResourceLocation) :
    EmiPatternCraftingRecipe(
        listOf(EmiIngredient.of(Tags.Items.GUNPOWDERS)),
        EmiStack.of(RagiumItems.BLAST_CHARGE),
        id,
    ) {
    override fun getInputWidget(slot: Int, x: Int, y: Int): SlotWidget = when (slot) {
        0 -> SlotWidget(EmiStack.of(RagiumItems.BLAST_CHARGE), x, y)
        else -> GeneratedSlotWidget(
            { random: Random -> getGunpowders(slot, random) },
            unique,
            x,
            y,
        )
    }

    override fun getOutputWidget(x: Int, y: Int): SlotWidget = GeneratedSlotWidget(
        { random: Random ->
            val stack: ItemStack = RagiumItems.BLAST_CHARGE.toStack()
            stack.set(RagiumDataComponents.BLAST_POWER, min(1, random.nextInt(8)).toFloat())
            EmiStack.of(stack)
        },
        unique,
        x,
        y,
    )

    private fun getGunpowders(slot: Int, random: Random): EmiIngredient = when {
        slot > random.nextInt(8) -> EmiIngredient.of(Tags.Items.GUNPOWDERS)
        else -> EmiStack.EMPTY
    }
}
