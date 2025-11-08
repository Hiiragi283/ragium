package hiiragi283.ragium.common.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialDefinitionEvent
import hiiragi283.ragium.api.material.addDefaultPrefix
import hiiragi283.ragium.api.material.addName
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber
object RagiumMaterialEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun gatherDefinition(event: HTMaterialDefinitionEvent) {
        vanilla(event)
        common(event)
        ragium(event)
    }

    @JvmStatic
    private fun vanilla(event: HTMaterialDefinitionEvent) {
        // Gem
        event.modify(VanillaMaterialKeys.LAPIS) {
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            addName("Lapis", "ラピス")
        }
        event.modify(VanillaMaterialKeys.QUARTZ) {
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            addName("Quartz", "水晶")
        }
        event.modify(VanillaMaterialKeys.AMETHYST) {
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            addName("Amethyst", "アメシスト")
        }
        event.modify(VanillaMaterialKeys.DIAMOND) {
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            addName("Diamond", "ダイアモンド")
        }
        event.modify(VanillaMaterialKeys.EMERALD) {
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            addName("Emerald", "エメラルド")
        }
        event.modify(VanillaMaterialKeys.ECHO) {
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            addName("Echo", "残響")
        }
        // Metal
        event.modify(VanillaMaterialKeys.COPPER) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Copper", "銅")
        }
        event.modify(VanillaMaterialKeys.IRON) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Iron", "鉄")
        }
        event.modify(VanillaMaterialKeys.GOLD) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Gold", "金")
        }
        event.modify(VanillaMaterialKeys.NETHERITE) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Netherite", "ネザライト")
        }
        // Other
        event.modify(VanillaMaterialKeys.COAL) {
            addDefaultPrefix(CommonMaterialPrefixes.FUEL)
            addName("Coal", "石炭")
        }
        event.modify(VanillaMaterialKeys.CHARCOAL) {
            addDefaultPrefix(CommonMaterialPrefixes.FUEL)
            addName("Charcoal", "木炭")
        }
        event.modify(VanillaMaterialKeys.REDSTONE) {
            addDefaultPrefix(CommonMaterialPrefixes.DUST)
            addName("Redstone", "レッドストーン")
        }
        event.modify(VanillaMaterialKeys.GLOWSTONE) {
            addDefaultPrefix(CommonMaterialPrefixes.DUST)
            addName("Glowstone", "グロウストーン")
        }
        event.modify(VanillaMaterialKeys.SOUL) {
            addName("Soul", "ソウル")
        }

        event.modify(VanillaMaterialKeys.BLACKSTONE) {
            addName("Blackstone", "ブラックストーン")
        }
        event.modify(VanillaMaterialKeys.OBSIDIAN) {
            addName("Obsidian", "黒曜石")
        }

        event.modify(VanillaMaterialKeys.WOOD) {
            addName("Wood", "木")
        }

        RagiumAPI.LOGGER.info("Modified Vanilla materials!")
    }

    @JvmStatic
    private fun common(event: HTMaterialDefinitionEvent) {
        event.modify(CommonMaterialKeys.Metals.entries) { addDefaultPrefix(CommonMaterialPrefixes.INGOT) }
        event.modify(CommonMaterialKeys.Alloys.entries) { addDefaultPrefix(CommonMaterialPrefixes.INGOT) }
        event.modify(CommonMaterialKeys.Gems.entries) { gems ->
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            when (gems) {
                CommonMaterialKeys.Gems.CINNABAR -> addName("Cinnabar", "辰砂")
                CommonMaterialKeys.Gems.FLUORITE -> {}
                CommonMaterialKeys.Gems.PERIDOT -> {}
                CommonMaterialKeys.Gems.RUBY -> {}
                CommonMaterialKeys.Gems.SALTPETER -> addName("Saltpeter", "硝石")
                CommonMaterialKeys.Gems.SAPPHIRE -> {}
                CommonMaterialKeys.Gems.SULFUR -> addName("Sulfur", "硫黄")
            }
        }

        event.modify(CommonMaterialKeys.COAL_COKE) {
            addDefaultPrefix(CommonMaterialPrefixes.FUEL)
            addName("Coal Coke", "石炭コークス")
        }
        event.modify(CommonMaterialKeys.PLASTIC) {
            addDefaultPrefix(CommonMaterialPrefixes.PLATE)
            addName("Plastic", "プラスチック")
        }
    }

    @JvmStatic
    private fun ragium(event: HTMaterialDefinitionEvent) {
        // Mineral
        event.modify(RagiumMaterialKeys.RAGINITE) {
            addDefaultPrefix(CommonMaterialPrefixes.DUST)
            addName("Raginite", "ラギナイト")
        }
        // Gem
        event.modify(RagiumMaterialKeys.RAGI_CRYSTAL) {
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            addName("Ragi-Crystal", "ラギクリスタリル")
        }
        event.modify(RagiumMaterialKeys.AZURE) {
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            addName("Azure Shard", "紺碧の欠片")
        }
        event.modify(RagiumMaterialKeys.CRIMSON_CRYSTAL) {
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            addName("Crimson Crystal", "深紅のクリスタリル")
        }
        event.modify(RagiumMaterialKeys.WARPED_CRYSTAL) {
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            addName("Warped Crystal", "歪んだクリスタリル")
        }
        event.modify(RagiumMaterialKeys.ELDRITCH_PEARL) {
            addDefaultPrefix(CommonMaterialPrefixes.GEM)
            addName("Eldritch Pearl", "異質な真珠")
        }
        // Metal
        event.modify(RagiumMaterialKeys.RAGI_ALLOY) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Ragi-Alloy", "ラギ合金")
        }
        event.modify(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Advanced Ragi-Alloy", "発展ラギ合金")
        }
        event.modify(RagiumMaterialKeys.AZURE_STEEL) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Azure Steel", "紺鉄")
        }
        event.modify(RagiumMaterialKeys.DEEP_STEEL) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Deep Steel", "深層鋼")
        }
        event.modify(RagiumMaterialKeys.NIGHT_METAL) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Night Metal", "夜金")
        }
        event.modify(RagiumMaterialKeys.IRIDESCENTIUM) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Iridescentium", "七色金")
        }
        // Food
        event.modify(FoodMaterialKeys.CHOCOLATE) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Chocolate", "チョコレート")
        }
        event.modify(FoodMaterialKeys.RAW_MEAT) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Raw Meat", "生肉")
        }
        event.modify(FoodMaterialKeys.COOKED_MEAT) {
            addDefaultPrefix(CommonMaterialPrefixes.INGOT)
            addName("Cooked Meat", "焼肉")
        }
        // Other
        event.modify(RagiumMaterialKeys.BAMBOO_CHARCOAL) {
            addDefaultPrefix(CommonMaterialPrefixes.FUEL)
            addName("Bamboo Charcoal", "竹炭")
        }
        RagiumAPI.LOGGER.info("Modified Ragium materials!")
    }
}
