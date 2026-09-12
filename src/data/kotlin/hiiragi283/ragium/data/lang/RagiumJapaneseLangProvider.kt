package hiiragi283.ragium.data.lang

import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.advancment.RagiumAdvancementKeys
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.data.recipe.RagiumOreSlurryData
import hiiragi283.ragium.common.effect.RagiumMobEffects
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.item.alchemy.RagiumPotions
import net.minecraft.data.PackOutput

class RagiumJapaneseLangProvider(output: PackOutput) :
    HTLangProvider(output, RagiumAPI.MOD_ID, HTLangTypes.JA_JP),
    RagiumLangProvider {
    override fun addTranslations() {
        addPatternTranslations(this)

        // Advancement
        add(RagiumAdvancementKeys.ROOT, "ラギウム", "ラギウムへようこそ!")
        add(RagiumAdvancementKeys.SOOTY_IRON, "黒鉄（くろがね）を手に入れる", "煤鉄をつくる")

        add(RagiumAdvancementKeys.MECHANICAL_MACHINE_PARTS, "機械・加工・機械", "マシンケーシング（機械加工）を作る")
        add(RagiumAdvancementKeys.ASSEMBLER, "ラヴェ（ン）ジャーズ，アッセンブル！", "組立機を手に入れる")
        add(RagiumAdvancementKeys.CRUSHER, "Macerator, Pulverizer, or Crusher?", "粉砕機を手に入れる")

        add(RagiumAdvancementKeys.HEAT_MACHINE_PARTS, "Heat And Cool", "マシンケーシング（熱）を作る")
        add(RagiumAdvancementKeys.BLACK_STEEL, "黒い咆哮", "黒鋼をつくる")
        add(RagiumAdvancementKeys.FREEZER, "私の消費電力は53万です", "冷凍機を手に入れる")
        add(RagiumAdvancementKeys.MELTER, "融けてしまいそう", "溶融炉を手に入れる")
        // Block
        add(RagiumBlocks.ASSEMBLER, "組立機")
        add(RagiumBlocks.CRUSHER, "破砕機")
        add(RagiumBlocks.COMPRESSOR, "圧縮機")
        add(RagiumBlocks.CUTTING_MACHINE, "裁断機")

        add(RagiumBlocks.FREEZER, "冷凍機")
        add(RagiumBlocks.MELTER, "溶融炉")
        add(RagiumBlocks.PYROLYZER, "熱分解炉")
        add(RagiumBlocks.REFINERY, "精製機")

        add(RagiumBlocks.CHEMICAL_BATH, "化学浴槽")
        add(RagiumBlocks.CHEMICAL_REACTOR, "化学反応槽")
        add(RagiumBlocks.ELECTROLYZER, "電解機")
        add(RagiumBlocks.MIXER, "混合機")

        add(RagiumBlocks.BREWERY, "醸造機")
        add(RagiumBlocks.PLANTER, "栽培機")

        add(RagiumBlocks.SCANNER, "レーザースキャナ")

        add(RagiumBlocks.MACHINE_CASING, "機械ケーシング")
        // Fluid
        addFluid(RagiumFluids.HONEY, "ハチミツ")
        add(RagiumFluids.POTION.getFluidType().descriptionId, "無効なポーション")
        add(RagiumFluids.POTION.bucketHolder, $$"%1$s入りバケツ")
        addFluid(RagiumFluids.OMINOUS_FLUX, "不吉な流動体")
        addFluid(RagiumFluids.MOLTEN_GLASS, "溶融ガラス")
        addFluid(RagiumFluids.MOLTEN_REDSTONE, "励起レッドストーン")
        addFluid(RagiumFluids.MOLTEN_GLOWSTONE, "活性グロウストーン")
        addFluid(RagiumFluids.MOLTEN_ENDER, "共振エンダー")

        addFluid(RagiumFluids.HYDROGEN, "水素")
        addFluid(RagiumFluids.OXYGEN, "酸素")
        addFluid(RagiumFluids.CHLORINE, "塩素")

        addFluid(RagiumFluids.WOOD_TAR, "木タール")
        addFluid(RagiumFluids.COAL_TAR, "石炭タール")
        addFluid(RagiumFluids.AROMATIC_COMPOUND, "芳香族化合物")
        addFluid(RagiumFluids.CRUDE_OIL, "原油")
        addFluid(RagiumFluids.NAPHTHA, "ナフサ")
        addFluid(RagiumFluids.FUEL, "燃料油")
        addFluid(RagiumFluids.ANTI_RUST_OIL, "防錆油")
        addFluid(RagiumFluids.NITRIC_ACID, "硝酸")
        addFluid(RagiumFluids.LIQUID_EXPLOSIVE, "液体爆薬")
        addFluid(RagiumFluids.NAOH_SOLUTION, "水酸化ナトリウム水溶液")
        addFluid(RagiumFluids.SULFUR_DIOXIDE, "二酸化硫黄")
        addFluid(RagiumFluids.SULFUR_TRIOXIDE, "三酸化硫黄")
        addFluid(RagiumFluids.SULFURIC_ACID, "硫酸")
        addFluid(RagiumFluids.HYDROGEN_CHLORIDE, "塩化水素")
        addFluid(RagiumFluids.HYDROCHLORIC_ACID, "塩酸")

        add(RagiumFluids.ORE_SLURRY.getFluidType().descriptionId, "無効な鉱石泥")
        add(RagiumFluids.ORE_SLURRY.bucketHolder, $$"%1$s入りバケツ")
        // Item
        add(RagiumItems.BAMBOO_CHARCOAL, "竹炭")
        add(RagiumItems.TAR, "タール")
        add(RagiumItems.PITCH_COKE, "ピッチコークス")
        add(RagiumItems.PARTICLE_BOARD, "パーティクルボード")
        add(RagiumItems.PLASTIC_PLATE, "プラスチック板")
        add(RagiumItems.SYNTHETIC_FEATHER, "合成羽")
        add(RagiumItems.SYNTHETIC_FIBER, "合成繊維")
        add(RagiumItems.SYNTHETIC_LEATHER, "合成牛皮")
        add(RagiumItems.BEESWAX, "密猟")
        add(RagiumItems.ELDER_HEART, "エルダーの心臓")
        add(RagiumItems.WITHER_DOLL, "ウィザー人形")
        add(RagiumItems.WITHER_STAR, "ウィザースター")

        add(RagiumItems.MEMORY_DISC, "メモリーディスク")

        // Mob Effect
        add(RagiumMobEffects.FROSTBITE, "凍傷")

        // Ore Slurry
        addOreSlurry(RagiumOreSlurryData.COPPER, "銅の鉱石泥")
        addOreSlurry(RagiumOreSlurryData.IRON, "鉄の鉱石泥")
        addOreSlurry(RagiumOreSlurryData.GOLD, "金の鉱石泥")

        // Potion
        addPotion(RagiumPotions.FROSTBITE, "凍傷")

        addCustomPotion("hunger", "空腹")
        addCustomPotion("darkness", "暗闇")
        addCustomPotion("golden_apple", "金リンゴ")
        addCustomPotion("enchanted_golden_apple", "エンチャントされた金リンゴ")

        // Recipe Type
        add(RagiumRecipeTypes.ASSEMBLING, "組立")
        add(RagiumRecipeTypes.COMPRESSING, "圧縮")
        add(RagiumRecipeTypes.CRUSHING, "粉砕")
        add(RagiumRecipeTypes.CUTTING, "切断")
        add(RagiumRecipeTypes.DRAINING, "排出")
        add(RagiumRecipeTypes.FILLING, "封入")

        add(RagiumRecipeTypes.FREEZING, "冷凍")
        add(RagiumRecipeTypes.MELTING, "溶融")
        add(RagiumRecipeTypes.PYROLYZING, "熱分解")
        add(RagiumRecipeTypes.REFINING, "蒸留")

        add(RagiumRecipeTypes.BATHING, "化学洗浄")
        add(RagiumRecipeTypes.ELECTROLYZING, "電気分解")
        add(RagiumRecipeTypes.MIXING, "混合")
        add(RagiumRecipeTypes.REACTING, "化学反応")

        add(RagiumRecipeTypes.BREWING, "醸造")
        add(RagiumRecipeTypes.PLANTING, "栽培")

        // Text - Lib
        add(HTCommonTranslation.ERROR, "エラー")
        add(HTCommonTranslation.INFINITE, "無限")
        add(HTCommonTranslation.NONE, "なし")
        add(HTCommonTranslation.EMPTY, "空")

        add(HTCommonTranslation.DOWN, "下")
        add(HTCommonTranslation.UP, "上")
        add(HTCommonTranslation.NORTH, "北")
        add(HTCommonTranslation.SOUTH, "南")
        add(HTCommonTranslation.WEST, "西")
        add(HTCommonTranslation.EAST, "東")

        add(HTCommonTranslation.INVALID_PACKET_S2C, $$"サーバー側からの不正なパケットを受信しました: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_C2S, $$"クライアント側からの不正なパケットを受信しました: %1$s")

        add(HTCommonTranslation.PROGRESS, $$"進捗率: %1$s %%")
        add(HTCommonTranslation.SECONDS, $$"%1$s 秒 (%2$s ticks)")

        add(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"常に少なくとも%1$sがあります")
        add(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION, "シフトキーを押して説明を表示")
        add(HTCommonTranslation.TOOLTIP_SHOW_DETAILS, "シフトキーを押して詳細を表示")

        add(HTCommonTranslation.DATAPACK_WIP, "開発中の要素を有効にします")
        // Text - Ragium
        add(RagiumTranslation.RAGIUM, "ラギウム")

        add(RagiumTranslation.CONFIG_ENERGY_CAPACITY, "エネルギー容量")
        add(RagiumTranslation.CONFIG_ENERGY_RATE, "エネルギー使用速度")

        add(RagiumTranslation.TOOLTIPS_MEMORY_DISC_DATA, $$"スキャン済み: %1$s")
    }
}
