package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTAdvancementGenerator
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component

object RagiumAdvancementGenerator : HTAdvancementGenerator("") {
    override fun createRoot(): AdvancementHolder = create("root") {
        display {
            setIcon(RagiumItems.RAGI_TICKET)
            title = Component.literal(RagiumAPI.MOD_NAME)
            setDescFromKey(RagiumTranslationKeys.ADV_ROOT_DESC)
            backGround = RagiumAPI.id("textures/block/ragi_stone.png")
            showToast = false
            showChat = false
        }
        hasItem("has_blank_ticket", RagiumItems.BLANK_TICKET)
    }

    override fun generate(registries: HolderLookup.Provider) {
        raginite()
        azure()
        blatinum()
        deep()

        val eternalTicket: AdvancementHolder = createSimple(root, RagiumItems.ETERNAL_TICKET) {
            setChallenge()
        }
    }

    private fun raginite() {
        val ragiTicket: AdvancementHolder = createSimple(root, RagiumItems.RAGI_TICKET)
        val raginite: AdvancementHolder = createSimple(ragiTicket, RagiumItems.RAGINITE_DUST)
        val ragiAlloy: AdvancementHolder = createSimple(raginite, RagiumItems.RAGI_ALLOY_INGOT)
        val forgeHammer: AdvancementHolder = create("forge_hammer", ragiAlloy) {
            display {
                setIcon(RagiumItems.RAGI_ALLOY_TOOLS.hammerItem)
                setTitleFromKey(RagiumTranslationKeys.ADV_FORGE_HAMMER_TITLE)
                setDescFromKey(RagiumTranslationKeys.ADV_FORGE_HAMMER_DESC)
            }
            hasItemTag("has_forge_hammer", RagiumItemTags.TOOLS_FORGE_HAMMER)
        }

        val basicMachine: AdvancementHolder = createSimple(forgeHammer, RagiumBlocks.MACHINE_CASING)
    }

    private fun azure() {
        val azureTicket: AdvancementHolder = createSimple(root, RagiumItems.AZURE_TICKET)
        val azureShard: AdvancementHolder = createSimple(azureTicket, RagiumItems.AZURE_SHARD)
        val azureSteel: AdvancementHolder = createSimple(azureShard, RagiumItems.AZURE_STEEL_INGOT)
    }

    private fun blatinum() {
    }

    private fun deep() {
    }
}
