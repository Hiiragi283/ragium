package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries

data class HTWorldGenData(private val name: String) {
    @JvmField
    val configuredKey: ResourceKey<ConfiguredFeature<*, *>> =
        ResourceKey.create(Registries.CONFIGURED_FEATURE, RagiumAPI.id(name))

    @JvmField
    val placedKey: ResourceKey<PlacedFeature> =
        ResourceKey.create(Registries.PLACED_FEATURE, RagiumAPI.id(name))

    @JvmField
    val modifierKey: ResourceKey<BiomeModifier> =
        ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RagiumAPI.id(name))

    lateinit var configuredHolder: Holder<ConfiguredFeature<*, *>>
        internal set

    lateinit var placedHolder: Holder<PlacedFeature>
        internal set
}
