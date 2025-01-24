package hiiragi283.ragium.api.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.extension.renderMultiblock
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.property.getOrDefault
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
        val entry: HTMachineRegistry.Entry = blockEntity.getEntryOrNull() ?: return
        entry
            .getOrDefault(HTMachinePropertyKeys.RENDERER_PRE)
            .render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay)
        blockEntity.renderMultiblock(poseStack, bufferSource, packedLight, packedOverlay)
        entry
            .getOrDefault(HTMachinePropertyKeys.RENDERER_POST)
            .render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay)
    }
}
