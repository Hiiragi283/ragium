package hiiragi283.ragium.api.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.extension.renderMultiblock
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider

class HTMachineBlockEntityRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<HTMachineBlockEntity> {
    override fun render(
        blockEntity: HTMachineBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        blockEntity.renderMultiblock(poseStack, bufferSource, packedLight, packedOverlay)
    }
}
