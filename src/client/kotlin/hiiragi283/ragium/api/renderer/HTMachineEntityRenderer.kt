package hiiragi283.ragium.api.renderer

import hiiragi283.ragium.api.machine.HTMachine
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

@Environment(EnvType.CLIENT)
fun interface HTMachineEntityRenderer {
    fun render(
        machine: HTMachine,
        world: World,
        pos: BlockPos,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    )
}
