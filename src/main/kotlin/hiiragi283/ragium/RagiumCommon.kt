package hiiragi283.ragium

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.data.registry.HTBrewingEffect
import hiiragi283.ragium.api.data.registry.HTSolarPower
import hiiragi283.ragium.api.network.HTPayloadRegistrar
import hiiragi283.ragium.client.network.HTUpdateAccessConfigPayload
import hiiragi283.ragium.client.network.HTUpdateTelepadPacket
import hiiragi283.ragium.common.network.HTUpdateBlockEntityPacket
import hiiragi283.ragium.common.network.HTUpdateEnergyStoragePacket
import hiiragi283.ragium.common.network.HTUpdateExperienceStoragePacket
import hiiragi283.ragium.common.network.HTUpdateFluidTankPacket
import hiiragi283.ragium.common.util.RagiumChunkLoader
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.impl.material.RagiumMaterialManager
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumEnchantmentComponents
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFeatures
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.setup.RagiumMiscRegister
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.dispenser.ProjectileDispenseBehavior
import net.minecraft.world.item.Item
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.DispenserBlock
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
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

        RagiumDataComponents.REGISTER.register(eventBus)
        RagiumEnchantmentComponents.REGISTER.register(eventBus)

        RagiumFluidContents.REGISTER.init(eventBus)

        RagiumBlocks.init(eventBus)
        RagiumItems.init(eventBus)

        RagiumAttachmentTypes.REGISTER.register(eventBus)
        RagiumBlockEntityTypes.init(eventBus)
        RagiumCreativeTabs.init(eventBus)
        RagiumEntityTypes.init(eventBus)
        RagiumFeatures.REGISTER.register(eventBus)
        RagiumMenuTypes.REGISTER.register(eventBus)
        RagiumRecipeSerializers.REGISTER.register(eventBus)

        val addons: List<RagiumAddon> = RagiumPlatform.INSTANCE.getAddons()
        for (addon: RagiumAddon in addons) {
            addon.onModConstruct(eventBus, dist)
        }
        eventBus.addListener(EventPriority.LOW) { event: ModifyDefaultComponentsEvent ->
            for (addon: RagiumAddon in addons) {
                addon.modifyComponents(event)
            }
        }
        eventBus.addListener(EventPriority.LOW) { event: BuildCreativeModeTabContentsEvent ->
            for (addon: RagiumAddon in addons) {
                addon.buildCreativeTabs(RagiumAddon.CreativeTabHelper(event))
            }
        }

        container.registerConfig(ModConfig.Type.COMMON, RagiumConfig.COMMON_SPEC)
        container.registerConfig(ModConfig.Type.CLIENT, RagiumConfig.CLIENT_SPEC)

        RagiumAPI.LOGGER.info("Ragium loaded!")
    }

    private fun registerRegistries(event: NewRegistryEvent) {
        event.register(RagiumAPI.EQUIP_ACTION_TYPE_REGISTRY)
        event.register(RagiumAPI.MATERIAL_RECIPE_TYPE_REGISTRY)

        RagiumAPI.LOGGER.info("Registered new registries!")
    }

    private fun registerDataPackRegistries(event: DataPackRegistryEvent.NewRegistry) {
        event.dataPackRegistry(RagiumAPI.BREWING_EFFECT_KEY, HTBrewingEffect.DIRECT_CODEC, HTBrewingEffect.DIRECT_CODEC)
        event.dataPackRegistry(RagiumAPI.SOLAR_POWER_KEY, HTSolarPower.DIRECT_CODEC, HTSolarPower.DIRECT_CODEC)

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

        for (addon: RagiumAddon in RagiumPlatform.INSTANCE.getAddons()) {
            addon.onCommonSetup(event)
        }
        RagiumAPI.LOGGER.info("Loaded common setup!")
    }

    private fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        event.register(RagiumDataMaps.ENCHANT_FUEL)

        event.register(RagiumDataMaps.MOB_HEAD)

        event.register(RagiumDataMaps.THERMAL_FUEL)
        event.register(RagiumDataMaps.COMBUSTION_FUEL)
        event.register(RagiumDataMaps.NUCLEAR_FUEL)

        event.register(RagiumDataMaps.ARMOR_EQUIP)

        event.register(RagiumDataMaps.MATERIAL_RECIPE)

        RagiumAPI.LOGGER.info("Registered data map types!")
    }

    private fun registerPackets(event: RegisterPayloadHandlersEvent) {
        val registrar = HTPayloadRegistrar(event.registrar(RagiumAPI.MOD_ID))
        // Server -> Client
        registrar.registerS2C(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC)
        registrar.registerS2C(HTUpdateEnergyStoragePacket.TYPE, HTUpdateEnergyStoragePacket.STREAM_CODEC)
        registrar.registerS2C(HTUpdateExperienceStoragePacket.TYPE, HTUpdateExperienceStoragePacket.STREAM_CODEC)
        registrar.registerS2C(HTUpdateFluidTankPacket.TYPE, HTUpdateFluidTankPacket.STREAM_CODEC)
        // Client -> Server
        registrar.registerC2S(HTUpdateAccessConfigPayload.TYPE, HTUpdateAccessConfigPayload.STREAM_CODEC)
        registrar.registerC2S(HTUpdateTelepadPacket.TYPE, HTUpdateTelepadPacket.STREAM_CODEC)

        for (addon: RagiumAddon in RagiumPlatform.INSTANCE.getAddons()) {
            addon.registerPayloads(registrar)
        }

        RagiumAPI.LOGGER.info("Registered packets!")
    }
}
