package hiiragi283.ragium.api.model

import hiiragi283.ragium.client.extension.getModel
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView
import java.util.function.Supplier

@Environment(EnvType.CLIENT)
class HTAliasedModel(private val parent: Identifier) : FabricBakedModel {
    override fun emitBlockQuads(
        blockView: BlockRenderView,
        state: BlockState,
        pos: BlockPos,
        randomSupplier: Supplier<Random>,
        context: RenderContext,
    ) {
        getModel(parent).emitBlockQuads(blockView, state, pos, randomSupplier, context)
    }

    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>, context: RenderContext) {
        getModel(parent).emitItemQuads(stack, randomSupplier, context)
    }
}
