package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTFluidContentRegister
import hiiragi283.ragium.api.util.RagiumConstantValues
import net.minecraft.sounds.SoundEvents
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
        register(RagiumConstantValues.HONEY, defaultProperties())

    @JvmField
    val EXPERIENCE: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("experience", defaultProperties())

    @JvmField
    val CHOCOLATE: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("chocolate", defaultProperties())

    @JvmField
    val MUSHROOM_STEW: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register(RagiumConstantValues.MUSHROOM_STEW, defaultProperties())

    //    Hydrogen    //

    // val HYDROGEN: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("hydrogen", gaseous())

    //    Nitrogen    //

    // val NITROGEN: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("nitrogen", gaseous())

    // val AMMONIA: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("ammonia", gaseous())

    // val NITRIC_ACID: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("nitric_acid", defaultProperties())

    // val MIXTURE_ACID: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("mixture_acid", defaultProperties())

    //    Oxygen    //

    // val OXYGEN: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("oxygen", gaseous())

    // val ROCKET_FUEL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("rocket_fuel", gaseous())

    //    Alkali    //

    // val ALKALI_SOLUTION: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("alkali_solution", defaultProperties())

    //    Sulfur    //

    // val SULFUR_DIOXIDE: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("sulfur_dioxide", gaseous())

    // val SULFUR_TRIOXIDE: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("sulfur_trioxide", gaseous())

    // val SULFURIC_ACID: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("sulfuric_acid", defaultProperties())

    //    Oil    //

    /*val CRUDE_OIL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register(
        "crude_oil",
        defaultProperties()
            .canSwim(false)
            .pathType(PathType.LAVA)
            .density(3000)
            .viscosity(6000)
            .motionScale(0.0001),
    )*/

    // val NAPHTHA: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("naphtha", defaultProperties())

    // val FUEL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("fuel", defaultProperties())

    // val NITRO_FUEL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("nitro_fuel", defaultProperties())

    // val AROMATIC_COMPOUND: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("aromatic_compound", defaultProperties())

    //    Bio    //

    // val PLANT_OIL: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("plant_oil", defaultProperties())

    // val BIOMASS: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("biomass", defaultProperties())

    //    Sap    //

    // val SAP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register("sap", defaultProperties())

    @JvmField
    val CRIMSON_SAP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register(RagiumConstantValues.CRIMSON_SAP, defaultProperties())

    @JvmField
    val WARPED_SAP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register(RagiumConstantValues.WARPED_SAP, defaultProperties())
}
