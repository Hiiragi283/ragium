package hiiragi283.ragium

import com.mojang.serialization.Codec
import hiiragi283.core.api.mod.HTCommonMod
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.data.registry.HTWoodDefinition
import hiiragi283.ragium.common.datagen.RagiumServerResourceProvider
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
import hiiragi283.ragium.setup.RagiumWidgetTypes
import net.mehvahdjukaar.moonlight.api.platform.RegHelper
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.registries.DataPackRegistryEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent

@Mod(RagiumAPI.MOD_ID)
data object Ragium : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        eventBus.addListener(RagiumMiscRegister::register)

        RagiumDataComponents.REGISTER.register(eventBus)

        RagiumFluids.register(eventBus)
        RagiumBlockEntityTypes.register(eventBus)
        RagiumBlocks.register(eventBus)
        RagiumItems.register(eventBus)

        RagiumAttachmentTypes.REGISTER.register(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumRecipeSerializers.REGISTER.register(eventBus)
        RagiumRecipeTypes.REGISTER.register(eventBus)
        RagiumWidgetTypes.REGISTER.register(eventBus)

        container.registerConfig(ModConfig.Type.COMMON, RagiumConfig.COMMON_SPEC)

        RegHelper.registerDynamicResourceProvider(RagiumServerResourceProvider)

        RagiumAPI.LOGGER.info("Ragium loaded")
    }

    override fun registerDynamicRegistries(event: DataPackRegistryEvent.NewRegistry) {
        val woodDefinition: Codec<HTWoodDefinition> = HTWoodDefinition.CODEC.codec
        event.dataPackRegistry(RagiumAPI.WOOD_DEFINITION_KEY, woodDefinition, woodDefinition)
    }

    override fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        event.register(RagiumDataMapTypes.FERMENT_SOURCE)

        event.register(RagiumDataMapTypes.MOB_HEAD)

        event.register(RagiumDataMapTypes.COOLANT)
        event.register(RagiumDataMapTypes.MAGMATIC_FUEL)
        event.register(RagiumDataMapTypes.COMBUSTION_FUEL)
        event.register(RagiumDataMapTypes.FERTILIZER)

        event.register(RagiumDataMapTypes.EXPLOSIVE)
        event.register(RagiumDataMapTypes.UPGRADE)
    }
}
