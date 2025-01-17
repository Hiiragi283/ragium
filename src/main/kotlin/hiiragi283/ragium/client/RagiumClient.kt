package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.renderer.HTMachineBlockEntityRenderer
import hiiragi283.ragium.api.multiblock.renderer.HTMultiblockComponentRenderer
import hiiragi283.ragium.api.multiblock.renderer.HTMultiblockComponentRendererRegistry
import hiiragi283.ragium.client.screen.HTMachineContainerScreen
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMenuTypes
import hiiragi283.ragium.common.init.RagiumMultiblockComponentTypes
import hiiragi283.ragium.common.machine.HTAxisMultiblockComponent
import hiiragi283.ragium.common.machine.HTSimpleMultiblockComponent
import hiiragi283.ragium.common.machine.HTTagMultiblockComponent
import hiiragi283.ragium.common.machine.HTTieredMultiblockComponent
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
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
        event.register(RagiumMenuTypes.DEFAULT_MACHINE.get(), ::HTMachineContainerScreen)
        event.register(RagiumMenuTypes.LARGE_MACHINE.get(), ::HTMachineContainerScreen)

        LOGGER.info("Registered machine screens!")
    }

    @SubscribeEvent
    private fun registerBlockEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        fun register(type: Supplier<out BlockEntityType<out HTMachineBlockEntity>>) {
            event.registerBlockEntityRenderer(type.get(), HTMachineBlockEntityRenderer.PROVIDER)
        }

        register(RagiumBlockEntityTypes.LARGE_MACHINE)
        register(RagiumBlockEntityTypes.DEFAULT_MACHINE)
        register(RagiumBlockEntityTypes.MULTI_SMELTER)

        LOGGER.info("Registered BlockEntityRenderers!")
    }

    @SubscribeEvent
    fun clientSetup(event: FMLClientSetupEvent) {
        ItemProperties.registerGeneric(
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
