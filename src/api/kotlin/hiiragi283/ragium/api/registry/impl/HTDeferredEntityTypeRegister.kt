package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

class HTDeferredEntityTypeRegister(namespace: String) : HTDeferredRegister<EntityType<*>>(Registries.ENTITY_TYPE, namespace) {
    fun <ENTITY : Entity> registerType(
        name: String,
        factory: EntityType.EntityFactory<ENTITY>,
        category: MobCategory,
        builderAction: (EntityType.Builder<ENTITY>) -> Unit,
    ): HTDeferredEntityType<ENTITY> {
        val holder = HTDeferredEntityType<ENTITY>(createId(name))
        register(name) { id: ResourceLocation ->
            EntityType.Builder
                .of(factory, category)
                .apply(builderAction)
                .build(id.path)
        }
        return holder
    }
}
