package hiiragi283.ragium

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.common.network.HTBlockEntityUpdatePacket
import hiiragi283.ragium.common.network.HTFluidSlotUpdatePacket
import hiiragi283.ragium.common.storage.energy.HTEnergyNetworkManagerImpl
import hiiragi283.ragium.setup.RagiumArmorMaterials
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.setup.RagiumMiscRegister
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
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
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.common.NeoForgeMod
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
        eventBus.addListener(RagiumMiscRegister::onRegister)
        eventBus.addListener(::commonSetup)
        eventBus.addListener(::registerDataMapTypes)
        eventBus.addListener(::registerPackets)

        RagiumDataComponents.REGISTER.register(eventBus)

        RagiumFluidContents.REGISTER.init(eventBus)

        RagiumBlocks.init(eventBus)
        RagiumItems.init(eventBus)

        RagiumArmorMaterials.REGISTER.register(eventBus)
        RagiumBlockEntityTypes.init(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumEntityTypes.REGISTER.register(eventBus)

        RagiumRecipeSerializers.REGISTER.register(eventBus)
        RagiumRecipeTypes.REGISTER.register(eventBus)

        RagiumMenuTypes.REGISTER.register(eventBus)

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onModConstruct(eventBus, dist)
        }

        HTEnergyNetworkManagerImpl.registerEvents()
        RagiumRuntimeEvents.registerEvents()

        container.registerConfig(ModConfig.Type.COMMON, RagiumConfig.COMMON_SPEC)

        LOGGER.info("Ragium loaded!")
    }

    private fun registerRegistries(event: NewRegistryEvent) {
        event.register(RagiumRegistries.BLOCK_ACTION_SERIALIZERS)

        LOGGER.info("Registered new registries!")
    }

    private fun construct(event: FMLConstructModEvent) {}

    private fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            RagiumFluidContents.REGISTER.registerDispensers()

            RagiumItems.REGISTER.entries
                .map(ItemLike::asItem)
                .filter { item: Item -> item is ProjectileItem }
                .associateWith(::ProjectileDispenseBehavior)
                .forEach(DispenserBlock::registerBehavior)
        }

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onCommonSetup(event)
        }
        LOGGER.info("Loaded common setup!")
    }

    private fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        LOGGER.info("Registered data map types!")
    }

    private fun registerPackets(event: RegisterPayloadHandlersEvent) {
        val registrar: PayloadRegistrar = event.registrar(RagiumAPI.MOD_ID)

        registrar.playToClient(
            HTBlockEntityUpdatePacket.TYPE,
            HTBlockEntityUpdatePacket.STREAM_CODEC,
            HTCustomPayload::handle,
        )
        registrar.playToClient(
            HTFluidSlotUpdatePacket.TYPE,
            HTFluidSlotUpdatePacket.STREAM_CODEC,
            HTCustomPayload::handle,
        )

        LOGGER.info("Registered packets!")
    }
}
