package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPatternProvider
import hiiragi283.ragium.client.extension.renderMultiblock
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.world.World

@Environment(EnvType.CLIENT)
object HTMultiblockMachineBlockEntityRenderer : BlockEntityRenderer<HTMachineBlockEntityBase> {
    override fun render(
        entity: HTMachineBlockEntityBase,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        val world: World = entity.world ?: return
        if (entity is HTMultiblockPatternProvider) {
            renderMultiblock(
                entity,
                world,
                world.getBlockState(entity.pos).getOrNull(Properties.HORIZONTAL_FACING),
                matrices,
                vertexConsumers,
            )
        }
    }
}
