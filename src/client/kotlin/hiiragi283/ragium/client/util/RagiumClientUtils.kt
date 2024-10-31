package hiiragi283.ragium.client.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.client.renderer.HTMultiblockRenderer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.json.JsonUnbakedModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.CustomPayload
import net.minecraft.registry.Registries
import net.minecraft.resource.Resource
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World
import java.io.BufferedReader

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

/*fun <T : CustomPayload> CustomPayload.Id<T>.registerClientReceiver(
    handler: (T, MinecraftClient, ClientPlayerEntity, PacketSender) -> Unit,
) {
    ClientPlayNetworking.registerGlobalReceiver(this) { payload: T, context: ClientPlayNetworking.Context ->
        handler(payload, context.client(), context.player(), context.responseSender())
    }
}*/

val ClientPlayNetworking.Context.world: ClientWorld?
    get() = client().world

fun ClientPlayNetworking.Context.getBlockEntity(pos: BlockPos): BlockEntity? = world?.getBlockEntity(pos)

fun ClientPlayNetworking.Context.getMachineEntity(pos: BlockPos): HTMachineEntity<*>? = world?.getMachineEntity(pos)

//    Fluid    //

fun Fluid.getSpriteAndColor(): Pair<Sprite, Int>? {
    val handler: FluidRenderHandler = FluidRenderHandlerRegistry.INSTANCE.get(this) ?: return null
    val sprite: Sprite = handler.getFluidSprites(null, null, defaultState).getOrNull(0) ?: return null
    val color: Int = handler.getFluidColor(null, null, defaultState)
    return sprite to color
}

//    ModelTransformation    //

private fun getModelTransform(id: Identifier): ModelTransformation {
    val resource: Resource = MinecraftClient
        .getInstance()
        .resourceManager
        .getResource(id)
        .orElseThrow()
    val reader: BufferedReader = resource.inputStream.bufferedReader()
    return JsonUnbakedModel.deserialize(reader).transformations
}

val DEFAULT_ITEM_TRANSFORM: ModelTransformation by lazy {
    getModelTransform(Identifier.of("models/item/generated.json"))
}

val FLUID_CUBE_TRANSFORM: ModelTransformation by lazy { getModelTransform(RagiumAPI.id("models/item/empty_fluid_cube.json")) }

//    BakedModel    //

fun getBlockModel(block: Block): BakedModel = MinecraftClient
    .getInstance()
    .bakedModelManager
    .getModel(ModelIdentifier(Registries.BLOCK.getId(block), ""))

val HTMachineTier.hullModel: BakedModel
    get() = MinecraftClient
        .getInstance()
        .bakedModelManager
        .getModel(ModelIdentifier(this.getHull().id, ""))
