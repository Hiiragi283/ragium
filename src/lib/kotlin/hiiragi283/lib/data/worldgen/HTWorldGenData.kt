package hiiragi283.lib.data.worldgen

import hiiragi283.lib.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries

/**
 * 地形生成物に関する[ResourceKey]を束ねたクラスです。
 * @param configuredKey 生成する構造物を決定する[ConfiguredFeature]の[ResourceKey]
 * @param placedKey 構造物を生成する条件を決定する[PlacedFeature]の[ResourceKey]
 * @param modifierKey 構造物を生成する環境や段階を決定する[BiomeModifier]の[ResourceKey]
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
data class HTWorldGenData(
    val configuredKey: ResourceKey<ConfiguredFeature<*, *>>,
    val placedKey: ResourceKey<PlacedFeature>,
    val modifierKey: ResourceKey<BiomeModifier>
) {
    /**
     * [id]に基づいて各[ResourceKey]を作成します。
     */
    constructor(id: Identifier) : this(
        Registries.CONFIGURED_FEATURE.createKey(id),
        Registries.PLACED_FEATURE.createKey(id),
        NeoForgeRegistries.Keys.BIOME_MODIFIERS.createKey(id)
    )

    /**
     * [id]に基づいて[ConfiguredFeature]をのぞく各[ResourceKey]を作成します。
     */
    constructor(configuredData: HTWorldGenData, id: Identifier) : this(
        configuredData.configuredKey,
        Registries.PLACED_FEATURE.createKey(id),
        NeoForgeRegistries.Keys.BIOME_MODIFIERS.createKey(id)
    )
}
