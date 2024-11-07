package hiiragi283.ragium.api.renderer

import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.client.extension.renderMultiblock
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

@Environment(EnvType.CLIENT)
object HTMultiblockPreviewRenderer : HTDynamicMachineRenderer {
    override fun render(
        entity: HTMachineBlockEntityBase,
        world: World,
        pos: BlockPos,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        if (entity is HTMultiblockController) {
            renderMultiblock(
                entity,
                world,
                world.getBlockState(pos).getOrNull(Properties.HORIZONTAL_FACING),
                matrices,
                vertexConsumers,
            )
        }
    }
}
