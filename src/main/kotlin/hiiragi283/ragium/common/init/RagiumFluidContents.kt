package hiiragi283.ragium.common.init

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
    val CHOCOLATE: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("chocolate", defaultProperties())

    @JvmField
    val MUSHROOM_STEW: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("mushroom_stew", defaultProperties())

    //    Hydrogen    //

    @JvmField
    val HYDROGEN: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("hydrogen", gaseous())

    //    Nitrogen    //

    @JvmField
    val NITROGEN: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("nitrogen", gaseous())

    @JvmField
    val AMMONIA: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("ammonia", gaseous())

    @JvmField
    val NITRIC_ACID: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("nitric_acid", defaultProperties())

    @JvmField
    val MIXTURE_ACID: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("mixture_acid", defaultProperties())

    //    Oxygen    //

    @JvmField
    val OXYGEN: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("oxygen", gaseous())

    @JvmField
    val ROCKET_FUEL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("rocket_fuel", gaseous())

    //    Alkali    //

    @JvmField
    val ALKALI_SOLUTION: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("alkali_solution", defaultProperties())

    //    Sulfur    //

    @JvmField
    val SULFUR_DIOXIDE: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("sulfur_dioxide", gaseous())

    @JvmField
    val SULFUR_TRIOXIDE: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("sulfur_trioxide", gaseous())

    @JvmField
    val SULFURIC_ACID: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("sulfuric_acid", defaultProperties())

    //    Oil    //

    @JvmField
    val CRUDE_OIL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register(
            "crude_oil",
            defaultProperties()
                .canSwim(false)
                .pathType(PathType.LAVA)
                .density(3000)
                .viscosity(6000)
                .motionScale(0.0001),
        )

    @JvmField
    val NAPHTHA: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("naphtha", defaultProperties())

    @JvmField
    val FUEL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("fuel", defaultProperties())

    @JvmField
    val NITRO_FUEL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("nitro_fuel", defaultProperties())

    @JvmField
    val AROMATIC_COMPOUND: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("aromatic_compound", defaultProperties())

    //    Bio    //

    @JvmField
    val PLANT_OIL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("plant_oil", defaultProperties())

    @JvmField
    val BIOMASS: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("biomass", defaultProperties())

    //    Sap    //

    @JvmField
    val SAP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("sap", defaultProperties())

    @JvmField
    val CRIMSON_SAP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("crimson_sap", defaultProperties())

    @JvmField
    val WARPED_SAP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("warped_sap", defaultProperties())

    //    Other    //
}
