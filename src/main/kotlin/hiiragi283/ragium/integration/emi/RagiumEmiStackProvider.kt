package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.EmiStackProvider
import dev.emi.emi.api.neoforge.NeoForgeEmiStack
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.stack.EmiStackInteraction
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.screen.HTContainerScreen
import hiiragi283.ragium.api.screen.HTDefinitionContainerScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.Vec3i
import net.minecraft.world.inventory.Slot

object RagiumEmiStackProvider : EmiStackProvider<Screen> {
    override fun getStackAt(screen: Screen, x: Int, y: Int): EmiStackInteraction {
        if (screen is HTContainerScreen<*>) {
            // Get stack from slots
            for (slot: Slot in screen.menu.slots) {
                if (HTSlotHelper.isIn(x, screen.startX + slot.x, 18)) {
                    if (HTSlotHelper.isIn(y, screen.startY + slot.y, 18)) {
                        return EmiStackInteraction(EmiStack.of(slot.item), null, false)
                    }
                }
            }
            // Get stack from tanks
            if (screen is HTDefinitionContainerScreen<*>) {
                for ((index: Int, vec: Vec3i) in screen.menu.fluidSlots.entries) {
                    if (HTSlotHelper.isIn(x, screen.startX + vec.x, 18)) {
                        if (HTSlotHelper.isIn(y, screen.startY + vec.y, 18)) {
                            return EmiStackInteraction(NeoForgeEmiStack.of(screen.getFluidStack(index)), null, false)
                        }
                    }
                }
            }
        }

        return EmiStackInteraction.EMPTY
    }
}
