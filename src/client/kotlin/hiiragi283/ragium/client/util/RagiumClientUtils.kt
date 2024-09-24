package hiiragi283.ragium.client.util

import hiiragi283.ragium.client.renderer.HTMultiblockRenderer
import hiiragi283.ragium.common.block.entity.HTMultiblockController
import hiiragi283.ragium.common.util.getOrDefault
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.CustomPayload
import net.minecraft.state.property.Properties
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

fun <T> renderMultiblock(
    entity: T,
    matrices: MatrixStack,
    vertexConsumers: VertexConsumerProvider,
) where T : BlockEntity, T : HTMultiblockController {
    if (!entity.showPreview) return
    val direction: Direction = entity.cachedState.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
    val world: World = entity.world ?: return
    entity.buildMultiblock(HTMultiblockRenderer(world, matrices, vertexConsumers).rotate(direction))
}

//    Network    //

fun <T : CustomPayload> CustomPayload.Id<T>.registerGlobalReceiver(handler: ClientPlayNetworking.PlayPayloadHandler<T>) {
    ClientPlayNetworking.registerGlobalReceiver(this, handler)
}

fun <T : CustomPayload> CustomPayload.Id<T>.registerGlobalReceiver(
    handler: (T, MinecraftClient, ClientPlayerEntity, PacketSender) -> Unit,
) {
    ClientPlayNetworking.registerGlobalReceiver(this) { payload: T, context: ClientPlayNetworking.Context ->
        handler(payload, context.client(), context.player(), context.responseSender())
    }
}
