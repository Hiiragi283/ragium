package hiiragi283.ragium

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTBrewingRecipeData
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.network.HTPayloadHandlers
import hiiragi283.ragium.client.network.HTUpdateAccessConfigPayload
import hiiragi283.ragium.common.network.HTUpdateBlockEntityPacket
import hiiragi283.ragium.common.network.HTUpdateMenuPacket
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.impl.material.RagiumMaterialManager
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumCriteriaTriggers
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumEnchantmentComponents
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFeatures
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.setup.RagiumMiscRegister
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.util.RagiumChunkLoader
import net.minecraft.core.dispenser.ProjectileDispenseBehavior
import net.minecraft.world.item.Item
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.DispenserBlock
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.DataPackRegistryEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(eventBus: IEventBus, container: ModContainer, dist: Dist) {
    init {
        NeoForgeMod.enableMilkFluid()

        eventBus.addListener(::commonSetup)
        eventBus.addListener(RagiumMiscRegister::register)
        eventBus.addListener(::registerDataMapTypes)
        eventBus.addListener(::registerPackets)
        eventBus.addListener(::registerRegistries)
        eventBus.addListener(::registerDataPackRegistries)
        eventBus.addListener(RagiumChunkLoader::registerController)

        RagiumDataComponents.init(eventBus)
        RagiumEnchantmentComponents.REGISTER.register(eventBus)

        RagiumFluidContents.init(eventBus)

        RagiumBlocks.init(eventBus)
        RagiumItems.init(eventBus)

        RagiumAttachmentTypes.REGISTER.register(eventBus)
        RagiumBlockEntityTypes.init(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumCriteriaTriggers.REGISTER.register(eventBus)
        RagiumEntityTypes.init(eventBus)
        RagiumFeatures.REGISTER.register(eventBus)
        RagiumMenuTypes.REGISTER.register(eventBus)
        RagiumRecipeSerializers.REGISTER.register(eventBus)

        container.registerConfig(ModConfig.Type.COMMON, RagiumConfig.COMMON_SPEC)
        container.registerConfig(ModConfig.Type.CLIENT, RagiumConfig.CLIENT_SPEC)

        RagiumAPI.LOGGER.info("Ragium loaded!")
    }

    private fun registerRegistries(event: NewRegistryEvent) {
        event.register(RagiumAPI.BREWING_RECIPE_TYPE_REGISTRY)
        event.register(RagiumAPI.EQUIP_ACTION_TYPE_REGISTRY)
        event.register(RagiumAPI.SLOT_TYPE_REGISTRY)

        RagiumAPI.LOGGER.info("Registered new registries!")
    }

    private fun registerDataPackRegistries(event: DataPackRegistryEvent.NewRegistry) {
        event.dataPackRegistry(RagiumAPI.BREWING_RECIPE_KEY, HTBrewingRecipeData.CODEC, HTBrewingRecipeData.CODEC)

        RagiumAPI.LOGGER.info("Registered new data pack registries!")
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            RagiumItems.REGISTER
                .asSequence()
                .map(ItemLike::asItem)
                .filter { item: Item -> item is ProjectileItem }
                .associateWith(::ProjectileDispenseBehavior)
                .forEach(DispenserBlock::registerBehavior)

            RagiumFluidContents.registerInteractions()
            RagiumAPI.LOGGER.info("Registered dispenser behaviors!")
        }
        event.enqueueWork(RagiumFluidContents::registerInteractions)
        event.enqueueWork(RagiumMaterialManager::gatherAttributes)

        RagiumAPI.LOGGER.info("Loaded common setup!")
    }

    private fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        event.register(RagiumDataMapTypes.MOB_HEAD)

        event.register(RagiumDataMapTypes.COOLANT)
        event.register(RagiumDataMapTypes.MAGMATIC_FUEL)
        event.register(RagiumDataMapTypes.COMBUSTION_FUEL)

        event.register(RagiumDataMapTypes.ARMOR_EQUIP)
        event.register(RagiumDataMapTypes.ROCK_CHANCE)
        event.register(RagiumDataMapTypes.UPGRADE)

        RagiumAPI.LOGGER.info("Registered data map types!")
    }

    private fun registerPackets(event: RegisterPayloadHandlersEvent) {
        val registrar: PayloadRegistrar = event.registrar(RagiumAPI.MOD_ID)
        registrar.playBidirectional(HTUpdateMenuPacket.TYPE, HTUpdateMenuPacket.STREAM_CODEC, HTPayloadHandlers::handleBoth)

        // Server -> Client
        registrar.playToClient(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC, HTPayloadHandlers::handleS2C)
        // Client -> Server
        registrar.playToServer(HTUpdateAccessConfigPayload.TYPE, HTUpdateAccessConfigPayload.STREAM_CODEC, HTPayloadHandlers::handleC2S)

        RagiumAPI.LOGGER.info("Registered packets!")
    }
}
