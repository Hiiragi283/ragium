package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.client.gui.screen.HTAccessConfigurationScreen
import hiiragi283.ragium.client.gui.screen.HTDrumScreen
import hiiragi283.ragium.client.gui.screen.HTEnergyNetworkAccessScreen
import hiiragi283.ragium.client.gui.screen.HTFluidCollectorScreen
import hiiragi283.ragium.client.gui.screen.HTFuelGeneratorScreen
import hiiragi283.ragium.client.gui.screen.HTGenericScreen
import hiiragi283.ragium.client.gui.screen.HTItemBufferScreen
import hiiragi283.ragium.client.gui.screen.HTItemToItemScreen
import hiiragi283.ragium.client.gui.screen.HTMachineScreen
import hiiragi283.ragium.client.gui.screen.HTMelterScreen
import hiiragi283.ragium.client.gui.screen.HTRefineryScreen
import hiiragi283.ragium.client.gui.screen.HTTelepadScreen
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.material.HTMoltenCrystalData
import hiiragi283.ragium.common.variant.HTColorMaterial
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import org.slf4j.Logger
import java.awt.Color

@OnlyIn(Dist.CLIENT)
@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
class RagiumClient(eventBus: IEventBus, container: ModContainer) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(::clientSetup)

        eventBus.addListener(::registerBlockColor)
        eventBus.addListener(::registerItemColor)
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::registerScreens)
        eventBus.addListener(::registerEntityRenderer)
        eventBus.addListener(::registerTooltipRenderer)

        container.registerExtensionPoint(IConfigScreenFactory::class.java, IConfigScreenFactory(::ConfigurationScreen))
    }

    private fun clientSetup(event: FMLClientSetupEvent) {
        LOGGER.info("Loaded client setup!")
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
            HTDeviceVariant.WATER_COLLECTOR.blockHolder.get(),
        )
        // LED Blocks
        for ((color: HTColorMaterial, block: HTSimpleDeferredBlock) in RagiumBlocks.LED_BLOCKS) {
            event.register(
                { _: BlockState, _: BlockAndTintGetter?, _: BlockPos?, tint: Int ->
                    when {
                        tint != 0 -> return@register -1
                        else -> color.color.textureDiffuseColor
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

        LOGGER.info("Registered BlockColor!")
    }

    private fun registerItemColor(event: RegisterColorHandlersEvent.Item) {
        // Water Collector
        event.register({ _: ItemStack, tint: Int -> if (tint == 0) 0x3f76e4 else -1 }, HTDeviceVariant.WATER_COLLECTOR)
        // LED Blocks
        for ((variant: HTColorMaterial, block: HTSimpleDeferredBlock) in RagiumBlocks.LED_BLOCKS) {
            event.register(
                { _: ItemStack, tint: Int -> if (tint != 0) -1 else variant.color.textureDiffuseColor },
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

        LOGGER.info("Registered ItemColor!")
    }

    private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        // Fluid
        event.registerFluidType(
            HTSimpleFluidExtensions(vanillaId("block/honey_block_top")),
            RagiumFluidContents.HONEY.getType(),
        )

        fun liquid(content: HTFluidContent<*, *, *>, color: Color) {
            event.registerFluidType(HTSimpleFluidExtensions.liquid(color), content.getType())
        }

        fun molten(content: HTFluidContent<*, *, *>, color: Color) {
            event.registerFluidType(HTSimpleFluidExtensions.molten(color), content.getType())
        }

        liquid(RagiumFluidContents.EXPERIENCE, Color(0x66ff33))
        liquid(RagiumFluidContents.MUSHROOM_STEW, Color(0xcc9966))

        molten(RagiumFluidContents.CRUDE_OIL, Color(0x333333))
        liquid(RagiumFluidContents.NATURAL_GAS, Color(0xcccccc))
        liquid(RagiumFluidContents.NAPHTHA, Color(0xff9966))
        liquid(RagiumFluidContents.FUEL, Color(0xcc3300))
        liquid(RagiumFluidContents.CRIMSON_FUEL, Color(0x663333))
        liquid(RagiumFluidContents.LUBRICANT, Color(0xff9900))

        liquid(RagiumFluidContents.SAP, Color(0x996633))

        for (data: HTMoltenCrystalData in HTMoltenCrystalData.entries) {
            val color = Color(data.color)
            // molten
            molten(data.molten, color)
            // sap
            val sap: HTFluidContent<*, *, *> = data.sap ?: continue
            liquid(sap, color)
        }

        LOGGER.info("Registered client extensions!")
    }

    private fun registerScreens(event: RegisterMenuScreensEvent) {
        fun <BE : HTMachineBlockEntity> registerMachine(menuType: HTDeferredMenuType<out HTBlockEntityContainerMenu<BE>>) {
            event.register(
                menuType.get(),
                HTMachineScreen.create(menuType.id.withPath { "textures/gui/container/$it.png" }),
            )
        }

        registerMachine(RagiumMenuTypes.ALLOY_SMELTER)
        registerMachine(RagiumMenuTypes.CRUSHER)
        registerMachine(RagiumMenuTypes.ENGRAVER)
        registerMachine(RagiumMenuTypes.SIMULATOR)
        registerMachine(RagiumMenuTypes.SINGLE_ITEM)
        registerMachine(RagiumMenuTypes.SMELTER)

        event.register(RagiumMenuTypes.POTION_BUNDLE.get(), ::HTGenericScreen)
        event.register(RagiumMenuTypes.UNIVERSAL_BUNDLE.get(), ::HTGenericScreen)

        event.register(RagiumMenuTypes.COMPRESSOR.get(), HTItemToItemScreen.Companion::compressor)
        event.register(RagiumMenuTypes.DRUM.get(), ::HTDrumScreen)
        event.register(RagiumMenuTypes.ENERGY_NETWORK_ACCESS.get(), ::HTEnergyNetworkAccessScreen)
        event.register(RagiumMenuTypes.EXTRACTOR.get(), HTItemToItemScreen.Companion::extractor)
        event.register(RagiumMenuTypes.FLUID_COLLECTOR.get(), ::HTFluidCollectorScreen)
        event.register(RagiumMenuTypes.FUEL_GENERATOR.get(), ::HTFuelGeneratorScreen)
        event.register(RagiumMenuTypes.ITEM_BUFFER.get(), ::HTItemBufferScreen)
        event.register(RagiumMenuTypes.MELTER.get(), ::HTMelterScreen)
        event.register(RagiumMenuTypes.PULVERIZER.get(), HTItemToItemScreen.Companion::pulverizer)
        event.register(RagiumMenuTypes.REFINERY.get(), ::HTRefineryScreen)
        event.register(RagiumMenuTypes.ACCESS_CONFIG.get(), ::HTAccessConfigurationScreen)
        event.register(RagiumMenuTypes.TELEPAD.get(), ::HTTelepadScreen)

        LOGGER.info("Registered Screens!")
    }

    private fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(RagiumEntityTypes.BLAST_CHARGE.get(), ::ThrownItemRenderer)
        event.registerEntityRenderer(RagiumEntityTypes.ELDRITCH_EGG.get(), ::ThrownItemRenderer)

        LOGGER.info("Registered BlockEntityRenderers!")
    }

    private fun registerTooltipRenderer(event: RegisterClientTooltipComponentFactoriesEvent) {
        LOGGER.info("Registered ClientTooltipComponents!")
    }
}
