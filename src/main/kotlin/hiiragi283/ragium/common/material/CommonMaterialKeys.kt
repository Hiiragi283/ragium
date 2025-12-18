package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike

object CommonMaterialKeys {
    enum class Metals(private val enName: String, private val jpName: String) : HTMaterialLike.Translatable {
        // 3rd
        ALUMINUM("Aluminum", "アルミニウム"),

        // 4th
        TITANIUM("Titanium", "チタン"),
        CHROME("Chromium", "クロム"),
        CHROMIUM("Chromium", "クロム"),
        MANGANESE("Manganese", "マンガン"),
        COBALT("Cobalt", "コバルト"),
        NICKEL("Nickel", "ニッケル"),
        ZINC("Zinc", "亜鉛"),

        // 5th
        PALLADIUM("Palladium", "パラジウム"),
        SILVER("silver", "銀"),
        TIN("Tin", "錫"),
        ANTIMONY("Antimony", "アンチモン"),

        // 6th
        TUNGSTEN("Tungsten", "タングステン"),
        OSMIUM("Osmium", "オスミウム"),
        IRIDIUM("Iridium", "イリジウム"),
        PLATINUM("Platinum", "プラチナ"),
        LEAD("Lead", "鉛"),

        // 7th
        URANIUM("Uranium", "ウラン"),
        ;

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> enName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(name.lowercase())
    }

    enum class Alloys(private val enName: String, private val jpName: String) : HTMaterialLike.Translatable {
        STEEL("Steel", "鋼鉄"),
        INVAR("Invar", "インバー"),
        ELECTRUM("Electrum", "エレクトラム"),
        BRONZE("Bronze", "青銅"),
        BRASS("Brass", "真鍮"),
        ENDERIUM("Enderium", "エンダリウム"),
        LUMIUM("Lumium", "ルミウム"),
        SIGNALUM("Signalum", "シグナルム"),
        CONSTANTAN("Constantan", "コンスタンタン"),
        ;

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> enName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(name.lowercase())
    }

    enum class Gems(private val enName: String, private val jpName: String) : HTMaterialLike.Translatable {
        CINNABAR("Cinnabar", "辰砂"),
        FLUORITE("Fluorite", "蛍石"),
        PERIDOT("Peridot", "ペリドット"),
        RUBY("Ruby", "ルビー"),
        SALT("Salt", "塩"),
        SALTPETER("Saltpeter", "硝石"),
        SAPPHIRE("Sapphire", "サファイア"),
        SULFUR("Sulfur", "硫黄"),
        ;

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> enName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(name.lowercase())
    }

    // Fuels
    @JvmStatic
    val BIO: HTMaterialKey = HTMaterialKey.of("bio")

    @JvmStatic
    val COAL_COKE: HTMaterialKey = HTMaterialKey.of("coal_coke")

    // Plates
    @JvmStatic
    val PLASTIC: HTMaterialKey = HTMaterialKey.of("plastic")

    @JvmStatic
    val RUBBER: HTMaterialKey = HTMaterialKey.of("rubber")
}
