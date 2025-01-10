package hiiragi283.ragium.api

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.render.HTMultiblockComponentRenderer
import hiiragi283.ragium.api.render.HTMultiblockComponentRendererRegistry
import hiiragi283.ragium.api.render.HTMultiblockMachineBlockEntityRenderer
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.client.gui.HTMachineScreen
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.screen.ScreenHandlerType

object RagiumClientAPI {
    //    Block    //

    @JvmStatic
    fun <T : HTMachineBlockEntityBase> registerMultiblockRenderer(type: BlockEntityType<T>) {
        BlockEntityRendererFactories.register(type) { HTMultiblockMachineBlockEntityRenderer }
    }

    //    Screen    //

    @JvmStatic
    fun <T : HTMachineScreenHandlerBase> registerMachineScreen(type: ScreenHandlerType<T>) {
        HandledScreens.register(type, ::HTMachineScreen)
    }

    //    Multiblock Renderer    //

    @JvmStatic
    fun <T : HTMultiblockComponent> registerPatternRenderer(
        type: HTMultiblockComponent.Type<T>,
        renderer: HTMultiblockComponentRenderer<T>,
    ) {
        HTMultiblockComponentRendererRegistry.register(type, renderer)
    }
}
