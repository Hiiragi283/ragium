package hiiragi283.ragium.client.renderer.block

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @see mekanism.client.render.tileentity.MekanismTileEntityRenderer
 */
@OnlyIn(Dist.CLIENT)
abstract class HTBlockEntityRenderer<BE : BlockEntity>(protected val context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<BE>
