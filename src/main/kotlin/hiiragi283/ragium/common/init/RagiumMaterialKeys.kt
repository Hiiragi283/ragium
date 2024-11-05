package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.material.HTMaterialKey

object RagiumMaterialKeys {
    //    Tier 1    //

    @JvmField
    val CRUDE_RAGINITE: HTMaterialKey = HTMaterialKey.of("crude_raginite")

    @JvmField
    val RAGI_ALLOY: HTMaterialKey = HTMaterialKey.of("ragi_alloy")

    @JvmField
    val ASH: HTMaterialKey = HTMaterialKey.of("ash")

    @JvmField
    val COPPER: HTMaterialKey = HTMaterialKey.of("copper")

    @JvmField
    val IRON: HTMaterialKey = HTMaterialKey.of("iron")

    @JvmField
    val NITER: HTMaterialKey = HTMaterialKey.of("niter")

    @JvmField
    val SULFUR: HTMaterialKey = HTMaterialKey.of("sulfur")

    @JvmField
    val TIER_ONE: List<HTMaterialKey> = listOf(
        CRUDE_RAGINITE,
        RAGI_ALLOY,
        ASH,
        COPPER,
        IRON,
        NITER,
        SULFUR,
    )

    //    Tier 2    //

    @JvmField
    val RAGINITE: HTMaterialKey = HTMaterialKey.of("raginite")

    @JvmField
    val RAGI_STEEL: HTMaterialKey = HTMaterialKey.of("ragi_steel")

    @JvmField
    val FLUORITE: HTMaterialKey = HTMaterialKey.of("fluorite")

    @JvmField
    val GOLD: HTMaterialKey = HTMaterialKey.of("gold")

    @JvmField
    val PLASTIC: HTMaterialKey = HTMaterialKey.of("plastic") // PE

    @JvmField
    val SILICON: HTMaterialKey = HTMaterialKey.of("silicon")

    @JvmField
    val STEEL: HTMaterialKey = HTMaterialKey.of("steel")

    @JvmField
    val TIER_TWO: List<HTMaterialKey> = listOf(
        RAGINITE,
        RAGI_STEEL,
        FLUORITE,
        GOLD,
        PLASTIC,
        SILICON,
        STEEL,
    )

    //    Tier 3    //

    @JvmField
    val RAGI_CRYSTAL: HTMaterialKey = HTMaterialKey.of("ragi_crystal")

    @JvmField
    val REFINED_RAGI_STEEL: HTMaterialKey = HTMaterialKey.of("refined_ragi_steel")

    @JvmField
    val ALUMINUM: HTMaterialKey = HTMaterialKey.of("aluminum")

    @JvmField
    val BAUXITE: HTMaterialKey = HTMaterialKey.of("bauxite")

    @JvmField
    val ENGINEERING_PLASTIC: HTMaterialKey = HTMaterialKey.of("engineering_plastic")

    @JvmField
    val STELLA: HTMaterialKey = HTMaterialKey.of("stella")

    @JvmField
    val TIER_THREE: List<HTMaterialKey> = listOf(
        RAGI_CRYSTAL,
        REFINED_RAGI_STEEL,
        ALUMINUM,
        BAUXITE,
        ENGINEERING_PLASTIC,
        STELLA,
    )

    //    Tier 4    //

    @JvmField
    val RAGIUM: HTMaterialKey = HTMaterialKey.of("ragium")

    //    Integration    //
}
