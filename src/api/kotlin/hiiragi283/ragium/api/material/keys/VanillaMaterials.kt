package hiiragi283.ragium.api.material.keys

import hiiragi283.ragium.api.material.HTMaterialKey

/**
 * バニラで使用されている素材の一覧
 */
object VanillaMaterials {
    //    Dust    //

    @JvmField
    val GLOWSTONE: HTMaterialKey = HTMaterialKey.of("glowstone")

    @JvmField
    val OBSIDIAN: HTMaterialKey = HTMaterialKey.of("obsidian")

    //    Gem    //

    @JvmField
    val AMETHYST: HTMaterialKey = HTMaterialKey.of("amethyst")

    @JvmField
    val COAL: HTMaterialKey = HTMaterialKey.of("coal")

    @JvmField
    val DIAMOND: HTMaterialKey = HTMaterialKey.of("diamond")

    @JvmField
    val EMERALD: HTMaterialKey = HTMaterialKey.of("emerald")

    @JvmField
    val LAPIS: HTMaterialKey = HTMaterialKey.of("lapis")

    @JvmField
    val NETHERITE_SCRAP: HTMaterialKey = HTMaterialKey.of("netherite_scrap")

    @JvmField
    val QUARTZ: HTMaterialKey = HTMaterialKey.of("quartz")

    //    Metal    //

    @JvmField
    val COPPER: HTMaterialKey = HTMaterialKey.of("copper")

    @JvmField
    val GOLD: HTMaterialKey = HTMaterialKey.of("gold")

    @JvmField
    val IRON: HTMaterialKey = HTMaterialKey.of("iron")

    @JvmField
    val NETHERITE: HTMaterialKey = HTMaterialKey.of("netherite")

    //    Mineral    //

    @JvmField
    val REDSTONE: HTMaterialKey = HTMaterialKey.of("redstone")
}
