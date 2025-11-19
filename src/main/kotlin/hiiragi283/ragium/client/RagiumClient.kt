package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.impl.HTDeferredEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.client.event.HTClientItemTooltipComponent
import hiiragi283.ragium.client.event.HTItemTooltipContent
import hiiragi283.ragium.client.gui.screen.HTAccessConfigurationScreen
import hiiragi283.ragium.client.gui.screen.HTBlockEntityContainerScreen
import hiiragi283.ragium.client.gui.screen.HTDrumScreen
import hiiragi283.ragium.client.gui.screen.HTEnergyNetworkAccessScreen
import hiiragi283.ragium.client.gui.screen.HTFluidCollectorScreen
import hiiragi283.ragium.client.gui.screen.HTFuelGeneratorScreen
import hiiragi283.ragium.client.gui.screen.HTGenericScreen
import hiiragi283.ragium.client.gui.screen.HTProcessorScreen
import hiiragi283.ragium.client.gui.screen.HTRefineryScreen
import hiiragi283.ragium.client.gui.screen.HTSingleFluidProcessorScreen
import hiiragi283.ragium.client.gui.screen.HTTelepadScreen
import hiiragi283.ragium.client.key.RagiumKeyMappings
import hiiragi283.ragium.client.model.HTFuelGeneratorModel
import hiiragi283.ragium.client.renderer.RagiumModelLayers
import hiiragi283.ragium.client.renderer.block.HTCrateRenderer
import hiiragi283.ragium.client.renderer.block.HTFuelGeneratorRenderer
import hiiragi283.ragium.client.renderer.block.HTRefineryRenderer
import hiiragi283.ragium.client.renderer.block.HTSingleFluidMachineRenderer
import hiiragi283.ragium.client.renderer.item.HTFuelGeneratorItemRenderer
import hiiragi283.ragium.common.block.entity.HTBlockEntity
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
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
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
                    getter != null && pos != null -> BiomeColors.getAverageWaterColor(getter, pos)
                    else -> -1
                }
            },
            RagiumBlocks.WATER_COLLECTOR.get(),
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
        event.register({ _: ItemStack, tint: Int -> if (tint == 0) 0x3f76e4 else -1 }, RagiumBlocks.WATER_COLLECTOR)
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
        event.liquid(RagiumFluidContents.AWKWARD_WATER, Color(-0xc7a23a))
        event.registerFluidType(
            HTSimpleFluidExtensions(vanillaId("block", "honey_block_top")),
            RagiumFluidContents.HONEY.getType(),
        )
        event.liquid(RagiumFluidContents.EXPERIENCE, Color(0x66ff33))
        event.liquid(RagiumFluidContents.MUSHROOM_STEW, Color(0xcc9966))

        event.registerFluidType(
            HTSimpleFluidExtensions(RagiumAPI.id("block", "chocolate")),
            RagiumFluidContents.CHOCOLATE.getType(),
        )
        event.liquid(RagiumFluidContents.MEAT, Color(0xcc3333))
        event.liquid(RagiumFluidContents.ORGANIC_MUTAGEN, Color(0x336600))

        event.molten(RagiumFluidContents.CRUDE_OIL, Color(0x333333))
        event.liquid(RagiumFluidContents.NATURAL_GAS, Color(0xcccccc))
        event.liquid(RagiumFluidContents.NAPHTHA, Color(0xff9966))
        event.liquid(RagiumFluidContents.LUBRICANT, Color(0xff9900))

        event.liquid(RagiumFluidContents.FUEL, Color(0xcc3300))
        event.liquid(RagiumFluidContents.CRIMSON_FUEL, Color(0x663333))
        event.liquid(RagiumFluidContents.GREEN_FUEL, Color(0x99cc33))

        event.liquid(RagiumFluidContents.SAP, Color(0x996633))

        event.liquid(RagiumFluidContents.SULFURIC_ACID, Color(0xff3300))
        event.liquid(RagiumFluidContents.NITRIC_ACID, Color(0xcc99ff))
        event.liquid(RagiumFluidContents.MIXTURE_ACID, Color(0xcc6600))

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val color = Color(data.color)
            // molten
            event.molten(data.molten, color)
            // sap
            val sap: HTFluidContent<*, *, *> = data.sap ?: continue
            event.liquid(sap, color)
        }

        // Item
        event.registerItem(
            object : IClientItemExtensions {
                override fun getCustomRenderer(): HTFuelGeneratorItemRenderer = HTFuelGeneratorItemRenderer
            },
            RagiumBlocks.THERMAL_GENERATOR.asItem(),
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
        event.register(RagiumMenuTypes.BREWERY, HTSingleFluidProcessorScreen.Companion::brewery)
        event.register(RagiumMenuTypes.COMPRESSOR, ::HTProcessorScreen)
        event.register(RagiumMenuTypes.CRUSHER, HTSingleFluidProcessorScreen.Companion::chancedItemOutput)
        event.register(RagiumMenuTypes.CUTTING_MACHINE, ::HTProcessorScreen)
        event.register(RagiumMenuTypes.DRUM, ::HTDrumScreen)
        event.register(RagiumMenuTypes.ENERGY_NETWORK_ACCESS, ::HTEnergyNetworkAccessScreen)
        event.register(RagiumMenuTypes.EXTRACTOR, HTSingleFluidProcessorScreen.Companion::itemWithCatalyst)
        event.register(RagiumMenuTypes.FLUID_COLLECTOR, ::HTFluidCollectorScreen)
        event.register(RagiumMenuTypes.FUEL_GENERATOR, ::HTFuelGeneratorScreen)
        event.register(RagiumMenuTypes.ITEM_BUFFER, ::HTBlockEntityContainerScreen)
        event.register(RagiumMenuTypes.MELTER, HTSingleFluidProcessorScreen.Companion::melter)
        event.register(RagiumMenuTypes.CAPTURER, ::HTBlockEntityContainerScreen)
        event.register(RagiumMenuTypes.PLANTER, HTSingleFluidProcessorScreen.Companion::chancedItemOutput)
        event.register(RagiumMenuTypes.PULVERIZER, ::HTProcessorScreen)
        event.register(RagiumMenuTypes.REFINERY, ::HTRefineryScreen)
        event.register(RagiumMenuTypes.SIMULATOR, HTSingleFluidProcessorScreen.Companion::itemWithCatalyst)
        event.register(RagiumMenuTypes.SINGLE_ITEM, ::HTProcessorScreen)
        event.register(RagiumMenuTypes.SMELTER, ::HTProcessorScreen)
        event.register(RagiumMenuTypes.TELEPAD, ::HTTelepadScreen)
        event.register(RagiumMenuTypes.WASHER, HTSingleFluidProcessorScreen.Companion::chancedItemOutput)

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
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.MELTER.get(), ::HTSingleFluidMachineRenderer)
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.REFINERY.get(), ::HTRefineryRenderer)
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.WASHER.get(), ::HTSingleFluidMachineRenderer)

        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.THERMAL_GENERATOR.get(), ::HTFuelGeneratorRenderer)
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

    private fun RegisterClientExtensionsEvent.liquid(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.liquid(color), content.getType())
    }

    private fun RegisterClientExtensionsEvent.molten(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.molten(color), content.getType())
    }

    private fun <BE : HTBlockEntity> RegisterMenuScreensEvent.register(
        menuType: HTDeferredMenuType.WithContext<out HTBlockEntityContainerMenu<BE>, BE>,
        factory: HTBlockEntityScreenFactory<BE>,
    ) {
        this.register(
            menuType.get(),
            factory.partially1(menuType.id.withPath { "textures/gui/container/$it.png" }),
        )
    }
}

private typealias HTScreenFactory<MENU, SCREEN> = (ResourceLocation, MENU, Inventory, Component) -> SCREEN

private typealias HTBlockEntityScreenFactory<BE> = HTScreenFactory<HTBlockEntityContainerMenu<BE>, HTBlockEntityContainerScreen<BE>>
