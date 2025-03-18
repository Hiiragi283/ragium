package hiiragi283.ragium.api.addon

import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent

interface RagiumAddon {
    val priority: Int

    fun onModConstruct(eventBus: IEventBus, dist: Dist) {}

    fun onCommonSetup(event: FMLCommonSetupEvent) {}

    fun onServerSetup(event: FMLDedicatedServerSetupEvent) {}

    fun onClientSetup(event: FMLClientSetupEvent) {}
}
