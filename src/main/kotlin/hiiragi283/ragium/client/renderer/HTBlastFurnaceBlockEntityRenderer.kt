package hiiragi283.ragium.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.extension.renderMultiblock
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider

class HTBlastFurnaceBlockEntityRenderer<T>(context: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<T> where T : HTMachineBlockEntity, T : HTMultiblockController {
    override fun render(
        blockEntity: T,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        blockEntity.renderMultiblock(poseStack, bufferSource, packedLight, packedOverlay)
    }
}
