package hiiragi283.ragium.client.event

import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.chemical.HTClientChemicalHelper
import hiiragi283.ragium.common.fluid.RagiumFluids
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.FluidTooltipEvent
import net.neoforged.neoforge.fluids.FluidStack

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, value = [Dist.CLIENT])
data object RagiumClientEventHandler {
    @SubscribeEvent
    fun onFluidTooltip(event: FluidTooltipEvent) {
        val stack: FluidStack = event.fluidStack
        val appender: (Text) -> Unit = event.toolTip::add
        // Potion Fluid
        if (RagiumFluids.POTION.isOf(stack)) {
            HTPotionHelper.getContents(stack).addToTooltip(event.context, appender, event.flags, stack)
        }
        // Chemical Tooltip
        HTClientChemicalHelper.addToTooltip(stack, appender)
    }
}
