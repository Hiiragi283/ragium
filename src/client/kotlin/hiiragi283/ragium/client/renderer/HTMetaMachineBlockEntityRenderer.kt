package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.client.util.renderMultiblock
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.random.Random
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
        val world: World = entity.world ?: return
        val (machineType: HTMachineType, tier: HTMachineTier) = entity.definition
        if (machineType.isGenerator()) return
        // render hull model
        val state: BlockState = tier.getHull().value.defaultState
        MinecraftClient.getInstance().blockRenderManager.renderBlock(
            state,
            entity.pos,
            world,
            matrices,
            vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)),
            true,
            Random.create(),
        )
        // render multiblock preview
        (entity.machineEntity as? HTMultiblockController)?.let {
            renderMultiblock(
                it,
                world,
                entity.cachedState.getOrNull(Properties.HORIZONTAL_FACING),
                matrices,
                vertexConsumers,
            )
        }
    }
}
