package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.client.renderer.HTMachineBlockEntityRenderer
import hiiragi283.ragium.api.client.renderer.HTMultiblockComponentRenderer
import hiiragi283.ragium.api.client.renderer.HTMultiblockComponentRendererRegistry
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.inventory.HTMachineMenuType
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import hiiragi283.ragium.client.screen.HTMachineContainerScreen
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.multiblock.HTAxisMultiblockComponent
import hiiragi283.ragium.common.multiblock.HTSimpleMultiblockComponent
import hiiragi283.ragium.common.multiblock.HTTagMultiblockComponent
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.FastColor
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.registries.DeferredHolder
import org.slf4j.Logger
import java.util.function.Supplier

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object RagiumClient {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun registerItemColor(event: RegisterColorHandlersEvent.Item) {
        event.register(
            { stack: ItemStack, tintIndex: Int ->
                if (tintIndex != 1) {
                    -1
                } else {
                    FastColor.ARGB32.opaque(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).color)
                }
            },
            RagiumItems.POTION_CAN,
        )

        LOGGER.info("Registered ItemColor!")
    }

    @SubscribeEvent
    fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
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
            HTSimpleFluidExtensions(ResourceLocation.withDefaultNamespace("block/slime_block")),
            RagiumFluidTypes.SLIME,
        )
        event.registerFluidType(
            HTSimpleFluidExtensions(ResourceLocation.withDefaultNamespace("block/black_concrete_powder")),
            RagiumFluidTypes.CRUDE_OIL,
        )

        RagiumVirtualFluids.entries.forEach { fluid: RagiumVirtualFluids ->
            val textureId: ResourceLocation = when (fluid.textureType) {
                RagiumVirtualFluids.TextureType.GASEOUS -> "block/white_concrete"
                RagiumVirtualFluids.TextureType.LIQUID -> "block/bone_block_side"
                RagiumVirtualFluids.TextureType.STICKY -> "block/quartz_block_bottom"
            }.let(ResourceLocation::withDefaultNamespace)
            event.registerFluidType(HTSimpleFluidExtensions(textureId, fluid.color), fluid.get().fluidType)
        }

        LOGGER.info("Registered client extensions!")
    }

    @SubscribeEvent
    fun registerMenu(event: RegisterMenuScreensEvent) {
        RagiumMenuTypes.REGISTER.forEach { holder: DeferredHolder<MenuType<*>, out MenuType<*>> ->
            val menuType: MenuType<*> = holder.get()
            if (menuType is HTMachineMenuType<*>) {
                event.register(menuType, ::HTMachineContainerScreen)
            }
        }

        LOGGER.info("Registered machine screens!")
    }

    @SubscribeEvent
    fun registerBlockEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        fun <T> register(type: Supplier<out BlockEntityType<out T>>) where T : HTMachineBlockEntity, T : HTMultiblockController {
            event.registerBlockEntityRenderer(type.get(), ::HTMachineBlockEntityRenderer)
        }

        register(RagiumBlockEntityTypes.BLAST_FURNACE)
        register(RagiumBlockEntityTypes.PRIMITIVE_BLAST_FURNACE)

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
        )

        LOGGER.info("Loaded client setup!")
    }
}
