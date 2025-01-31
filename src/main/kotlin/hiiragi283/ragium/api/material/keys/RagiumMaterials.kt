package hiiragi283.ragium.api.material.keys

import hiiragi283.ragium.api.material.HTMaterialKey

object RagiumMaterials {
    //    Gem    //

    @JvmField
    val RAGI_CRYSTAL: HTMaterialKey = HTMaterialKey.of("ragi_crystal")

    //    Metal    //

    @JvmField
    val RAGI_ALLOY: HTMaterialKey = HTMaterialKey.of("ragi_alloy")

    @JvmField
    val RAGI_STEEL: HTMaterialKey = HTMaterialKey.of("ragi_steel")

    @JvmField
    val REFINED_RAGI_STEEL: HTMaterialKey = HTMaterialKey.of("refined_ragi_steel")

    @JvmField
    val RAGIUM: HTMaterialKey = HTMaterialKey.of("ragium")

    @JvmField
    val DEEP_STEEL: HTMaterialKey = HTMaterialKey.of("deep_steel")

    @JvmField
    val DRAGONIUM: HTMaterialKey = HTMaterialKey.of("dragonium")

    @JvmField
    val ECHORIUM: HTMaterialKey = HTMaterialKey.of("echorium")

    @JvmField
    val FIERIUM: HTMaterialKey = HTMaterialKey.of("fierium")

    @JvmField
    val END_CONTENTS: List<HTMaterialKey> = listOf(
        RAGIUM,
        DRAGONIUM,
        ECHORIUM,
        FIERIUM,
    )

    //    Mineral    //

    @JvmField
    val CRUDE_RAGINITE: HTMaterialKey = HTMaterialKey.of("crude_raginite")

    @JvmField
    val RAGINITE: HTMaterialKey = HTMaterialKey.of("raginite")
}
