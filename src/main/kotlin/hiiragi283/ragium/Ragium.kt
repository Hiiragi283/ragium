package hiiragi283.ragium

import com.mojang.logging.LogUtils
import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.data.registry.HTWoodDefinition
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMiscRegister
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.registries.DataPackRegistryEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(RagiumAPI.MOD_ID)
object Ragium {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    init {
        val eventBus: IEventBus = MOD_BUS

        eventBus.addListener(::registerRegistries)
        eventBus.addListener(::registerDataPackRegistries)
        eventBus.addListener(RagiumMiscRegister::register)
        eventBus.addListener(::registerDataMapTypes)

        RagiumDataComponents.REGISTER.register(eventBus)

        RagiumFluids.register(eventBus)
        RagiumBlockEntityTypes.register(eventBus)
        RagiumBlocks.register(eventBus)
        RagiumItems.register(eventBus)

        RagiumAttachmentTypes.REGISTER.register(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumRecipeSerializers.REGISTER.register(eventBus)
        RagiumRecipeTypes.REGISTER.register(eventBus)

        LOADING_CONTEXT.activeContainer.registerConfig(ModConfig.Type.COMMON, RagiumConfig.COMMON_SPEC)

        LOGGER.info("Ragium loaded!")
    }

    private fun registerRegistries(event: NewRegistryEvent) {
        LOGGER.info("Registered new registries!")
    }

    private fun registerDataPackRegistries(event: DataPackRegistryEvent.NewRegistry) {
        val woodDefinition: Codec<HTWoodDefinition> = HTWoodDefinition.CODEC.codec
        event.dataPackRegistry(RagiumAPI.WOOD_DEFINITION_KEY, woodDefinition, woodDefinition)

        LOGGER.info("Registered new data pack registries!")
    }

    @JvmStatic
    private fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        event.register(RagiumDataMapTypes.MOB_HEAD)

        event.register(RagiumDataMapTypes.COOLANT)
        event.register(RagiumDataMapTypes.MAGMATIC_FUEL)
        event.register(RagiumDataMapTypes.COMBUSTION_FUEL)
        event.register(RagiumDataMapTypes.FERTILIZER)

        event.register(RagiumDataMapTypes.ROCK_CHANCE)
        event.register(RagiumDataMapTypes.UPGRADE)

        LOGGER.info("Registered data map types!")
    }
}
