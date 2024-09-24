package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.advancement.HTBuildMultiblockCriterion
import hiiragi283.ragium.common.alchemy.RagiElement
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
                RagiumContents.RAGINITE_ORE,
                Identifier.of("textures/block/bricks.png"),
            ) {
                hasItems(RagiumContents.RAW_RAGINITE)
            }.putEnglish("Ragium - Tier 1")
            .putEnglishDesc("Get Raw Raginite and Start!")
            .putJapaneseDesc("ラギナイトの原石を手に入れて始める")
            .build()

        val ragiAlloy: AdvancementEntry = register
            .createChild(
                "tier1/ragi_alloy",
                root,
                RagiumContents.Ingots.RAGI_ALLOY,
                AdvancementFrame.GOAL,
            ) {
                hasItems(RagiumContents.Ingots.RAGI_ALLOY)
            }.putEnglish("")
            .putEnglishDesc("Craft Ragi-Alloy Ingot")
            .putJapanese("")
            .putJapaneseDesc("ラギ合金インゴットを作る")
            .build()

        val ragiAlloyHull: AdvancementEntry = register
            .createChild(
                "tier1/ragi_alloy_hull",
                ragiAlloy,
                RagiumContents.Hulls.RAGI_ALLOY,
            ) {
                hasItems(RagiumContents.Hulls.RAGI_ALLOY)
            }.putEnglish("Not made of bronze")
            .putEnglishDesc("Craft Ragi-Alloy Hull")
            .putJapanese("ブロンズ製ではない")
            .putJapaneseDesc("ラギ合金筐体を作る")
            .build()

        val alloyFurnace: AdvancementEntry = register
            .createChild(
                "tier1/alloy_furnace",
                ragiAlloyHull,
                RagiumContents.BRICK_ALLOY_FURNACE,
            ) {
                hasItems(RagiumContents.BRICK_ALLOY_FURNACE)
            }.putEnglish("Pen Pineapple Apple Pen")
            .putEnglishDesc("Craft Alloy Furnace")
            .putJapanese("ペンパイナッポーアッポーペン")
            .putJapaneseDesc("合金かまどを作る")
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
                RagiumContents.MANUAL_GRINDER,
            ) {
                hasItems(RagiumContents.MANUAL_GRINDER)
            }.putEnglish("Traditional One")
            .putEnglishDesc("Craft Manual Grinder")
            .putJapanese("あのクルクル")
            .putJapaneseDesc("石臼を作る")
            .build()

        val raginiteDust: AdvancementEntry = register
            .createChild(
                "tier1/raginite_dust",
                manualGrinder,
                RagiumContents.Dusts.RAGINITE,
                AdvancementFrame.GOAL,
            ) {
                hasItems(RagiumContents.Dusts.RAGINITE)
            }.putEnglish("")
            .putEnglishDesc("Wash Raw Raginite Dust with Cauldron")
            .putJapanese("")
            .putJapaneseDesc("ラギナイトの原石の粉を大釜で洗う")
            .build()
    }

    //    Tier 2    //

    private fun registerTier2(register: HTAdvancementRegister) {
        val root: AdvancementEntry = register
            .createRoot(
                "tier2/root",
                RagiumContents.Ingots.RAGI_STEEL,
                Identifier.of("textures/block/polished_blackstone_bricks.png"),
            ) {
                hasItems(RagiumContents.Ingots.RAGI_STEEL)
            }.putEnglish("Ragium - Tier 2")
            .putEnglishDesc("The Industrial Revolution")
            .putJapaneseDesc("産業革命")
            .build()

        val ragiSteelHull: AdvancementEntry = register
            .createChild(
                "tier2/ragi_steel_hull",
                root,
                RagiumContents.Hulls.RAGI_STEEL,
            ) {
                hasItems(RagiumContents.Hulls.RAGI_STEEL)
            }.putEnglish("Not made of steel...")
            .putEnglishDesc("Craft Ragi-Steel Hull")
            .putJapanese("スチール製ではない...")
            .putJapaneseDesc("ラギスチール筐体を作る")
            .build()

        val compressor: AdvancementEntry = register
            .createChild(
                "tier2/compressor",
                ragiSteelHull,
                HTMachineType.Single.COMPRESSOR,
            ) {
                hasItems(HTMachineType.Single.COMPRESSOR)
            }.putEnglish("saves.zip.zip.zip")
            .putEnglishDesc("Craft Compressor")
            .putJapanese("saves.zip.zip.zip")
            .putJapaneseDesc("圧縮機を作る")
            .build()

        val extractor: AdvancementEntry = register
            .createChild(
                "tier2/extractor",
                ragiSteelHull,
                HTMachineType.Single.EXTRACTOR,
            ) {
                hasItems(HTMachineType.Single.EXTRACTOR)
            }.putEnglish("")
            .putEnglishDesc("Craft Extractor")
            .putJapanese("")
            .putJapaneseDesc("抽出器を作る")
            .build()

        val grinder: AdvancementEntry = register
            .createChild(
                "tier2/grinder",
                ragiSteelHull,
                HTMachineType.Single.GRINDER,
            ) {
                hasItems(HTMachineType.Single.EXTRACTOR)
            }.putEnglish("True Grinder")
            .putEnglishDesc("Craft Grinder")
            .putJapanese("本物の粉砕機")
            .putJapaneseDesc("粉砕機を作る")
            .build()

        val metalFormer: AdvancementEntry = register
            .createChild(
                "tier2/metal_former",
                ragiSteelHull,
                HTMachineType.Single.METAL_FORMER,
            ) {
                hasItems(HTMachineType.Single.EXTRACTOR)
            }.putEnglish("")
            .putEnglishDesc("Craft Metal Former")
            .putJapanese("")
            .putJapaneseDesc("金属加工機を作る")
            .build()

        val mixer: AdvancementEntry = register
            .createChild(
                "tier2/mixer",
                ragiSteelHull,
                HTMachineType.Single.MIXER,
            ) {
                hasItems(HTMachineType.Single.EXTRACTOR)
            }.putEnglish("")
            .putEnglishDesc("Craft Mixer")
            .putJapanese("")
            .putJapaneseDesc("ミキサーを作る")
            .build()

        val soap: AdvancementEntry = register
            .createChild(
                "tier2/soap",
                mixer,
                RagiumContents.SOAP_INGOT,
            ) {
                hasItems(RagiumContents.SOAP_INGOT)
            }.putEnglish("")
            .putEnglishDesc("Craft Soap Ingot")
            .putJapanese("")
            .putJapaneseDesc("石鹸を作る")
            .build()

        val refinedRaginiteDust: AdvancementEntry = register
            .createChild(
                "tier2/refined_raginite_dust",
                soap,
                RagiumContents.Dusts.RAGI_CRYSTAL,
                AdvancementFrame.GOAL,
            ) {
                hasItems(RagiumContents.Dusts.RAGI_CRYSTAL)
            }.putEnglish("")
            .putEnglishDesc("Craft Refined Raginite Dust")
            .putJapanese("")
            .putJapaneseDesc("精製ラギナイト粉末を作る")
            .build()

        val blazingBlastFurnace: AdvancementEntry = register
            .createChild(
                "tier2/blazing_blast_furnace",
                ragiSteelHull,
                HTMachineType.Multi.BLAZING_BLAST_FURNACE,
            ) {
                buildMultiblock(HTMachineType.Multi.BLAZING_BLAST_FURNACE)
            }.putEnglish("Blazing Poweeer!")
            .putEnglishDesc("Build Blazing Blast Furnace")
            .putJapanese("ブレイズパワーーー！")
            .putJapaneseDesc("ブレイズ高炉を建てる")
            .build()
    }

    //    Tier 3    //

    private fun registerTier3(register: HTAdvancementRegister) {
        val root: AdvancementEntry = register
            .createRoot(
                "tier3/root",
                RagiumContents.Ingots.REFINED_RAGI_STEEL,
                Identifier.of("textures/block/end_stone_bricks.png"),
            ) {
                hasItems(RagiumContents.Ingots.REFINED_RAGI_STEEL)
            }.putEnglish("Ragium - Tier 3")
            .putEnglishDesc("Age of Electricity")
            .putJapaneseDesc("電気時代")
            .build()

        val refinedRagiSteelHull: AdvancementEntry = register
            .createChild(
                "tier3/refined_steel_hull",
                root,
                RagiumContents.Hulls.REFINED_RAGI_STEEL,
            ) {
                hasItems(RagiumContents.Hulls.REFINED_RAGI_STEEL)
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

        val oil: AdvancementEntry = register
            .createChild(
                "tier3/oil",
                distillationTower,
                RagiumContents.Fluids.OIL,
            ) {
                hasItems(RagiumContents.Fluids.OIL)
            }.putEnglish("")
            .putEnglishDesc("Get Oil")
            .putJapanese("")
            .putJapaneseDesc("石油を手に入れる")
            .build()
    }

    //    Tier 4    //

    private fun registerTier4(register: HTAdvancementRegister) {
        val root: AdvancementEntry = register
            .createRoot(
                "tier4/root",
                RagiumContents.ALCHEMICAL_INFUSER,
                Identifier.of("textures/block/crying_obsidian.png"),
            ) {
                hasItems(RagiumContents.ALCHEMICAL_INFUSER)
            }.putEnglish("Ragium - Tier 4")
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapaneseDesc("チュートリアルは終わりだ。")
            .build()

        val ragium: AdvancementEntry = register
            .createChild(
                "tier4/ragium",
                root,
                RagiElement.RAGIUM.dustItem,
            ) {
                hasItems(RagiElement.RAGIUM.dustItem)
            }.putEnglish(RagiElement.RAGIUM.enName)
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapanese(RagiElement.RAGIUM.jaName)
            .putJapaneseDesc("ネザーまたは燃焼室の上で成長する")
            .build()

        val rigium: AdvancementEntry = register
            .createChild(
                "tier4/rigium",
                root,
                RagiElement.RIGIUM.dustItem,
            ) {
                hasItems(RagiElement.RAGIUM.dustItem)
            }.putEnglish(RagiElement.RIGIUM.enName)
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapanese(RagiElement.RIGIUM.jaName)
            .putJapaneseDesc("荒地またはy=192より高い場所で成長する")
            .build()

        val rugium: AdvancementEntry = register
            .createChild(
                "tier4/rugium",
                root,
                RagiElement.RUGIUM.dustItem,
            ) {
                hasItems(RagiElement.RAGIUM.dustItem)
            }.putEnglish(RagiElement.RUGIUM.enName)
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapanese(RagiElement.RUGIUM.jaName)
            .putJapaneseDesc("ジャングルまたは---で成長する")
            .build()

        val regium: AdvancementEntry = register
            .createChild(
                "tier4/regium",
                root,
                RagiElement.REGIUM.dustItem,
            ) {
                hasItems(RagiElement.RAGIUM.dustItem)
            }.putEnglish(RagiElement.REGIUM.enName)
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapanese(RagiElement.REGIUM.jaName)
            .putJapaneseDesc("海洋または---で成長する")
            .build()

        val rogium: AdvancementEntry = register
            .createChild(
                "tier4/rogium",
                root,
                RagiElement.ROGIUM.dustItem,
            ) {
                hasItems(RagiElement.RAGIUM.dustItem)
            }.putEnglish(RagiElement.ROGIUM.enName)
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapanese(RagiElement.ROGIUM.jaName)
            .putJapaneseDesc("エンドまたは---で成長する")
            .build()
    }
}
