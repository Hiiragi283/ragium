package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.util.HTPotionBundle
import hiiragi283.ragium.client.screen.gui.HTBasicMachineScreen
import hiiragi283.ragium.client.screen.gui.HTEnergyNetworkAccessScreen
import hiiragi283.ragium.client.screen.gui.HTFluidCollectorScreen
import hiiragi283.ragium.client.screen.gui.HTItemCollectorScreen
import hiiragi283.ragium.client.screen.gui.HTItemWithFluidToItemScreen
import hiiragi283.ragium.client.screen.gui.HTMelterScreen
import hiiragi283.ragium.client.screen.gui.HTRefineryScreen
import hiiragi283.ragium.client.screen.tooltip.HTClientPotionTooltip
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.FoliageColor
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import net.neoforged.neoforge.registries.DeferredItem
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
        eventBus.addListener(::registerBlockColor)
        eventBus.addListener(::registerItemColor)
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::registerScreens)
        eventBus.addListener(::registerEntityRenderer)
        eventBus.addListener(::registerTooltipRenderer)

        container.registerExtensionPoint(IConfigScreenFactory::class.java, IConfigScreenFactory(::ConfigurationScreen))
    }

    private fun registerBlockColor(event: RegisterColorHandlersEvent.Block) {
        // Exp Berry Bush
        event.register(
            { _: BlockState, getter: BlockAndTintGetter?, pos: BlockPos?, tint: Int ->
                if (tint != 0) return@register -1
                when {
                    getter != null && pos != null -> BiomeColors.getAverageFoliageColor(getter, pos)
                    else -> FoliageColor.getDefaultColor()
                }
            },
            RagiumBlocks.EXP_BERRY_BUSH.get(),
        )
        // Water Collector
        event.register(
            { _: BlockState, getter: BlockAndTintGetter?, pos: BlockPos?, tint: Int ->
                if (tint != 0) return@register -1
                if (getter != null && pos != null) {
                    return@register BiomeColors.getAverageWaterColor(getter, pos)
                }
                -1
            },
            RagiumBlocks.Devices.WATER_COLLECTOR.get(),
        )
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
        event.register(
            { _: ItemStack, tint: Int ->
                if (tint == 0) 0x3f76e4 else -1
            },
            RagiumBlocks.Devices.WATER_COLLECTOR,
        )

        // Crude Oil Bucket
        for (bucket: DeferredItem<*> in RagiumFluidContents.REGISTER.itemEntries) {
            event.register(DynamicFluidContainerModel.Colors(), bucket)
        }

        LOGGER.info("Registered ItemColor!")
    }

    private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        // Fluid
        event.registerFluidType(
            HTSimpleFluidExtensions(vanillaId("block/honey_block_top")),
            RagiumFluidContents.HONEY.getType(),
        )

        fun register(content: HTFluidContent<*, *, *>, color: Color) {
            event.registerFluidType(HTSimpleFluidExtensions(color), content.getType())
        }
        register(RagiumFluidContents.EXPERIENCE, Color(0x66ff33))
        register(RagiumFluidContents.MUSHROOM_STEW, Color(0xcc9966))

        register(RagiumFluidContents.CRUDE_OIL, Color(0x333333))
        register(RagiumFluidContents.LPG, Color(0xffcc99))
        register(RagiumFluidContents.NAPHTHA, Color(0xff9966))
        register(RagiumFluidContents.DIESEL, Color(0xff3300))
        register(RagiumFluidContents.CRIMSON_DIESEL, Color(0x663333))
        register(RagiumFluidContents.LUBRICANT, Color(0xff9900))

        register(RagiumFluidContents.SAP, Color(0x996633))
        register(RagiumFluidContents.SYRUP, Color(0xcc9966))
        register(RagiumFluidContents.CRIMSON_SAP, Color(0x660000))
        register(RagiumFluidContents.WARPED_SAP, Color(0x006666))

        LOGGER.info("Registered client extensions!")
    }

    private fun registerScreens(event: RegisterMenuScreensEvent) {
        fun registerBasic(
            menuType: HTDeferredMenuType<out HTDefinitionContainerMenu>,
            texture: ResourceLocation = menuType.id.withPath { "textures/gui/container/$it.png" },
        ) {
            event.register(menuType.get(), HTBasicMachineScreen.create(texture))
        }

        registerBasic(RagiumMenuTypes.ALLOY_SMELTER)
        registerBasic(RagiumMenuTypes.CRUSHER)
        registerBasic(RagiumMenuTypes.ENGRAVER)
        registerBasic(RagiumMenuTypes.EXTRACTOR)
        registerBasic(RagiumMenuTypes.FORMING_PRESS)
        registerBasic(RagiumMenuTypes.SINGLE_ITEM)

        event.register(RagiumMenuTypes.ENERGY_NETWORK_ACCESS.get(), ::HTEnergyNetworkAccessScreen)
        event.register(RagiumMenuTypes.FLUID_COLLECTOR.get(), ::HTFluidCollectorScreen)
        event.register(RagiumMenuTypes.INFUSER.get(), HTItemWithFluidToItemScreen::infuser)
        event.register(RagiumMenuTypes.ITEM_COLLECTOR.get(), ::HTItemCollectorScreen)
        event.register(RagiumMenuTypes.MELTER.get(), ::HTMelterScreen)
        event.register(RagiumMenuTypes.REFINERY.get(), ::HTRefineryScreen)
        event.register(RagiumMenuTypes.SOLIDIFIER.get(), HTItemWithFluidToItemScreen::solidifier)

        LOGGER.info("Registered Screens!")
    }

    private fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(RagiumEntityTypes.BLAST_CHARGE.get(), ::ThrownItemRenderer)
        event.registerEntityRenderer(RagiumEntityTypes.ELDRITCH_EGG.get(), ::ThrownItemRenderer)

        LOGGER.info("Registered BlockEntityRenderers!")
    }

    private fun registerTooltipRenderer(event: RegisterClientTooltipComponentFactoriesEvent) {
        event.register(HTPotionBundle::class.java, ::HTClientPotionTooltip)

        LOGGER.info("Registered ClientTooltipComponents!")
    }
}
