package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTAdvancementRegister
import hiiragi283.ragium.api.machine.HTMachine
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
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

    private fun Advancement.Builder.buildMultiblock(type: HTMachine, minTier: HTMachineTier): Advancement.Builder = criterion(
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

    private fun Advancement.Builder.hasMachine(type: HTMachine, tier: HTMachineTier): Advancement.Builder = criterion(
        "has_items",
        InventoryChangedCriterion.Conditions.items(type.createItemStack(tier).item),
    )

    //    Tier 1    //

    private fun registerTier1(register: HTAdvancementRegister) {
        val root: AdvancementEntry = register
            .createRoot(
                "tier1/root",
                RagiumContents.Ores.CRUDE_RAGINITE,
                Identifier.of("textures/block/bricks.png"),
            ) {
                hasItems(RagiumContents.RawMaterials.CRUDE_RAGINITE)
            }.putEnglish("Ragium - Tier 1")
            .putEnglishDesc("Get Raw Crude Raginite and Start!")
            .putJapaneseDesc("粗製ラギナイトの原石を手に入れて始める")
            .build()

        val ragiAlloy: AdvancementEntry = register
            .createChild(
                "tier1/ragi_alloy",
                root,
                RagiumContents.Ingots.RAGI_ALLOY,
            ) {
                hasItems(RagiumContents.Ingots.RAGI_ALLOY)
            }.putEnglish("Not a Red Alloy")
            .putEnglishDesc("Craft Ragi-Alloy Ingot")
            .putJapanese("赤合金ではない")
            .putJapaneseDesc("ラギ合金インゴットを作る")
            .build()

        val manualGrinder: AdvancementEntry = register
            .createChild(
                "tier1/manual_grinder",
                ragiAlloy,
                RagiumBlocks.MANUAL_GRINDER,
            ) {
                hasItems(RagiumBlocks.MANUAL_GRINDER)
            }.putEnglish("Traditional One")
            .putEnglishDesc("Craft Ragi-Grinder")
            .putJapanese("あのクルクル")
            .putJapaneseDesc("らぎ臼を作る")
            .build()

        val manualForge: AdvancementEntry = register
            .createChild(
                "tier1/manual_forge",
                manualGrinder,
                RagiumBlocks.MANUAL_FORGE,
            ) {
                hasItems(RagiumBlocks.MANUAL_FORGE)
            }.putEnglish("NeoForge")
            .putEnglishDesc("Craft Ragi-Anvil")
            .putJapanese("")
            .putJapaneseDesc("らぎ金床を作る")
            .build()

        val ragiAlloyPlate: AdvancementEntry = register
            .createChild(
                "tier1/ragi_alloy_plate",
                manualForge,
                RagiumItems.FORGE_HAMMER,
            ) {
                hasItems(RagiumItems.FORGE_HAMMER)
            }.putEnglish("")
            .putEnglishDesc("Obtain Ragi-Alloy Plate")
            .putJapanese("")
            .putJapaneseDesc("ラギ合金板を手に入れる")
            .build()

        val ragiAlloyHull: AdvancementEntry = register
            .createChild(
                "tier1/ragi_alloy_hull",
                ragiAlloyPlate,
                RagiumContents.Hulls.PRIMITIVE,
            ) {
                hasItems(RagiumContents.Hulls.PRIMITIVE)
            }.putEnglish("Not made of bronze")
            .putEnglishDesc("Craft Ragi-Alloy Hull")
            .putJapanese("ブロンズ製ではない")
            .putJapaneseDesc("ラギ合金筐体を作る")
            .build()

        val alloyFurnace: AdvancementEntry = register
            .createChild(
                "tier1/alloy_furnace",
                ragiAlloyHull,
                RagiumMachineKeys.ALLOY_FURNACE,
                HTMachineTier.PRIMITIVE,
            ) {
                hasMachine(RagiumMachineKeys.ALLOY_FURNACE, HTMachineTier.PRIMITIVE)
            }.putEnglish("Gotcha!")
            .putEnglishDesc("Craft Alloy Furnace")
            .putJapanese("ガッチャ！")
            .putJapaneseDesc("合金かまどを作る")
            .build()

        val heatGenerator: AdvancementEntry = register
            .createChild(
                "tier1/heat_generator",
                ragiAlloyHull,
                RagiumMachineKeys.STEAM_GENERATOR,
                HTMachineTier.PRIMITIVE,
            ) {
                hasMachine(RagiumMachineKeys.STEAM_GENERATOR, HTMachineTier.PRIMITIVE)
            }.putEnglish("Wireless Energy Transmission")
            .putEnglishDesc("Craft Heat Generator")
            .putJapanese("無線送電")
            .putJapaneseDesc("火力発電機を作る")
            .build()

        val manualMixer: AdvancementEntry = register
            .createChild(
                "tier1/manual_mixer",
                ragiAlloyPlate,
                RagiumBlocks.MANUAL_MIXER,
            ) {
                hasItems(RagiumBlocks.MANUAL_MIXER)
            }.putEnglish("")
            .putEnglishDesc("Craft Ragi-Basin")
            .putJapanese("")
            .putJapaneseDesc("らぎ釜を作る")
            .build()

        val raginiteDust: AdvancementEntry = register
            .createChild(
                "tier1/raginite_dust",
                manualMixer,
                RagiumContents.Dusts.RAGINITE,
            ) {
                hasItems(RagiumContents.Dusts.RAGINITE)
            }.putEnglish("Rascal the Raccoon")
            .putEnglishDesc("Wash Raw Raginite Dust with Ragi-Basin")
            .putJapanese("あらいぐまラスカル")
            .putJapaneseDesc("ラギナイトの原石の粉をらぎ釜で洗う")
            .build()

        val brickBlastFurnace: AdvancementEntry = register
            .createChild(
                "tier1/brick_blast_furnace",
                raginiteDust,
                RagiumMachineKeys.BLAST_FURNACE,
                HTMachineTier.PRIMITIVE,
            ) {
                buildMultiblock(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.PRIMITIVE)
            }.putEnglish("Do not rust me!")
            .putEnglishDesc("Build Brick Blast Furnace")
            .putJapanese("頭がさびて力が出ないよ～")
            .putJapaneseDesc("レンガ高炉を建てる")
            .build()

        val ragiSteelIngot: AdvancementEntry = register
            .createChild(
                "tier1/ragi_steel_ingot",
                brickBlastFurnace,
                RagiumContents.Ingots.RAGI_STEEL,
                AdvancementFrame.GOAL,
            ) {
                hasItems(RagiumContents.Ingots.RAGI_STEEL)
            }.putEnglish("The Red Comet")
            .putEnglishDesc("Obtain Ragi-Steel Ingot")
            .putJapanese("赤い彗星")
            .putJapaneseDesc("ラギスチールインゴットを手に入れる")
            .build()
    }

    //    Tier 2    //

    private fun registerTier2(register: HTAdvancementRegister) {
        val root: AdvancementEntry = register
            .createRoot(
                "tier2/root",
                RagiumContents.Ingots.RAGI_STEEL,
                Identifier.of("textures/block/blast_furnace_top.png"),
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
                RagiumContents.Hulls.BASIC,
            ) {
                hasItems(RagiumContents.Hulls.BASIC)
            }.putEnglish("Not made of steel...")
            .putEnglishDesc("Craft Ragi-Steel Hull")
            .putJapanese("スチール製ではない...")
            .putJapaneseDesc("ラギスチール筐体を作る")
            .build()

        val assembler: AdvancementEntry = register
            .createChild(
                "tier2/assembler",
                ragiSteelHull,
                RagiumMachineKeys.ASSEMBLER,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineKeys.ASSEMBLER, HTMachineTier.BASIC)
            }.putEnglish("Avengers, Assemble!")
            .putEnglishDesc("Craft Assembler")
            .putJapanese("アベンジャーズ，アッセンブル！")
            .putJapaneseDesc("組立機を作る")
            .build()

        val extractor: AdvancementEntry = register
            .createChild(
                "tier2/extractor",
                ragiSteelHull,
                RagiumMachineKeys.EXTRACTOR,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineKeys.EXTRACTOR, HTMachineTier.BASIC)
            }.putEnglish("")
            .putEnglishDesc("Craft Extractor")
            .putJapanese("")
            .putJapaneseDesc("抽出器を作る")
            .build()

        val metalFormer: AdvancementEntry = register
            .createChild(
                "tier2/metal_former",
                ragiSteelHull,
                RagiumMachineKeys.METAL_FORMER,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineKeys.METAL_FORMER, HTMachineTier.BASIC)
            }.putEnglish("It's High Quality.")
            .putEnglishDesc("Craft Metal Former")
            .putJapanese("It's High Quality.")
            .putJapaneseDesc("金属加工機を作る")
            .build()

        val mixer: AdvancementEntry = register
            .createChild(
                "tier2/mixer",
                ragiSteelHull,
                RagiumMachineKeys.MIXER,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineKeys.MIXER, HTMachineTier.BASIC)
            }.putEnglish("Do not mix chlorine bleach and acidic liquids")
            .putEnglishDesc("Craft Mixer")
            .putJapanese("カビキラーとサンポールを混ぜてはいけない")
            .putJapaneseDesc("ミキサーを作る")
            .build()

        val soap: AdvancementEntry = register
            .createChild(
                "tier2/soap",
                mixer,
                RagiumItems.SOAP_INGOT,
            ) {
                hasItems(RagiumItems.SOAP_INGOT)
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
                RagiumMachineKeys.BLAST_FURNACE,
                HTMachineTier.BASIC,
            ) {
                buildMultiblock(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.BASIC)
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
                RagiumAPI.id("textures/block/advanced_casing.png"),
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
                RagiumContents.Hulls.ADVANCED,
            ) {
                hasItems(RagiumContents.Hulls.ADVANCED)
            }.putEnglish("Not made of steeeeel!!")
            .putEnglishDesc("Craft Refined Ragi-Steel Hull")
            .putJapanese("スチール製ではなあああい!!")
            .putJapaneseDesc("精製ラギスチール筐体を作る")
            .build()

        val chemicalReactor: AdvancementEntry = register
            .createChild(
                "tier3/chemical_reactor",
                refinedRagiSteelHull,
                RagiumMachineKeys.CHEMICAL_REACTOR,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.BASIC)
            }.putEnglish("Are you ready?")
            .putEnglishDesc("Craft Chemical Reactor")
            .putJapanese("Are you ready?")
            .putJapaneseDesc("化学反応槽を作る")
            .build()

        val electrolyzer: AdvancementEntry = register
            .createChild(
                "tier3/electrolyzer",
                refinedRagiSteelHull,
                RagiumMachineKeys.ELECTROLYZER,
                HTMachineTier.BASIC,
            ) {
                hasMachine(RagiumMachineKeys.ELECTROLYZER, HTMachineTier.BASIC)
            }.putEnglish("")
            .putEnglishDesc("Craft Electrolyzer")
            .putJapanese("")
            .putJapaneseDesc("電解槽を作る")
            .build()

        val distillationTower: AdvancementEntry = register
            .createChild(
                "tier3/distillation_tower",
                refinedRagiSteelHull,
                RagiumItems.EMPTY_FLUID_CUBE,
            ) {
                buildMultiblock(RagiumMachineKeys.DISTILLATION_TOWER, HTMachineTier.PRIMITIVE)
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
                RagiumContents.Gems.RAGIUM,
                Identifier.of("textures/block/crying_obsidian.png"),
            ) {
                hasItems(RagiumContents.Gems.RAGIUM)
            }.putEnglish("Ragium - Tier 4")
            .putEnglishDesc("IT'S THE END OF TUTORIAL")
            .putJapaneseDesc("チュートリアルは終わりだ。")
            .build()
    }
}
