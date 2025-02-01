package hiiragi283.ragium.api.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import net.minecraft.client.renderer.MultiBufferSource

@Suppress("DEPRECATION")
fun interface HTMachineRenderer {
    companion object {
        @JvmField
        val EMPTY =
            HTMachineRenderer { _: HTMachineBlockEntity, _: Float, _: PoseStack, _: MultiBufferSource, _: Int, _: Int -> }
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
