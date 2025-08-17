package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTAdvancementGenerator
import hiiragi283.ragium.api.extension.columnValues
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.variant.HTToolVariant
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.critereon.ConsumeItemTrigger
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger
import net.minecraft.advancements.critereon.PlayerTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component

object RagiumAdvancementGenerator : HTAdvancementGenerator() {
    override fun createRoot(): AdvancementHolder = root(RagiumAdvancements.ROOT) {
        display {
            setIcon(RagiumItems.getForgeHammer(RagiumMaterialType.RAGI_ALLOY))
            title = Component.literal(RagiumAPI.MOD_NAME)
            setDescFromKey(RagiumAdvancements.ROOT)
            backGround = vanillaId("textures/block/smooth_stone.png")
            showToast = false
            showChat = false
        }
        addCriterion("automatic", PlayerTrigger.TriggerInstance.tick())
    }

    override fun generate(registries: HolderLookup.Provider) {
        raginite()
        azure()
        crimson()
        warped()
        eldritch()
        iridescentium()

        child(RagiumAdvancements.CRAFTABLE_TEMPLATES, root) {
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
        val raginite: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGINITE,
            root,
            HTMaterialVariant.DUST,
            RagiumMaterialType.RAGINITE,
        )
        // Basic
        val ragiAlloy: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGI_ALLOY,
            raginite,
            HTMaterialVariant.INGOT,
            RagiumMaterialType.RAGI_ALLOY,
        )
        // Advanced
        val advRagiAlloy: AdvancementHolder = createSimple(
            RagiumAdvancements.ADV_RAGI_ALLOY,
            ragiAlloy,
            HTMaterialVariant.INGOT,
            RagiumMaterialType.ADVANCED_RAGI_ALLOY,
        )

        // Elite
        val ragiCrystal: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGI_CRYSTAL,
            raginite,
            HTMaterialVariant.GEM,
            RagiumMaterialType.RAGI_CRYSTAL,
        )
        val ragiCrystalHammer: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGI_CRYSTAL_HAMMER,
            ragiCrystal,
            RagiumItems.getForgeHammer(RagiumMaterialType.RAGI_CRYSTAL),
        ) {
            setGoal()
        }
        val ragiTicket: AdvancementHolder = child(RagiumAdvancements.RAGI_TICKET, ragiCrystal) {
            display {
                setIcon(RagiumItems.RAGI_TICKET)
                setTitleFromKey(RagiumAdvancements.RAGI_TICKET)
                setDescFromKey(RagiumAdvancements.RAGI_TICKET)
                setGoal()
            }
            addCriterion("use_ragi_ticket", ConsumeItemTrigger.TriggerInstance.usedItem(RagiumItems.RAGI_TICKET))
        }
    }

    private fun azure() {
        val azureShard: AdvancementHolder =
            createSimple(
                RagiumAdvancements.AZURE_SHARD,
                root,
                HTMaterialVariant.GEM,
                RagiumMaterialType.AZURE,
            )
        val azureSteel: AdvancementHolder =
            createSimple(
                RagiumAdvancements.AZURE_STEEL,
                azureShard,
                HTMaterialVariant.INGOT,
                RagiumMaterialType.AZURE_STEEL,
            )
        val azureGears: AdvancementHolder = child(RagiumAdvancements.AZURE_GEARS, azureSteel) {
            display {
                setIcon(RagiumItems.getAzureTool(HTToolVariant.PICKAXE))
                setTitleFromKey(RagiumAdvancements.AZURE_GEARS)
                setDescFromKey(RagiumAdvancements.AZURE_GEARS)
                setGoal()
            }
            hasAnyItem("has_azure_tool", RagiumItems.TOOLS.columnValues(RagiumMaterialType.AZURE_STEEL))
        }
    }

    private fun crimson() {
        val crimsonCrystal: AdvancementHolder = createSimple(
            RagiumAdvancements.CRIMSON_CRYSTAL,
            root,
            HTMaterialVariant.GEM,
            RagiumMaterialType.CRIMSON_CRYSTAL,
        )
        val crimsonSoil: AdvancementHolder = child(RagiumAdvancements.CRIMSON_SOIL, crimsonCrystal) {
            display {
                setIcon(RagiumBlocks.CRIMSON_SOIL)
                setTitleFromKey(RagiumAdvancements.CRIMSON_SOIL)
                setDescFromKey(RagiumAdvancements.CRIMSON_SOIL)
                setGoal()
            }
            hasAllItem("has_crimson_soil", RagiumBlocks.CRIMSON_SOIL)
        }
    }

    private fun warped() {
        val warpedCrystal: AdvancementHolder = createSimple(
            RagiumAdvancements.WARPED_CRYSTAL,
            root,
            HTMaterialVariant.GEM,
            RagiumMaterialType.WARPED_CRYSTAL,
        )
        val dimAnchor: AdvancementHolder = child(RagiumAdvancements.DIM_ANCHOR, warpedCrystal) {
            display {
                setIcon(RagiumBlocks.Devices.DIM_ANCHOR)
                setTitleFromKey(RagiumAdvancements.DIM_ANCHOR)
                setDescFromKey(RagiumAdvancements.DIM_ANCHOR)
                setGoal()
            }
            addCriterion(
                "place_dim_anchor",
                ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(RagiumBlocks.Devices.DIM_ANCHOR.get()),
            )
        }
        val teleportTicket: AdvancementHolder = child(RagiumAdvancements.TELEPORT_TICKET, warpedCrystal) {
            display {
                setIcon(RagiumItems.TELEPORT_TICKET)
                setTitleFromKey(RagiumAdvancements.TELEPORT_TICKET)
                setDescFromKey(RagiumAdvancements.TELEPORT_TICKET)
                setGoal()
            }
            addCriterion("use_teleport_ticket", ConsumeItemTrigger.TriggerInstance.usedItem(RagiumItems.TELEPORT_TICKET))
        }
    }

    private fun eldritch() {
        val eldritchPearl: AdvancementHolder = createSimple(
            RagiumAdvancements.ELDRITCH_PEARL,
            root,
            HTMaterialVariant.GEM,
            RagiumMaterialType.ELDRITCH_PEARL,
        )
        val eldritchEgg: AdvancementHolder = child(RagiumAdvancements.ELDRITCH_EGG, eldritchPearl) {
            display {
                setIcon(RagiumItems.ELDRITCH_EGG)
                setTitleFromKey(RagiumAdvancements.ELDRITCH_EGG)
                setDescFromKey(RagiumAdvancements.ELDRITCH_EGG)
                setGoal()
            }
            addCriterion("use_eldritch_egg", ConsumeItemTrigger.TriggerInstance.usedItem(RagiumItems.ELDRITCH_EGG))
        }
        val mysteriousObsidian: AdvancementHolder = child(RagiumAdvancements.MYSTERIOUS_OBSIDIAN, eldritchPearl) {
            display {
                setIcon(RagiumBlocks.MYSTERIOUS_OBSIDIAN)
                setTitleFromKey(RagiumAdvancements.MYSTERIOUS_OBSIDIAN)
                setDescFromKey(RagiumAdvancements.MYSTERIOUS_OBSIDIAN)
                setGoal()
            }
            hasItemsIn("has_mysterious_obsidian", RagiumCommonTags.Items.OBSIDIANS_MYSTERIOUS)
        }
    }

    private fun iridescentium() {
        val iridescentium: AdvancementHolder = createSimple(
            RagiumAdvancements.IRIDESCENTIUM,
            root,
            HTMaterialVariant.INGOT,
            RagiumMaterialType.IRIDESCENTIUM,
        )
        val eternalComponent: AdvancementHolder = createSimple(
            RagiumAdvancements.ETERNAL_COMPONENT,
            iridescentium,
            RagiumItems.ETERNAL_COMPONENT,
        ) { setChallenge() }
    }
}
