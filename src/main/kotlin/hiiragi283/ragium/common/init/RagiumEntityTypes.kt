package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.entity.HTAnvilDynamiteEntity
import hiiragi283.ragium.common.entity.HTBedrockDynamiteEntity
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import hiiragi283.ragium.common.entity.HTFlatteningDynamiteEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumEntityTypes {
    @JvmField
    val DYNAMITE: EntityType<HTDynamiteEntity> = register(
        "dynamite",
        ::HTDynamiteEntity,
        SpawnGroup.MISC,
    ) {
        dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10)
    }

    @JvmField
    val ANVIL_DYNAMITE: EntityType<HTAnvilDynamiteEntity> = register(
        "anvil_dynamite",
        ::HTAnvilDynamiteEntity,
        SpawnGroup.MISC,
    ) {
        dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10)
    }

    @JvmField
    val BEDROCK_DYNAMITE: EntityType<HTBedrockDynamiteEntity> = register(
        "bedrock_dynamite",
        ::HTBedrockDynamiteEntity,
        SpawnGroup.MISC,
    ) {
        dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10)
    }

    @JvmField
    val FLATTENING_DYNAMITE: EntityType<HTFlatteningDynamiteEntity> = register(
        "flattening_dynamite",
        ::HTFlatteningDynamiteEntity,
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
            .build(),
    )
}
