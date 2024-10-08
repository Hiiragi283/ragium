package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.gui.HTGeneratorScreen
import hiiragi283.ragium.client.gui.HTGenericScreen
import hiiragi283.ragium.client.gui.HTProcessorScreen
import hiiragi283.ragium.client.model.HTMachineModel
import hiiragi283.ragium.client.renderer.HTAlchemicalInfuserBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTItemDisplayBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTMetaMachineBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTOblivionCubeEntityRenderer
import hiiragi283.ragium.client.util.registerClient
import hiiragi283.ragium.client.util.registerClientReceiver
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.network.HTFloatingItemPayload
import hiiragi283.ragium.common.network.HTInventoryPayload
import hiiragi283.ragium.common.network.HTOpenBackpackPayload
import hiiragi283.ragium.data.RagiumModels
import io.wispforest.accessories.api.AccessoriesCapability
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.block.Block
import net.minecraft.client.MinecraftClient
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
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

@Environment(EnvType.CLIENT)
object RagiumClient : ClientModInitializer {
    override fun onInitializeClient() {
        RagiumKeyBinds

        registerBlocks()
        registerEntities()
        registerItems()
        registerScreens()
        registerEvents()
        registerNetworks()

        RagiumAPI.log { info("Ragium-Client initialized!") }
    }

    //    Blocks    //

    private fun registerBlocks() {
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutoutMipped(),
            RagiumContents.RAGINITE_ORE,
            RagiumContents.DEEPSLATE_RAGINITE_ORE,
            RagiumContents.POROUS_NETHERRACK,
            RagiumContents.OBLIVION_CLUSTER,
            RagiumContents.META_MACHINE,
        )

        registerCutout(RagiumContents.ITEM_DISPLAY)

        RagiumContents.Element.entries
            .map(RagiumContents.Element::clusterBlock)
            .forEach(::registerCutout)

        RagiumFluids.PETROLEUM.registerClient(Identifier.of("block/black_concrete"))

        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.ALCHEMICAL_INFUSER) { HTAlchemicalInfuserBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.ITEM_DISPLAY) { HTItemDisplayBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.META_MACHINE) { HTMetaMachineBlockEntityRenderer }
    }

    private fun registerCutout(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
    }

    private fun registerCutoutMipped(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped())
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
    }

    //    Screens    //

    private fun registerScreens() {
        HandledScreens.register(RagiumScreenHandlerTypes.GENERATOR, ::HTGeneratorScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.PROCESSOR, ::HTProcessorScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.ALCHEMICAL_INFUSER, ::HTGenericScreen)
    }

    //    Events    //

    private fun registerEvents() {
        ModelLoadingPlugin.register { context: ModelLoadingPlugin.Context ->
            // register block state resolver
            /*HTMachineBlockRegistry.forEachBlock { block: HTMachineBlock ->
                context.registerBlockStateResolver(block) { context1: BlockStateResolver.Context ->
                    context1.block().stateManager.states.forEach { state: BlockState ->
                        context1.setModel(state, HTMachineModel)
                    }
                }
            }*/
            // register item model resolver
            context.modifyModelOnLoad().register onLoad@{ original: UnbakedModel, _: ModelModifier.OnLoad.Context ->
                when (RagiumModels.MACHINE_MODEL_ID) {
                    in original.modelDependencies -> HTMachineModel
                    else -> original
                }
            }
        }

        ClientTickEvents.END_CLIENT_TICK.register { client: MinecraftClient ->
            while (RagiumKeyBinds.OPEN_BACKPACK.wasPressed()) {
                val capability: AccessoriesCapability = client.player?.accessoriesCapability() ?: break
                when {
                    capability.isEquipped { it.contains(RagiumComponentTypes.INVENTORY) } -> HTOpenBackpackPayload.NORMAL
                    capability.isEquipped(RagiumContents.ENDER_BACKPACK) -> HTOpenBackpackPayload.ENDER
                    else -> break
                }.let(ClientPlayNetworking::send)
            }
        }
    }

    //    Networks    //

    private fun registerNetworks() {
        RagiumNetworks.FLOATING_ITEM.registerClientReceiver { payload: HTFloatingItemPayload, context: ClientPlayNetworking.Context ->
            context.client().gameRenderer.showFloatingItem(payload.stack)
        }

        RagiumNetworks.SET_STACK.registerClientReceiver { payload: HTInventoryPayload.Setter, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int, stack: ItemStack) = payload
            (context.player().world.getBlockEntity(pos) as? Inventory)?.setStack(slot, stack)
        }

        RagiumNetworks.REMOVE_STACK.registerClientReceiver { payload: HTInventoryPayload.Remover, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int) = payload
            (context.player().world.getBlockEntity(pos) as? Inventory)?.removeStack(slot)
        }
    }
}
