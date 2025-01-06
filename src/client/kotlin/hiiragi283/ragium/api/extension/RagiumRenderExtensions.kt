package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.client.renderer.HTMultiblockRenderer
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedModelManager
import net.minecraft.client.render.model.json.JsonUnbakedModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.resource.Resource
import net.minecraft.state.property.Properties
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.RotationAxis
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
    pitch: Float = 0.0f,
    yaw: Float = 0.0f,
    light: Int = 15728880,
) {
    if (world == null) return
    matrices.push()
    matrices.translate(pos)
    matrices.translate(0.5, 0, 0.5)
    matrices.scale(scale, scale, scale)
    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch))
    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw))
    MinecraftClient.getInstance().itemRenderer.renderItem(
        stack,
        ModelTransformationMode.FIXED,
        light,
        OverlayTexture.DEFAULT_UV,
        matrices,
        vertexConsumers,
        world,
        0,
    )
    matrices.pop()
}

fun <T> renderMultiblock(
    provider: T,
    matrices: MatrixStack,
    vertexConsumers: VertexConsumerProvider,
) where T : HTMultiblockProvider, T : BlockEntity {
    renderMultiblock(
        provider,
        provider.world,
        provider.world?.getBlockState(provider.pos)?.getOrNull(Properties.HORIZONTAL_FACING),
        matrices,
        vertexConsumers,
    )
}

fun <T : HTMultiblockProvider> renderMultiblock(
    provider: T,
    world: World?,
    facing: Direction?,
    matrices: MatrixStack,
    vertexConsumers: VertexConsumerProvider,
) {
    if (!provider.multiblockManager.showPreview) return
    world?.let {
        provider.buildMultiblock(HTMultiblockRenderer(it, matrices, vertexConsumers, provider).rotate(facing))
    }
}

fun renderBeam(
    matrices: MatrixStack,
    vertexConsumers: VertexConsumerProvider,
    tickDelta: Float,
    world: World,
    color: DyeColor = DyeColor.WHITE,
    textureId: Identifier = Identifier.ofVanilla("textures/entity/beacon_beam.png"),
) {
    BeaconBlockEntityRenderer.renderBeam(
        matrices,
        vertexConsumers,
        textureId,
        tickDelta,
        1f,
        world.time,
        -2,
        2,
        color.entityColor,
        0.2f,
        0.25f,
    )
}

/*fun renderSide(
    matrices: MatrixStack,
    vertexConsumer: VertexConsumerProvider,
    layer: RenderLayer,
    side: Direction,
    yTop: Float = 1f,
    yBottom: Float = 0f
) {
    renderSide(matrices.peek().positionMatrix, vertexConsumer.getBuffer(layer), side, yTop, yBottom)
}

fun renderSide(
    matrix: Matrix4f,
    vertexConsumer: VertexConsumer,
    side: Direction,
    yTop: Float = 1f,
    yBottom: Float = 0f
) {
    when (side) {
        Direction.DOWN -> renderSide(matrix, vertexConsumer, 0f, 1f, yBottom, yBottom, 0f, 0f, 1f, 1f)
        Direction.UP -> renderSide(matrix, vertexConsumer, 0f, 1f, yTop, yTop, 1f, 1f, 0f, 0f)
        Direction.NORTH -> renderSide(matrix, vertexConsumer, 0f, 1f, 1f, 0f, 0f, 0f, 0f, 0f)
        Direction.SOUTH -> renderSide(matrix, vertexConsumer, 0f, 1f, 0f, 1f, 1f, 1f, 1f, 1f)
        Direction.WEST -> renderSide(matrix, vertexConsumer, 0f, 0f, 0f, 1f, 0f, 1f, 1f, 0f)
        Direction.EAST -> renderSide(matrix, vertexConsumer, 1f, 1f, 1f, 0f, 0f, 1f, 1f, 0f)
    }
}

private fun renderSide(
    matrix: Matrix4f,
    vertexConsumer: VertexConsumer,
    x1: Float,
    x2: Float,
    y1: Float,
    y2: Float,
    z1: Float,
    z2: Float,
    z3: Float,
    z4: Float
) {
    vertexConsumer.vertex(matrix, x1, y1, z1)
    vertexConsumer.vertex(matrix, x2, y1, z2)
    vertexConsumer.vertex(matrix, x2, y2, z3)
    vertexConsumer.vertex(matrix, x1, y2, z4)
}*/

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

fun getModel(id: Identifier): BakedModel {
    val manager: BakedModelManager = MinecraftClient.getInstance().bakedModelManager
    return manager.getModel(id) ?: manager.getModel(ModelIdentifier(id, ""))
}

fun getBlockModel(block: Block): BakedModel = getModel(Registries.BLOCK.getId(block))

val HTMachineTier.hullModel: BakedModel
    get() = getModel(this.getHull().id)
