package hiiragi283.ragium.common.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialDefinitionEvent
import hiiragi283.ragium.api.material.addName
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
        ragium(event)
    }

    @JvmStatic
    private fun vanilla(event: HTMaterialDefinitionEvent) {
        // Gem
        event.modify(VanillaMaterialKeys.LAPIS) {
            addName("Lapis", "ラピス")
        }
        event.modify(VanillaMaterialKeys.QUARTZ) {
            addName("Quartz", "水晶")
        }
        event.modify(VanillaMaterialKeys.AMETHYST) {
            addName("Amethyst", "アメシスト")
        }
        event.modify(VanillaMaterialKeys.DIAMOND) {
            addName("Diamond", "ダイアモンド")
        }
        event.modify(VanillaMaterialKeys.EMERALD) {
            addName("Emerald", "エメラルド")
        }
        event.modify(VanillaMaterialKeys.ECHO) {
            addName("Echo", "残響")
        }
        // Metal
        event.modify(VanillaMaterialKeys.COPPER) {
            addName("Copper", "銅")
        }
        event.modify(VanillaMaterialKeys.IRON) {
            addName("Iron", "鉄")
        }
        event.modify(VanillaMaterialKeys.GOLD) {
            addName("Gold", "金")
        }
        event.modify(VanillaMaterialKeys.NETHERITE) {
            addName("Netherite", "ネザライト")
        }
        // Other
        event.modify(VanillaMaterialKeys.COAL) {
            addName("Coal", "石炭")
        }
        event.modify(VanillaMaterialKeys.CHARCOAL) {
            addName("Charcoal", "木炭")
        }
        event.modify(VanillaMaterialKeys.REDSTONE) {
            addName("Redstone", "レッドストーン")
        }
        event.modify(VanillaMaterialKeys.GLOWSTONE) {
            addName("Glowstone", "グロウストーン")
        }
        event.modify(VanillaMaterialKeys.SOUL) {
            addName("Soul", "ソウル")
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
    private fun ragium(event: HTMaterialDefinitionEvent) {
        // Mineral
        event.modify(RagiumMaterialKeys.RAGINITE) {
            addName("Raginite", "ラギナイト")
        }
        event.modify(RagiumMaterialKeys.CINNABAR) {
            addName("Cinnabar", "辰砂")
        }
        event.modify(RagiumMaterialKeys.SALTPETER) {
            addName("Saltpeter", "硝石")
        }
        event.modify(RagiumMaterialKeys.SULFUR) {
            addName("Sulfur", "硫黄")
        }
        // Gem
        event.modify(RagiumMaterialKeys.RAGI_CRYSTAL) {
            addName("Ragi-Crystal", "ラギクリスタリル")
        }
        event.modify(RagiumMaterialKeys.AZURE) {
            addName("Azure Shard", "紺碧の欠片")
        }
        event.modify(RagiumMaterialKeys.CRIMSON_CRYSTAL) {
            addName("Crimson Crystal", "深紅のクリスタリル")
        }
        event.modify(RagiumMaterialKeys.WARPED_CRYSTAL) {
            addName("Warped Crystal", "歪んだクリスタリル")
        }
        event.modify(RagiumMaterialKeys.ELDRITCH_PEARL) {
            addName("Eldritch Pearl", "異質な真珠")
        }
        // Metal
        event.modify(RagiumMaterialKeys.RAGI_ALLOY) {
            addName("Ragi-Alloy", "ラギ合金")
        }
        event.modify(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) {
            addName("Advanced Ragi-Alloy", "発展ラギ合金")
        }
        event.modify(RagiumMaterialKeys.AZURE_STEEL) {
            addName("Azure Steel", "紺鉄")
        }
        event.modify(RagiumMaterialKeys.DEEP_STEEL) {
            addName("Deep Steel", "深層鋼")
        }
        event.modify(RagiumMaterialKeys.GILDIUM) {
            addName("Gildium", "鍍金")
        }
        event.modify(RagiumMaterialKeys.IRIDESCENTIUM) {
            addName("Iridescentium", "七色金")
        }
        // Food
        event.modify(RagiumMaterialKeys.CHOCOLATE) {
            addName("Chocolate", "チョコレート")
        }
        event.modify(RagiumMaterialKeys.MEAT) {
            addName("Meat", "生肉")
        }
        event.modify(RagiumMaterialKeys.COOKED_MEAT) {
            addName("Cooked Meat", "焼肉")
        }
        // Other
        event.modify(RagiumMaterialKeys.BAMBOO_CHARCOAL) {
            addName("Bamboo Charcoal", "竹炭")
        }
        event.modify(RagiumMaterialKeys.COAL_COKE) {
            addName("Coal Coke", "石炭コークス")
        }
        event.modify(RagiumMaterialKeys.PLASTIC) {
            addName("Plastic", "プラスチック")
        }
        RagiumAPI.LOGGER.info("Modified Ragium materials!")
    }
}
