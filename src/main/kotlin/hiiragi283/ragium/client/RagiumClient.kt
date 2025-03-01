package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import hiiragi283.ragium.client.renderer.HTBlastFurnaceBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTFlareRenderer
import hiiragi283.ragium.client.screen.*
import hiiragi283.ragium.common.init.*
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import org.slf4j.Logger
import java.util.function.Supplier

@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
class RagiumClient(eventBus: IEventBus, container: ModContainer) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::registerMenu)
        eventBus.addListener(::registerBlockEntityRenderer)
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

        RagiumVirtualFluids.entries.forEach { fluid: RagiumVirtualFluids ->
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
        event.register(RagiumMenuTypes.POTION_BUNDLE.get(), ::HTPotionBundleContainer)

        event.register(RagiumMenuTypes.ASSEMBLER.get(), ::HTAssemblerContainer)
        event.register(RagiumMenuTypes.BLAST_FURNACE.get(), ::HTBlastFurnaceContainer)
        event.register(RagiumMenuTypes.BREWERY.get(), ::HTBreweryContainer)
        event.register(RagiumMenuTypes.EXTRACTOR.get(), ::HTExtractorContainer)
        event.register(RagiumMenuTypes.INFUSER.get(), ::HTInfuserContainer)
        event.register(RagiumMenuTypes.LASER_ASSEMBLY.get(), ::HTLaserAssemblyContainer)
        event.register(RagiumMenuTypes.MIXER.get(), ::HTMixerContainer)
        event.register(RagiumMenuTypes.PRIMITIVE_BLAST_FURNACE.get(), ::HTPrimitiveBlastFurnaceContainer)
        event.register(RagiumMenuTypes.REFINERY.get(), ::HTRefineryContainer)
        event.register(RagiumMenuTypes.SINGLE_ITEM.get(), ::HTSingleItemContainer)
        event.register(RagiumMenuTypes.SOLIDIFIER.get(), ::HTSolidifierContainer)

        LOGGER.info("Registered machine screens!")
    }

    private fun registerBlockEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        fun <T> register(type: Supplier<out BlockEntityType<out T>>) where T : HTMachineBlockEntity, T : HTMultiblockController {
            event.registerBlockEntityRenderer(type.get(), ::HTBlastFurnaceBlockEntityRenderer)
        }

        register(RagiumBlockEntityTypes.BLAST_FURNACE)
        register(RagiumBlockEntityTypes.PRIMITIVE_BLAST_FURNACE)

        event.registerEntityRenderer(RagiumEntityTypes.DYNAMITE.get(), ::ThrownItemRenderer)
        event.registerEntityRenderer(RagiumEntityTypes.FLARE.get(), ::HTFlareRenderer)

        LOGGER.info("Registered BlockEntityRenderers!")
    }

    fun clientSetup(event: FMLClientSetupEvent) {
        /*ItemProperties.registerGeneric(
            RagiumAPI.id("machine_tier"),
        ) { stack: ItemStack, level: ClientLevel?, player: LivingEntity?, seed: Int ->
            when (stack.machineTier) {
                HTMachineTier.BASIC -> 0.4f
                HTMachineTier.ADVANCED -> 0.6f
                HTMachineTier.ELITE -> 0.8f
                HTMachineTier.ULTIMATE -> 1f
            }
        }

        HTMultiblockComponentRendererRegistry.register(
            RagiumMultiblockComponentTypes.SIMPLE.get(),
            HTMultiblockComponentRenderer.BlockRenderer<HTSimpleMultiblockComponent> {
                _: HTControllerDefinition,
                _: Level,
                component: HTSimpleMultiblockComponent,
                ->
                component.block.defaultBlockState()
            },
        )

        HTMultiblockComponentRendererRegistry.register(
            RagiumMultiblockComponentTypes.TAG.get(),
            HTMultiblockComponentRenderer.BlockRenderer<HTTagMultiblockComponent> {
                _: HTControllerDefinition,
                world: Level,
                component: HTTagMultiblockComponent,
                ->
                null
            },
        )

        HTMultiblockComponentRendererRegistry.register(
            RagiumMultiblockComponentTypes.AXIS.get(),
            HTMultiblockComponentRenderer.BlockRenderer<HTAxisMultiblockComponent> {
                controller: HTControllerDefinition,
                world: Level,
                component: HTAxisMultiblockComponent,
                ->
                component.getPlacementState(controller)
            },
        )*/

        LOGGER.info("Loaded client setup!")
    }
}
