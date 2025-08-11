package hiiragi283.ragium

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.values
import hiiragi283.ragium.client.network.HTTransferIOUpdatePayload
import hiiragi283.ragium.common.network.HTBlockEntityUpdatePacket
import hiiragi283.ragium.common.network.HTFluidSlotUpdatePacket
import hiiragi283.ragium.setup.RagiumArmorMaterials
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumCustomRecipeSerializers
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.setup.RagiumMiscRegister
import hiiragi283.ragium.setup.RagiumPayloadRegister
import hiiragi283.ragium.util.RagiumChunkLoader
import net.minecraft.core.dispenser.ProjectileDispenseBehavior
import net.minecraft.world.item.Item
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.block.DispenserBlock
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
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

        eventBus.addListener(::commonSetup)
        eventBus.addListener(::registerDataMapTypes)
        eventBus.addListener(::registerPackets)
        eventBus.addListener(::registerRegistries)
        eventBus.addListener(RagiumChunkLoader::registerController)
        eventBus.addListener(RagiumMiscRegister::onRegister)

        RagiumDataComponents.REGISTER.register(eventBus)

        RagiumFluidContents.REGISTER.init(eventBus)

        RagiumBlocks.init(eventBus)
        RagiumItems.init(eventBus)

        RagiumArmorMaterials.REGISTER.register(eventBus)
        RagiumBlockEntityTypes.init(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumEntityTypes.REGISTER.register(eventBus)

        RagiumCustomRecipeSerializers.REGISTER.register(eventBus)

        RagiumMenuTypes.REGISTER.register(eventBus)

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onModConstruct(eventBus, dist)
        }

        RagiumConfig.register(container)

        LOGGER.info("Ragium loaded!")
    }

    private fun registerRegistries(event: NewRegistryEvent) {
        LOGGER.info("Registered new registries!")
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            RagiumFluidContents.REGISTER.registerDispensers()

            RagiumItems.REGISTER.values
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
        val registrar = RagiumPayloadRegister(event.registrar(RagiumAPI.MOD_ID))

        registrar.registerS2C(HTBlockEntityUpdatePacket.TYPE, HTBlockEntityUpdatePacket.STREAM_CODEC)
        registrar.registerS2C(HTFluidSlotUpdatePacket.TYPE, HTFluidSlotUpdatePacket.STREAM_CODEC)

        registrar.registerC2S(HTTransferIOUpdatePayload.TYPE, HTTransferIOUpdatePayload.STREAM_CODEC)

        LOGGER.info("Registered packets!")
    }
}
