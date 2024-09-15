package hiiragi283.ragium.client

import hiiragi283.ragium.client.gui.HTBurningBoxScreen
import hiiragi283.ragium.client.gui.HTMachineScreen
import hiiragi283.ragium.client.renderer.HTMachineBlockEntityRenderer
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import hiiragi283.ragium.common.recipe.HTMachineType
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.block.Block
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories

@Environment(EnvType.CLIENT)
object RagiumClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerItems()
        registerBlocks()
        registerScreens()

        Ragium.log { info("Ragium-Client initialized!") }
    }

    private fun registerItems() {
        /*ModelLoadingPlugin.register { context: ModelLoadingPlugin.Context ->
            context.modifyModelOnLoad().register { model: UnbakedModel, context1: ModelModifier.OnLoad.Context ->
                when (context1.topLevelId()) {
                    HTFluidCubeModel.MODEL_ID -> HTFluidCubeModel
                    else -> model
                }
            }
        }*/
    }

    private fun registerBlocks() {
        registerCutoutMipped(RagiumBlocks.MANUAL_GRINDER)
        registerCutoutMipped(RagiumBlocks.BURNING_BOX)

        HTMachineType
            .getEntries()
            .map(HTMachineType::block)
            .forEach(::registerCutoutMipped)

        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.MACHINE, ::HTMachineBlockEntityRenderer)
    }

    private fun registerCutoutMipped(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped())
    }

    private fun registerScreens() {
        HandledScreens.register(RagiumScreenHandlerTypes.MACHINE, ::HTMachineScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.BURNING_BOX, ::HTBurningBoxScreen)
    }
}
