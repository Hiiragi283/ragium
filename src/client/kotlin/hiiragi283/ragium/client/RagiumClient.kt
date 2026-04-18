package hiiragi283.ragium.client

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.event.HTRegisterWidgetRendererEvent
import hiiragi283.core.api.mod.HTClientMod
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.core.client.HTSimpleFluidExtensions
import hiiragi283.core.client.data.HCClientResourceProvider
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.data.RagiumClientResourceProvider
import hiiragi283.ragium.client.gui.widget.HTEnergySlotWidgetRenderer
import hiiragi283.ragium.client.render.HTBatteryRenderer
import hiiragi283.ragium.client.render.HTTankRenderer
import hiiragi283.ragium.client.render.block.HTCrateRenderer
import hiiragi283.ragium.client.render.block.HTImitationSpawnerRenderer
import hiiragi283.ragium.common.block.entity.storage.HTUniversalChestBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumWidgetTypes
import net.mehvahdjukaar.moonlight.api.platform.RegHelper
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import java.awt.Color

@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
data object RagiumClient : HTClientMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        configScreen(container)

        HCClientResourceProvider.addSupportedNamespaces(RagiumAPI.MOD_ID)

        RegHelper.registerDynamicResourceProvider(RagiumClientResourceProvider)
        RagiumAPI.LOGGER.info("Hiiragi-Core loaded on client side")
    }

    override fun registerWidgetRenderer(event: HTRegisterWidgetRendererEvent) {
        event.register(RagiumWidgetTypes.ENERGY_SLOT.get(), ::HTEnergySlotWidgetRenderer)
    }

    override fun registerBlockColors(event: RegisterColorHandlersEvent.Block) {
        // Universal Chest
        event.register(
            { _: BlockState, getter: BlockAndTintGetter?, pos: BlockPos?, tint: Int ->
                when {
                    tint != 0 -> -1
                    getter != null && pos != null -> {
                        val color: HTDefaultColor = getter
                            .getTypedBlockEntity<HTUniversalChestBlockEntity>(pos)
                            ?.color
                            ?: HTDefaultColor.WHITE
                        color.dyeColor.textureDiffuseColor
                    }
                    else -> -1
                }
            },
            RagiumBlocks.UNIVERSAL_CHEST.get(),
        )
    }

    override fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
        // Buckets
        val bucketColor = DynamicFluidContainerModel.Colors()
        for (item: ItemLike in RagiumFluids.REGISTER.asItemSequence()) {
            event.register(bucketColor, item)
        }
        // Colored items
        event.register(
            { stack: ItemStack, tint: Int ->
                when {
                    tint != 0 -> -1
                    else -> stack.get(HCDataComponents.COLOR)?.dyeColor?.textureDiffuseColor ?: -1
                }
            },
            RagiumBlocks.UNIVERSAL_CHEST,
            // RagiumItems.UNIVERSAL_BUNDLE,
        )
    }

    override fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        registerFluidExtensions(event)

        event.registerItem(
            object : IClientItemExtensions {
                override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer = HTBatteryRenderer.ItemRenderer
            },
            RagiumBlocks.BATTERY.asItem(),
            RagiumBlocks.CREATIVE_BATTERY.asItem(),
        )
        event.registerItem(
            object : IClientItemExtensions {
                override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer = HTTankRenderer.ItemRenderer
            },
            RagiumBlocks.TANK.asItem(),
            RagiumBlocks.CREATIVE_TANK.asItem(),
        )
    }

    private fun registerFluidExtensions(event: RegisterClientExtensionsEvent) {
        // Overworld
        event.clear(RagiumFluids.HYDROGEN, Color(0x3333cc))
        event.clear(RagiumFluids.STEAM, Color(0xcccccc))

        event.clear(RagiumFluids.OXYGEN, Color(0x00cccc))

        event.dull(RagiumFluids.CREOSOTE, Color(0x663333))
        event.clear(RagiumFluids.SYNTHETIC_GAS, Color(0xffcc99))
        event.molten(RagiumFluids.SYNTHETIC_OIL, Color(0x333344))

        event.dull(RagiumFluids.GLUE, Color(0xff9933))
        event.clear(RagiumFluids.METHANE, Color(0xcc9999))
        event.dull(RagiumFluids.CRUDE_BIO, Color(0x336600))
        event.clear(RagiumFluids.ETHANOL, Color(0x99cc66))
        event.clear(RagiumFluids.BIOFUEL, Color(0x66cc00))

        event.clear(RagiumFluids.NITROGEN, Color(0x0099cc))
        event.dull(RagiumFluids.LIQUID_NITROGEN, Color(0x0099cc))

        event.dull(RagiumFluids.NAOH_SOLUTION, Color(0x003399))
        event.dull(RagiumFluids.MERCURY, Color(0xcc99cc))
        // Nether
        event.dull(RagiumFluids.CRUDE_OIL, Color(0x333333))
        event.clear(RagiumFluids.NAPHTHA, Color(0xff6600))
        event.clear(RagiumFluids.FUEL, Color(0xff9900))

        event.clear(RagiumFluids.NITROGEN_DIOXIDE, Color(0x3399cc))
        event.clear(RagiumFluids.AMMONIA, Color(0x6699cc))
        event.dull(RagiumFluids.NITRIC_ACID, Color(0x9999cc))

        event.clear(RagiumFluids.SULFUR_DIOXIDE, Color(0xcc6600))
        event.clear(RagiumFluids.SULFUR_TRIOXIDE, Color(0xcc9900))
        event.dull(RagiumFluids.SULFURIC_ACID, Color(0xcccc00))
        // The End
        event.clear(RagiumFluids.HELIUM, Color(0xffff99))
        // End Game
        event.clear(RagiumFluids.RAGI_MATTER, Color(0xff0033))
    }

    override fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        // Block Entity
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.BATTERY.get(), HTBatteryRenderer::BlockRenderer)
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.CREATIVE_BATTERY.get(), HTBatteryRenderer::BlockRenderer)

        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.CRATE.get(), ::HTCrateRenderer)
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.CREATIVE_CRATE.get(), ::HTCrateRenderer)

        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.TANK.get(), HTTankRenderer::BlockRenderer)
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.CREATIVE_TANK.get(), HTTankRenderer::BlockRenderer)

        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.IMITATION_SPAWNER.get(), ::HTImitationSpawnerRenderer)
    }

    //    Extensions    //

    private fun RegisterClientExtensionsEvent.clear(content: HTFluidContent, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.clear(color), content.getFluidType())
    }

    private fun RegisterClientExtensionsEvent.dull(content: HTFluidContent, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.dull(color), content.getFluidType())
    }

    private fun RegisterClientExtensionsEvent.molten(content: HTFluidContent, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.molten(color), content.getFluidType())
    }
}
