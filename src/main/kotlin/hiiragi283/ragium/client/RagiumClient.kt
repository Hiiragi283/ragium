package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.client.renderer.HTChargerRenderer
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.FoliageColor
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import net.neoforged.neoforge.registries.DeferredItem
import org.slf4j.Logger
import java.awt.Color

@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
class RagiumClient(eventBus: IEventBus) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(::registerBlockColor)
        eventBus.addListener(::registerItemColor)
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::registerEntityRenderer)
    }

    private fun registerBlockColor(event: RegisterColorHandlersEvent.Block) {
        // Exp Berry Bush
        event.register(
            { state: BlockState, getter: BlockAndTintGetter?, pos: BlockPos?, tint: Int ->
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
            { state: BlockState, getter: BlockAndTintGetter?, pos: BlockPos?, tint: Int ->
                if (tint != 0) return@register -1
                if (getter != null && pos != null) {
                    return@register BiomeColors.getAverageWaterColor(getter, pos)
                }
                -1
            },
            RagiumBlocks.WATER_COLLECTOR.get(),
        )

        LOGGER.info("Registered BlockColor!")
    }

    private fun registerItemColor(event: RegisterColorHandlersEvent.Item) {
        // Water Collector
        event.register(
            { stack: ItemStack, tint: Int ->
                if (tint == 0) 0x3f76e4 else -1
            },
            RagiumBlocks.WATER_COLLECTOR,
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
        register(RagiumFluidContents.CHOCOLATE, Color(0x663333))
        register(RagiumFluidContents.MUSHROOM_STEW, Color(0xcc9966))

        register(RagiumFluidContents.HYDROGEN, Color(0x3333ff))

        register(RagiumFluidContents.NITROGEN, Color(0x33ccff))
        register(RagiumFluidContents.AMMONIA, Color(0x9999ff))
        register(RagiumFluidContents.NITRIC_ACID, Color(0xcc99ff))
        register(RagiumFluidContents.MIXTURE_ACID, Color(0xff9900))

        register(RagiumFluidContents.OXYGEN, Color(0x66ccff))
        register(RagiumFluidContents.ROCKET_FUEL, Color(0x0066ff))

        register(RagiumFluidContents.ALKALI_SOLUTION, Color(0x0000cc))

        register(RagiumFluidContents.SULFUR_DIOXIDE, Color(0xff6600))
        register(RagiumFluidContents.SULFUR_TRIOXIDE, Color(0xff6600))
        register(RagiumFluidContents.SULFURIC_ACID, Color(0xff3300))

        register(RagiumFluidContents.CRUDE_OIL, Color(0x333333))
        register(RagiumFluidContents.NAPHTHA, Color(0xff9900))
        register(RagiumFluidContents.FUEL, Color(0xcc6633))
        register(RagiumFluidContents.NITRO_FUEL, Color(0xff33333))
        register(RagiumFluidContents.AROMATIC_COMPOUND, Color(0xcc6633))

        register(RagiumFluidContents.PLANT_OIL, Color(0x999933))
        register(RagiumFluidContents.BIOMASS, Color(0x006600))

        register(RagiumFluidContents.SAP, Color(0x996633))
        register(RagiumFluidContents.CRIMSON_SAP, Color(0x660000))
        register(RagiumFluidContents.WARPED_SAP, Color(0x006666))

        register(RagiumFluidContents.MOLTEN_RAGIUM, Color(0xff0033))

        LOGGER.info("Registered client extensions!")
    }

    private fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.CHARGER.get(), ::HTChargerRenderer)

        LOGGER.info("Registered BlockEntityRenderers!")
    }
}
