package hiiragi283.ragium.client

import hiiragi283.ragium.client.gui.HTGenericScreen
import hiiragi283.ragium.client.gui.HTMachineScreen
import hiiragi283.ragium.client.model.HTMachineModel
import hiiragi283.ragium.client.renderer.HTAlchemicalInfuserBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTItemDisplayBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTMultiMachineBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTOblivionCubeEntityRenderer
import hiiragi283.ragium.client.util.registerGlobalReceiver
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.block.HTMachineBlockBase
import hiiragi283.ragium.common.block.entity.HTMultiblockController
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.machine.HTMachineBlockRegistry
import hiiragi283.ragium.common.network.HTFloatingItemPayload
import hiiragi283.ragium.common.network.HTInventoryPayload
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.model.loading.v1.BlockStateResolver
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.color.world.BiomeColors
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView
import net.minecraft.world.biome.FoliageColors

@Environment(EnvType.CLIENT)
object RagiumClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerBlocks()
        registerEntities()
        registerItems()
        registerScreens()
        registerEvents()
        registerNetworks()

        Ragium.log { info("Ragium-Client initialized!") }
    }

    //    Blocks    //

    private fun registerBlocks() {
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutoutMipped(),
            RagiumContents.RAGINITE_ORE,
            RagiumContents.DEEPSLATE_RAGINITE_ORE,
            RagiumContents.OBLIVION_CLUSTER,
        )

        HTMachineBlockRegistry.forEachBlock(RagiumClient::registerCutoutMipped)

        registerCutout(RagiumContents.RUBBER_SAPLING)
        registerCutout(RagiumContents.ITEM_DISPLAY)

        RagiElement.entries
            .map(RagiElement::clusterBlock)
            .forEach(::registerCutout)

        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.ALCHEMICAL_INFUSER) { HTAlchemicalInfuserBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.ITEM_DISPLAY) { HTItemDisplayBlockEntityRenderer }

        registerMultiblockRenderer(RagiumBlockEntityTypes.BLAST_FURNACE)
        registerMultiblockRenderer(RagiumBlockEntityTypes.DISTILLATION_TOWER)

        ColorProviderRegistry.BLOCK.register({ _: BlockState, world: BlockRenderView?, pos: BlockPos?, _: Int ->
            when {
                world != null && pos != null -> BiomeColors.getFoliageColor(world, pos)
                else -> FoliageColors.getDefaultColor()
            }
        }, RagiumContents.RUBBER_LEAVES)
    }

    private fun registerCutout(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
    }

    private fun registerCutoutMipped(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped())
    }

    private fun <T> registerMultiblockRenderer(type: BlockEntityType<T>) where T : BlockEntity, T : HTMultiblockController {
        BlockEntityRendererFactories.register(type, ::HTMultiMachineBlockEntityRenderer)
    }

    //    Entities    //

    private fun registerEntities() {
        EntityRendererRegistry.register(RagiumEntityTypes.OBLIVION_CUBE, ::HTOblivionCubeEntityRenderer)
        EntityModelLayerRegistry.registerModelLayer(HTOblivionCubeEntityRenderer.ENTITY_MODEL_LAYER) {
            val modelData = ModelData()
            modelData.root.addChild(
                EntityModelPartNames.CUBE,
                ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F),
                ModelTransform.pivot(0F, 0F, 0F),
            )
            TexturedModelData.of(modelData, 64, 64)
        }
    }

    //    Items    //

    private fun registerItems() {
        RagiumContents.Fluids.entries.forEach { fluid: RagiumContents.Fluids ->
            ColorProviderRegistry.ITEM.register({ _: ItemStack, _: Int -> fluid.color.rgb }, fluid)
        }

        ColorProviderRegistry.ITEM.register(
            { _: ItemStack, _: Int -> FoliageColors.getDefaultColor() },
            RagiumContents.RUBBER_LEAVES,
        )
    }

    //    Screens    //

    private fun registerScreens() {
        HandledScreens.register(RagiumScreenHandlerTypes.MACHINE, ::HTMachineScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.BURNING_BOX, ::HTGenericScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.ALCHEMICAL_INFUSER, ::HTGenericScreen)
    }

    //    Events    //

    private fun registerEvents() {
        ModelLoadingPlugin.register { context: ModelLoadingPlugin.Context ->
            // register block state resolver
            HTMachineBlockRegistry.forEachBlock { block: HTMachineBlockBase ->
                context.registerBlockStateResolver(block) { context1: BlockStateResolver.Context ->
                    context1.block().stateManager.states.forEach { state: BlockState ->
                        context1.setModel(state, HTMachineModel)
                    }
                }
            }
            // register item model resolver
            context.modifyModelOnLoad().register onLoad@{ original: UnbakedModel, _: ModelModifier.OnLoad.Context ->
                when (RagiumModels.MACHINE_MODEL_ID) {
                    in original.modelDependencies -> HTMachineModel
                    else -> original
                }
            }
        }
    }

    //    Networks    //

    private fun registerNetworks() {
        RagiumNetworks.FLOATING_ITEM.registerGlobalReceiver { payload: HTFloatingItemPayload, context: ClientPlayNetworking.Context ->
            context.client().gameRenderer.showFloatingItem(payload.stack)
        }

        RagiumNetworks.SET_STACK.registerGlobalReceiver { payload: HTInventoryPayload.Setter, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int, stack: ItemStack) = payload
            (context.player().world.getBlockEntity(pos) as? Inventory)?.setStack(slot, stack)
        }

        RagiumNetworks.REMOVE_STACK.registerGlobalReceiver { payload: HTInventoryPayload.Remover, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int) = payload
            (context.player().world.getBlockEntity(pos) as? Inventory)?.removeStack(slot)
        }
    }
}
