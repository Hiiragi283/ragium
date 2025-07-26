package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTFluidContentRegister
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.pathfinder.PathType
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType

object RagiumFluidContents {
    @JvmField
    val REGISTER = HTFluidContentRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(
        name: String,
        properties: FluidType.Properties,
    ): HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        REGISTER.register(name, properties.descriptionId("block.ragium.$name"))

    @JvmStatic
    private fun defaultProperties(): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)

    @JvmStatic
    private fun gaseous(): FluidType.Properties = defaultProperties().density(-1000)

    //    Vanilla    //

    @JvmField
    val HONEY: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("honey", defaultProperties())

    @JvmField
    val EXPERIENCE: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("experience", defaultProperties())

    @JvmField
    val MUSHROOM_STEW: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("mushroom_stew", defaultProperties())

    //    Oil    //

    val CRUDE_OIL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register(
        "crude_oil",
        defaultProperties()
            .canSwim(false)
            .pathType(PathType.LAVA)
            .density(3000)
            .viscosity(6000)
            .motionScale(0.0001),
    )

    val LPG: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("lpg", gaseous())

    val NAPHTHA: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("naphtha", defaultProperties())

    val DIESEL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("fuel", defaultProperties())

    val CRIMSON_DIESEL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register(
        "crimson_diesel",
        defaultProperties(),
    )

    val LUBRICANT: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("lubricant", defaultProperties())

    //    Sap    //

    val SAP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("sap", defaultProperties())

    val SYRUP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("syrup", defaultProperties())

    @JvmField
    val CRIMSON_SAP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("crimson_sap", defaultProperties())

    @JvmField
    val WARPED_SAP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("warped_sap", defaultProperties())
}
