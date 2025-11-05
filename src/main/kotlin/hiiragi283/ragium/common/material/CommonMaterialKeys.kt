package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike

object CommonMaterialKeys {
    enum class Metals : HTMaterialLike {
        // 3rd
        ALUMINUM,

        // 4th
        TITANIUM,
        CHROME,
        CHROMIUM,
        MANGANESE,
        COBALT,
        NICKEL,
        ZINC,

        // 5th
        PALLADIUM,
        SILVER,
        TIN,
        ANTIMONY,

        // 6th
        TUNGSTEN,
        OSMIUM,
        IRIDIUM,
        PLATINUM,
        LEAD,

        // 7th
        URANIUM,
        ;

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(name.lowercase())
    }

    enum class Alloys : HTMaterialLike {
        STEEL,
        INVAR,
        ELECTRUM,
        BRONZE,
        BRASS,
        ENDERIUM,
        LUMIUM,
        SIGNALUM,
        CONSTANTAN,
        ;

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(name.lowercase())
    }

    enum class Gems : HTMaterialLike {
        CINNABAR,
        FLUORITE,
        PERIDOT,
        RUBY,
        SALTPETER,
        SAPPHIRE,
        SULFUR,
        ;

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(name.lowercase())
    }

    @JvmStatic
    val COAL_COKE: HTMaterialKey = HTMaterialKey.of("coal_coke")

    @JvmStatic
    val PLASTIC: HTMaterialKey = HTMaterialKey.of("plastic")
}
