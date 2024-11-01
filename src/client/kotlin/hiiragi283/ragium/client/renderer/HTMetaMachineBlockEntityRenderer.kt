package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.machine.HTClientMachinePropertyKeys
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.world.World

@Environment(EnvType.CLIENT)
object HTMetaMachineBlockEntityRenderer : BlockEntityRenderer<HTMetaMachineBlockEntity> {
    override fun render(
        entity: HTMetaMachineBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        val machine: HTMachineEntity<*> = entity.machineEntity ?: return
        val world: World = entity.world ?: return
        entity.definition.machineType.ifPresent(HTClientMachinePropertyKeys.DYNAMIC_RENDERER) {
            it.render(machine, world, entity.pos, tickDelta, matrices, vertexConsumers, light, overlay)
        }
    }
}
