package hiiragi283.ragium.data.server.bootstrap

import com.simibubi.create.api.equipment.potatoCannon.PotatoCannonProjectileType
import com.simibubi.create.api.registry.CreateRegistries
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.createKey
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext

object RagiumPotatoProjectileProvider : RegistrySetBuilder.RegistryBootstrap<PotatoCannonProjectileType> {
    override fun run(context: BootstrapContext<PotatoCannonProjectileType>) {
        register(context, "ambrosia") {
            reloadTicks(100)
            damage(Short.MAX_VALUE.toInt())
            velocity(4f)
            gravity(0f)

            addItems(RagiumItems.AMBROSIA)
        }
    }

    @JvmStatic
    private inline fun register(
        context: BootstrapContext<PotatoCannonProjectileType>,
        name: String,
        builderAction: PotatoCannonProjectileType.Builder.() -> Unit,
    ) {
        context.register(
            CreateRegistries.POTATO_PROJECTILE_TYPE.createKey(RagiumAPI.id(name)),
            PotatoCannonProjectileType.Builder().apply(builderAction).build(),
        )
    }
}
