package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries

data class HTWorldGenData(
    val configuredKey: ResourceKey<ConfiguredFeature<*, *>>,
    val placedKey: ResourceKey<PlacedFeature>,
    val modifierKey: ResourceKey<BiomeModifier>,
) {
    constructor(name: String) : this(
        Registries.CONFIGURED_FEATURE.createKey(RagiumAPI.id(name)),
        Registries.PLACED_FEATURE.createKey(RagiumAPI.id(name)),
        NeoForgeRegistries.Keys.BIOME_MODIFIERS.createKey(RagiumAPI.id(name)),
    )

    constructor(configuredData: HTWorldGenData, name: String) : this(
        configuredData.configuredKey,
        Registries.PLACED_FEATURE.createKey(RagiumAPI.id(name)),
        NeoForgeRegistries.Keys.BIOME_MODIFIERS.createKey(RagiumAPI.id(name)),
    )
}
