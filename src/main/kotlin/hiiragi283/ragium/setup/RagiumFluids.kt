package hiiragi283.ragium.setup

import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTFluidContentRegister
import hiiragi283.core.support.fluid.HTExplosiveFluidType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.fluid.HTCreativeFluidType
import hiiragi283.ragium.common.fluid.HTLiquidGasFluidType
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.pathfinder.PathType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidType

object RagiumFluids {
    @JvmField
    val REGISTER = HTFluidContentRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.addAlias("ethylene", "ethene")

        REGISTER.register(eventBus)
    }

    //    Overworld    //

    @JvmField
    val MOLTEN_RAGINITE: HTFluidContent.Virtual = REGISTER.registerVirtual("molten_raginite") { properties = molten() }

    @JvmField
    val MOLTEN_STAINLESS_STEEL: HTFluidContent.Virtual = REGISTER.registerVirtual("molten_stainless_steel") { properties = molten() }

    // H
    @JvmField
    val HYDROGEN: HTFluidContent.Virtual = REGISTER.registerVirtual("hydrogen") { properties = gas() }

    @JvmField
    val STEAM: HTFluidContent.Virtual = REGISTER.registerVirtual("steam") { properties = gas().temperature(400) }

    // O
    @JvmField
    val OXYGEN: HTFluidContent.Virtual = REGISTER.registerVirtual("oxygen") { properties = gas() }

    // C
    @JvmField
    val CREOSOTE: HTFluidContent.Flowing = REGISTER.registerFlowing("creosote") { properties = liquid() }

    @JvmField
    val SYNTHETIC_GAS: HTFluidContent.Virtual = REGISTER.registerVirtual("synthetic_gas") { properties = gas() }

    @JvmField
    val SYNTHETIC_OIL: HTFluidContent.Flowing = REGISTER.registerFlowing("synthetic_oil") { properties = liquid() }

    // Organic
    @JvmField
    val GLUE: HTFluidContent.Flowing = REGISTER.registerFlowing("glue") { properties = liquid().motionScale(0.0001) }

    @JvmField
    val METHANE: HTFluidContent.Virtual = REGISTER.registerVirtual("methane") { properties = gas() }

    @JvmField
    val CRUDE_BIO: HTFluidContent.Flowing = REGISTER.registerFlowing("crude_bio") { properties = liquid().motionScale(0.0001) }

    @JvmField
    val ETHANOL: HTFluidContent.Flowing = REGISTER.registerFlowing("ethanol") { properties = liquid() }

    @JvmField
    val BIOFUEL: HTFluidContent.Flowing = REGISTER.registerFlowing("biofuel") { properties = liquid() }

    // N
    @JvmField
    val NITROGEN: HTFluidContent.Virtual = REGISTER.registerVirtual("nitrogen") { properties = gas() }

    @JvmField
    val LIQUID_NITROGEN: HTFluidContent.Flowing = REGISTER.registerFlowing("liquid_nitrogen") {
        properties = liquid().temperature(300 - 196)
        typeFactory = ::HTLiquidGasFluidType
        blockFactory = null
    }

    // Na
    @JvmField
    val NAOH_SOLUTION: HTFluidContent.Flowing = REGISTER.registerFlowing("sodium_hydroxide_solution") { properties = liquid() }

    // Si
    @JvmField
    val MINERAL_WATER: HTFluidContent.Flowing = REGISTER.registerFlowing("mineral_water") { properties = liquid() }

    // Hg
    @JvmField
    val MERCURY: HTFluidContent.Virtual = REGISTER.registerVirtual("mercury") { properties = liquid() }

    //    Nether    //

    // C
    @JvmField
    val CRUDE_OIL: HTFluidContent.Flowing = REGISTER.registerFlowing("crude_oil") {
        properties = molten()
            .canSwim(false)
            .pathType(PathType.LAVA)
            .density(3000)
            .viscosity(6000)
            .motionScale(0.0001)
        typeFactory = { HTExplosiveFluidType(2f, it) }

        blockProperties = { it.speedFactor(0.4f) }
    }

    @JvmField
    val NAPHTHA: HTFluidContent.Flowing = REGISTER.registerFlowing("naphtha") {
        properties = liquid()
        typeFactory = { HTExplosiveFluidType(3f, it) }
    }

    @JvmField
    val FUEL: HTFluidContent.Flowing = REGISTER.registerFlowing("fuel") {
        properties = liquid()
        typeFactory = { HTExplosiveFluidType(4f, it) }
    }

    // N
    @JvmField
    val NITROGEN_DIOXIDE: HTFluidContent.Virtual = REGISTER.registerVirtual("nitrogen_dioxide") { properties = gas() }

    @JvmField
    val AMMONIA: HTFluidContent.Flowing = REGISTER.registerFlowing("ammonia") { properties = gas() }

    @JvmField
    val NITRIC_ACID: HTFluidContent.Flowing = REGISTER.registerFlowing("nitric_acid") { properties = liquid() }

    // S
    @JvmField
    val SULFUR_DIOXIDE: HTFluidContent.Virtual = REGISTER.registerVirtual("sulfur_dioxide") { properties = gas() }

    @JvmField
    val SULFUR_TRIOXIDE: HTFluidContent.Virtual = REGISTER.registerVirtual("sulfur_trioxide") { properties = gas() }

    @JvmField
    val SULFURIC_ACID: HTFluidContent.Flowing = REGISTER.registerFlowing("sulfuric_acid") { properties = liquid() }

    //    The End    //

    // He
    @JvmField
    val CHORUS_GAS: HTFluidContent.Virtual = REGISTER.registerVirtual("chorus_gas") { properties = gas() }

    //    End Game    //

    @JvmField
    val RAGI_MATTER: HTFluidContent.Flowing = REGISTER.registerFlowing("ragi_matter") {
        properties = liquid()
        typeFactory = ::HTCreativeFluidType
    }

    //    Extensions    //

    @JvmStatic
    private fun create(fill: SoundEvent, empty: SoundEvent): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, fill)
        .sound(SoundActions.BUCKET_EMPTY, empty)

    @JvmStatic
    private fun liquid(): FluidType.Properties = create(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)

    @JvmStatic
    private fun gas(): FluidType.Properties = liquid()
        .canPushEntity(false)
        .canSwim(false)
        .fallDistanceModifier(1f)
        .supportsBoating(false)
        .density(-1000)

    @JvmStatic
    private fun molten(): FluidType.Properties = create(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA)
}
