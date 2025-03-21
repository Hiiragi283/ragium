package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumFluidTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import org.slf4j.Logger

@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
class RagiumClient(eventBus: IEventBus) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::addItemColor)
    }

    private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        // Fluid
        event.registerFluidType(
            HTSimpleFluidExtensions(ResourceLocation.withDefaultNamespace("block/honey_block_top")),
            RagiumFluidTypes.HONEY,
        )
        event.registerFluidType(
            HTSimpleFluidExtensions(ResourceLocation.withDefaultNamespace("block/snow")),
            RagiumFluidTypes.SNOW,
        )
        event.registerFluidType(
            HTSimpleFluidExtensions(ResourceLocation.withDefaultNamespace("block/black_concrete_powder")),
            RagiumFluidTypes.CRUDE_OIL,
        )

        for (fluid: RagiumVirtualFluids in RagiumVirtualFluids.entries) {
            val textureId: ResourceLocation = when (fluid.textureType) {
                RagiumVirtualFluids.TextureType.GASEOUS -> "block/white_concrete"
                RagiumVirtualFluids.TextureType.LIQUID -> "block/bone_block_side"
                RagiumVirtualFluids.TextureType.MOLTEN -> "block/dead_bubble_coral_block"
                RagiumVirtualFluids.TextureType.STICKY -> "block/quartz_block_bottom"
            }.let(ResourceLocation::withDefaultNamespace)
            event.registerFluidType(HTSimpleFluidExtensions(textureId, fluid.color), fluid.get().fluidType)
        }

        LOGGER.info("Registered client extensions!")
    }

    private fun addItemColor(event: RegisterColorHandlersEvent.Item) {
        event.register(DynamicFluidContainerModel.Colors(), *RagiumItems.FLUID_CUBES)

        LOGGER.info("Registered ItemColor!")
    }
}
