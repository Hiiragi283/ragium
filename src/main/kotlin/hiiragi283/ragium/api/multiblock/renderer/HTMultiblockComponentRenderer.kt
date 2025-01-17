package hiiragi283.ragium.api.multiblock.renderer

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.extension.translate
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.BlockRenderDispatcher
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.data.ModelData

fun interface HTMultiblockComponentRenderer<T : HTMultiblockComponent> {
    fun render(
        controller: HTControllerDefinition,
        level: Level,
        pos: BlockPos,
        component: T,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    )

    fun interface BlockRenderer<T : HTMultiblockComponent> : HTMultiblockComponentRenderer<T> {
        fun getBlockState(controller: HTControllerDefinition, level: Level, component: T): BlockState?

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        override fun render(
            controller: HTControllerDefinition,
            level: Level,
            pos: BlockPos,
            component: T,
            poseStack: PoseStack,
            bufferSource: MultiBufferSource,
            packedLight: Int,
            packedOverlay: Int,
        ) {
            getBlockState(controller, level, component)?.let { state: BlockState ->
                val blockRenderer: BlockRenderDispatcher = Minecraft.getInstance().blockRenderer
                poseStack.pushPose()
                poseStack.translate(pos)
                poseStack.translate(0.125, 0.125, 0.125)
                poseStack.scale(0.75f, 0.75f, 0.75f)
                blockRenderer.renderSingleBlock(
                    state,
                    poseStack,
                    bufferSource,
                    packedLight,
                    packedOverlay,
                    ModelData.EMPTY,
                    null,
                )
                poseStack.popPose()
            }
        }
    }
}
