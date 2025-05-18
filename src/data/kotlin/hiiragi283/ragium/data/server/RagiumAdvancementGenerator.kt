package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTAdvancementGenerator
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component

object RagiumAdvancementGenerator : HTAdvancementGenerator("") {
    override fun createRoot(): AdvancementHolder = create("root") {
        display {
            setIcon(RagiumItems.RAGI_ALLOY_HAMMER)
            title = Component.literal(RagiumAPI.MOD_NAME)
            setDescFromKey(RagiumTranslationKeys.ADV_ROOT_DESC)
            backGround = RagiumAPI.id("textures/block/ragi_stone.png")
            showToast = false
            showChat = false
        }
        hasAllItem("has_blank_ticket", RagiumItems.BLANK_TICKET)
    }

    override fun generate(registries: HolderLookup.Provider) {
        raginite()
        azure()
        blatinum()
        deep()

        val eternalTicket: AdvancementHolder = create("eternal_ticket", root) {
            display {
                setIcon(RagiumItems.ETERNAL_TICKET)
                setTitleFromKey(RagiumTranslationKeys.ADV_ETERNAL_TICKET_TITLE)
                setDescFromKey(RagiumTranslationKeys.ADV_ETERNAL_TICKET_DESC)
                setChallenge()
            }
            hasAllItem("has_ticket", RagiumItems.ETERNAL_TICKET)
        }
    }

    private fun raginite() {
        val ragiTicket: AdvancementHolder = createSimple(root, RagiumItems.RAGI_TICKET)
        val raginite: AdvancementHolder = createSimple(ragiTicket, RagiumItems.RAGINITE_DUST)
        val ragiAlloy: AdvancementHolder = createSimple(raginite, RagiumItems.RAGI_ALLOY_INGOT)
        val basicMachine: AdvancementHolder = createSimple(ragiAlloy, RagiumBlocks.MACHINE_CASING)
    }

    private fun azure() {
        val azureTicket: AdvancementHolder = createSimple(root, RagiumItems.AZURE_TICKET)
        val azureShard: AdvancementHolder = createSimple(azureTicket, RagiumItems.AZURE_SHARD)
        val azureSteel: AdvancementHolder = createSimple(azureShard, RagiumItems.AZURE_STEEL_INGOT)
        val azureTools: AdvancementHolder = create("azure_tools", azureSteel) {
            display {
                setIcon(RagiumItems.AZURE_STEEL_TOOLS.pickaxeItem)
                setTitleFromKey(RagiumTranslationKeys.ADV_AZURE_TOOL_TITLE)
                setDescFromKey(RagiumTranslationKeys.ADV_AZURE_TOOL_DESC)
                setGoal()
            }
            hasAnyItem("has_azure_tool", RagiumItems.AZURE_STEEL_TOOLS.itemHolders)
        }
    }

    private fun blatinum() {
    }

    private fun deep() {
    }
}
