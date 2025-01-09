package hiiragi283.ragium.api.render

import hiiragi283.ragium.api.extension.renderItem
import hiiragi283.ragium.api.extension.translate
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.client.renderer.HTMultiblockRenderer
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

fun interface HTMultiblockComponentRenderer<T : HTMultiblockComponent> {
    fun render(
        controller: HTControllerDefinition,
        world: World,
        pos: BlockPos,
        component: T,
        matrix: MatrixStack,
        consumerProvider: VertexConsumerProvider,
        random: Random,
    )

    fun interface BlockRender<T : HTMultiblockComponent> : HTMultiblockComponentRenderer<T> {
        fun getBlockState(controller: HTControllerDefinition, world: World, component: T): BlockState?

        override fun render(
            controller: HTControllerDefinition,
            world: World,
            pos: BlockPos,
            component: T,
            matrix: MatrixStack,
            consumerProvider: VertexConsumerProvider,
            random: Random,
        ) {
            getBlockState(controller, world, component)?.let { state: BlockState ->
                val blockRenderManager: BlockRenderManager = MinecraftClient.getInstance().blockRenderManager
                matrix.push()
                matrix.translate(pos)
                matrix.translate(0.125, 0.125, 0.125)
                matrix.scale(0.75f, 0.75f, 0.75f)
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
                matrix.pop()
            }
        }
    }

    fun interface ItemRender<T : HTMultiblockComponent> : HTMultiblockComponentRenderer<T> {
        fun getItemStack(controller: HTControllerDefinition, world: World, component: T): ItemStack?

        override fun render(
            controller: HTControllerDefinition,
            world: World,
            pos: BlockPos,
            component: T,
            matrix: MatrixStack,
            consumerProvider: VertexConsumerProvider,
            random: Random,
        ) {
            getItemStack(controller, world, component)?.let { stack: ItemStack ->
                renderItem(
                    world,
                    Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()),
                    stack,
                    matrix,
                    consumerProvider,
                )
            }
        }
    }
}
