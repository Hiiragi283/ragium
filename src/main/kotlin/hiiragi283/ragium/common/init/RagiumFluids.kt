package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.fluid.HTVirtualFluid
import net.minecraft.fluid.Fluid
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import java.awt.Color

enum class RagiumFluids(
    val color: Color,
    val enName: String,
    val jaName: String,
    val type: TextureType = TextureType.LIQUID,
) : HTFluidContent {
    // Vanilla
    MILK(Color(0xffffff), "Milk", "牛乳"),
    HONEY("Honey", "蜂蜜", TextureType.HONEY),
    EXPERIENCE(Color(0x99cc00), "Liquid Experience", "液体経験値"),

    // Molten Materials
    // BASALT("Molten Basalt", "溶融バサルト", TextureType.BASALT),
    // GLASS("Molten Glass", "溶融ガラス", TextureType.GLASS),

    // Organics
    SEED_OIL(Color(0x99cc33), "Seed Oil", "種油"),
    TALLOW(Color(0xcc9933), "Tallow", "獣脂", TextureType.STICKY),

    BIOMASS(Color(0x006600), "Biomass", "バイオマス", TextureType.STICKY),
    GLYCEROL(Color(0x99cc66), "Glycerol", "グリセロール"),

    SAP(Color(0x996633), "Sap", "樹液", TextureType.STICKY),
    CRIMSON_SAP(Color(0x660000), "Crimson Sap", "深紅の樹液", TextureType.STICKY),
    WARPED_SAP(Color(0x006666), "Warped Sap", "歪んだ樹液", TextureType.STICKY),

    // Foods
    // BATTER(Color(0xffcc66), "Batter", "バッター液"),
    // CHOCOLATE(Color(0x663300), "Chocolate", "チョコレート", TextureType.STICKY),
    // SWEET_BERRIES(Color(0x990000), "Sweet Berries", "スイートベリー", TextureType.STICKY),

    // Natural Resources
    AIR(Color(0xccffff), "Air", "空気", TextureType.GASEOUS),
    SALT_WATER(Color(0x003399), "Salt Water", "塩水"),
    CRUDE_OIL(Color(0x000000), "Crude Oil", "原油", TextureType.STICKY),

    // Elements
    HYDROGEN(Color(0x0000cc), "Hydrogen", "水素", TextureType.GASEOUS),
    NITROGEN(Color(0x66cccc), "Nitrogen", "窒素", TextureType.GASEOUS),

    // OXYGEN(Color(0x99ccff), "Oxygen", "酸素", TextureType.GASEOUS),
    CHLORINE(Color(0xccff33), "Chlorine", "塩素", TextureType.GASEOUS),
    MERCURY(Color(0xcccccc), "Mercury", "水銀"),

    // Non-organic Chemical Compounds
    CARBON_MONOXIDE(Color(0x99ccff), "Carbon Monoxide", "一酸化炭素", TextureType.GASEOUS),
    CARBON_DIOXIDE(Color(0x99ccff), "Carbon Dioxide", "二酸化炭素", TextureType.GASEOUS),

    LIQUID_NITROGEN(Color(0x66cccc), "Liquid Nitrogen", "液体窒素"),
    AMMONIA(Color(0x9999ff), "Ammonia", "アンモニア", TextureType.GASEOUS),
    NITRIC_ACID(Color(0xcc99ff), "Nitric Acid", "硝酸"),
    AQUA_REGIA(Color(0xffff00), "Aqua Regia", "王水"),

    HYDROGEN_FLUORIDE(Color(0x33cccc), "Hydrogen Fluoride", "フッ化水素", TextureType.GASEOUS),

    ALKALI_SOLUTION(Color(0x000099), "Alkali Solution", "アルカリ溶液"),

    SULFUR_DIOXIDE(Color(0xff6600), "Sulfur Dioxide", "二酸化硫黄", TextureType.STICKY),

    // SULFUR_TRIOXIDE(Color(0xff6600), "Sulfur Trioxide", "三酸化硫黄", TextureType.STICKY),
    SULFURIC_ACID(Color(0xff3300), "Sulfuric Acid", "硫酸", TextureType.STICKY),
    MIXTURE_ACID(Color(0xff9900), "Mixture Acid", "混酸"),

    HYDROCHLORIC_ACID(Color(0xccff99), "Hydrochloric Acid", "塩酸"),
    HYDROGEN_CHLORIDE(Color(0xccff66), "Hydrogen Chloride", "塩化水素"),

    ALUMINA_SOLUTION(Color(0xcccccc), "Alumina Solution", "アルミナ溶液"),

    CHEMICAL_SLUDGE(Color(0x333366), "Chemical Sludge", "化学汚泥", TextureType.STICKY),
    CHLOROSILANE(Color(0xcccccc), "Chlorosilane", "塩化ケイ素", TextureType.GASEOUS),

    // Oil products
    REFINED_GAS(Color(0xcccccc), "Refined Gas", "精製ガス", TextureType.GASEOUS),
    NAPHTHA(Color(0xff9900), "Naphtha", "ナフサ"),
    RESIDUAL_OIL(Color(0x663300), "Residual Oil", "残渣油", TextureType.STICKY),

    ALCOHOL(Color(0x99ffff), "Alcohol", "アルコール"),
    AROMATIC_COMPOUNDS(Color(0x666699), "Aromatic Compounds", "芳香族化合物"),
    NOBLE_GAS(Color(0xff00ff), "Noble Gas", "希ガス", TextureType.GASEOUS),

    // Fuels
    BIO_FUEL(Color(0x99ff00), "Bio Fuel", "バイオ燃料"),
    FUEL(Color(0xcc6633), "Fuel", "燃料"),
    NITRO_FUEL(Color(0xff33333), "Nitro Fuel", "ニトロ燃料"),

    // Explosives
    NITRO_GLYCERIN(Color(0x99cc66), "Nitroglycerin", "ニトログリセリン", TextureType.EXPLOSIVE),
    TRINITROTOLUENE(Color(0x666699), "Trinitrotoluene", "トリニトロトルエン", TextureType.EXPLOSIVE),

    // Radioactive
    URANIUM_HEXAFLUORIDE(Color(0x33ff00), "Uranium Hexafluoride", "六フッ化ウラン", TextureType.RADIOACTIVE),
    ENRICHED_URANIUM_HEXAFLUORIDE(Color(0x33ff00), "Enriched Uranium Hexafluoride", "濃縮六フッ化ウラン"),
    ;

    constructor(enName: String, jaName: String, type: TextureType = TextureType.LIQUID) : this(
        Color.WHITE,
        enName,
        jaName,
        type,
    )

    companion object {
        @JvmStatic
        internal fun register() {
            RagiumFluids.entries.forEach { fluid: RagiumFluids ->
                Registry.register(Registries.FLUID, fluid.key, HTVirtualFluid())
            }
        }
    }

    override val key: RegistryKey<Fluid> = HTContent.fluidKey(name.lowercase())

    val translationKey: String = Util.createTranslationKey("fluid", id)

    //    TextureType    //

    enum class TextureType(
        val stillTex: Identifier = Identifier.of("block/bone_block_side"),
        val floatingTex: Identifier = stillTex,
        val overTex: Identifier? = null,
    ) {
        BASALT(Identifier.of("block/smooth_basalt")),
        EXPLOSIVE,
        GASEOUS(Identifier.of("block/white_concrete")),
        GLASS(Identifier.of("block/glass")),
        HONEY(Identifier.of("block/honey_block_top")),
        LIQUID,
        RADIOACTIVE,
        STICKY(Identifier.of("block/quartz_block_bottom")),
    }
}
