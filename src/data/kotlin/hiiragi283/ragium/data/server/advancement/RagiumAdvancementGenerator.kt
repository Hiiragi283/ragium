package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.advancement.HTAdvancementGenerator
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.variant.VanillaToolVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCriteriaTriggers
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.advancements.critereon.ConsumeItemTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.advancements.critereon.PlayerInteractTrigger
import net.minecraft.advancements.critereon.PlayerTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.block.Blocks
import java.util.Optional

object RagiumAdvancementGenerator : HTAdvancementGenerator() {
    override fun generate(registries: HolderLookup.Provider) {
        root(RagiumAdvancements.ROOT) {
            display {
                setIcon(RagiumItems.getHammer(RagiumMaterialKeys.RAGI_ALLOY))
                title = RagiumTranslation.RAGIUM.translate()
                setDescFromKey(RagiumAdvancements.ROOT)
                backGround = RagiumAPI.id("textures/block/night_metal_block.png")
                showToast = false
                showChat = false
            }
            addCriterion("automatic", PlayerTrigger.TriggerInstance.tick())
        }

        raginite()
        azure()
        deep()
        nightMetal()

        crimson()
        warped()
        eldritch()

        iridescentium()

        child(RagiumAdvancements.CRAFTABLE_TEMPLATES, RagiumAdvancements.ROOT) {
            display {
                setIcon(RagiumItems.getSmithingTemplate(RagiumMaterialKeys.AZURE_STEEL))
                setTitleFromKey(RagiumAdvancements.CRAFTABLE_TEMPLATES)
                setDescFromKey(RagiumAdvancements.CRAFTABLE_TEMPLATES)
            }
            hasAnyItem("has_upgrade", RagiumItems.SMITHING_TEMPLATES.values)
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
            RagiumAdvancements.AMBROSIA,
            RagiumAdvancements.ALLOY_SMELTER,
            RagiumItems.AMBROSIA,
        ) { setChallenge() }

        child(RagiumAdvancements.RAGI_CHERRY, RagiumAdvancements.RAGINITE) {
            display {
                setIcon(RagiumItems.RAGI_CHERRY)
                setTitleFromKey(RagiumAdvancements.RAGI_CHERRY)
                setDescFromKey(RagiumAdvancements.RAGI_CHERRY)
            }
            addCriterion(
                "use_ragi_cherry",
                ConsumeItemTrigger.TriggerInstance.usedItem(itemPredicate(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY)),
            )
        }
        /*child(RagiumAdvancements.RAGI_CHERRY_TOAST, RagiumAdvancements.RAGI_CHERRY) {
            display {
                setIcon(RagiumDelightContents.RAGI_CHERRY_TOAST_BLOCK)
                setTitleFromKey(RagiumAdvancements.RAGI_CHERRY_TOAST)
                setDescFromKey(RagiumAdvancements.RAGI_CHERRY_TOAST)
                setGoal()
            }
            hasAnyItem("has_ragi_cherry_toast_block", RagiumDelightContents.RAGI_CHERRY_TOAST_BLOCK)
            addConditions(ModLoadedCondition(RagiumConst.FARMERS_DELIGHT))
        }*/
        // Advanced
        createSimple(
            RagiumAdvancements.ADV_RAGI_ALLOY,
            RagiumAdvancements.ALLOY_SMELTER,
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
        )
        createSimple(
            RagiumAdvancements.MELTER,
            RagiumAdvancements.ADV_RAGI_ALLOY,
            RagiumBlocks.MELTER,
        ) { setGoal() }
        createSimple(
            RagiumAdvancements.REFINERY,
            RagiumAdvancements.ADV_RAGI_ALLOY,
            RagiumBlocks.REFINERY,
        ) { setGoal() }
        createSimple(
            RagiumAdvancements.PLASTIC,
            RagiumAdvancements.REFINERY,
            RagiumItems.getPlate(CommonMaterialKeys.PLASTIC),
            RagiumModTags.Items.PLASTICS,
        )
        createSimple(
            RagiumAdvancements.POTION_BUNDLE,
            RagiumAdvancements.PLASTIC,
            RagiumItems.POTION_BUNDLE,
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
            RagiumItems.getHammer(RagiumMaterialKeys.RAGI_CRYSTAL),
        ) { setGoal() }
        createUse(
            RagiumAdvancements.RAGI_TICKET,
            RagiumAdvancements.RAGI_CRYSTAL,
            RagiumItems.LOOT_TICKET,
        ) { setGoal() }
    }

    private fun azure() {
        child(RagiumAdvancements.BUDDING_AZURE, RagiumAdvancements.ROOT) {
            display {
                setIcon(RagiumItems.BLUE_KNOWLEDGE)
                setTitleFromKey(RagiumAdvancements.BUDDING_AZURE)
                setDescFromKey(RagiumAdvancements.BUDDING_AZURE)
            }
            addCriterion(
                "use_blue_knowledge",
                ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                    LocationPredicate.Builder
                        .location()
                        .setBlock(BlockPredicate.Builder.block().of(Blocks.BUDDING_AMETHYST)),
                    ItemPredicate.Builder.item().of(RagiumModTags.Items.BUDDING_AZURE_ACTIVATOR),
                ),
            )
        }
        createSimple(
            RagiumAdvancements.AZURE_SHARD,
            RagiumAdvancements.BUDDING_AZURE,
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
                setIcon(RagiumItems.getTool(VanillaToolVariant.PICKAXE, RagiumMaterialKeys.AZURE_STEEL))
                setTitleFromKey(RagiumAdvancements.AZURE_GEARS)
                setDescFromKey(RagiumAdvancements.AZURE_GEARS)
                setGoal()
            }
            hasAnyItem("has_azure_tool", RagiumItems.TOOLS.columnValues(RagiumMaterialKeys.AZURE_STEEL))
        }

        createSimple(
            RagiumAdvancements.MIXER,
            RagiumAdvancements.AZURE_STEEL,
            RagiumBlocks.MIXER,
        ) { setGoal() }
    }

    private fun deep() {
        createSimple(
            RagiumAdvancements.RESONANT_DEBRIS,
            RagiumAdvancements.AZURE_STEEL,
            RagiumBlocks.RESONANT_DEBRIS,
            RagiumCommonTags.Items.ORES_DEEP_SCRAP,
        )
        createSimple(
            RagiumAdvancements.DEEP_STEEL,
            RagiumAdvancements.RESONANT_DEBRIS,
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.DEEP_STEEL,
        )
        child(RagiumAdvancements.BEHEAD_MOB, RagiumAdvancements.DEEP_STEEL) {
            display {
                setIcon(RagiumItems.getTool(VanillaToolVariant.AXE, RagiumMaterialKeys.DEEP_STEEL))
                setTitleFromKey(RagiumAdvancements.BEHEAD_MOB)
                setDescFromKey(RagiumAdvancements.BEHEAD_MOB)
                setGoal()
            }
            addCriterion("behead_mob", RagiumCriteriaTriggers.beheadMob())
        }
        createSimple(
            RagiumAdvancements.ECHO_STAR,
            RagiumAdvancements.DEEP_STEEL,
            RagiumItems.ECHO_STAR,
        ) { setChallenge() }
    }

    private fun nightMetal() {
        createSimple(
            RagiumAdvancements.NIGHT_METAL,
            RagiumAdvancements.ALLOY_SMELTER,
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.NIGHT_METAL,
        )
        createSimple(
            RagiumAdvancements.SIMULATOR,
            RagiumAdvancements.NIGHT_METAL,
            RagiumBlocks.SIMULATOR,
        ) { setGoal() }
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
        createUse(
            RagiumAdvancements.TELEPORT_KEY,
            RagiumAdvancements.WARPED_CRYSTAL,
            RagiumItems.TELEPORT_KEY,
        ) { setGoal() }
        createUse(
            RagiumAdvancements.WARPED_WART,
            RagiumAdvancements.WARPED_CRYSTAL,
            RagiumBlocks.WARPED_WART,
        )
    }

    private fun eldritch() {
        createSimple(
            RagiumAdvancements.ELDRITCH_PEARL,
            RagiumAdvancements.MIXER,
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
            addCriterion(
                "capture_mob",
                PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(itemPredicate(RagiumItems.ELDRITCH_EGG), Optional.empty()),
            )
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
