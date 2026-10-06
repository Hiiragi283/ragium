package hiiragi283.ragium.data.loot

import hiiragi283.lib.registry.createKey
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootTable

data object RagiumGlobalLootTables {
    @JvmStatic
    private fun create(path: String): ResourceKey<LootTable> =
        Registries.LOOT_TABLE.createKey(RagiumAPI.id("modifiers", path))

    //    Block    //

    @JvmField
    val SULFUR_FROM_COAL_ORES: ResourceKey<LootTable> = create("sulfur_from_coal_ores")

    @JvmField
    val CINNABAR_FROM_REDSTONE_ORES: ResourceKey<LootTable> = create("cinnabar_from_redstone_ores")

    @JvmField
    val FLUORITE_FROM_LAPIS_ORES: ResourceKey<LootTable> = create("fluorite_from_lapis_ores")

    @JvmField
    val CARBON_FROM_DIAMOND_ORES: ResourceKey<LootTable> = create("carbon_from_diamond_ores")

    //    Entity    //

    @JvmField
    val ELDER_HEART: ResourceKey<LootTable> = create("elder_heart")

    @JvmField
    val TRADER_CATALOG: ResourceKey<LootTable> = create("trader_catalog")
}
