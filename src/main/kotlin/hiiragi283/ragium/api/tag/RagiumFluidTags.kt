package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.fluidTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid

object RagiumFluidTags {
    //    Common    //

    //    Ragium    //

    @JvmField
    val NITRO_FUEL: TagKey<Fluid> = fluidTagKey(RagiumAPI.id("fuels/nitro"))

    @JvmField
    val NON_NITRO_FUEL: TagKey<Fluid> = fluidTagKey(RagiumAPI.id("fuels/non_nitro"))

    @JvmField
    val NUCLEAR_FUEL: TagKey<Fluid> = fluidTagKey(RagiumAPI.id("fuels/nuclear"))

    @JvmField
    val THERMAL_FUEL: TagKey<Fluid> = fluidTagKey(RagiumAPI.id("fuels/thermal"))
}
