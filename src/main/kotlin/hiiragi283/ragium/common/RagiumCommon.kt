package hiiragi283.ragium.common

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.internal.DefaultMachinePlugin
import hiiragi283.ragium.common.internal.InternalRagiumAPI
import hiiragi283.ragium.common.internal.RagiumEvents
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import org.slf4j.Logger

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(eventBus: IEventBus, container: ModContainer) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        container.registerExtensionPoint(RagiumPlugin.Provider::class.java) {
            RagiumPlugin.Provider {
                listOf(DefaultMachinePlugin)
            }
        }

        RagiumEvents.register(eventBus)

        RagiumComponentTypes.REGISTER.register(eventBus)

        InternalRagiumAPI.collectPlugins()
        InternalRagiumAPI.registerMachines()

        RagiumFluids.register(eventBus)
        RagiumBlocks.register(eventBus)
        RagiumItems.register(eventBus)

        RagiumBlockEntityTypes.REGISTER.register(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumRecipes.CATEGORY.register(eventBus)
        RagiumRecipes.SERIALIZER.register(eventBus)
        RagiumRecipes.TYPE.register(eventBus)

        InternalRagiumAPI.registerMaterials()

        container.registerConfig(ModConfig.Type.STARTUP, RagiumConfig.SPEC)

        LOGGER.info("Ragium loaded!")
    }
}
