package hiiragi283.ragium.client

import hiiragi283.ragium.client.gui.HTGenericScreen
import hiiragi283.ragium.client.gui.HTMachineScreen
import hiiragi283.ragium.client.renderer.HTAlchemicalInfuserBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTItemDisplayBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTMultiMachineBlockEntityRenderer
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.block.entity.HTMultiblockController
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.network.HTInventoryPayload
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import java.awt.Color

@Environment(EnvType.CLIENT)
object RagiumClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerItems()
        registerBlocks()
        registerScreens()
        registerEvents()
        registerNetworks()

        Ragium.log { info("Ragium-Client initialized!") }
    }

    //    Items    //

    private fun registerItems() {
        RagiumItems.REGISTER.registerColors { item: Item, color: Color ->
            ColorProviderRegistry.ITEM.register({ _: ItemStack, _: Int -> color.rgb }, item)
        }
    }

    //    Blocks    //

    private fun registerBlocks() {
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutoutMipped(),
            RagiumBlocks.RAGINITE_ORE,
            RagiumBlocks.DEEPSLATE_RAGINITE_ORE,
            RagiumBlocks.MANUAL_GRINDER,
            RagiumBlocks.BURNING_BOX,
            RagiumBlocks.GEAR_BOX,
            RagiumBlocks.BLAZING_BOX,
            RagiumBlocks.ITEM_DISPLAY,
        )

        HTMachineType
            .getEntries()
            .map(HTMachineType::block)
            .forEach(RagiumClient::registerCutoutMipped)

        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.ALCHEMICAL_INFUSER) { HTAlchemicalInfuserBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.ITEM_DISPLAY) { HTItemDisplayBlockEntityRenderer }

        HTMachineType.Multi.entries
            .map(HTMachineType.Multi::blockEntityType)
            .forEach(::registerMultiblockRenderer)

        RagiElement.entries
            .map(RagiElement::clusterBlock)
            .forEach(::registerCutout)
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

    //    Screens    //

    private fun registerScreens() {
        HandledScreens.register(RagiumScreenHandlerTypes.MACHINE, ::HTMachineScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.BURNING_BOX, ::HTGenericScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.ALCHEMICAL_INFUSER, ::HTGenericScreen)
    }

    //    Events    //

    private fun registerEvents() {
    }

    //    Networks    //

    private fun registerNetworks() {
        ClientPlayNetworking.registerGlobalReceiver(
            RagiumNetworks.SET_STACK,
        ) { payload: HTInventoryPayload.Setter, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int, stack: ItemStack) = payload
            (context.player().world.getBlockEntity(pos) as? Inventory)?.setStack(slot, stack)
        }

        ClientPlayNetworking.registerGlobalReceiver(
            RagiumNetworks.REMOVE_STACK,
        ) { payload: HTInventoryPayload.Remover, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int) = payload
            (context.player().world.getBlockEntity(pos) as? Inventory)?.removeStack(slot)
        }
    }
}
