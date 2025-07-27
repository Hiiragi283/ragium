package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.entity.HTThrownCaptureEgg
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object RagiumEntityTypes {
    @JvmField
    val REGISTER: DeferredRegister<EntityType<*>> = DeferredRegister.create(Registries.ENTITY_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Entity> register(
        name: String,
        factory: EntityType.EntityFactory<T>,
        category: MobCategory,
        builderAction: (EntityType.Builder<T>) -> Unit,
    ): Supplier<EntityType<T>> = REGISTER.register(name) { id: ResourceLocation ->
        EntityType.Builder
            .of(factory, category)
            .apply(builderAction)
            .build(id.path)
    }

    @JvmField
    val ELDRITCH_EGG: Supplier<EntityType<HTThrownCaptureEgg>> =
        register("eldritch_egg", ::HTThrownCaptureEgg, MobCategory.MISC) { builder: EntityType.Builder<HTThrownCaptureEgg> ->
            builder
                .sized(0.25f, 0.25f)
                .clientTrackingRange(4)
                .updateInterval(10)
        }
}
