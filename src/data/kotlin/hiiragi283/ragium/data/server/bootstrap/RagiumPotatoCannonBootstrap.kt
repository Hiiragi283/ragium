package hiiragi283.ragium.data.server.bootstrap

import com.simibubi.create.api.equipment.potatoCannon.PotatoCannonProjectileType
import com.simibubi.create.api.registry.CreateRegistries
import com.simibubi.create.content.equipment.potatoCannon.AllPotatoProjectileEntityHitActions
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumFoods
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey

/**
 * @see [com.simibubi.create.content.equipment.potatoCannon.AllPotatoProjectileTypes]
 */
object RagiumPotatoCannonBootstrap : RegistrySetBuilder.RegistryBootstrap<PotatoCannonProjectileType> {
    override fun run(context: BootstrapContext<PotatoCannonProjectileType>) {
        fun register(name: String, type: PotatoCannonProjectileType) {
            context.register(
                ResourceKey.create(CreateRegistries.POTATO_PROJECTILE_TYPE, RagiumAPI.id(name)),
                type,
            )
        }

        register(
            "chocolate",
            PotatoCannonProjectileType
                .Builder()
                .damage(1)
                .reloadTicks(15)
                .velocity(1.45f)
                .knockback(0.5f)
                .renderTumbling()
                .onEntityHit(AllPotatoProjectileEntityHitActions.FoodEffects(RagiumFoods.CHOCOLATE, false))
                .addItems(RagiumItems.CHOCOLATE)
                .build(),
        )
    }
}
