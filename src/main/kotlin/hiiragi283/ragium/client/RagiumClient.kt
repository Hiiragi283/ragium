package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.impl.HTDeferredEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.api.world.getTypedBlockEntity
import hiiragi283.ragium.client.event.HTClientItemTooltipComponent
import hiiragi283.ragium.client.event.HTItemTooltipContent
import hiiragi283.ragium.client.gui.screen.HTAccessConfigurationScreen
import hiiragi283.ragium.client.gui.screen.HTBlockEntityContainerScreen
import hiiragi283.ragium.client.gui.screen.HTBlockEntityScreenFactory
import hiiragi283.ragium.client.gui.screen.HTDrumScreen
import hiiragi283.ragium.client.gui.screen.HTEnergyNetworkAccessScreen
import hiiragi283.ragium.client.gui.screen.HTFluidCollectorScreen
import hiiragi283.ragium.client.gui.screen.HTGenericScreen
import hiiragi283.ragium.client.gui.screen.HTTelepadScreen
import hiiragi283.ragium.client.gui.screen.generator.HTCombustionGeneratorScreen
import hiiragi283.ragium.client.gui.screen.generator.HTGeneratorScreen
import hiiragi283.ragium.client.gui.screen.generator.HTMagmaticGeneratorScreen
import hiiragi283.ragium.client.gui.screen.processor.HTCrusherScreen
import hiiragi283.ragium.client.gui.screen.processor.HTMixerScreen
import hiiragi283.ragium.client.gui.screen.processor.HTMobCrusherScreen
import hiiragi283.ragium.client.gui.screen.processor.HTProcessorScreen
import hiiragi283.ragium.client.gui.screen.processor.HTRefineryScreen
import hiiragi283.ragium.client.gui.screen.processor.HTSingleFluidProcessorScreen
import hiiragi283.ragium.client.key.RagiumKeyMappings
import hiiragi283.ragium.client.model.HTFuelGeneratorModel
import hiiragi283.ragium.client.renderer.RagiumModelLayers
import hiiragi283.ragium.client.renderer.block.HTCrateRenderer
import hiiragi283.ragium.client.renderer.block.HTFuelGeneratorRenderer
import hiiragi283.ragium.client.renderer.block.HTImitationSpawnerRenderer
import hiiragi283.ragium.client.renderer.block.HTRefineryRenderer
import hiiragi283.ragium.client.renderer.block.HTSingleFluidMachineRenderer
import hiiragi283.ragium.client.renderer.item.HTFuelGeneratorItemRenderer
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTFluidCollectorBlockEntity
import hiiragi283.ragium.common.entity.charge.HTAbstractCharge
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.material.HTColorMaterial
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.client.model.MinecartModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MinecartRenderer
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import java.awt.Color

@OnlyIn(Dist.CLIENT)
@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
class RagiumClient(eventBus: IEventBus, container: ModContainer) {
    init {
        eventBus.addListener(::clientSetup)

        eventBus.addListener(::registerBlockColor)
        eventBus.addListener(::registerItemColor)
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::registerScreens)
        eventBus.addListener(::registerEntityRenderer)
        eventBus.addListener(::registerLayerDefinitions)
        eventBus.addListener(::registerClientReloadListeners)
        eventBus.addListener(::registerTooltipRenderer)
        eventBus.addListener(::registerKeyMappings)

        container.registerExtensionPoint(IConfigScreenFactory::class.java, IConfigScreenFactory(::ConfigurationScreen))
    }

    private fun clientSetup(event: FMLClientSetupEvent) {
        RagiumAPI.LOGGER.info("Loaded Client Setup!")
    }

    private fun registerBlockColor(event: RegisterColorHandlersEvent.Block) {
        // Water Collector
        event.register(
            { _: BlockState, getter: BlockAndTintGetter?, pos: BlockPos?, tint: Int ->
                when {
                    tint != 0 -> -1
                    getter != null && pos != null -> {
                        val collector: HTFluidCollectorBlockEntity? =
                            getter.getTypedBlockEntity<HTFluidCollectorBlockEntity>(pos)
                        if (collector != null && collector.hasUpgrade(RagiumItems.EXP_COLLECTOR_UPGRADE)) {
                            0x66ff33
                        } else {
                            BiomeColors.getAverageWaterColor(getter, pos)
                        }
                    }
                    else -> -1
                }
            },
            RagiumBlocks.FLUID_COLLECTOR.get(),
        )
        // LED Blocks
        for ((color: HTColorMaterial, block: HTSimpleDeferredBlock) in RagiumBlocks.LED_BLOCKS) {
            event.register(
                { _: BlockState, _: BlockAndTintGetter?, _: BlockPos?, tint: Int ->
                    when {
                        tint != 0 -> return@register -1
                        else -> color.dyeColor.textureDiffuseColor
                    }
                },
                block.get(),
            )
        }

        // Cauldrons
        /*for (cauldron: DeferredBlock<*> in RagiumBlocks.CAULDRONS) {
            event.register(
                { state: BlockState, getter: BlockAndTintGetter?, pos: BlockPos?, tint: Int ->
                    val content: CauldronFluidContent =
                        CauldronFluidContent.getForBlock(state.block) ?: return@register -1
                    when {
                        getter != null && pos != null ->
                            IClientFluidTypeExtensions
                                .of(
                                    content.fluid,
                                ).getTintColor(state.fluidState, getter, pos)
                        else -> -1
                    }
                },
                cauldron.get(),
            )
        }*/

        RagiumAPI.LOGGER.info("Registered BlockColor!")
    }

    private fun registerItemColor(event: RegisterColorHandlersEvent.Item) {
        // Water Collector
        event.register({ _: ItemStack, tint: Int -> if (tint == 0) 0x3f76e4 else -1 }, RagiumBlocks.FLUID_COLLECTOR)
        // LED Blocks
        for ((variant: HTColorMaterial, block: HTSimpleDeferredBlock) in RagiumBlocks.LED_BLOCKS) {
            event.register(
                { _: ItemStack, tint: Int -> if (tint != 0) -1 else variant.dyeColor.textureDiffuseColor },
                block.get(),
            )
        }

        // Buckets
        for (bucket: HTDeferredItem<*> in RagiumFluidContents.REGISTER.itemEntries) {
            event.register(DynamicFluidContainerModel.Colors(), bucket)
        }
        // Backpack
        event.register(
            { stack: ItemStack, tint: Int ->
                when {
                    tint != 0 -> -1
                    else -> stack.get(RagiumDataComponents.COLOR)?.textureDiffuseColor ?: -1
                }
            },
            RagiumItems.UNIVERSAL_BUNDLE,
        )
        // Potion Drop
        event.register(
            { stack: ItemStack, tint: Int ->
                if (tint == 0) {
                    stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).color
                } else {
                    -1
                }
            },
            RagiumItems.POTION_DROP,
        )

        RagiumAPI.LOGGER.info("Registered ItemColor!")
    }

    private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        // Fluid
        event.registerFluidType(
            HTSimpleFluidExtensions(vanillaId("block", "honey_block_top")),
            RagiumFluidContents.HONEY.getType(),
        )
        event.dull(RagiumFluidContents.MUSHROOM_STEW, Color(0xcc9966))
        event.dull(RagiumFluidContents.CREAM, Color(0xffffcc))
        event.registerFluidType(
            HTSimpleFluidExtensions(RagiumAPI.id("block", "chocolate")),
            RagiumFluidContents.CHOCOLATE.getType(),
        )
        event.clear(RagiumFluidContents.RAGI_CHERRY_JUICE, Color(0xcccc66))

        event.dull(RagiumFluidContents.SLIME, Color(0x66cc66))
        event.molten(RagiumFluidContents.GELLED_EXPLOSIVE, Color(0x339933))
        event.dull(RagiumFluidContents.CRUDE_BIO, Color(0x336600))
        event.clear(RagiumFluidContents.BIOFUEL, Color(0x99cc00))

        event.dull(RagiumFluidContents.CRUDE_OIL, Color(0x333333))
        event.clear(RagiumFluidContents.NATURAL_GAS, Color(0xcccccc))
        event.dull(RagiumFluidContents.NAPHTHA, Color(0xff6633))
        event.clear(RagiumFluidContents.FUEL, Color(0xcc3300))
        event.dull(RagiumFluidContents.LUBRICANT, Color(0xff9900))

        event.clear(RagiumFluidContents.SAP, Color(0x996633))
        event.clear(RagiumFluidContents.SPRUCE_RESIN, Color(0xcc6600))

        event.molten(RagiumFluidContents.DESTABILIZED_RAGINITE, Color(0xff0033))

        event.clear(RagiumFluidContents.EXPERIENCE, Color(0x66ff33))
        event.clear(RagiumFluidContents.COOLANT, Color(0x009999))

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val color: Color = data.color
            // molten
            event.molten(data.molten, color)
            // sap
            val sap: HTFluidContent<*, *, *, *, *> = data.sap ?: continue
            event.dull(sap, color)
        }

        // Item
        event.registerItem(
            object : IClientItemExtensions {
                override fun getCustomRenderer(): HTFuelGeneratorItemRenderer = HTFuelGeneratorItemRenderer
            },
            RagiumBlocks.THERMAL_GENERATOR.asItem(),
            RagiumBlocks.CULINARY_GENERATOR.asItem(),
            RagiumBlocks.MAGMATIC_GENERATOR.asItem(),
            RagiumBlocks.COMBUSTION_GENERATOR.asItem(),
            RagiumBlocks.ENCHANTMENT_GENERATOR.asItem(),
        )

        RagiumAPI.LOGGER.info("Registered client extensions!")
    }

    private fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(RagiumMenuTypes.ACCESS_CONFIG.get(), ::HTAccessConfigurationScreen)
        event.register(RagiumMenuTypes.POTION_BUNDLE.get(), ::HTGenericScreen)
        event.register(RagiumMenuTypes.UNIVERSAL_BUNDLE.get(), ::HTGenericScreen)

        event.register(RagiumMenuTypes.ALLOY_SMELTER, ::HTProcessorScreen)
        event.register(RagiumMenuTypes.BREWERY, HTSingleFluidProcessorScreen.Companion::combine)
        event.register(RagiumMenuTypes.COMBUSTION_GENERATOR, ::HTCombustionGeneratorScreen)
        event.register(RagiumMenuTypes.COMPRESSOR, HTSingleFluidProcessorScreen.Companion::itemWithCatalyst)
        event.register(RagiumMenuTypes.CUTTING_MACHINE, ::HTProcessorScreen)
        event.register(RagiumMenuTypes.DRUM, ::HTDrumScreen)
        event.register(RagiumMenuTypes.ENCHANTER, HTSingleFluidProcessorScreen.Companion::combine)
        event.register(RagiumMenuTypes.ENERGY_NETWORK_ACCESS, ::HTEnergyNetworkAccessScreen)
        event.register(RagiumMenuTypes.EXTRACTOR, HTSingleFluidProcessorScreen.Companion::itemWithCatalyst)
        event.register(RagiumMenuTypes.FLUID_COLLECTOR, ::HTFluidCollectorScreen)
        event.register(RagiumMenuTypes.ITEM_COLLECTOR, ::HTBlockEntityContainerScreen)
        event.register(RagiumMenuTypes.ITEM_GENERATOR, HTGeneratorScreen.createFactory("item_generator"))
        event.register(RagiumMenuTypes.MAGMATIC_GENERATOR, ::HTMagmaticGeneratorScreen)
        event.register(RagiumMenuTypes.MELTER, HTSingleFluidProcessorScreen.Companion::melter)
        event.register(RagiumMenuTypes.MIXER, ::HTMixerScreen)
        event.register(RagiumMenuTypes.MOB_CRUSHER, ::HTMobCrusherScreen)
        event.register(RagiumMenuTypes.PROCESSOR, ::HTProcessorScreen)
        event.register(RagiumMenuTypes.REFINERY, ::HTRefineryScreen)
        event.register(RagiumMenuTypes.SIMULATOR, HTSingleFluidProcessorScreen.Companion::itemWithCatalyst)
        event.register(RagiumMenuTypes.SINGLE_ITEM_WITH_FLUID, ::HTCrusherScreen)
        event.register(RagiumMenuTypes.SMELTER, HTProcessorScreen.createFactory("smelter"))
        event.register(RagiumMenuTypes.TELEPAD, ::HTTelepadScreen)

        RagiumAPI.LOGGER.info("Registered Screens!")
    }

    private fun registerLayerDefinitions(event: EntityRenderersEvent.RegisterLayerDefinitions) {
        event.registerLayerDefinition(RagiumModelLayers.FUEL_GENERATOR, HTFuelGeneratorModel::createLayer)

        for (location: ModelLayerLocation in RagiumModelLayers.DRUM_MINECARTS.values) {
            event.registerLayerDefinition(location, MinecartModel<*>::createBodyLayer)
        }

        RagiumAPI.LOGGER.info("Registered Layer Definitions!")
    }

    private fun registerClientReloadListeners(event: RegisterClientReloadListenersEvent) {
        event.registerReloadListener(HTFuelGeneratorItemRenderer)

        RagiumAPI.LOGGER.info("Registered Client Reload Listeners!")
    }

    private fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        // Block Entity
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.IMITATION_SPAWNER.get(), ::HTImitationSpawnerRenderer)

        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.MELTER.get(), ::HTSingleFluidMachineRenderer)
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.REFINERY.get(), ::HTRefineryRenderer)

        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.THERMAL_GENERATOR.get(), ::HTFuelGeneratorRenderer)

        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.CULINARY_GENERATOR.get(), ::HTFuelGeneratorRenderer)
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.MAGMATIC_GENERATOR.get(), ::HTFuelGeneratorRenderer)

        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.COMBUSTION_GENERATOR.get(), ::HTFuelGeneratorRenderer)
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.ENCHANTMENT_GENERATOR.get(), ::HTFuelGeneratorRenderer)

        for (tier: HTCrateTier in HTCrateTier.entries) {
            event.registerBlockEntityRenderer(tier.getBlockEntityType().get(), ::HTCrateRenderer)
        }
        // Entity
        for (type: HTDeferredEntityType<out HTAbstractCharge> in RagiumEntityTypes.CHARGES.values) {
            event.registerEntityRenderer(type.get(), ::ThrownItemRenderer)
        }
        event.registerEntityRenderer(RagiumEntityTypes.ELDRITCH_EGG.get(), ::ThrownItemRenderer)

        for (tier: HTDrumTier in HTDrumTier.entries) {
            val layerDefinition: ModelLayerLocation = RagiumModelLayers.DRUM_MINECARTS[tier] ?: continue
            event.registerEntityRenderer(
                tier.getEntityType().get(),
            ) { context: EntityRendererProvider.Context -> MinecartRenderer(context, layerDefinition) }
        }

        RagiumAPI.LOGGER.info("Registered Entity Renderers!")
    }

    private fun registerTooltipRenderer(event: RegisterClientTooltipComponentFactoriesEvent) {
        event.register(HTItemTooltipContent::class.java, ::HTClientItemTooltipComponent)

        RagiumAPI.LOGGER.info("Registered ClientTooltipComponents!")
    }

    private fun registerKeyMappings(event: RegisterKeyMappingsEvent) {
        event.register(RagiumKeyMappings.OPEN_UNIVERSAL_BUNDLE)

        RagiumAPI.LOGGER.info("Registered Key Mappings!")
    }

    //    Extensions    //

    private fun RegisterClientExtensionsEvent.clear(content: HTFluidContent<*, *, *, *, *>, color: Color) {
        this.registerFluidType(
            HTSimpleFluidExtensions(
                vanillaId("block", "water_still"),
                color,
                vanillaId("block", "water_flow"),
            ),
            content.getType(),
        )
    }

    private fun RegisterClientExtensionsEvent.dull(content: HTFluidContent<*, *, *, *, *>, color: Color) {
        this.registerFluidType(
            HTSimpleFluidExtensions(
                RagiumConst.NEOFORGE.toId("block", "milk_still"),
                color,
                RagiumConst.NEOFORGE.toId("block", "milk_flowing"),
            ),
            content.getType(),
        )
    }

    private fun RegisterClientExtensionsEvent.molten(content: HTFluidContent<*, *, *, *, *>, color: Color) {
        this.registerFluidType(
            HTSimpleFluidExtensions(
                RagiumAPI.id("block", "molten_still"),
                color,
                RagiumAPI.id("block", "molten_flow"),
            ),
            content.getType(),
        )
    }

    private fun <BE : HTBlockEntity> RegisterMenuScreensEvent.register(
        menuType: HTDeferredMenuType.WithContext<out HTBlockEntityContainerMenu<BE>, BE>,
        factory: HTBlockEntityScreenFactory<BE>,
    ) {
        this.register(menuType.get(), factory)
    }
}
