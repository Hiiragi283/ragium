package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumClientAPI
import hiiragi283.ragium.client.renderer.HTMultiMachineBlockEntityRenderer
import hiiragi283.ragium.common.block.entity.HTMultiblockController
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories

object InternalRagiumClientAPI : RagiumClientAPI {
    override fun <T> registerMultiblockRenderer(type: BlockEntityType<T>) where T : BlockEntity, T : HTMultiblockController {
        BlockEntityRendererFactories.register(type, ::HTMultiMachineBlockEntityRenderer)
    }
}
