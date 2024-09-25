package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.feature.ConfiguredFeature

object RagiumConfiguredFeatures {
    @JvmField
    val RUBBER: RegistryKey<ConfiguredFeature<*, *>> = create("rubber")

    @JvmStatic
    private fun create(name: String): RegistryKey<ConfiguredFeature<*, *>> =
        RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Ragium.id(name))
}
