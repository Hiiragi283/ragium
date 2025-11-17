package hiiragi283.ragium

import blusunrize.immersiveengineering.api.tool.RailgunHandler
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.material.prefix.HTRegisterPrefixEvent
import hiiragi283.ragium.api.network.HTPayloadHandlers
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.client.integration.accessories.HTBackAccessoryRenderer
import hiiragi283.ragium.client.integration.accessories.HTBundleAccessoryRenderer
import hiiragi283.ragium.client.integration.accessories.HTGogglesAccessoryRenderer
import hiiragi283.ragium.client.key.RagiumKeyMappings
import hiiragi283.ragium.client.network.HTOpenUniversalBundlePacket
import hiiragi283.ragium.common.RagiumAccessory
import hiiragi283.ragium.common.data.map.HTSoulVialEntityIngredient
import hiiragi283.ragium.common.entity.HTThrownCaptureEgg
import hiiragi283.ragium.common.material.MekanismMaterialPrefixes
import hiiragi283.ragium.setup.RagiumChemicals
import hiiragi283.ragium.setup.RagiumDelightContents
import hiiragi283.ragium.setup.RagiumIntegrationItems
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMatterTypes
import io.wispforest.accessories.api.AccessoriesAPI
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry
import io.wispforest.accessories.api.client.AccessoryRenderer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.RegisterEvent
import kotlin.collections.forEach

@Mod(RagiumAPI.MOD_ID)
class RagiumIntegration(eventBus: IEventBus, dist: Dist) {
    companion object {
        @JvmStatic
        fun isLoaded(modId: String): Boolean = ModList.get().isLoaded(modId)
    }

    init {
        eventBus.addListener(::commonSetup)
        eventBus.addListener(::clientSetup)
        eventBus.addListener(::register)
        eventBus.addListener(::registerPackets)

        NeoForge.EVENT_BUS.addListener { event: HTRegisterPrefixEvent ->
            MekanismMaterialPrefixes.entries.forEach(event::register)
        }
        NeoForge.EVENT_BUS.addListener { _: ClientTickEvent.Post ->
            if (RagiumKeyMappings.OPEN_UNIVERSAL_BUNDLE.consumeClick()) {
                PacketDistributor.sendToServer(HTOpenUniversalBundlePacket)
            }
        }

        RagiumIntegrationItems.init(eventBus)

        if (isLoaded(RagiumConst.FARMERS_DELIGHT)) {
            RagiumDelightContents.register(eventBus)
        }
        if (isLoaded(RagiumConst.MEKANISM)) {
            RagiumChemicals.init(eventBus)
        }
        if (isLoaded(RagiumConst.REPLICATION)) {
            RagiumMatterTypes.REGISTER.register(eventBus)
        }
    }

    private fun register(event: RegisterEvent) {
        // Sub Entity Type Ingredient
        if (isLoaded(RagiumConst.EIO_BASE)) {
            event.register(RagiumAPI.SUB_ENTITY_INGREDIENT_TYPE_KEY) { helper ->
                helper.register(RagiumConst.EIO_BASE.toId("soul_vial"), HTSoulVialEntityIngredient.CODEC)
            }
        }
    }

    private fun registerPackets(event: RegisterPayloadHandlersEvent) {
        val registrar: PayloadRegistrar = event.registrar(RagiumAPI.MOD_ID)
        registrar.playToServer(
            HTOpenUniversalBundlePacket.TYPE,
            HTOpenUniversalBundlePacket.STREAM_CODEC,
            HTPayloadHandlers::handleC2S,
        )
    }

    //    Common    //

    private fun commonSetup(event: FMLCommonSetupEvent) {
        if (isLoaded(RagiumConst.ACCESSORIES)) {
            event.enqueueWork(::registerAccessories)
        }
        if (isLoaded(RagiumConst.IMMERSIVE)) {
            event.enqueueWork(::registerRailgun)
        }
    }

    private fun registerAccessories() {
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

    private fun registerRailgun() {
        RailgunHandler.registerProjectile(
            { Ingredient.of(RagiumItems.ELDRITCH_EGG) },
            object : RailgunHandler.IRailgunProjectile {
                override fun getProjectile(shooter: Player?, ammo: ItemStack, defaultProjectile: Entity): Entity? {
                    if (shooter != null) {
                        val egg = HTThrownCaptureEgg(shooter.level(), shooter)
                        egg.item = ammo
                        egg.shootFromRotation(shooter, shooter.xRot, shooter.yRot, 0f, 2.5f, 0f)
                        return egg
                    }
                    return super.getProjectile(shooter, ammo, defaultProjectile)
                }
            },
        )
        RagiumAPI.LOGGER.info("Registered Railgun Projectiles!")
    }

    //    Client    //

    private fun clientSetup(event: FMLClientSetupEvent) {
        event.enqueueWork(::registerRenderer)
    }

    private fun registerRenderer() {
        registerRenderer(RagiumItems.ECHO_STAR, ::HTBackAccessoryRenderer)
        registerRenderer(RagiumItems.NIGHT_VISION_GOGGLES, ::HTGogglesAccessoryRenderer)
        registerRenderer(RagiumItems.POTION_BUNDLE, ::HTBundleAccessoryRenderer)
        registerRenderer(RagiumItems.UNIVERSAL_BUNDLE, ::HTBundleAccessoryRenderer)
        RagiumAPI.LOGGER.info("Registered Accessory Renderer!")
    }

    private fun registerRenderer(item: ItemLike, supplier: () -> AccessoryRenderer) {
        AccessoriesRendererRegistry.registerRenderer(item.asItem(), supplier)
    }
}
