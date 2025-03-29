package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluidTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.FoliageColor
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import org.slf4j.Logger
import java.awt.Color

@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
class RagiumClient(eventBus: IEventBus) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::addBlockColor)
        eventBus.addListener(::addItemColor)
    }

    private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        // Fluid
        event.registerFluidType(HTSimpleFluidExtensions(vanillaId("block/honey_block_top")), RagiumFluidTypes.HONEY)
        event.registerFluidType(HTSimpleFluidExtensions(vanillaId("block/snow")), RagiumFluidTypes.SNOW)
        event.registerFluidType(HTSimpleFluidExtensions(Color(0x333333)), RagiumFluidTypes.CRUDE_OIL)

        for (fluid: RagiumVirtualFluids in RagiumVirtualFluids.entries) {
            event.registerFluidType(HTSimpleFluidExtensions(fluid.color), fluid.get().fluidType)
        }

        LOGGER.info("Registered client extensions!")
    }

    private val waterExtension: IClientFluidTypeExtensions by lazy { IClientFluidTypeExtensions.of(Fluids.WATER) }

    private fun addBlockColor(event: RegisterColorHandlersEvent.Block) {
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
                when {
                    getter != null && pos != null -> waterExtension.getTintColor(
                        state.fluidState,
                        getter,
                        pos,
                    )
                    else -> -1
                }
            },
            RagiumBlocks.WATER_COLLECTOR.get(),
        )

        LOGGER.info("Registered BlockColor!")
    }

    private fun addItemColor(event: RegisterColorHandlersEvent.Item) {
        // Exp Berry Bush
        event.register(
            { stack: ItemStack, tint: Int ->
                if (tint == 0) FoliageColor.getDefaultColor() else -1
            },
            RagiumBlocks.EXP_BERRY_BUSH,
        )
        // Water Collector
        event.register(
            { stack: ItemStack, tint: Int ->
                if (tint == 0) waterExtension.tintColor else -1
            },
            RagiumBlocks.WATER_COLLECTOR,
        )

        // Crude Oil Bucket
        for (bucket: RagiumItems.Buckets in RagiumItems.Buckets.entries) {
            event.register(DynamicFluidContainerModel.Colors(), bucket)
        }

        LOGGER.info("Registered ItemColor!")
    }
}
