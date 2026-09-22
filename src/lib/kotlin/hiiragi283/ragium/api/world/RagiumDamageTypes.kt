package hiiragi283.ragium.api.world

import hiiragi283.lib.registry.createKey
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageScaling
import net.minecraft.world.damagesource.DamageType

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
data object RagiumDamageTypes {
    @JvmField
    val CHEMICAL_BURN: ResourceKey<DamageType> = create("chemical_burn")

    @JvmStatic
    private fun create(name: String): ResourceKey<DamageType> = Registries.DAMAGE_TYPE.createKey(RagiumAPI.id(name))

    @JvmStatic
    fun bootstrap(context: BootstrapContext<DamageType>) {
        context.register(
            CHEMICAL_BURN,
            DamageType("chemicalBurn", DamageScaling.ALWAYS, 0.1f, DamageEffects.BURNING)
        )
    }
}
