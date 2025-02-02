package hiiragi283.ragium.api.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.machine.HTMachineAccess
import net.minecraft.client.renderer.MultiBufferSource

@Suppress("DEPRECATION")
fun interface HTMachineRenderer {
    companion object {
        @JvmField
        val EMPTY =
            HTMachineRenderer { _: HTMachineAccess, _: Float, _: PoseStack, _: MultiBufferSource, _: Int, _: Int -> }
    }

    fun render(
        blockEntity: HTMachineAccess,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    )
}
