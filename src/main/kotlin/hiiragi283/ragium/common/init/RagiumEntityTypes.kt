package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import hiiragi283.ragium.common.entity.HTRemoverDynamiteEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumEntityTypes {
    @JvmField
    val REMOVER_DYNAMITE: EntityType<HTRemoverDynamiteEntity> = register(
        "remover_dynamite",
        ::HTRemoverDynamiteEntity,
        SpawnGroup.MISC,
    ) {
        dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10)
    }

    @JvmField
    val DYNAMITE: EntityType<HTDynamiteEntity> = register(
        "dynamite",
        ::HTDynamiteEntity,
        SpawnGroup.MISC,
    ) {
        dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10)
    }

    @JvmStatic
    private fun <T : Entity> register(
        name: String,
        factory: EntityType.EntityFactory<T>,
        spawnGroup: SpawnGroup,
        builderAction: EntityType.Builder<T>.() -> Unit,
    ): EntityType<T> = Registry.register(
        Registries.ENTITY_TYPE,
        RagiumAPI.id(name),
        EntityType.Builder
            .create(factory, spawnGroup)
            .apply(builderAction)
            .build(name),
    )
}
