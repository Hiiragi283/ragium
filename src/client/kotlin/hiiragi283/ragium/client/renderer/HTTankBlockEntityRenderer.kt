package hiiragi283.ragium.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.lib.renderer.HTBlockEntityRenderer
import hiiragi283.lib.renderer.HTRenderHelper
import hiiragi283.lib.renderer.state.HTFluidBERenderState
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f

class HTTankBlockEntityRenderer(context: BlockEntityRendererProvider.Context) :
    HTBlockEntityRenderer<HTTankBlockEntity, HTFluidBERenderState>(context) {
    companion object {
        @JvmStatic
        private val FROM: Vector3f = Vector3f(2 / 16f, 0f, 2 / 16f).add(0.01f, 0.01f, 0.01f)
    }

    override fun createRenderState(): HTFluidBERenderState = HTFluidBERenderState()

    override fun extractRenderState(
        blockEntity: HTTankBlockEntity,
        state: HTFluidBERenderState,
        partialTicks: Float,
        cameraPosition: Vec3,
        breakProgress: ModelFeatureRenderer.CrumblingOverlay?
    ) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress)
        state.extractRenderState(blockEntity.tank)
    }

    override fun submit(
        state: HTFluidBERenderState,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        camera: CameraRenderState
    ) {
        val sprite: TextureAtlasSprite = state.sprite ?: return
        val fillingLevel: Float = state.fillingLevel
        if (fillingLevel <= 0f) return
        HTRenderHelper.submitCube(
            submitNodeCollector,
            poseStack,
            Sheets.translucentBlockItemSheet(),
            FROM,
            Vector3f(14 / 16f, fillingLevel, 14 / 16f).add(-0.01f, -0.01f, -0.01f),
            sprite,
            state.color,
            state.lightCoords
        )
    }
}
