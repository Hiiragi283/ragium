package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
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
    val CREOSOTE: TagKey<Fluid> = fluidTagKey(commonId("creosote"))

    @JvmField
    val HYDROFLUORIC_ACID: TagKey<Fluid> = fluidTagKey(commonId("hydrofluoric_acid"))

    @JvmField
    val MEAT: TagKey<Fluid> = fluidTagKey(commonId("meat"))

    @JvmField
    val OXYGEN: TagKey<Fluid> = fluidTagKey(commonId("oxygen"))

    @JvmField
    val SULFURIC_ACID: TagKey<Fluid> = fluidTagKey(commonId("sulfuric_acid"))

    //    Ragium    //

    @JvmField
    val NITRO_FUEL: TagKey<Fluid> = fluidTagKey(RagiumAPI.id("fuels/nitro"))

    @JvmField
    val NON_NITRO_FUEL: TagKey<Fluid> = fluidTagKey(RagiumAPI.id("fuels/non_nitro"))

    @JvmField
    val THERMAL_FUEL: TagKey<Fluid> = fluidTagKey(RagiumAPI.id("fuels/thermal"))
}
