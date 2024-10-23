package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTAdvancementRegister
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.predicate.ComponentPredicate
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
        register = HTAdvancementRegister(RagiumAPI.MOD_ID, consumer)
        registerTier1(register)
        registerTier2(register)
        registerTier3(register)
        registerTier4(register)
    }

    private fun Advancement.Builder.buildMultiblock(type: HTMachineConvertible, minTier: HTMachineTier): Advancement.Builder = criterion(
        "build_multiblock",
        RagiumAPI.getInstance().createBuiltMachineCriterion(type, minTier),
    )

    private fun Advancement.Builder.hasItems(vararg items: ItemConvertible): Advancement.Builder = criterion(
        "has_items",
        InventoryChangedCriterion.Conditions.items(*items),
    )

    private fun Advancement.Builder.hasItems(tagKey: TagKey<Item>): Advancement.Builder = criterion(
        "has_items",
        InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(tagKey)),
    )

    private fun Advancement.Builder.hasMachine(type: HTMachineConvertible, tier: HTMachineTier): Advancement.Builder = criterion(
        "has_items",
        InventoryChangedCriterion.Conditions.items(
            ItemPredicate.Builder
                .create()
                .items(RagiumBlocks.META_MACHINE)
                .component(
                    ComponentPredicate
                        .builder()
                        .add(HTMachineType.COMPONENT_TYPE, type.asMachine())
                        .add(HTMachineTier.COMPONENT_TYPE, tier)
                        .build(),
                ),
        ),
    )

    //    Tier 1    //

    private fun registerTier1(register: HTAdvancementRegister) {
        val root: AdvancementEntry = register
            .createRoot(
                "tier1/root",
                RagiumContents.Ores.CRUDE_RAGINITE,
                Identifier.of("textures/block/bricks.png"),
            ) {
                hasItems(RagiumContents.RawMaterials.RAGINITE)
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
            }.putEnglish("Not a Red Alloy")
            .putEnglishDesc("Craft Ragi-Alloy Ingot")
            .putJapanese("赤合金ではない")
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

        /*val brickAlloyFurnace: AdvancementEntry = register
            .createChild(
                "tier1/brick_alloy_furnace",
                ragiAlloyHull,
                RagiumContents.BRICK_ALLOY_FURNACE,
            ) {
                hasItems(RagiumContents.BRICK_ALLOY_FURNACE)
            }.putEnglish("Pen Pineapple Apple Pen")
            .putEnglishDesc("Craft Alloy Furnace")
            .putJapanese("ペンパイナッポーアッポーペン")
            .putJapaneseDesc("合金かまどを作る")
            .build()*/

        val alloyFurnace: AdvancementEntry = register
            .createChild(
                "tier1/alloy_furnace",
                ragiAlloyHull,
                RagiumMachineTypes.Processor.ALLOY_FURNACE,
                HTMachineTier.PRIMITIVE,
            ) {
                hasMachine(RagiumMachineTypes.Processor.ALLOY_FURNACE, HTMachineTier.PRIMITIVE)
            }.putEnglish("Gotcha!")
            .putEnglishDesc("Craft Alloy Furnace")
            .putJapanese("ガッチャ！")
            .putJapaneseDesc("合金かまどを作る")
            .build()

        val brickBlastFurnace: AdvancementEntry = register
            .createChild(
                "tier1/brick_blast_furnace",
                ragiAlloyHull,
                RagiumMachineTypes.BLAST_FURNACE,
                HTMachineTier.PRIMITIVE,
            ) {
                buildMultiblock(RagiumMachineTypes.BLAST_FURNACE, HTMachineTier.PRIMITIVE)
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
                RagiumContents.Dusts.RAGINITE,
                AdvancementFrame.GOAL,
            ) {
                hasItems(RagiumContents.Dusts.RAGINITE)
            }.putEnglish("Rascal the Raccoon")
            .putEnglishDesc("Wash Raw Raginite Dust with Cauldron")
            .putJapanese("あらいぐまラスカル")
            .putJapaneseDesc("ラギナイトの原石の粉を大釜で洗う")
            .build()

        val grinder: AdvancementEntry = register
            .createChild(
                "tier1/grinder",
                manualGrinder,
                RagiumMachineTypes.Processor.GRINDER,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineTypes.Processor.GRINDER, HTMachineTier.BASIC)
            }.putEnglish("True Grinder")
            .putEnglishDesc("Craft Grinder")
            .putJapanese("本物の粉砕機")
            .putJapaneseDesc("粉砕機を作る")
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

        val assembler: AdvancementEntry = register
            .createChild(
                "tier2/assembler",
                ragiSteelHull,
                RagiumMachineTypes.Processor.ASSEMBLER,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineTypes.Processor.ASSEMBLER, HTMachineTier.BASIC)
            }.putEnglish("Avengers, Assemble!")
            .putEnglishDesc("Craft Assembler")
            .putJapanese("アベンジャーズ，アッセンブル！")
            .putJapaneseDesc("組立機を作る")
            .build()

        val compressor: AdvancementEntry = register
            .createChild(
                "tier2/compressor",
                ragiSteelHull,
                RagiumMachineTypes.Processor.COMPRESSOR,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineTypes.Processor.COMPRESSOR, HTMachineTier.BASIC)
            }.putEnglish("saves.zip.zip.zip")
            .putEnglishDesc("Craft Compressor")
            .putJapanese("saves.zip.zip.zip")
            .putJapaneseDesc("圧縮機を作る")
            .build()

        val extractor: AdvancementEntry = register
            .createChild(
                "tier2/extractor",
                ragiSteelHull,
                RagiumMachineTypes.Processor.EXTRACTOR,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineTypes.Processor.EXTRACTOR, HTMachineTier.BASIC)
            }.putEnglish("")
            .putEnglishDesc("Craft Extractor")
            .putJapanese("")
            .putJapaneseDesc("抽出器を作る")
            .build()

        val metalFormer: AdvancementEntry = register
            .createChild(
                "tier2/metal_former",
                ragiSteelHull,
                RagiumMachineTypes.Processor.METAL_FORMER,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineTypes.Processor.METAL_FORMER, HTMachineTier.BASIC)
            }.putEnglish("It's High Quality.")
            .putEnglishDesc("Craft Metal Former")
            .putJapanese("It's High Quality.")
            .putJapaneseDesc("金属加工機を作る")
            .build()

        val mixer: AdvancementEntry = register
            .createChild(
                "tier2/mixer",
                ragiSteelHull,
                RagiumMachineTypes.Processor.MIXER,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineTypes.Processor.MIXER, HTMachineTier.BASIC)
            }.putEnglish("Do not mix chlorine bleach and acidic liquids")
            .putEnglishDesc("Craft Mixer")
            .putJapanese("カビキラーとサンポールを混ぜてはいけない")
            .putJapaneseDesc("ミキサーを作る")
            .build()

        val soap: AdvancementEntry = register
            .createChild(
                "tier2/soap",
                mixer,
                RagiumContents.Misc.SOAP_INGOT,
            ) {
                hasItems(RagiumContents.Misc.SOAP_INGOT)
            }.putEnglish("BIG BROTHER IS WASHING YOU...")
            .putEnglishDesc("Craft Soap Ingot")
            .putJapanese("ビッグブラザーはあなたを洗っている...")
            .putJapaneseDesc("石鹸を作る")
            .build()

        val ragiCrystal: AdvancementEntry = register
            .createChild(
                "tier2/ragi_crystal",
                soap,
                RagiumContents.Dusts.RAGI_CRYSTAL,
                AdvancementFrame.GOAL,
            ) {
                hasItems(RagiumContents.Dusts.RAGI_CRYSTAL)
            }.putEnglish("Justis! Judgement! Seigi!")
            .putEnglishDesc("Craft Ragi-Crystal")
            .putJapanese("ジャスティス！ジャッジメント！セイギ！")
            .putJapaneseDesc("ラギクリスタリルを作る")
            .build()

        val blazingBlastFurnace: AdvancementEntry = register
            .createChild(
                "tier2/blazing_blast_furnace",
                ragiSteelHull,
                RagiumMachineTypes.BLAST_FURNACE,
                HTMachineTier.BASIC,
            ) {
                buildMultiblock(RagiumMachineTypes.BLAST_FURNACE, HTMachineTier.BASIC)
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

        val chemicalReactor: AdvancementEntry = register
            .createChild(
                "tier3/chemical_reactor",
                refinedRagiSteelHull,
                RagiumMachineTypes.Processor.CHEMICAL_REACTOR,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            }.putEnglish("Are you ready?")
            .putEnglishDesc("Craft Chemical Reactor")
            .putJapanese("Are you ready?")
            .putJapaneseDesc("化学反応槽を作る")
            .build()

        val electrolyzer: AdvancementEntry = register
            .createChild(
                "tier3/electrolyzer",
                refinedRagiSteelHull,
                RagiumMachineTypes.Processor.ELECTROLYZER,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineTypes.Processor.ELECTROLYZER, HTMachineTier.BASIC)
            }.putEnglish("")
            .putEnglishDesc("Craft Electrolyzer")
            .putJapanese("")
            .putJapaneseDesc("電解槽を作る")
            .build()

        val distillationTower: AdvancementEntry = register
            .createChild(
                "tier3/distillation_tower",
                refinedRagiSteelHull,
                RagiumContents.Misc.EMPTY_FLUID_CUBE,
            ) {
                buildMultiblock(RagiumMachineTypes.DISTILLATION_TOWER, HTMachineTier.PRIMITIVE)
            }.putEnglish("GregTech is waiting for you :)")
            .putEnglishDesc("Build Distillation Tower")
            .putJapanese("GregTechがあなたを待っている^ ^")
            .putJapaneseDesc("蒸留塔を建てる")
            .build()

        val oil: AdvancementEntry = register
            .createChild(
                "tier3/petroleum",
                distillationTower,
                Items.SOUL_SAND,
            ) {
                hasItems(Items.SOUL_SAND)
            }.putEnglish("")
            .putEnglishDesc("Get Crude Oil")
            .putJapanese("")
            .putJapaneseDesc("石油を手に入れる")
            .build()
    }

    //    Tier 4    //

    private fun registerTier4(register: HTAdvancementRegister) {
        val root: AdvancementEntry = register
            .createRoot(
                "tier4/root",
                RagiumBlocks.META_MACHINE,
                Identifier.of("textures/block/crying_obsidian.png"),
            ) {
                hasItems(RagiumBlocks.META_MACHINE)
            }.putEnglish("Ragium - Tier 4")
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapaneseDesc("チュートリアルは終わりだ。")
            .build()

        val ragium: AdvancementEntry = register
            .createChild(
                "tier4/ragium",
                root,
                RagiumContents.Element.RAGIUM.dustItem,
            ) {
                hasItems(RagiumContents.Element.RAGIUM.dustItem)
            }.putEnglish(RagiumContents.Element.RAGIUM.enName)
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapanese(RagiumContents.Element.RAGIUM.jaName)
            .putJapaneseDesc("ネザーまたは燃焼室の上で成長する")
            .build()

        val rigium: AdvancementEntry = register
            .createChild(
                "tier4/rigium",
                root,
                RagiumContents.Element.RIGIUM.dustItem,
            ) {
                hasItems(RagiumContents.Element.RAGIUM.dustItem)
            }.putEnglish(RagiumContents.Element.RIGIUM.enName)
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapanese(RagiumContents.Element.RIGIUM.jaName)
            .putJapaneseDesc("荒地またはy=192より高い場所で成長する")
            .build()

        val rugium: AdvancementEntry = register
            .createChild(
                "tier4/rugium",
                root,
                RagiumContents.Element.RUGIUM.dustItem,
            ) {
                hasItems(RagiumContents.Element.RAGIUM.dustItem)
            }.putEnglish(RagiumContents.Element.RUGIUM.enName)
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapanese(RagiumContents.Element.RUGIUM.jaName)
            .putJapaneseDesc("ジャングルまたは---で成長する")
            .build()

        val regium: AdvancementEntry = register
            .createChild(
                "tier4/regium",
                root,
                RagiumContents.Element.REGIUM.dustItem,
            ) {
                hasItems(RagiumContents.Element.RAGIUM.dustItem)
            }.putEnglish(RagiumContents.Element.REGIUM.enName)
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapanese(RagiumContents.Element.REGIUM.jaName)
            .putJapaneseDesc("海洋または---で成長する")
            .build()

        val rogium: AdvancementEntry = register
            .createChild(
                "tier4/rogium",
                root,
                RagiumContents.Element.ROGIUM.dustItem,
            ) {
                hasItems(RagiumContents.Element.RAGIUM.dustItem)
            }.putEnglish(RagiumContents.Element.ROGIUM.enName)
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapanese(RagiumContents.Element.ROGIUM.jaName)
            .putJapaneseDesc("エンドまたは---で成長する")
            .build()
    }
}
