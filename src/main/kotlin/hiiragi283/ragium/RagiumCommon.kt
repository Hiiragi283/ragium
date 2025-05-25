package hiiragi283.ragium

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.common.network.HTBlockEntityUpdatePacket
import hiiragi283.ragium.common.storage.energy.HTEnergyNetworkManagerImpl
import hiiragi283.ragium.setup.RagiumAdvancementTriggers
import hiiragi283.ragium.setup.RagiumArmorMaterials
import hiiragi283.ragium.setup.RagiumBlockActionSerializers
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCauldronInteractions
import hiiragi283.ragium.setup.RagiumComponentTypes
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent
import org.slf4j.Logger

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(eventBus: IEventBus, container: ModContainer, dist: Dist) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        NeoForgeMod.enableMilkFluid()

        eventBus.addListener(::registerRegistries)
        eventBus.addListener(::construct)
        eventBus.addListener(::commonSetup)
        eventBus.addListener(::registerCauldronContents)
        eventBus.addListener(::registerDataMapTypes)
        eventBus.addListener(::registerPackets)

        RagiumComponentTypes.REGISTER.register(eventBus)

        RagiumFluidContents
        RagiumFluidContents.REGISTER.init(eventBus)

        RagiumBlocks.init(eventBus)
        RagiumItems.init(eventBus)

        RagiumArmorMaterials.REGISTER.register(eventBus)
        RagiumBlockEntityTypes.REGISTER.register(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)

        RagiumAdvancementTriggers.REGISTER.register(eventBus)
        RagiumRecipeSerializers.REGISTER.register(eventBus)
        RagiumRecipeTypes.REGISTER.register(eventBus)
        RagiumBlockActionSerializers.REGISTER.register(eventBus)

        HTEnergyNetworkManagerImpl

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onModConstruct(eventBus, dist)
        }

        LOGGER.info("Ragium loaded!")
    }

    private fun registerRegistries(event: NewRegistryEvent) {
        event.register(RagiumRegistries.BLOCK_ACTION_SERIALIZERS)

        LOGGER.info("Registered new registries!")
    }

    private fun construct(event: FMLConstructModEvent) {}

    private fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork(RagiumCauldronInteractions::initDefaultInteractions)
        event.enqueueWork(RagiumFluidContents.REGISTER::registerDispensers)

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onCommonSetup(event)
        }
        LOGGER.info("Loaded common setup!")
    }

    private fun registerCauldronContents(event: RegisterCauldronFluidContentEvent) {
        RagiumCauldronInteractions.registerCauldronContents(event)

        LOGGER.info("Registered cauldron contents!")
    }

    private fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        event.register(RagiumDataMaps.BLOCK_INTERACTION)
        event.register(RagiumDataMaps.TREE_TAP)

        LOGGER.info("Registered data map types!")
    }

    private fun registerPackets(event: RegisterPayloadHandlersEvent) {
        val registrar: PayloadRegistrar = event.registrar(RagiumAPI.MOD_ID)

        registrar.playToClient(
            HTBlockEntityUpdatePacket.TYPE,
            HTBlockEntityUpdatePacket.STREAM_CODEC,
            HTCustomPayload::handle,
        )

        LOGGER.info("Registered packets!")
    }
}
