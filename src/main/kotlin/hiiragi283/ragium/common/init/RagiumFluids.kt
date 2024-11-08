package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTRegistryContent
import hiiragi283.ragium.api.content.HTTranslationProvider
import net.minecraft.fluid.Fluid
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Util
import java.awt.Color

enum class RagiumFluids(val color: Color, override val enName: String, override val jaName: String) :
    HTRegistryContent<Fluid>,
    HTTranslationProvider {
    // Vanilla
    MILK(Color(0xffffff), "Milk", "牛乳"),
    HONEY(Color(0xffcc33), "Honey", "蜂蜜"),

    // Molten Materials

    // Organics
    BIOMASS(Color(0x006600), "Biomass", "バイオマス"),
    GLYCEROL(Color(0x99cc66), "Glycerol", "グリセロール"),
    TALLOW(Color(0xcc9933), "Tallow", "獣脂"),
    SEED_OIL(Color(0x99cc33), "Seed Oil", "種油"),

    // Foods
    BATTER(Color(0xffcc66), "Batter", "バッター液"),
    CHOCOLATE(Color(0x663300), "Chocolate", "チョコレート"),
    STARCH_SYRUP(Color(0x99ffff), "Starch Syrup", "水あめ"),
    SWEET_BERRIES(Color(0x990000), "Sweet Berries", "スイートベリー"),

    // Natural Resources
    AIR(Color(0xccffff), "Air", "空気"),
    SALT_WATER(Color(0x003399), "Salt Water", "塩水"),
    CRUDE_OIL(Color(0x000000), "Crude Oil", "原油"),

    // Elements
    HYDROGEN(Color(0x0000cc), "Hydrogen", "水素"),
    NITROGEN(Color(0x66cccc), "Nitrogen", "窒素"),
    OXYGEN(Color(0x99ccff), "Oxygen", "酸素"),
    CHLORINE(Color(0xccff33), "Chlorine", "塩素"),

    // Non-organic Chemical Compounds
    CARBON_MONOXIDE(Color(0x99ccff), "Carbon Monoxide", "一酸化炭素"),
    CARBON_DIOXIDE(Color(0x99ccff), "Carbon Dioxide", "二酸化炭素"),

    NITRIC_ACID(Color(0xcc99ff), "Nitric Acid", "硝酸"),
    AQUA_REGIA(Color(0xffff00), "Aqua Regia", "王水"),

    HYDROGEN_FLUORIDE(Color(0x33cccc), "Hydrogen Fluoride", "フッ化水素"),

    ALKALI_SOLUTION(Color(0x000099), "Alkali Solution", "アルカリ溶液"),

    SULFURIC_ACID(Color(0xff3300), "Sulfuric Acid", "硫酸"),
    MIXTURE_ACID(Color(0xff3300), "Mixture Acid", "混酸"),

    HYDROCHLORIC_ACID(Color(0xccff99), "Hydrochloric Acid", "塩酸"),
    HYDROGEN_CHLORIDE(Color(0xccff66), "Hydrogen Chloride", "塩化水素"),

    ALUMINA_SOLUTION(Color(0xcccccc), "Alumina Solution", "アルミナ溶液"),

    // Oil products
    REFINED_GAS(Color(0xcccccc), "Refined Gas", "精製ガス"),
    NAPHTHA(Color(0xff9900), "Naphtha", "ナフサ"),
    RESIDUAL_OIL(Color(0x000033), "Residual Oil", "残渣油"),

    ALCOHOL(Color(0x99ffff), "Alcohol", "アルコール"),
    AROMATIC_COMPOUNDS(Color(0x666699), "Aromatic Compounds", "芳香族化合物"),
    ASPHALT(Color(0x000066), "Asphalt", "アスファルト"),
    NOBLE_GAS(Color(0xff00ff), "Noble Gas", "希ガス"),

    // Fuels
    BIO_FUEL(Color(0x99ff00), "Bio Fuel", "バイオ燃料"),
    FUEL(Color(0xcc6633), "Fuel", "燃料"),
    NITRO_FUEL(Color(0xff33333), "Nitro Fuel", "ニトロ燃料"),

    // Explosives
    NITRO_GLYCERIN(Color(0x99cc66), "Nitroglycerin", "ニトログリセリン"),
    TRINITROTOLUENE(Color(0x666699), "Trinitrotoluene", "トリニトロトルエン"),
    ;

    override val registry: Registry<Fluid> = Registries.FLUID
    override val key: RegistryKey<Fluid> = RegistryKey.of(RegistryKeys.FLUID, RagiumAPI.id(name.lowercase()))

    val translationKey: String = Util.createTranslationKey("fluid", id)
}
