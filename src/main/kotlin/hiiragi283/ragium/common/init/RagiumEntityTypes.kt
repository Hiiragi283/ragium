package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.entity.HTDynamite
import hiiragi283.ragium.common.entity.HTFlare
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.Level
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumEntityTypes {
    @JvmField
    val REGISTER: DeferredRegister<EntityType<*>> = DeferredRegister.create(Registries.ENTITY_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Entity> register(
        name: String,
        factory: (EntityType<T>, Level) -> T,
        size: Float,
        category: MobCategory = MobCategory.MISC,
    ): DeferredHolder<EntityType<*>, EntityType<T>> = REGISTER.register(name) { id: ResourceLocation ->
        EntityType.Builder
            .of(factory, category)
            .sized(size, size)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(id.toString())
    }

    @JvmField
    val FLARE: DeferredHolder<EntityType<*>, EntityType<HTFlare>> = register("flare", ::HTFlare, 1f)

    @JvmField
    val DYNAMITE: DeferredHolder<EntityType<*>, EntityType<HTDynamite>> = register("dynamite", ::HTDynamite, 0.25f)
}
