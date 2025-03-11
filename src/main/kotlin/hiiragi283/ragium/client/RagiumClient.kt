package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import guideme.Guide
import guideme.GuideItemSettings
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.client.renderer.HTFlareRenderer
import hiiragi283.ragium.client.screen.*
import hiiragi283.ragium.common.entity.HTDynamite
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumFluidTypes
import hiiragi283.ragium.common.init.RagiumMenuTypes
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.util.*

@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
object RagiumClient {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val GUIDE: Guide = Guide
        .builder(RagiumAPI.id("guide"))
        .folder("ragium_guide")
        .itemSettings(
            GuideItemSettings(
                Optional.of(Component.translatable(RagiumTranslationKeys.GUIDE_NAME)),
                listOf(),
                Optional.of(RagiumAPI.id("item/ragi_navi")),
            ),
        ).build()

    init {
        MOD_BUS.addListener(::registerClientExtensions)
        MOD_BUS.addListener(::registerMenu)
        MOD_BUS.addListener(::registerBlockEntityRenderer)
        MOD_BUS.addListener(::clientSetup)
    }

    private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        // Fluid
        event.registerFluidType(
            HTSimpleFluidExtensions(ResourceLocation.withDefaultNamespace("block/glass")),
            RagiumFluidTypes.GLASS,
        )
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

    private fun registerMenu(event: RegisterMenuScreensEvent) {
        event.register(RagiumMenuTypes.POTION_BUNDLE.get(), ::HTPotionBundleScreen)

        event.register(RagiumMenuTypes.ASSEMBLER.get(), ::HTAssemblerScreen)
        event.register(RagiumMenuTypes.BREWERY.get(), ::HTBreweryScreen)
        event.register(RagiumMenuTypes.EXTRACTOR.get(), ::HTExtractorScreen)
        event.register(RagiumMenuTypes.INFUSER.get(), ::HTInfuserScreen)
        event.register(RagiumMenuTypes.MIXER.get(), ::HTMixerScreen)
        event.register(RagiumMenuTypes.PRIMITIVE_BLAST_FURNACE.get(), ::HTPrimitiveBlastFurnaceScreen)
        event.register(RagiumMenuTypes.REFINERY.get(), ::HTRefineryScreen)
        event.register(RagiumMenuTypes.SINGLE_ITEM.get(), ::HTSingleItemScreen)
        event.register(RagiumMenuTypes.SOLIDIFIER.get(), ::HTSolidifierScreen)

        LOGGER.info("Registered machine screens!")
    }

    private fun registerBlockEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        for (entityType: EntityType<out HTDynamite> in RagiumEntityTypes.getDynamites()) {
            event.registerEntityRenderer(entityType, ::ThrownItemRenderer)
        }
        event.registerEntityRenderer(RagiumEntityTypes.FLARE.get(), ::HTFlareRenderer)

        LOGGER.info("Registered BlockEntityRenderers!")
    }

    private fun clientSetup(event: FMLClientSetupEvent) {
        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onClientSetup(event)
        }

        LOGGER.info("Loaded client setup!")
    }
}
