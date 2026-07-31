package hiiragi283.ragium.data.advancement

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.advancement.AdvancementKey
import hiiragi283.core.api.data.advancement.HTAdvancementProvider
import hiiragi283.core.api.data.advancement.builder.HTAdvancementBuilder
import hiiragi283.core.api.item.HTItemLike
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartKey
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.util.getOrThrow
import hiiragi283.core.api.util.some
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput

class RagiumAdvancementProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTAdvancementProvider(packOutput, future, RagiumAPI.MOD_ID) {
    private fun getItem(part: HTPartKey, key: HTMaterialKey): HTMaterialContents.ItemEntry = HiiragiCoreAccess.INSTANCE.registeredContents.items.getResult(part, key).getOrThrow()

    private fun <T> createSimple(key: AdvancementKey, parentKey: AdvancementKey, item: T) where T : HTItemLike<*>, T : HTIdLike {
        HTAdvancementBuilder.create(key) {
            +parentKey
            display { +item.toStack() }
            inventory("has_${item.path}") { +ItemPredicate.Builder.item().of(item) }
        }.save(exporter)
    }

    private fun ItemPredicate.Builder.of(prefix: HTTagPrefix, key: HTMaterialKey): ItemPredicate.Builder = this.of(prefix.itemTagKey(key))

    override fun buildAdvancements() {
        HTAdvancementBuilder.create(RagiumAdvancementKeys.ROOT) {
            display {
                +getItem(CommonParts.DUST, RagiumMaterialKeys.RAGINITE).toStack()
                backGround = vanillaId(HTConst.TEXTURES, HTConst.BLOCK, "smooth_stone.png").some()
                showToast = false
                showChat = false
            }
            inventory("has_raginite") { +ItemPredicate.Builder.item().of(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE) }
        }.save(exporter)

        basic()
        advanced()
        elite()
        ultimate()
    }

    override fun getName(): String = "Advancements - $modId"

    private fun basic() {
        HTAdvancementBuilder.create(RagiumAdvancementKeys.RAGI_ALLOY) {
            +RagiumAdvancementKeys.ROOT
            display {
                +getItem(CommonParts.INGOT, RagiumMaterialKeys.RAGI_ALLOY).toStack()
                type = AdvancementType.GOAL
            }
            inventory("has_ragi_alloy") { +ItemPredicate.Builder.item().of(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY) }
        }.save(exporter)

        createSimple(RagiumAdvancementKeys.ALLOY_SMELTER, RagiumAdvancementKeys.RAGI_ALLOY, RagiumBlocks.ALLOY_SMELTER)
    }

    private fun advanced() {
        HTAdvancementBuilder.create(RagiumAdvancementKeys.ADVANCED_RAGI_ALLOY) {
            +RagiumAdvancementKeys.RAGI_ALLOY
            display { +getItem(CommonParts.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY).toStack() }
            inventory("has_adv_ragi_alloy") { +ItemPredicate.Builder.item().of(CommonTagPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) }
        }.save(exporter)

        HTAdvancementBuilder.create(RagiumAdvancementKeys.THERMOMETER) {
            +RagiumAdvancementKeys.ALLOY_SMELTER
            display {
                +RagiumItems.THERMOMETER.toStack()
                type = AdvancementType.GOAL
            }
            inventory("has_thermometer") { +ItemPredicate.Builder.item().of(RagiumItems.THERMOMETER) }
        }.save(exporter)
        createSimple(RagiumAdvancementKeys.REFINERY, RagiumAdvancementKeys.THERMOMETER, RagiumBlocks.REFINERY)
        HTAdvancementBuilder.create(RagiumAdvancementKeys.PLASTIC) {
            +RagiumAdvancementKeys.REFINERY
            display { +getItem(CommonParts.PLATE, CommonMaterialKeys.PLASTIC).toStack() }
            inventory("has_plastic") { +ItemPredicate.Builder.item().of(HiiragiCoreTags.Items.PLASTICS) }
        }.save(exporter)
        HTAdvancementBuilder.create(RagiumAdvancementKeys.REFINED_SILICON) {
            +RagiumAdvancementKeys.REFINERY
            display { +getItem(CommonParts.DUST, CommonMaterialKeys.SILICON).toStack() }
            inventory("has_silicon") { +ItemPredicate.Builder.item().of(CommonTagPrefixes.DUST, CommonMaterialKeys.SILICON) }
        }.save(exporter)

        createSimple(RagiumAdvancementKeys.PYROLYZER, RagiumAdvancementKeys.THERMOMETER, RagiumBlocks.PYROLYZER)
        HTAdvancementBuilder.create(RagiumAdvancementKeys.CRIMSON_CRYSTAL) {
            +RagiumAdvancementKeys.PYROLYZER
            display { +getItem(CommonParts.GEM, HCMaterialKeys.CRIMSON_CRYSTAL).toStack() }
            inventory("has_crimson_crystal") { +ItemPredicate.Builder.item().of(CommonTagPrefixes.GEM, HCMaterialKeys.CRIMSON_CRYSTAL) }
        }.save(exporter)
        HTAdvancementBuilder.create(RagiumAdvancementKeys.WARPED_CRYSTAL) {
            +RagiumAdvancementKeys.PYROLYZER
            display { +getItem(CommonParts.GEM, HCMaterialKeys.WARPED_CRYSTAL).toStack() }
            inventory("has_warped_crystal") { +ItemPredicate.Builder.item().of(CommonTagPrefixes.GEM, HCMaterialKeys.WARPED_CRYSTAL) }
        }.save(exporter)
    }

    private fun elite() {
        HTAdvancementBuilder.create(RagiumAdvancementKeys.RAGI_CRYSTAL) {
            +RagiumAdvancementKeys.ADVANCED_RAGI_ALLOY
            display { +getItem(CommonParts.GEM, RagiumMaterialKeys.RAGI_CRYSTAL).toStack() }
            inventory("has_ragi_crystal") { +ItemPredicate.Builder.item().of(CommonTagPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL) }
        }.save(exporter)
        HTAdvancementBuilder.create(RagiumAdvancementKeys.STAINLESS_STEEL) {
            +RagiumAdvancementKeys.RAGI_CRYSTAL
            display { +getItem(CommonParts.INGOT, RagiumMaterialKeys.STAINLESS_STEEL).toStack() }
            inventory("has_stainless_steel") { +ItemPredicate.Builder.item().of(CommonTagPrefixes.INGOT, RagiumMaterialKeys.STAINLESS_STEEL) }
        }.save(exporter)

        HTAdvancementBuilder.create(RagiumAdvancementKeys.ELECTRIC_CIRCUIT) {
            +RagiumAdvancementKeys.REFINED_SILICON
            display {
                +RagiumItems.ELECTRIC_CIRCUIT.toStack()
                type = AdvancementType.GOAL
            }
            inventory("has_electric_circuit") { +ItemPredicate.Builder.item().of(RagiumItems.ELECTRIC_CIRCUIT) }
        }.save(exporter)
        createSimple(RagiumAdvancementKeys.BREWERY, RagiumAdvancementKeys.ELECTRIC_CIRCUIT, RagiumBlocks.BREWERY)
        createSimple(RagiumAdvancementKeys.MIXER, RagiumAdvancementKeys.ELECTRIC_CIRCUIT, RagiumBlocks.MIXER)
    }

    private fun ultimate() {}
}
