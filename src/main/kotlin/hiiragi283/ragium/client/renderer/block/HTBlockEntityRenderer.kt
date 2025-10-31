package hiiragi283.ragium.client.renderer.block

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.joml.Quaternionf

/**
 * @see mekanism.client.render.tileentity.MekanismTileEntityRenderer
 */
@OnlyIn(Dist.CLIENT)
abstract class HTBlockEntityRenderer<BE : BlockEntity>(protected val context: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<BE> {
    companion object {
        /**
         * @see blusunrize.immersiveengineering.client.render.tile.IEBlockEntityRenderer.ROTATE_FOR_FACING
         */
        @JvmField
        val ROTATION: Map<Direction, Quaternionf> = Direction.entries.associateWith { direction: Direction ->
            Quaternionf().rotateY(Mth.DEG_TO_RAD * (180 - direction.toYRot()))
        }

        @JvmStatic
        fun rotate(poseStack: PoseStack, direction: Direction) {
            poseStack.mulPose(ROTATION[direction]!!)
        }
    }
}
