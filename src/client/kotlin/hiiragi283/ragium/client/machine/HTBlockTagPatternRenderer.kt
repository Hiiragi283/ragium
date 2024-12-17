package hiiragi283.ragium.client.machine

import hiiragi283.ragium.api.extension.translate
import hiiragi283.ragium.api.render.HTMultiblockPatternRenderer
import hiiragi283.ragium.api.util.HTRegistryEntryList
import hiiragi283.ragium.client.renderer.HTMultiblockRenderer
import hiiragi283.ragium.common.machine.HTBlockTagPattern
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

object HTBlockTagPatternRenderer : HTMultiblockPatternRenderer<HTBlockTagPattern> {
    override fun render(
        x: Int,
        y: Int,
        z: Int,
        pattern: HTBlockTagPattern,
        world: World,
        matrix: MatrixStack,
        consumerProvider: VertexConsumerProvider,
        random: Random,
    ) {
        val blockRenderManager: BlockRenderManager = MinecraftClient.getInstance().blockRenderManager
        matrix.push()
        matrix.translate(x, y, z)
        matrix.translate(0.125, 0.125, 0.125)
        matrix.scale(0.75f, 0.75f, 0.75f)
        getPreviewState(pattern.entryList, world)?.let { state: BlockState ->
            val consumer: VertexConsumer = consumerProvider.getBuffer(RenderLayers.getBlockLayer(state))
            blockRenderManager.renderBlock(
                state,
                HTMultiblockRenderer.DUMMY_POS,
                world,
                matrix,
                consumer,
                false,
                Random.create(),
            )
        }
        matrix.pop()
    }

    private fun getPreviewState(entryList: HTRegistryEntryList<Block>, world: World): BlockState? = when (entryList.size) {
        0 -> null
        1 -> entryList[0].defaultState
        else -> entryList[((world.time % (20 * entryList.size)) / 20).toInt()].defaultState
    }
}
