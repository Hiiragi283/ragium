package hiiragi283.ragium.client.util

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.client.renderer.HTMultiblockRenderer
import hiiragi283.ragium.common.fluid.HTFluidContent
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

//    MatrixStack    //

fun MatrixStack.translate(x: Number, y: Number, z: Number) {
    translate(x.toDouble(), y.toDouble(), z.toDouble())
}

fun MatrixStack.translate(pos: Vec3i) {
    translate(pos.x, pos.y, pos.z)
}

fun MatrixStack.translate(pos: Vec3d) {
    translate(pos.x, pos.y, pos.z)
}

fun MatrixStack.scale(x: Number, y: Number, z: Number) {
    scale(x.toFloat(), y.toFloat(), z.toFloat())
}

fun MatrixStack.scale(pos: Vec3d) {
    scale(pos.x, pos.y, pos.z)
}

//    BlockEntityRenderer    //

fun renderItem(
    world: World?,
    pos: Vec3d,
    stack: ItemStack,
    matrices: MatrixStack,
    vertexConsumers: VertexConsumerProvider,
    scale: Float = 1.0f,
) {
    if (world == null) return
    matrices.push()
    matrices.translate(pos)
    matrices.translate(0.5, 0, 0.5)
    matrices.scale(scale, scale, scale)
    MinecraftClient.getInstance().itemRenderer.renderItem(
        stack,
        ModelTransformationMode.FIXED,
        15728880,
        OverlayTexture.DEFAULT_UV,
        matrices,
        vertexConsumers,
        world,
        0,
    )
    matrices.pop()
}

fun <T : HTMultiblockController> renderMultiblock(
    controller: T,
    world: World?,
    facing: Direction?,
    matrices: MatrixStack,
    vertexConsumers: VertexConsumerProvider,
) {
    if (!controller.showPreview) return
    world?.let {
        controller.buildMultiblock(it, HTMultiblockRenderer(it, matrices, vertexConsumers).rotate(facing))
    }
}

//    Network    //

fun <T : CustomPayload> CustomPayload.Id<T>.registerClientReceiver(handler: ClientPlayNetworking.PlayPayloadHandler<T>) {
    ClientPlayNetworking.registerGlobalReceiver(this, handler)
}

fun <T : CustomPayload> CustomPayload.Id<T>.registerClientReceiver(
    handler: (T, MinecraftClient, ClientPlayerEntity, PacketSender) -> Unit,
) {
    ClientPlayNetworking.registerGlobalReceiver(this) { payload: T, context: ClientPlayNetworking.Context ->
        handler(payload, context.client(), context.player(), context.responseSender())
    }
}

//    HTFluidContent    //

fun HTFluidContent.registerClient(stillTex: Identifier, flowingTex: Identifier = stillTex, color: Int = -1) {
    registerClient(SimpleFluidRenderHandler(stillTex, flowingTex, color))
}

fun HTFluidContent.registerClient(renderHandler: FluidRenderHandler) {
    // register render handler
    FluidRenderHandlerRegistry.INSTANCE.register(still, flowing, renderHandler)
    // register render layers
    BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), still, flowing)
}
