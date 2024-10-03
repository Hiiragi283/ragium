package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.entity.HTOblivionCoreEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumEntityTypes {
    @JvmField
    val OBLIVION_CUBE: EntityType<HTOblivionCoreEntity> = register(
        "oblivion_cube",
        ::HTOblivionCoreEntity,
        SpawnGroup.MONSTER,
    ) {
        dimensions(0.75f, 0.75f)
    }

    @JvmStatic
    private fun <T : Entity> register(
        name: String,
        factory: EntityType.EntityFactory<T>,
        spawnGroup: SpawnGroup,
        builderAction: EntityType.Builder<T>.() -> Unit,
    ): EntityType<T> = Registry.register(
        Registries.ENTITY_TYPE,
        Ragium.id(name),
        EntityType.Builder
            .create(factory, spawnGroup)
            .apply(builderAction)
            .build(name),
    )

    @JvmStatic
    fun init() {
        FabricDefaultAttributeRegistry.register(
            OBLIVION_CUBE,
            HTOblivionCoreEntity.createAttributes(),
        )
    }
}
