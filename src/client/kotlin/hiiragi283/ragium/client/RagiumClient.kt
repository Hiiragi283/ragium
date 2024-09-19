package hiiragi283.ragium.client

import hiiragi283.ragium.client.gui.HTBurningBoxScreen
import hiiragi283.ragium.client.gui.HTMachineScreen
import hiiragi283.ragium.client.renderer.HTMultiMachineBlockEntityRenderer
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import hiiragi283.ragium.common.machine.HTMachineType
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.block.Block
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.awt.Color

@Environment(EnvType.CLIENT)
object RagiumClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerItems()
        registerBlocks()
        registerScreens()

        registerEvents()

        Ragium.log { info("Ragium-Client initialized!") }
    }

    private fun registerItems() {
        RagiumItems.REGISTER.registerColors { item: Item, color: Color ->
            ColorProviderRegistry.ITEM.register({ _: ItemStack, _: Int -> color.rgb }, item)
        }
    }

    private fun registerBlocks() {
        registerCutoutMipped(RagiumBlocks.RAGINITE_ORE)
        registerCutoutMipped(RagiumBlocks.DEEPSLATE_RAGINITE_ORE)
        registerCutoutMipped(RagiumBlocks.MANUAL_GRINDER)
        registerCutoutMipped(RagiumBlocks.BURNING_BOX)
        registerCutoutMipped(RagiumBlocks.GEAR_BOX)
        registerCutoutMipped(RagiumBlocks.BLAZING_BOX)

        HTMachineType
            .getEntries()
            .map(HTMachineType::block)
            .forEach(RagiumClient::registerCutoutMipped)

        HTMachineType.Multi.entries.forEach { type: HTMachineType.Multi ->
            BlockEntityRendererFactories.register(type.blockEntityType) { HTMultiMachineBlockEntityRenderer }
        }
    }

    private fun registerCutoutMipped(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped())
    }

    private fun registerScreens() {
        HandledScreens.register(RagiumScreenHandlerTypes.MACHINE, ::HTMachineScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.BURNING_BOX, ::HTBurningBoxScreen)
    }

    private fun registerEvents() {
        /*ItemTooltipCallback.EVENT.register { stack: ItemStack, context: Item.TooltipContext, type: TooltipType, tooltips: MutableList<Text> ->
            stack.get(RagiumComponentTypes.TOOLTIPS)?.appendTooltip(context, tooltips::add, type)
        }*/
    }
}
