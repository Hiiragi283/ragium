package hiiragi283.ragium.internal.data

import hiiragi283.lib.data.map.HTDynamicDataMap
import hiiragi283.lib.data.pack.HTDynamicDataRegister
import hiiragi283.lib.data.tag.HTTagsProvider
import hiiragi283.lib.data.tag.builders
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialAccess
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.columnPart
import hiiragi283.lib.material.forEachPart
import hiiragi283.lib.material.part.CommonParts
import hiiragi283.lib.material.part.HTPart
import hiiragi283.lib.material.part.property.HTPartPropertyKeys
import hiiragi283.lib.material.part.property.tagPrefix
import hiiragi283.lib.material.property.HTMaterialPropertyKeys
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTTagPrefix
import kotlin.system.measureTimeMillis
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

data object RagiumDynamicServerResources {
    @JvmStatic
    fun initialize() {
        HTDynamicDataRegister.LOGGER.info("Ragium Data loading took {} ms", measureTimeMillis(::initializeInternal))
    }

    @JvmStatic
    private fun initializeInternal() {
        val existing: HTMaterialContents.Provider = HTMaterialAccess.INSTANCE.getExistingContents()
        val registered: HTMaterialContents.Provider = HTMaterialAccess.INSTANCE.getRegisteredContents()
        // Data Map
        HTDynamicDataMap(NeoForgeDataMaps.FURNACE_FUELS) {
            for (entry: HTMaterial in HTMaterial.getManager()) {
                val baseTime: Int = entry[HTMaterialPropertyKeys.FUEL_TIME] ?: continue
                val key: HTMaterialKey = entry.key
                // Block
                setOf(registered.blocks.columnPart(key), registered.items.columnPart(key)).forEach { map ->
                    for ((part: HTPart, _) in map) {
                        val tagKey: TagKey<Item> = part.tagPrefix?.itemTagKey(key) ?: continue
                        val fuelScale: Float = part[HTPartPropertyKeys.FUEL_SCALE] ?: continue
                        val fuelTime: Int = (baseTime * fuelScale).toInt()
                        add(tagKey, FurnaceFuel(fuelTime))
                    }
                }
            }
        }
        // Loot Table
        registered.blocks.forEachPart { part: HTPart, material: HTMaterial, block: HTMaterialContents.BlockEntry ->
            if (HTPartPropertyKeys.IS_ORE in part) {
                val raw: HTMaterialContents.ItemEntry? = registered.items[CommonParts.RAW, material.key]
                // 暫定的に幸運は適応しない
                HTDynamicDataRegister.addLootTable(block.get()) {
                    LootTable.lootTable()
                        .withPool(
                            LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1f))
                                .add(LootItem.lootTableItem(raw ?: it)),
                        ).setParamSet(LootContextParamSets.BLOCK)
                }
            } else {
                HTDynamicDataRegister.addLootTable(block.get()) {
                    LootTable.lootTable()
                        .withPool(
                            LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1f))
                                .add(LootItem.lootTableItem(it)),
                        ).setParamSet(LootContextParamSets.BLOCK)
                }
            }
        }
        // Tag
        HTTagsProvider.Dynamic(Registries.BLOCK) {
            // Material Block
            existing.blocks.forEachTag { prefix: HTTagPrefix, key: HTMaterialKey, block: HTMaterialContents.BlockEntry ->
                builders(prefix, key).add(block)
            }
            registered.blocks.forEachTag { prefix: HTTagPrefix, key: HTMaterialKey, block: HTMaterialContents.BlockEntry ->
                builders(prefix, key).add(block)
                builder(BlockTags.MINEABLE_WITH_PICKAXE).add(block)
            }
        }
        HTTagsProvider.Dynamic(Registries.ITEM) {
            // Material Block
            existing.blocks.forEachTag { prefix: HTTagPrefix, key: HTMaterialKey, block ->
                builders(prefix, key).add(block.item)
            }
            registered.blocks.forEachTag { prefix: HTTagPrefix, key: HTMaterialKey, block ->
                builders(prefix, key).add(block.item)
            }
            // Material Item
            existing.items.forEachTag { prefix: HTTagPrefix, key: HTMaterialKey, item: HTMaterialContents.ItemEntry ->
                builders(prefix, key).add(item)
            }
            registered.items.forEachTag { prefix: HTTagPrefix, key: HTMaterialKey, item: HTMaterialContents.ItemEntry ->
                builders(prefix, key).add(item)
                if (prefix == CommonTagPrefixes.GEM || prefix == CommonTagPrefixes.INGOT) {
                    builder(ItemTags.BEACON_PAYMENT_ITEMS).addTag(prefix.itemTagKey(key))
                }
            }
        }
        // Recipe
        RagiumDynamicRecipeProvider.initialize()
    }

    private inline fun <V : Any> HTMaterialContents<V>.forEachTag(action: (HTTagPrefix, HTMaterialKey, V) -> Unit) {
        this.forEachPart { part: HTPart, material: HTMaterial, entry: V ->
            val prefix: HTTagPrefix = part.tagPrefix ?: return@forEachPart
            action(prefix, material.key, entry)
        }
    }
}
