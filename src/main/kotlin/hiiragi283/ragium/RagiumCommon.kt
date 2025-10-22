package hiiragi283.ragium

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.data.registry.HTBrewingEffect
import hiiragi283.ragium.api.data.registry.HTSolarPower
import hiiragi283.ragium.api.network.HTPayloadRegister
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.client.network.HTOpenPotionBundlePacket
import hiiragi283.ragium.client.network.HTOpenUniversalBundlePacket
import hiiragi283.ragium.client.network.HTUpdateAccessConfigPayload
import hiiragi283.ragium.client.network.HTUpdateTelepadPacket
import hiiragi283.ragium.common.network.HTUpdateBlockEntityPacket
import hiiragi283.ragium.common.network.HTUpdateFluidTankPacket
import hiiragi283.ragium.common.util.RagiumChunkLoader
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumAccessoryRegister
import hiiragi283.ragium.setup.RagiumArmorMaterials
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
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.dispenser.ProjectileDispenseBehavior
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
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
import net.neoforged.neoforge.registries.RegisterEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(eventBus: IEventBus, container: ModContainer, dist: Dist) {
    init {
        NeoForgeMod.enableMilkFluid()

        eventBus.addListener(::commonSetup)
        eventBus.addListener(::onRegister)
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

        RagiumArmorMaterials.REGISTER.register(eventBus)
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
        RagiumAPI.LOGGER.info("Registered new registries!")
    }

    private fun registerDataPackRegistries(event: DataPackRegistryEvent.NewRegistry) {
        event.dataPackRegistry(RagiumAPI.BREWING_EFFECT_KEY, HTBrewingEffect.DIRECT_CODEC, HTBrewingEffect.DIRECT_CODEC)
        event.dataPackRegistry(RagiumAPI.SOLAR_POWER_KEY, HTSolarPower.DIRECT_CODEC, HTSolarPower.DIRECT_CODEC)

        RagiumAPI.LOGGER.info("Registered new data pack registries!")
    }

    private fun onRegister(event: RegisterEvent) {
        event.register(Registries.RECIPE_TYPE) { helper ->
            register(helper, RagiumRecipeTypes.SAWMILL)
            // Machine
            register(helper, RagiumRecipeTypes.ALLOYING)
            register(helper, RagiumRecipeTypes.COMPRESSING)
            register(helper, RagiumRecipeTypes.CRUSHING)
            register(helper, RagiumRecipeTypes.ENCHANTING)
            register(helper, RagiumRecipeTypes.EXTRACTING)
            register(helper, RagiumRecipeTypes.FLUID_TRANSFORM)
            register(helper, RagiumRecipeTypes.MELTING)
            register(helper, RagiumRecipeTypes.PLANTING)
            register(helper, RagiumRecipeTypes.SIMULATING)
            register(helper, RagiumRecipeTypes.WASHING)
        }
    }

    private fun <R : Recipe<*>> register(helper: RegisterEvent.RegisterHelper<RecipeType<*>>, holder: HTDeferredRecipeType<*, R>) {
        helper.register(holder.id, RecipeType.simple<R>(holder.id))
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
        event.enqueueWork(RagiumAccessoryRegister::register)
        event.enqueueWork(RagiumFluidContents::registerInteractions)

        for (addon: RagiumAddon in RagiumPlatform.INSTANCE.getAddons()) {
            addon.onCommonSetup(event)
        }
        RagiumAPI.LOGGER.info("Loaded common setup!")
    }

    private fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        event.register(RagiumDataMaps.THERMAL_FUEL)
        event.register(RagiumDataMaps.COMBUSTION_FUEL)
        event.register(RagiumDataMaps.NUCLEAR_FUEL)

        event.register(RagiumDataMaps.MOB_HEAD)

        RagiumAPI.LOGGER.info("Registered data map types!")
    }

    private fun registerPackets(event: RegisterPayloadHandlersEvent) {
        with(HTPayloadRegister(event.registrar(RagiumAPI.MOD_ID))) {
            // Server -> Client
            registerS2C(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC)
            registerS2C(HTUpdateFluidTankPacket.TYPE, HTUpdateFluidTankPacket.STREAM_CODEC)
            // Client -> Server
            registerC2S(HTOpenPotionBundlePacket.TYPE, HTOpenPotionBundlePacket.STREAM_CODEC)
            registerC2S(HTOpenUniversalBundlePacket.TYPE, HTOpenUniversalBundlePacket.STREAM_CODEC)
            registerC2S(HTUpdateAccessConfigPayload.TYPE, HTUpdateAccessConfigPayload.STREAM_CODEC)
            registerC2S(HTUpdateTelepadPacket.TYPE, HTUpdateTelepadPacket.STREAM_CODEC)
        }

        RagiumAPI.LOGGER.info("Registered packets!")
    }
}
