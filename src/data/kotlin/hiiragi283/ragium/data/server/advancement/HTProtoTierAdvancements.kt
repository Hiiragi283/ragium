package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTAdvancementGenerator
import hiiragi283.ragium.api.data.HTDisplayInfoBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

object HTProtoTierAdvancements : HTAdvancementGenerator("") {
    override fun createRoot(): AdvancementHolder = create("root") {
        display {
            setIcon(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            title = Component.literal(RagiumAPI.MOD_NAME)
            description = Component.literal("Welcome to Ragium!")
            backGround = ResourceLocation.withDefaultNamespace("textures/block/bricks.png")
        }
        hasItemTag("has_raginite", HTTagPrefix.RAW_MATERIAL.createTag(RagiumMaterials.RAGINITE))
    }

    override fun generate(registries: HolderLookup.Provider) {
        // Ragi-Alloy
        val ragiAlloyCompound: AdvancementHolder = createSimple(root, RagiumItems.RAGI_ALLOY_COMPOUND)
        val ragiAlloy: AdvancementHolder =
            createMaterial(ragiAlloyCompound, HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)

        val forgeHammer: AdvancementHolder = createSimple(ragiAlloy, RagiumItems.FORGE_HAMMER)
        val itemMagnet: AdvancementHolder = createSimple(ragiAlloy, RagiumItems.ITEM_MAGNET)
        val expMagnet: AdvancementHolder =
            createSimple(itemMagnet, RagiumItems.EXP_MAGNET, HTDisplayInfoBuilder::setGoal)

        val manualGrinder: AdvancementHolder = createSimple(ragiAlloy, RagiumBlocks.MANUAL_GRINDER)
        val raginiteDust: AdvancementHolder =
            createMaterial(manualGrinder, HTTagPrefix.DUST, RagiumMaterials.RAGINITE)

        // Steel
        val steelCompound: AdvancementHolder = createSimple(root, RagiumItems.STEEL_COMPOUND)
        val steel: AdvancementHolder = createMaterial(steelCompound, HTTagPrefix.INGOT, CommonMaterials.STEEL)
        val frame: AdvancementHolder =
            createSimple(steel, RagiumBlocks.MACHINE_FRAME, HTDisplayInfoBuilder::setChallenge)

        val allMolds: AdvancementHolder = create("all_molds", steel) {
            display {
                setIcon(RagiumItems.INGOT_PRESS_MOLD)
                title = Component.literal("Cast Off!")
                setGoal()
            }
            hasItem(
                "has_molds",
                RagiumItems.BALL_PRESS_MOLD,
                RagiumItems.BLOCK_PRESS_MOLD,
                RagiumItems.GEAR_PRESS_MOLD,
                RagiumItems.INGOT_PRESS_MOLD,
                RagiumItems.PLASTIC_PLATE,
                RagiumItems.ROD_PRESS_MOLD,
                RagiumItems.WIRE_PRESS_MOLD,
            )
        }
    }
}
