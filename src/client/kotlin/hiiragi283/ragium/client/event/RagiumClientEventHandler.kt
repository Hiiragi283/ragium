package hiiragi283.ragium.client.event

import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.RagiumAPI
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
        if (stack.`is`(RagiumFluids.POTION.get())) {
            HTPotionHelper.getPotion(stack).addToTooltip(event.context, event.toolTip::add, event.flags, stack)
        }
    }
}
