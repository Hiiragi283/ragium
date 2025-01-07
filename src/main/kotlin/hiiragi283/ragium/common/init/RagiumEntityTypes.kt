package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.entity.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
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
    val BLAZING_DYNAMITE: EntityType<HTBlazingDynamiteEntity> = register(
        "blazing_dynamite",
        ::HTBlazingDynamiteEntity,
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

    @JvmField
    val FROSTING_DYNAMITE: EntityType<HTFrostingDynamiteEntity> = register(
        "frosting_dynamite",
        ::HTFrostingDynamiteEntity,
        SpawnGroup.MISC,
    ) {
        dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10)
    }

    @JvmField
    val ECHO_BULLET: EntityType<HTEchoBulletEntity> = register(
        "echo_bullet",
        ::HTEchoBulletEntity,
        SpawnGroup.MISC,
    ) {
        dimensions(1.0F, 1.0F)
        maxTrackingRange(4)
        trackingTickInterval(10)
    }

    @JvmField
    val DYNAMITES: List<EntityType<out ThrownItemEntity>> = listOf(
        DYNAMITE,
        ANVIL_DYNAMITE,
        BLAZING_DYNAMITE,
        BEDROCK_DYNAMITE,
        FLATTENING_DYNAMITE,
        FROSTING_DYNAMITE,
    )

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
