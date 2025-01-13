package hiiragi283.ragium.common

import com.mojang.logging.LogUtils
import hiiragi283.ragium.Config
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import org.slf4j.Logger

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(
    eventBus: IEventBus,
    container: ModContainer,
) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(::commonSetup)

        RagiumFluids.register(eventBus)
        RagiumItems.register(eventBus)

        container.registerConfig(ModConfig.Type.STARTUP, Config.SPEC)

        LOGGER.info("Ragium loaded!")
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {}
}
