package hiiragi283.ragium.api.render

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

object HTMultiblockPatternRendererRegistry {
    private val registry: MutableMap<Class<*>, HTMultiblockPatternRenderer<*>> = mutableMapOf()

    @JvmStatic
    fun <T : HTMultiblockPattern> register(clazz: Class<T>, renderer: HTMultiblockPatternRenderer<T>) {
        registry[clazz] = renderer
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : HTMultiblockPattern> render(
        x: Int,
        y: Int,
        z: Int,
        pattern: T,
        world: World,
        matrix: MatrixStack,
        consumerProvider: VertexConsumerProvider,
        random: Random,
    ) {
        (registry[pattern::class.java] as? HTMultiblockPatternRenderer<T>)?.render(
            x,
            y,
            z,
            pattern,
            world,
            matrix,
            consumerProvider,
            random,
        )
    }
}
