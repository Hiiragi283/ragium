package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.advancement.HTAdvancementBuilder
import hiiragi283.ragium.api.data.advancement.HTAdvancementGenerator
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.integration.food.RagiumDelightAddon
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.variant.HTHammerToolVariant
import hiiragi283.ragium.common.variant.HTVanillaToolVariant
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger
import net.minecraft.advancements.critereon.PlayerTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.neoforged.neoforge.common.conditions.ModLoadedCondition

object RagiumAdvancementGenerator : HTAdvancementGenerator() {
    override fun generate(registries: HolderLookup.Provider) {
        root(RagiumAdvancements.ROOT) {
            display {
                setIcon(RagiumItems.getTool(HTHammerToolVariant, RagiumMaterialKeys.RAGI_ALLOY))
                title = Component.literal(RagiumAPI.MOD_NAME)
                setDescFromKey(RagiumAdvancements.ROOT)
                backGround = RagiumAPI.id("textures/block/${RagiumConst.IRIDESCENTIUM}_block.png")
                showToast = false
                showChat = false
            }
            addCriterion("automatic", PlayerTrigger.TriggerInstance.tick())
        }

        raginite()
        azure()
        deep()

        crimson()
        warped()
        eldritch()

        iridescentium()

        child(RagiumAdvancements.CRAFTABLE_TEMPLATES, RagiumAdvancements.ROOT) {
            display {
                setIcon(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
                setTitleFromKey(RagiumAdvancements.CRAFTABLE_TEMPLATES)
                setDescFromKey(RagiumAdvancements.CRAFTABLE_TEMPLATES)
            }
            hasAnyItem(
                "has_upgrade",
                RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE,
                RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE,
            )
        }
    }

    private fun raginite() {
        createSimple(
            RagiumAdvancements.RAGINITE,
            RagiumAdvancements.ROOT,
            CommonMaterialPrefixes.DUST,
            RagiumMaterialKeys.RAGINITE,
        )
        // Basic
        createSimple(
            RagiumAdvancements.RAGI_ALLOY,
            RagiumAdvancements.RAGINITE,
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.RAGI_ALLOY,
        )
        createSimple(
            RagiumAdvancements.ALLOY_SMELTER,
            RagiumAdvancements.RAGI_ALLOY,
            RagiumBlocks.ALLOY_SMELTER,
        ) { setGoal() }

        createSimple(
            RagiumAdvancements.RAGI_CHERRY,
            RagiumAdvancements.RAGINITE,
            RagiumItems.RAGI_CHERRY,
            RagiumCommonTags.Items.FOODS_RAGI_CHERRY,
        )
        HTAdvancementBuilder
            .child(RagiumAdvancements.RAGI_CHERRY)
            .display {
                setIcon(RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK)
                setTitleFromKey(RagiumAdvancements.RAGI_CHERRY_TOAST)
                setDescFromKey(RagiumAdvancements.RAGI_CHERRY_TOAST)
                setGoal()
            }.hasAnyItem("has_ragi_cherry_toast_block", RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK)
            .addConditions(ModLoadedCondition(RagiumConst.FARMERS_DELIGHT))
            .save(output, RagiumAdvancements.RAGI_CHERRY_TOAST)
        // Advanced
        createSimple(
            RagiumAdvancements.ADV_RAGI_ALLOY,
            RagiumAdvancements.RAGI_ALLOY,
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
        )
        createSimple(
            RagiumAdvancements.MELTER,
            RagiumAdvancements.ADV_RAGI_ALLOY,
            RagiumBlocks.MELTER,
        ) { setGoal() }

        // Elite
        createSimple(
            RagiumAdvancements.RAGI_CRYSTAL,
            RagiumAdvancements.RAGINITE,
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.RAGI_CRYSTAL,
        )
        createSimple(
            RagiumAdvancements.RAGI_CRYSTAL_HAMMER,
            RagiumAdvancements.RAGI_CRYSTAL,
            RagiumItems.getTool(HTHammerToolVariant, RagiumMaterialKeys.RAGI_CRYSTAL),
        ) { setGoal() }
        child(RagiumAdvancements.RAGI_TICKET, RagiumAdvancements.RAGI_CRYSTAL) {
            display {
                setIcon(RagiumItems.LOOT_TICKET)
                setTitleFromKey(RagiumAdvancements.RAGI_TICKET)
                setDescFromKey(RagiumAdvancements.RAGI_TICKET)
                setGoal()
            }
            useItem(RagiumItems.LOOT_TICKET)
        }
    }

    private fun azure() {
        createSimple(
            RagiumAdvancements.AZURE_SHARD,
            RagiumAdvancements.ALLOY_SMELTER,
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.AZURE,
        )
        createSimple(
            RagiumAdvancements.AZURE_STEEL,
            RagiumAdvancements.AZURE_SHARD,
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.AZURE_STEEL,
        )
        child(RagiumAdvancements.AZURE_GEARS, RagiumAdvancements.AZURE_STEEL) {
            display {
                setIcon(RagiumItems.getTool(HTVanillaToolVariant.PICKAXE, RagiumMaterialKeys.AZURE_STEEL))
                setTitleFromKey(RagiumAdvancements.AZURE_GEARS)
                setDescFromKey(RagiumAdvancements.AZURE_GEARS)
                setGoal()
            }
            hasAnyItem("has_azure_tool", RagiumItems.TOOLS.columnValues(RagiumMaterialKeys.AZURE_STEEL))
        }

        createSimple(
            RagiumAdvancements.SIMULATOR,
            RagiumAdvancements.AZURE_STEEL,
            RagiumBlocks.SIMULATOR,
        ) { setGoal() }
    }

    private fun deep() {
        createSimple(
            RagiumAdvancements.RESONANT_DEBRIS,
            RagiumAdvancements.SIMULATOR,
            RagiumBlocks.RESONANT_DEBRIS,
            RagiumCommonTags.Items.ORES_DEEP_SCRAP,
        )
        createSimple(
            RagiumAdvancements.DEEP_STEEL,
            RagiumAdvancements.RESONANT_DEBRIS,
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.DEEP_STEEL,
        )
        child(RagiumAdvancements.DEEP_GEARS, RagiumAdvancements.DEEP_STEEL) {
            display {
                setIcon(RagiumItems.getTool(HTVanillaToolVariant.PICKAXE, RagiumMaterialKeys.DEEP_STEEL))
                setTitleFromKey(RagiumAdvancements.DEEP_GEARS)
                setDescFromKey(RagiumAdvancements.DEEP_GEARS)
                setGoal()
            }
            hasAnyItem("has_azure_tool", RagiumItems.TOOLS.columnValues(RagiumMaterialKeys.DEEP_STEEL))
        }

        createSimple(
            RagiumAdvancements.ECHO_STAR,
            RagiumAdvancements.DEEP_STEEL,
            RagiumItems.ECHO_STAR,
        ) { setChallenge() }
    }

    private fun crimson() {
        createSimple(
            RagiumAdvancements.CRIMSON_CRYSTAL,
            RagiumAdvancements.MELTER,
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.CRIMSON_CRYSTAL,
        )
        child(RagiumAdvancements.CRIMSON_SOIL, RagiumAdvancements.CRIMSON_CRYSTAL) {
            display {
                setIcon(RagiumBlocks.CRIMSON_SOIL)
                setTitleFromKey(RagiumAdvancements.CRIMSON_SOIL)
                setDescFromKey(RagiumAdvancements.CRIMSON_SOIL)
                setGoal()
            }
            hasAnyItem("has_crimson_soil", RagiumBlocks.CRIMSON_SOIL)
        }
    }

    private fun warped() {
        createSimple(
            RagiumAdvancements.WARPED_CRYSTAL,
            RagiumAdvancements.MELTER,
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.WARPED_CRYSTAL,
        )
        child(RagiumAdvancements.DIM_ANCHOR, RagiumAdvancements.WARPED_CRYSTAL) {
            display {
                setIcon(RagiumBlocks.DIM_ANCHOR)
                setTitleFromKey(RagiumAdvancements.DIM_ANCHOR)
                setDescFromKey(RagiumAdvancements.DIM_ANCHOR)
                setGoal()
            }
            addCriterion(
                "place_dim_anchor",
                ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(RagiumBlocks.DIM_ANCHOR.get()),
            )
        }
        child(RagiumAdvancements.TELEPORT_KEY, RagiumAdvancements.WARPED_CRYSTAL) {
            display {
                setIcon(RagiumItems.TELEPORT_KEY)
                setTitleFromKey(RagiumAdvancements.TELEPORT_KEY)
                setDescFromKey(RagiumAdvancements.TELEPORT_KEY)
                setGoal()
            }
            useItem(RagiumItems.TELEPORT_KEY)
        }
    }

    private fun eldritch() {
        createSimple(
            RagiumAdvancements.ELDRITCH_PEARL,
            RagiumAdvancements.MELTER,
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.ELDRITCH_PEARL,
        )
        child(RagiumAdvancements.ELDRITCH_EGG, RagiumAdvancements.ELDRITCH_PEARL) {
            display {
                setIcon(RagiumItems.ELDRITCH_EGG)
                setTitleFromKey(RagiumAdvancements.ELDRITCH_EGG)
                setDescFromKey(RagiumAdvancements.ELDRITCH_EGG)
                setGoal()
            }
            useItem(RagiumItems.ELDRITCH_EGG)
        }
        child(RagiumAdvancements.MYSTERIOUS_OBSIDIAN, RagiumAdvancements.ELDRITCH_PEARL) {
            display {
                setIcon(RagiumBlocks.MYSTERIOUS_OBSIDIAN)
                setTitleFromKey(RagiumAdvancements.MYSTERIOUS_OBSIDIAN)
                setDescFromKey(RagiumAdvancements.MYSTERIOUS_OBSIDIAN)
                setGoal()
            }
            hasAnyItem(RagiumCommonTags.Items.OBSIDIANS_MYSTERIOUS)
        }
    }

    private fun iridescentium() {
        createSimple(
            RagiumAdvancements.IRIDESCENTIUM,
            RagiumAdvancements.ELDRITCH_PEARL,
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.IRIDESCENTIUM,
        )
        createSimple(
            RagiumAdvancements.ETERNAL_COMPONENT,
            RagiumAdvancements.IRIDESCENTIUM,
            RagiumItems.getComponent(HTComponentTier.ETERNAL),
        ) { setChallenge() }
    }
}
