package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.advancements.HTBlockInteractionTrigger
import hiiragi283.ragium.api.data.HTAdvancementGenerator
import hiiragi283.ragium.api.data.RagiumAdvancements
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.critereon.ConsumeItemTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.level.block.Blocks

object RagiumAdvancementGenerator : HTAdvancementGenerator() {
    override fun createRoot(): AdvancementHolder = create(RagiumAdvancements.ROOT) {
        display {
            setIcon(RagiumItems.RAGI_ALLOY_HAMMER)
            title = Component.literal(RagiumAPI.MOD_NAME)
            setDescFromKey(RagiumAdvancements.ROOT)
            backGround = RagiumAPI.id("textures/block/ragi_stone.png")
            showToast = false
            showChat = false
        }
        hasAllItem("has_blank_ticket", RagiumItems.BLANK_TICKET)
    }

    override fun generate(registries: HolderLookup.Provider) {
        raginite()
        azure()
        crimson()
        warped()
        eldritch()

        val eternalTicket: AdvancementHolder = create(RagiumAdvancements.ETERNAL_TICKET, root) {
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
        val ragiTicket: AdvancementHolder = createSimple(RagiumAdvancements.RAGI_TICKET, root, RagiumItems.RAGI_TICKET)
        val raginite: AdvancementHolder = createSimple(
            RagiumAdvancements.RAGINITE_DUST,
            ragiTicket,
            RagiumItems.RAGINITE_DUST,
            RagiumItemTags.DUSTS_RAGINITE,
        )
    }

    private fun azure() {
        val azureTicket: AdvancementHolder =
            createSimple(RagiumAdvancements.AZURE_TICKET, root, RagiumItems.AZURE_TICKET)
        val azureShard: AdvancementHolder =
            createSimple(RagiumAdvancements.AZURE_SHARD, azureTicket, RagiumItems.AZURE_SHARD)
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
            RagiumItems.CRIMSON_CRYSTAL,
            RagiumItemTags.GEMS_CRIMSON_CRYSTAL,
        )
        val crimsonSoil: AdvancementHolder = create(RagiumAdvancements.CRIMSON_SOIL, crimsonCrystal) {
            display {
                setIcon(RagiumBlocks.CRIMSON_SOIL)
                setTitleFromKey(RagiumAdvancements.CRIMSON_SOIL)
                setDescFromKey(RagiumAdvancements.CRIMSON_SOIL)
                setGoal()
            }
            addCriterion(
                "interact_soul_soil",
                HTBlockInteractionTrigger.TriggerInstance.interactBlock(Blocks.SOUL_SOIL),
            )
            hasAllItem("has_crimson_soil", RagiumBlocks.CRIMSON_SOIL)
            requirements(AdvancementRequirements.Strategy.OR)
        }
    }

    private fun warped() {
        val warpedCrystal: AdvancementHolder = createSimple(
            RagiumAdvancements.WARPED_CRYSTAL,
            root,
            RagiumItems.WARPED_CRYSTAL,
            RagiumItemTags.GEMS_WARPED_CRYSTAL,
        )
        val teleportTicket: AdvancementHolder = create(RagiumAdvancements.TELEPORT_TICKET, warpedCrystal) {
            display {
                setIcon(RagiumItems.TELEPORT_TICKET)
                setTitleFromKey(RagiumAdvancements.TELEPORT_TICKET)
                setDescFromKey(RagiumAdvancements.TELEPORT_TICKET)
                setGoal()
            }
            addCriterion("use_teleport_ticket", ConsumeItemTrigger.TriggerInstance.usedItem(RagiumItems.TELEPORT_TICKET))
            requirements(AdvancementRequirements.Strategy.OR)
        }
    }

    private fun eldritch() {
        val eldritchPearl: AdvancementHolder = createSimple(
            RagiumAdvancements.ELDRITCH_PEARL,
            root,
            RagiumItems.ELDRITCH_PEARL,
            RagiumItemTags.GEMS_ELDRITCH_PEARL,
        )
    }
}
