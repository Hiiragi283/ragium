package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeBuilder
import hiiragi283.ragium.api.event.HTRecipesUpdatedEvent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.property.HTPropertyMap
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.HolderGetter
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.LootTableLoadEvent
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    //    Loot Table Load    //

    @SubscribeEvent
    fun onLoadLootTable(event: LootTableLoadEvent) {
        val loot: LootTable = event.table

        fun modify(entityType: EntityType<*>, function: LootTable.() -> Unit) {
            if (loot.lootTableId == entityType.defaultLootTable.location()) {
                function(loot)
            }
        }

        // 行商人がカタログを落とすように
        modify(EntityType.WANDERING_TRADER) {
            addPool(
                LootPool
                    .lootPool()
                    .setRolls(ConstantValue.exactly(1f))
                    .add(LootItem.lootTableItem(RagiumItems.TRADER_CATALOG))
                    .build(),
            )
            LOGGER.info("Modified loot table for Wandering Trader!")
        }
    }

    //    Machine Recipe    //

    @SubscribeEvent
    fun onMachineRecipesUpdated(event: HTRecipesUpdatedEvent) {
        crushing(event)
    }

    @JvmStatic
    private fun crushing(event: HTRecipesUpdatedEvent) {
        val registry: HTMaterialRegistry = RagiumAPI.getInstance().getMaterialRegistry()
        for ((key: HTMaterialKey, properties: HTPropertyMap) in registry) {
            val name: String = key.name
            val type: HTMaterialType = properties.getOrDefault(HTMaterialPropertyKeys.MATERIAL_TYPE)
            val mainPrefix: HTTagPrefix? = type.getMainPrefix()
            val resultPrefix: HTTagPrefix? = type.getOreResultPrefix()
            // Ore
            if (resultPrefix != null) {
                event.register(
                    RagiumRecipes.CRUSHING,
                    RagiumAPI.id("${name}_dust_from_ore"),
                ) { lookup: HolderGetter<Item> ->
                    val result: Item = event.getFirstItem(resultPrefix, key) ?: return@register null
                    HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
                        .itemInput(HTTagPrefixes.ORE, key)
                        .itemOutput(result, properties.getOrDefault(HTMaterialPropertyKeys.ORE_CRUSHED_COUNT))
                        .createRecipe()
                }
            }
            val dust: Item = event.getFirstItem(HTTagPrefixes.DUST, key) ?: continue
            // Gem/Ingot
            if (mainPrefix != null) {
                event.register(
                    RagiumRecipes.CRUSHING,
                    RagiumAPI.id("${name}_dust_from_main"),
                ) { lookup: HolderGetter<Item> ->
                    HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
                        .itemInput(mainPrefix, key)
                        .itemOutput(dust)
                        .createRecipe()
                }
            }
            // Gear
            event.register(
                RagiumRecipes.CRUSHING,
                RagiumAPI.id("${name}_dust_from_gear"),
            ) { lookup: HolderGetter<Item> ->
                HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
                    .itemInput(HTTagPrefixes.GEAR, key)
                    .itemOutput(dust, 4)
                    .createRecipe()
            }
            // Plate
            event.register(
                RagiumRecipes.CRUSHING,
                RagiumAPI.id("${name}_dust_from_plate"),
            ) { lookup: HolderGetter<Item> ->
                HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
                    .itemInput(HTTagPrefixes.PLATE, key)
                    .itemOutput(dust)
                    .createRecipe()
            }
            // Raw
            event.register(
                RagiumRecipes.CRUSHING,
                RagiumAPI.id("${name}_dust_from_raw"),
            ) { lookup: HolderGetter<Item> ->
                HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
                    .itemInput(HTTagPrefixes.RAW_MATERIAL, key)
                    .itemOutput(dust, 2)
                    .createRecipe()
            }
        }
    }
}
