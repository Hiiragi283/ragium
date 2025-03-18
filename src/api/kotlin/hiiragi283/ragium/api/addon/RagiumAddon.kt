package hiiragi283.ragium.api.addon

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import java.util.function.BiConsumer

interface RagiumAddon {
    val priority: Int

    fun onModConstruct(eventBus: IEventBus, dist: Dist) {}

    fun onMaterialRegister(consumer: BiConsumer<HTMaterialKey, HTMaterialType>) {}

    fun onCommonSetup(event: FMLCommonSetupEvent) {}

    fun onServerSetup(event: FMLDedicatedServerSetupEvent) {}

    fun onClientSetup(event: FMLClientSetupEvent) {}
}
