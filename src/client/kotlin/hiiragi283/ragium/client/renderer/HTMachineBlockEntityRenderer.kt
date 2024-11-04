package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.machine.HTClientMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.world.World

@Environment(EnvType.CLIENT)
object HTMachineBlockEntityRenderer : BlockEntityRenderer<HTMachineBlockEntityBase> {
    override fun render(
        entity: HTMachineBlockEntityBase,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        val world: World = entity.world ?: return
        val key: HTMachineKey = entity.key
        key.asProperties().ifPresent(HTClientMachinePropertyKeys.DYNAMIC_RENDERER) {
            it.render(key, world, entity.pos, tickDelta, matrices, vertexConsumers, light, overlay)
        }
    }
}
