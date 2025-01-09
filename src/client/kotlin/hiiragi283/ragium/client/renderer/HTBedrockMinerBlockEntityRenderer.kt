package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.extension.renderBeam
import hiiragi283.ragium.api.extension.renderMultiblock
import hiiragi283.ragium.common.block.machine.consume.HTBedrockMinerBlockEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

@Environment(EnvType.CLIENT)
object HTBedrockMinerBlockEntityRenderer : BlockEntityRenderer<HTBedrockMinerBlockEntity> {
    override fun render(
        entity: HTBedrockMinerBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        val world: World = entity.world ?: return
        // render beam
        if (entity.isActive) {
            renderBeam(matrices, vertexConsumers, tickDelta, world)
        }
        // render multiblock
        entity.renderMultiblock(matrices, vertexConsumers, Random.create())
    }
}
