package hiiragi283.ragium.setup

import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.common.fluid.HTExplosiveFluidType
import hiiragi283.core.common.registry.register.HTFluidContentRegister
import hiiragi283.ragium.api.RagiumAPI
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
        REGISTER.register(eventBus)
    }

    //    Inorganic    //

    // Gaseous
    @JvmField
    val AIR: HTFluidContent = REGISTER.registerVirtual("air") { properties = gas() }

    // H
    @JvmField
    val HYDROGEN: HTFluidContent = REGISTER.registerVirtual("hydrogen") { properties = gas() }

    // C
    @JvmField
    val CARBON_DIOXIDE: HTFluidContent = REGISTER.registerVirtual("carbon_dioxide") { properties = gas() }

    // N
    @JvmField
    val NITROGEN: HTFluidContent = REGISTER.registerVirtual("nitrogen") { properties = gas() }

    @JvmField
    val AMMONIA: HTFluidContent = REGISTER.registerVirtual("ammonia") { properties = gas() }

    // O
    @JvmField
    val OXYGEN: HTFluidContent = REGISTER.registerVirtual("oxygen") { properties = gas() }

    // Liquid

    // N
    @JvmField
    val NITRIC_ACID: HTFluidContent = REGISTER.registerFlowing("nitric_acid") { properties = liquid() }

    @JvmField
    val MIXTURE_ACID: HTFluidContent = REGISTER.registerFlowing("mixture_acid") { properties = liquid() }

    // S
    @JvmField
    val SULFURIC_ACID: HTFluidContent = REGISTER.registerFlowing("sulfuric_acid") { properties = liquid() }

    //    Organic    //

    // Coal
    @JvmField
    val CREOSOTE: HTFluidContent = REGISTER.registerFlowing("creosote") { properties = liquid() }

    @JvmField
    val SYNTHETIC_GAS: HTFluidContent = REGISTER.registerVirtual("synthetic_gas") { properties = gas() }

    @JvmField
    val SYNTHETIC_OIL: HTFluidContent = REGISTER.registerVirtual("synthetic_oil") { properties = liquid() }

    // Crude Oil
    @JvmField
    val CRUDE_OIL: HTFluidContent = REGISTER.registerFlowing("crude_oil") {
        properties = molten()
            .canSwim(false)
            .pathType(PathType.LAVA)
            .density(3000)
            .viscosity(6000)
            .motionScale(0.0001)
        typeFactory = ::HTExplosiveFluidType.partially1(2f)

        blockProperties = { it.speedFactor(0.4f) }
    }

    @JvmField
    val NAPHTHA: HTFluidContent = REGISTER.registerVirtual("naphtha") {
        properties = liquid()
        typeFactory = ::HTExplosiveFluidType.partially1(3f)
    }

    @JvmField
    val RESIDUE_OIL: HTFluidContent = REGISTER.registerVirtual("residue_oil") {
        properties = liquid()
        typeFactory = ::HTExplosiveFluidType.partially1(1f)
    }

    // Gaseous
    @JvmField
    val METHANE: HTFluidContent = REGISTER.registerVirtual("methane") { properties = gas() }

    @JvmField
    val ETHYLENE: HTFluidContent = REGISTER.registerVirtual("ethylene") { properties = gas() }

    @JvmField
    val BUTADIENE: HTFluidContent = REGISTER.registerVirtual("butadiene") { properties = gas() }

    // Alcohol
    @JvmField
    val METHANOL: HTFluidContent = REGISTER.registerVirtual("methanol") { properties = liquid() }

    @JvmField
    val ETHANOL: HTFluidContent = REGISTER.registerVirtual("ethanol") { properties = liquid() }

    // Oil Products
    @JvmField
    val FUEL: HTFluidContent = REGISTER.registerVirtual("fuel") {
        properties = liquid()
        typeFactory = ::HTExplosiveFluidType.partially1(4f)
    }

    @JvmField
    val LUBRICANT: HTFluidContent = REGISTER.registerVirtual("lubricant") { properties = liquid() }

    //    Biomass    //

    @JvmField
    val SUNFLOWER_OIL: HTFluidContent = REGISTER.registerVirtual("sunflower_oil") { properties = liquid() }

    @JvmField
    val BIOFUEL: HTFluidContent = REGISTER.registerVirtual("biofuel") { properties = liquid() }

    //    Misc    //

    @JvmField
    val COOLANT: HTFluidContent = REGISTER.registerFlowing("coolant") { properties = liquid().temperature(273) }

    @JvmField
    val RAGI_MATTER: HTFluidContent = REGISTER.registerVirtual("ragi_matter") { properties = gas() }

    //    Extensions    //

    @JvmStatic
    private fun create(fill: SoundEvent, empty: SoundEvent): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, fill)
        .sound(SoundActions.BUCKET_EMPTY, empty)

    @JvmStatic
    private fun liquid(): FluidType.Properties = create(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)

    @JvmStatic
    private fun gas(): FluidType.Properties = liquid().density(-1000)

    @JvmStatic
    private fun molten(): FluidType.Properties = create(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA)
}
