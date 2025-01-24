package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.client.renderer.HTMachineBlockEntityRenderer
import hiiragi283.ragium.api.client.renderer.HTMultiblockComponentRenderer
import hiiragi283.ragium.api.client.renderer.HTMultiblockComponentRendererRegistry
import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.client.screen.HTMachineContainerScreen
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.machine.HTAxisMultiblockComponent
import hiiragi283.ragium.common.machine.HTSimpleMultiblockComponent
import hiiragi283.ragium.common.machine.HTTagMultiblockComponent
import hiiragi283.ragium.common.machine.HTTieredMultiblockComponent
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import org.slf4j.Logger
import java.util.function.Supplier

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object RagiumClient {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            event.registerFluidType(fluid, fluid.typeHolder)
        }

        LOGGER.info("Registered client extensions!")
    }

    @SubscribeEvent
    private fun registerMenu(event: RegisterMenuScreensEvent) {
        fun register(type: Supplier<out MenuType<out HTMachineContainerMenu>>) {
            event.register(type.get(), ::HTMachineContainerScreen)
        }

        register(RagiumMenuTypes.DEFAULT_MACHINE)
        register(RagiumMenuTypes.LARGE_MACHINE)

        register(RagiumMenuTypes.DISTILLATION_TOWER)
        register(RagiumMenuTypes.MULTI_SMELTER)

        LOGGER.info("Registered machine screens!")
    }

    @SubscribeEvent
    private fun registerBlockEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        fun register(type: Supplier<out BlockEntityType<out HTMachineBlockEntity>>) {
            event.registerBlockEntityRenderer(type.get(), ::HTMachineBlockEntityRenderer)
        }

        register(RagiumBlockEntityTypes.DEFAULT_GENERATOR)

        register(RagiumBlockEntityTypes.DEFAULT_PROCESSOR)
        register(RagiumBlockEntityTypes.LARGE_PROCESSOR)
        register(RagiumBlockEntityTypes.DISTILLATION_TOWER)
        register(RagiumBlockEntityTypes.MULTI_SMELTER)

        event.registerEntityRenderer(RagiumEntityTypes.DYNAMITE.get(), ::ThrownItemRenderer)

        LOGGER.info("Registered BlockEntityRenderers!")
    }

    @SubscribeEvent
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
        }*/

        HTMultiblockComponentRendererRegistry.register(
            RagiumMultiblockComponentTypes.SIMPLE.get(),
            HTMultiblockComponentRenderer.BlockRenderer<HTSimpleMultiblockComponent> {
                    _: HTControllerDefinition,
                    _: Level,
                    component: HTSimpleMultiblockComponent,
                ->
                component.block.get().defaultBlockState()
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
            RagiumMultiblockComponentTypes.TIER.get(),
            HTMultiblockComponentRenderer.BlockRenderer<HTTieredMultiblockComponent> {
                    controller: HTControllerDefinition,
                    world: Level,
                    component: HTTieredMultiblockComponent,
                ->
                component.getBlock(controller).defaultBlockState()
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
        )

        LOGGER.info("Loaded client setup!")
    }
}
