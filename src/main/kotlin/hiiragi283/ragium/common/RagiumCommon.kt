package hiiragi283.ragium.common

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.internal.HTMaterialRegistryImpl
import hiiragi283.ragium.common.internal.RagiumConfig
import hiiragi283.ragium.integration.RagiumMekIntegration
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import org.slf4j.Logger

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(eventBus: IEventBus, container: ModContainer) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(::construct)

        RagiumComponentTypes.REGISTER.register(eventBus)

        HTMachineType

        RagiumFluids.init()
        RagiumFluids.REGISTER.register(eventBus)
        RagiumFluidTypes.REGISTER.register(eventBus)

        RagiumBlocks.REGISTER.register(eventBus)

        RagiumBlocks.ITEM_REGISTER.register(eventBus)
        RagiumEntityTypes.REGISTER.register(eventBus)
        RagiumItems.REGISTER.register(eventBus)

        RagiumBlockEntityTypes.REGISTER.register(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumMenuTypes.REGISTER.register(eventBus)

        if (ModList.get().isLoaded("mekanism")) {
            RagiumMekIntegration.CHEMICAL_REGISTER.register(eventBus)
        }

        container.registerConfig(ModConfig.Type.STARTUP, RagiumConfig.SPEC)

        LOGGER.info("Ragium loaded!")
    }

    private fun construct(event: FMLConstructModEvent) {
        event.enqueueWork(HTMaterialRegistryImpl::initRegistry)
    }
}
