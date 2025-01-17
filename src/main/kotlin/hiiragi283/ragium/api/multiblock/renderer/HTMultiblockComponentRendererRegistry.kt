package hiiragi283.ragium.api.multiblock.renderer

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

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
        level: Level,
        pos: BlockPos,
        component: T,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        (registry[component.getType()] as? HTMultiblockComponentRenderer<T>)
            ?.render(
                controller,
                level,
                pos,
                component,
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay,
            )
    }
}
