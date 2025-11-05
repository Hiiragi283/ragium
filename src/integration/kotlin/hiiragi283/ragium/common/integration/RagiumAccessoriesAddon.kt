package hiiragi283.ragium.common.integration

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.network.HTPayloadRegistrar
import hiiragi283.ragium.client.RagiumKeyMappings
import hiiragi283.ragium.client.accessory.HTBackAccessoryRenderer
import hiiragi283.ragium.client.accessory.HTBundleAccessoryRenderer
import hiiragi283.ragium.client.accessory.HTGogglesAccessoryRenderer
import hiiragi283.ragium.client.network.HTOpenUniversalBundlePacket
import hiiragi283.ragium.common.accessory.HTDynamicLightingAccessory
import hiiragi283.ragium.common.accessory.HTMagnetizationAccessory
import hiiragi283.ragium.common.accessory.HTMobEffectAccessory
import hiiragi283.ragium.common.util.HTPacketHelper
import hiiragi283.ragium.setup.RagiumItems
import io.wispforest.accessories.api.AccessoriesAPI
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry
import io.wispforest.accessories.api.client.AccessoryRenderer
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.common.NeoForge

object RagiumAccessoriesAddon : RagiumAddon {
    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        // Common
        // Client
        NeoForge.EVENT_BUS.addListener(::onClientTick)
    }

    @JvmStatic
    private fun onClientTick(event: ClientTickEvent.Post) {
        if (RagiumKeyMappings.OPEN_UNIVERSAL_BUNDLE.consumeClick()) {
            HTPacketHelper.sendToServer(HTOpenUniversalBundlePacket)
        }
    }

    //    Common    //

    override fun onCommonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork(RagiumAccessoriesAddon::register)
    }

    @JvmStatic
    fun register() {
        // Charm
        register(RagiumItems.DYNAMIC_LANTERN, HTDynamicLightingAccessory)
        register(
            RagiumItems.MAGNET,
            HTMagnetizationAccessory.create { entity: ItemEntity, _ ->
                // IEのコンベヤ上にいるアイテムは無視する
                if (entity.persistentData.getBoolean(RagiumConst.PREVENT_ITEM_MAGNET)) return@create false
                entity.isAlive && !entity.hasPickUpDelay()
            },
        )
        register(
            RagiumItems.ADVANCED_MAGNET,
            HTMagnetizationAccessory.create { entity: ExperienceOrb, _ ->
                entity.isAlive && entity.value > 0
            },
        )

        // Face
        register(
            RagiumItems.NIGHT_VISION_GOGGLES,
            HTMobEffectAccessory(MobEffects.NIGHT_VISION, -1, ambient = true),
        )
        RagiumAPI.LOGGER.info("Registered Accessories!")
    }

    @JvmStatic
    private fun register(item: ItemLike, accessory: Accessory) {
        AccessoriesAPI.registerAccessory(item.asItem(), accessory)
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

    override fun registerPayloads(registrar: HTPayloadRegistrar) {
        registrar.registerC2S(HTOpenUniversalBundlePacket.TYPE, HTOpenUniversalBundlePacket.STREAM_CODEC)
    }
}
