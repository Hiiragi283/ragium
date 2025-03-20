package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumItems
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import org.slf4j.Logger

@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
class RagiumClient(eventBus: IEventBus) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(::addItemColor)
    }

    private fun addItemColor(event: RegisterColorHandlersEvent.Item) {
        event.register(DynamicFluidContainerModel.Colors(), *RagiumItems.FLUID_CUBES)

        LOGGER.info("Registered ItemColor!")
    }
}
