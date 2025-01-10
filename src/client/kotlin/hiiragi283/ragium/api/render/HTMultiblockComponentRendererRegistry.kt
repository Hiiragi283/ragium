package hiiragi283.ragium.api.render

import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

object HTMultiblockComponentRendererRegistry {
    private val registry: MutableMap<HTMultiblockComponent.Type<*>, HTMultiblockComponentRenderer<*>> = mutableMapOf()

    @JvmStatic
    fun <T : HTMultiblockComponent> register(type: HTMultiblockComponent.Type<T>, renderer: HTMultiblockComponentRenderer<T>) {
        registry[type] = renderer
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : HTMultiblockComponent> render(
        controller: HTControllerDefinition,
        world: World,
        pos: BlockPos,
        component: T,
        matrix: MatrixStack,
        consumerProvider: VertexConsumerProvider,
        random: Random,
    ) {
        (registry[component.type] as? HTMultiblockComponentRenderer<T>)?.render(
            controller,
            world,
            pos,
            component,
            matrix,
            consumerProvider,
            random,
        )
    }
}
