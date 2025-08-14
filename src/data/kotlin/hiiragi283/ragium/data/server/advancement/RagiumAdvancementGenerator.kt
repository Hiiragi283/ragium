package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTAdvancementGenerator
import hiiragi283.ragium.api.extension.columnValues
import hiiragi283.ragium.api.tag.RagiumCommonTags
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
            title = Component.literal(RagiumAPI.Companion.MOD_NAME)
            setDescFromKey(RagiumAdvancements.ROOT)
            backGround = RagiumAPI.Companion.id("textures/block/plastic_block.png")
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

        child(RagiumAdvancements.ETERNAL_TICKET, root) {
            display {
                setIcon(RagiumItems.ETERNAL_TICKET)
                setTitleFromKey(RagiumAdvancements.ETERNAL_TICKET)
                setDescFromKey(RagiumAdvancements.ETERNAL_TICKET)
                setChallenge()
            }
            hasAllItem("has_ticket", RagiumItems.ETERNAL_TICKET)
        }
    }

    private fun raginite() {
        val raginite: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGINITE,
            root,
            RagiumItems.getDust(RagiumMaterialType.RAGINITE),
            RagiumCommonTags.Items.DUSTS_RAGINITE,
        )
        // Basic
        val ragiAlloy: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGI_ALLOY,
            raginite,
            RagiumItems.getIngot(RagiumMaterialType.RAGI_ALLOY),
            RagiumCommonTags.Items.INGOTS_RAGI_ALLOY,
        )
        // Advanced
        // Elite
        val ragiCrystal: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGI_CRYSTAL,
            raginite,
            RagiumItems.getGem(RagiumMaterialType.RAGI_CRYSTAL),
            RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL,
        )
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
                RagiumItems.getGem(RagiumMaterialType.AZURE),
                RagiumCommonTags.Items.GEMS_AZURE,
            )
        val azureSteel: AdvancementHolder =
            createSimple(
                RagiumAdvancements.AZURE_STEEL,
                azureShard,
                RagiumItems.getIngot(RagiumMaterialType.AZURE_STEEL),
                RagiumCommonTags.Items.INGOTS_AZURE_STEEL,
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
            RagiumItems.getGem(RagiumMaterialType.CRIMSON_CRYSTAL),
            RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL,
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
            RagiumItems.getGem(RagiumMaterialType.WARPED_CRYSTAL),
            RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL,
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
            RagiumItems.getGem(RagiumMaterialType.ELDRITCH_PEARL),
            RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL,
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
}
