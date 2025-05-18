package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid

/**
 * Ragiumが使用する液体の[TagKey]の一覧
 */
object RagiumFluidTags {
    //    Common    //

    @JvmField
    val CHOCOLATES: TagKey<Fluid> = commonTag("chocolates")

    @JvmField
    val CREOSOTE: TagKey<Fluid> = commonTag("creosote")

    @JvmField
    val MEAT: TagKey<Fluid> = commonTag("meat")

    @JvmField
    val STEAM: TagKey<Fluid> = commonTag("steam")

    //    Ragium    //

    @JvmField
    val NITRO_FUEL: TagKey<Fluid> = commonTag("fuels", "nitro")

    @JvmField
    val NON_NITRO_FUEL: TagKey<Fluid> = commonTag("fuels", "non_nitro")

    @JvmField
    val THERMAL_FUEL: TagKey<Fluid> = commonTag("fuels", "thermal")

    @JvmStatic
    private fun commonTag(path: String): TagKey<Fluid> = fluidTagKey(commonId(path))

    @JvmStatic
    private fun commonTag(prefix: String, value: String): TagKey<Fluid> = fluidTagKey(commonId(prefix, value))
}
