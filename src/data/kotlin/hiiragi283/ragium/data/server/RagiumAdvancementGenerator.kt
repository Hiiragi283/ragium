package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTAdvancementGenerator
import hiiragi283.ragium.api.data.RagiumAdvancements
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.critereon.ConsumeItemTrigger
import net.minecraft.advancements.critereon.PlayerTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component

object RagiumAdvancementGenerator : HTAdvancementGenerator() {
    override fun createRoot(): AdvancementHolder = create(RagiumAdvancements.ROOT) {
        display {
            setIcon(RagiumItems.ForgeHammers.RAGI_ALLOY)
            title = Component.literal(RagiumAPI.MOD_NAME)
            setDescFromKey(RagiumAdvancements.ROOT)
            backGround = RagiumAPI.id("textures/block/plastic_block.png")
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

        create(RagiumAdvancements.ETERNAL_TICKET, root) {
            display {
                setIcon(RagiumItems.Tickets.ETERNAL)
                setTitleFromKey(RagiumAdvancements.ETERNAL_TICKET)
                setDescFromKey(RagiumAdvancements.ETERNAL_TICKET)
                setChallenge()
            }
            hasAllItem("has_ticket", RagiumItems.Tickets.ETERNAL)
        }
    }

    private fun raginite() {
        val raginite: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGINITE,
            root,
            RagiumItems.Dusts.RAGINITE,
            RagiumCommonTags.Items.DUSTS_RAGINITE,
        )
        // Basic
        val ragiAlloy: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGI_ALLOY,
            raginite,
            RagiumItems.Ingots.RAGI_ALLOY,
            RagiumCommonTags.Items.INGOTS_RAGI_ALLOY,
        )
        // Advanced
        // Elite
        val ragiCrystal: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGI_CRYSTAL,
            raginite,
            RagiumItems.Gems.RAGI_CRYSTAL,
            RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL,
        )
        val ragiTicket: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGI_TICKET,
            ragiCrystal,
            RagiumItems.Tickets.RAGI,
        ) { setGoal() }
    }

    private fun azure() {
        val azureTicket: AdvancementHolder =
            createSimple(RagiumAdvancements.AZURE_TICKET, root, RagiumItems.Tickets.AZURE)
        val azureShard: AdvancementHolder =
            createSimple(RagiumAdvancements.AZURE_SHARD, azureTicket, RagiumItems.Gems.AZURE_SHARD, RagiumCommonTags.Items.GEMS_AZURE)
        val azureGears: AdvancementHolder = create(RagiumAdvancements.AZURE_GEARS, azureShard) {
            display {
                setIcon(RagiumItems.AZURE_STEEL_TOOLS.pickaxeItem)
                setTitleFromKey(RagiumAdvancements.AZURE_GEARS)
                setDescFromKey(RagiumAdvancements.AZURE_GEARS)
                setGoal()
            }
            hasAnyItem("has_azure_tool", RagiumItems.AZURE_STEEL_TOOLS.itemHolders)
        }
    }

    private fun crimson() {
        val crimsonCrystal: AdvancementHolder = createSimple(
            RagiumAdvancements.CRIMSON_CRYSTAL,
            root,
            RagiumItems.Gems.CRIMSON_CRYSTAL,
            RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL,
        )
        val crimsonSoil: AdvancementHolder = create(RagiumAdvancements.CRIMSON_SOIL, crimsonCrystal) {
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
            RagiumItems.Gems.WARPED_CRYSTAL,
            RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL,
        )
        val teleportTicket: AdvancementHolder = create(RagiumAdvancements.TELEPORT_TICKET, warpedCrystal) {
            display {
                setIcon(RagiumItems.Tickets.TELEPORT)
                setTitleFromKey(RagiumAdvancements.TELEPORT_TICKET)
                setDescFromKey(RagiumAdvancements.TELEPORT_TICKET)
                setGoal()
            }
            addCriterion("use_teleport_ticket", ConsumeItemTrigger.TriggerInstance.usedItem(RagiumItems.Tickets.TELEPORT))
            requirements(AdvancementRequirements.Strategy.OR)
        }
    }

    private fun eldritch() {
        val eldritchPearl: AdvancementHolder = createSimple(
            RagiumAdvancements.ELDRITCH_PEARL,
            root,
            RagiumItems.Gems.ELDRITCH_PEARL,
            RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL,
        )
        val mysteriousObsidian: AdvancementHolder = create(RagiumAdvancements.MYSTERIOUS_OBSIDIAN, eldritchPearl) {
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
