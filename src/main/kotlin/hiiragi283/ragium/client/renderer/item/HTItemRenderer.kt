package hiiragi283.ragium.client.renderer.item

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

/**
 * @see mekanism.client.render.item.MekanismISTER
 */
abstract class HTItemRenderer :
    BlockEntityWithoutLevelRenderer(
        Minecraft.getInstance().blockEntityRenderDispatcher,
        Minecraft.getInstance().entityModels,
    ) {
    protected fun getBlockEntityRenderDispatcher(): BlockEntityRenderDispatcher = Minecraft.getInstance().blockEntityRenderDispatcher

    protected fun getEntityModels(): EntityModelSet = Minecraft.getInstance().entityModels

    protected fun getClientTicks(): Int = Minecraft.getInstance().levelRenderer.ticks

    protected fun getClientPartialTicks(): Float = Minecraft.getInstance().timer.getGameTimeDeltaPartialTick(false)

    protected fun renderItemAngle(poseStack: PoseStack, action: () -> Unit) {
        poseStack.pushPose()
        poseStack.translate(0.5, 0.5, 0.5)
        poseStack.mulPose(Axis.ZP.rotationDegrees(180f))
        action()
        poseStack.popPose()
    }

    //    BlockEntityWithoutLevelRenderer    //

    abstract override fun onResourceManagerReload(resourceManager: ResourceManager)

    abstract override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    )
}
