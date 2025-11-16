package hiiragi283.ragium.common.integration

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.network.HTPayloadHandlers
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.client.accessory.HTBackAccessoryRenderer
import hiiragi283.ragium.client.accessory.HTBundleAccessoryRenderer
import hiiragi283.ragium.client.accessory.HTGogglesAccessoryRenderer
import hiiragi283.ragium.client.key.RagiumKeyMappings
import hiiragi283.ragium.client.network.HTOpenUniversalBundlePacket
import hiiragi283.ragium.setup.RagiumItems
import io.wispforest.accessories.api.AccessoriesAPI
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry
import io.wispforest.accessories.api.client.AccessoryRenderer
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.registration.PayloadRegistrar

object RagiumAccessoriesAddon : RagiumAddon {
    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        // Common
        // Client
        NeoForge.EVENT_BUS.addListener(::onClientTick)
    }

    @JvmStatic
    private fun onClientTick(event: ClientTickEvent.Post) {
        if (RagiumKeyMappings.OPEN_UNIVERSAL_BUNDLE.consumeClick()) {
            PacketDistributor.sendToServer(HTOpenUniversalBundlePacket)
        }
    }

    //    Common    //

    override fun onCommonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork(RagiumAccessoriesAddon::register)
    }

    @JvmStatic
    fun register() {
        listOf(
            // Charm
            RagiumItems.DYNAMIC_LANTERN,
            RagiumItems.MAGNET,
            RagiumItems.ADVANCED_MAGNET,
            // Face
            RagiumItems.NIGHT_VISION_GOGGLES,
        ).forEach { item: HTDeferredItem<*> ->
            AccessoriesAPI.registerAccessory(item.asItem(), RagiumAccessory)
        }
        RagiumAPI.LOGGER.info("Registered Accessories!")
    }

    //    Client    //

    override fun onClientSetup(event: FMLClientSetupEvent) {
        event.enqueueWork(RagiumAccessoriesAddon::registerRenderer)
    }

    @JvmStatic
    private fun registerRenderer() {
        registerRenderer(RagiumItems.ECHO_STAR, ::HTBackAccessoryRenderer)
        registerRenderer(RagiumItems.NIGHT_VISION_GOGGLES, ::HTGogglesAccessoryRenderer)
        registerRenderer(RagiumItems.POTION_BUNDLE, ::HTBundleAccessoryRenderer)
        registerRenderer(RagiumItems.UNIVERSAL_BUNDLE, ::HTBundleAccessoryRenderer)
        RagiumAPI.LOGGER.info("Registered Accessory Renderer!")
    }

    @JvmStatic
    private fun registerRenderer(item: ItemLike, supplier: () -> AccessoryRenderer) {
        AccessoriesRendererRegistry.registerRenderer(item.asItem(), supplier)
    }

    override fun registerPayloads(registrar: PayloadRegistrar) {
        registrar.playToServer(
            HTOpenUniversalBundlePacket.TYPE,
            HTOpenUniversalBundlePacket.STREAM_CODEC,
            HTPayloadHandlers::handleC2S,
        )
    }
}
