package hiiragi283.ragium.client.renderer.block

import hiiragi283.ragium.client.model.HTModel
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @see mekanism.client.render.tileentity.ModelTileEntityRenderer
 */
@OnlyIn(Dist.CLIENT)
abstract class HTModelBlockEntityRenderer<BE : BlockEntity, MODEL : HTModel>(
    factory: (EntityModelSet) -> MODEL,
    context: BlockEntityRendererProvider.Context,
) : HTBlockEntityRenderer<BE>(context) {
    protected val model: MODEL = factory(context.modelSet)
}
