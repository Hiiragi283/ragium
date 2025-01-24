package hiiragi283.ragium.api.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource

@Suppress("DEPRECATION")
fun interface HTMachineRenderer {
    companion object {
        @JvmField
        val EMPTY =
            HTMachineRenderer { _: HTMachineBlockEntity, _: Float, _: PoseStack, _: MultiBufferSource, _: Int, _: Int -> }

        @JvmField
        val HULL_RENDERER =
            HTMachineRenderer {
                    blockEntity: HTMachineBlockEntity,
                    _: Float,
                    poseStack: PoseStack,
                    bufferSource: MultiBufferSource,
                    packedLight: Int,
                    packedOverlay: Int,
                ->
                Minecraft
                    .getInstance()
                    .blockRenderer
                    .renderSingleBlock(
                        blockEntity.machineTier
                            .getHull()
                            .get()
                            .defaultBlockState(),
                        poseStack,
                        bufferSource,
                        packedLight,
                        packedOverlay,
                    )
            }
    }

    fun render(
        blockEntity: HTMachineBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    )
}
