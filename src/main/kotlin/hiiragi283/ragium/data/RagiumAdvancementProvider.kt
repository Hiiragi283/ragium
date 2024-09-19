package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.advancement.HTBuildMultiblockCriterion
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.data.util.HTAdvancementRegister
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class RagiumAdvancementProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricAdvancementProvider(output, registryLookup) {
    companion object {
        @JvmStatic
        lateinit var register: HTAdvancementRegister
    }

    override fun generateAdvancement(registryLookup: RegistryWrapper.WrapperLookup, consumer: Consumer<AdvancementEntry>) {
        register = HTAdvancementRegister(Ragium.MOD_ID, consumer)
        registerTier1(register)
        registerTier2(register)
        registerTier3(register)
        registerTier4(register)
    }

    private fun Advancement.Builder.hasItems(vararg items: ItemConvertible): Advancement.Builder = criterion(
        "has_items",
        InventoryChangedCriterion.Conditions.items(*items),
    )

    private fun Advancement.Builder.buildMultiblock(machineType: HTMachineType.Multi): Advancement.Builder = criterion(
        "build_multiblock",
        HTBuildMultiblockCriterion.create(machineType),
    )

    private fun Advancement.Builder.hasItems(tagKey: TagKey<Item>): Advancement.Builder = criterion(
        "has_items",
        InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(tagKey)),
    )

    //    Tier 1    //

    private fun registerTier1(register: HTAdvancementRegister) {
        val root: AdvancementEntry = register
            .createRoot(
                "tier1/root",
                RagiumBlocks.RAGINITE_ORE,
                Identifier.of("textures/block/bricks.png"),
            ) {
                hasItems(RagiumItems.RAW_RAGINITE)
            }.putEnglish("Ragium - Tier 1")
            .putEnglishDesc("Get Raw Raginite and Start!")
            .putJapaneseDesc("ラギナイトの原石を手に入れて始める")
            .build()

        val ragiAlloy: AdvancementEntry = register
            .createChild(
                "tier1/ragi_alloy",
                root,
                RagiumItems.RAGI_ALLOY_INGOT,
                AdvancementFrame.GOAL,
            ) {
                hasItems(RagiumItems.RAGI_ALLOY_INGOT)
            }.putEnglish("")
            .putEnglishDesc("Craft Ragi-Alloy Ingot")
            .putJapanese("")
            .putJapaneseDesc("ラギ合金インゴットを作る")
            .build()

        val ragiAlloyHull: AdvancementEntry = register
            .createChild(
                "tier1/ragi_alloy_hull",
                ragiAlloy,
                RagiumItems.RAGI_ALLOY_INGOT,
            ) {
                hasItems(RagiumBlocks.RAGI_ALLOY_HULL)
            }.putEnglish("Not made of bronze")
            .putEnglishDesc("Craft Ragi-Alloy Hull")
            .putJapanese("ブロンズ製ではない")
            .putJapaneseDesc("ラギ合金筐体を作る")
            .build()

        val alloyFurnace: AdvancementEntry = register
            .createChild(
                "tier1/alloy_furnace",
                ragiAlloyHull,
                HTMachineType.Single.ALLOY_FURNACE,
            ) {
                hasItems(HTMachineType.Single.ALLOY_FURNACE)
            }.putEnglish("Pen Pineapple Apple Pen")
            .putEnglishDesc("Craft Alloy Furnace")
            .putJapanese("ペンパイナッポーアッポーペン")
            .putJapaneseDesc("かまど合金を作る")
            .build()

        val brickBlastFurnace: AdvancementEntry = register
            .createChild(
                "tier1/brick_blast_furnace",
                ragiAlloyHull,
                HTMachineType.Multi.BRICK_BLAST_FURNACE,
            ) {
                buildMultiblock(HTMachineType.Multi.BRICK_BLAST_FURNACE)
            }.putEnglish("Do not rust me!")
            .putEnglishDesc("Build Brick Blast Furnace")
            .putJapanese("頭がさびて力が出ないよ～")
            .putJapaneseDesc("レンガ高炉を建てる")
            .build()

        val manualGrinder: AdvancementEntry = register
            .createChild(
                "tier1/manual_grinder",
                ragiAlloy,
                RagiumBlocks.MANUAL_GRINDER,
            ) {
                hasItems(RagiumBlocks.MANUAL_GRINDER)
            }.putEnglish("Traditional One")
            .putEnglishDesc("Craft Manual Grinder")
            .putJapanese("あのクルクル")
            .putJapaneseDesc("石臼を作る")
            .build()

        val raginiteDust: AdvancementEntry = register
            .createChild(
                "tier1/raginite_dust",
                manualGrinder,
                RagiumItems.RAGINITE_DUST,
            ) {
                hasItems(RagiumItems.RAGINITE_DUST)
            }.putEnglish("")
            .putEnglishDesc("Wash Raw Raginite Dust with Cauldron")
            .putJapanese("")
            .putJapaneseDesc("ラギナイトの原石の粉を大釜で洗う")
            .build()

        val ragiSteel: AdvancementEntry = register
            .createChild(
                "tier1/ragi_steel",
                raginiteDust,
                RagiumItems.RAGI_STEEL_INGOT,
            ) {
                hasItems(RagiumItems.RAGI_STEEL_INGOT)
            }.putEnglish("The Red Comet")
            .putEnglishDesc("Craft Ragi-Steel")
            .putJapanese("赤い彗星")
            .putJapaneseDesc("ラギスチールを作る")
            .build()
    }

    //    Tier 2    //

    private fun registerTier2(register: HTAdvancementRegister) {
    }

    //    Tier 3    //

    private fun registerTier3(register: HTAdvancementRegister) {
        val root: AdvancementEntry = register
            .createRoot(
                "tier3/root",
                RagiumItems.REFINED_RAGI_STEEL_INGOT,
                Identifier.of("textures/block/end_stone_bricks.png"),
            ) {
                hasItems(RagiumItems.REFINED_RAGI_STEEL_INGOT)
            }.putEnglish("Ragium - Tier 3")
            .putEnglishDesc("Age of Electricity")
            .putJapaneseDesc("電気時代")
            .build()

        val refinedRagiSteelHull: AdvancementEntry = register
            .createChild(
                "tier3/refined_steel_hull",
                root,
                RagiumBlocks.REFINED_RAGI_STEEL_HULL,
            ) {
                hasItems(RagiumBlocks.REFINED_RAGI_STEEL_HULL)
            }.putEnglish("Not made of steeeeel!!")
            .putEnglishDesc("Craft Refined Ragi-Steel Hull")
            .putJapanese("スチール製ではなあああい!!")
            .putJapaneseDesc("精製ラギスチール筐体を作る")
            .build()

        val centrifuge: AdvancementEntry = register
            .createChild(
                "tier3/centrifuge",
                refinedRagiSteelHull,
                HTMachineType.Single.CENTRIFUGE,
            ) {
                hasItems(HTMachineType.Single.CENTRIFUGE)
            }.putEnglish("")
            .putEnglishDesc("Craft Centrifuge")
            .putJapanese("")
            .putJapaneseDesc("遠心分離機を作る")
            .build()

        val chemicalReactor: AdvancementEntry = register
            .createChild(
                "tier3/chemical_reactor",
                refinedRagiSteelHull,
                HTMachineType.Single.CHEMICAL_REACTOR,
            ) {
                hasItems(HTMachineType.Single.CHEMICAL_REACTOR)
            }.putEnglish("")
            .putEnglishDesc("Craft Chemical Reactor")
            .putJapanese("")
            .putJapaneseDesc("化学反応槽を作る")
            .build()

        val electrolyzer: AdvancementEntry = register
            .createChild(
                "tier3/electrolyzer",
                refinedRagiSteelHull,
                HTMachineType.Single.ELECTROLYZER,
            ) {
                hasItems(HTMachineType.Single.ELECTROLYZER)
            }.putEnglish("")
            .putEnglishDesc("Craft Electrolyzer")
            .putJapanese("")
            .putJapaneseDesc("電解槽を作る")
            .build()

        val distillationTower: AdvancementEntry = register
            .createChild(
                "tier3/distillation_tower",
                refinedRagiSteelHull,
                HTMachineType.Multi.DISTILLATION_TOWER,
            ) {
                buildMultiblock(HTMachineType.Multi.DISTILLATION_TOWER)
            }.putEnglish("")
            .putEnglishDesc("Build Distillation Tower")
            .putJapanese("")
            .putJapaneseDesc("蒸留塔を建てる")
            .build()
    }

    //    Tier 4    //

    private fun registerTier4(register: HTAdvancementRegister) {
        val root: AdvancementEntry = register
            .createRoot(
                "tier4/root",
                RagiumItems.RAGIUM_DUST,
                Identifier.of("textures/block/crying_obsidian.png"),
            ) {
                hasItems(
                    RagiumItems.RAGIUM_DUST,
                    RagiumItems.RIGIUM_DUST,
                    RagiumItems.RUGIUM_DUST,
                    RagiumItems.REGIUM_DUST,
                    RagiumItems.ROGIUM_DUST,
                )
            }.putEnglish("Ragium - Tier 4")
            .putEnglishDesc("It's the end of tutorial.")
            .putJapaneseDesc("チュートリアルは終わりだ。")
            .build()
    }
}
