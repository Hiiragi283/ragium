package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.client.extension.getBlockEntity
import hiiragi283.ragium.client.extension.registerClientReceiver
import hiiragi283.ragium.client.gui.*
import hiiragi283.ragium.client.model.HTFluidCubeModel
import hiiragi283.ragium.client.model.HTProcessorMachineModel
import hiiragi283.ragium.client.renderer.*
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.network.HTFloatingItemPayload
import hiiragi283.ragium.common.network.HTFluidSyncPayload
import hiiragi283.ragium.common.network.HTInventoryPayload
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

@Environment(EnvType.CLIENT)
object RagiumClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerBlocks()
        registerEntities()
        registerFluids()
        registerItems()
        registerScreens()
        registerEvents()
        registerNetworks()

        RagiumAPI.log { info("Ragium-Client initialized!") }
    }

    //    Blocks    //

    @JvmStatic
    private fun registerBlocks() {
        // cutout
        buildList {
            addAll(RagiumContents.Ores.entries)
            addAll(RagiumContents.Grates.entries)
            addAll(RagiumContents.Hulls.entries)
            addAll(RagiumContents.Exporters.entries)
            addAll(RagiumContents.Pipes.entries)
        }.map(HTContent<Block>::value).forEach(::registerCutoutMipped)

        RagiumAPI
            .getInstance()
            .machineRegistry.blocks
            .forEach(::registerCutoutMipped)

        registerCutoutMipped(RagiumBlocks.ITEM_DISPLAY)
        registerCutoutMipped(RagiumBlocks.POROUS_NETHERRACK)
        // block entity renderer
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.BEDROCK_MINER) { HTBedrockMinerBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.MANUAL_FORGE) { HTManualForgeBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.ITEM_DISPLAY) { HTItemDisplayBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.LARGE_PROCESSOR) { HTLargeProcessorBlockEntityRenderer }

        registerMachineRenderer(RagiumBlockEntityTypes.BLAST_FURNACE)
        registerMachineRenderer(RagiumBlockEntityTypes.DISTILLATION_TOWER)
        registerMachineRenderer(RagiumBlockEntityTypes.FLUID_DRILL)
        registerMachineRenderer(RagiumBlockEntityTypes.MULTI_SMELTER)
        registerMachineRenderer(RagiumBlockEntityTypes.SAW_MILL)

        ColorProviderRegistry.BLOCK.register({ state: BlockState, _: BlockRenderView?, _: BlockPos?, _: Int ->
            state.getOrNull(RagiumBlockProperties.COLOR)?.fireworkColor ?: -1
        }, RagiumBlocks.BACKPACK_INTERFACE)
    }

    @JvmStatic
    private fun registerCutout(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
    }

    @JvmStatic
    private fun registerCutoutMipped(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped())
    }

    @JvmStatic
    private fun <T : HTMachineBlockEntityBase> registerMachineRenderer(type: BlockEntityType<T>) {
        BlockEntityRendererFactories.register(type) { HTMultiblockMachineBlockEntityRenderer }
    }

    //    Entities    //

    @JvmStatic
    private fun registerEntities() {
        EntityRendererRegistry.register(RagiumEntityTypes.DYNAMITE, ::FlyingItemEntityRenderer)
    }

    //    Fluids    //

    @JvmStatic
    private fun registerFluids() {
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            FluidRenderHandlerRegistry.INSTANCE.register(
                fluid.value,
                SimpleFluidRenderHandler(
                    Identifier.of("block/white_concrete"),
                    Identifier.of("block/white_concrete"),
                    fluid.color.rgb,
                ),
            )
        }
    }

    //    Items    //

    @JvmStatic
    private fun registerItems() {
        ColorProviderRegistry.ITEM.register({ stack: ItemStack, _: Int ->
            stack.get(RagiumComponentTypes.COLOR)?.entityColor ?: -1
        }, RagiumItems.BACKPACK)
    }

    //    Screens    //

    @JvmStatic
    private fun registerScreens() {
        HandledScreens.register(RagiumScreenHandlerTypes.CHEMICAL_MACHINE, ::HTChemicalMachineScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.FLUID_DRILL, ::HTFluidDrillScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.LARGE_MACHINE, ::HTLargeMachineScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.SIMPLE_MACHINE, ::HTSimpleMachineScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.STEAM, ::HTSteamGeneratorScreen)
    }

    //    Events    //

    @JvmStatic
    private fun registerEvents() {
        ModelLoadingPlugin.register { context: ModelLoadingPlugin.Context ->
            // register item model resolver
            context.modifyModelOnLoad().register onLoad@{ original: UnbakedModel, _: ModelModifier.OnLoad.Context ->
                when {
                    RagiumAPI.id("block/dynamic_processor") in original.modelDependencies -> HTProcessorMachineModel.INACTIVE
                    RagiumAPI.id("block/active_dynamic_processor") in original.modelDependencies -> HTProcessorMachineModel.ACTIVE
                    RagiumAPI.id("item/fluid_cube") in original.modelDependencies -> HTFluidCubeModel
                    else -> original
                }
            }
            context.addModels(
                RagiumAPI.id("block/generator"),
                RagiumAPI.id("block/solar_generator"),
            )
        }
        ItemTooltipCallback.EVENT.register(
            RagiumAPI.id("description"),
        ) { stack: ItemStack, _: Item.TooltipContext, _: TooltipType, tooltips: MutableList<Text> ->
            stack.get(RagiumComponentTypes.DESCRIPTION)?.let(tooltips::add)
        }
    }

    //    Networks    //

    @JvmStatic
    private fun registerNetworks() {
        RagiumNetworks.FLOATING_ITEM.registerClientReceiver { payload: HTFloatingItemPayload, context: ClientPlayNetworking.Context ->
            context.client().gameRenderer.showFloatingItem(payload.stack)
        }

        RagiumNetworks.FLUID_SYNC.registerClientReceiver { payload: HTFluidSyncPayload, context: ClientPlayNetworking.Context ->
            val (index: Int, variant: FluidVariant, amount: Long) = payload
            val screen: Screen = context.client().currentScreen ?: return@registerClientReceiver
            if (screen is HTMachineScreenBase<*>) {
                screen.fluidCache[index] = variant
                screen.amountCache[index] = amount
            }
        }

        RagiumNetworks.MACHINE_SYNC.registerClientReceiver { payload: HTMachinePacket, context: ClientPlayNetworking.Context ->
            (context.getBlockEntity(payload.pos) as? HTMachineBlockEntityBase)?.onPacketReceived(payload)
        }

        RagiumNetworks.SET_STACK.registerClientReceiver { payload: HTInventoryPayload.Setter, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int, stack: ItemStack) = payload
            (context.getBlockEntity(pos) as? Inventory)?.setStack(slot, stack)
        }

        RagiumNetworks.REMOVE_STACK.registerClientReceiver { payload: HTInventoryPayload.Remover, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int) = payload
            (context.getBlockEntity(pos) as? Inventory)?.removeStack(slot)
        }
    }
}
