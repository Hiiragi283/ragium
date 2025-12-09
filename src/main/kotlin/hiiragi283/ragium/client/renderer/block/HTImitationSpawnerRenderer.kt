package hiiragi283.ragium.client.renderer.block

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import hiiragi283.ragium.common.block.entity.HTImitationSpawnerBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.SpawnerRenderer
import net.minecraft.client.renderer.entity.EntityRenderDispatcher
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

/**
 * @see SpawnerRenderer
 */
class HTImitationSpawnerRenderer(context: BlockEntityRendererProvider.Context) :
    HTBlockEntityRenderer<HTImitationSpawnerBlockEntity>(context) {
    private val entityRenderer: EntityRenderDispatcher = context.entityRenderer

    override fun render(
        blockEntity: HTImitationSpawnerBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        val level: Level = blockEntity.level ?: return
        val spawnerMob: HTSpawnerMob = blockEntity.spawnerMob ?: return
        val entity: Entity = spawnerMob.entityType.create(level) ?: return
        SpawnerRenderer.renderEntityInSpawner(
            partialTick,
            poseStack,
            bufferSource,
            packedLight,
            entity,
            entityRenderer,
            0.0,
            0.0,
        )
    }
}
