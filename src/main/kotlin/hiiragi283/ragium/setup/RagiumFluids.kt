package hiiragi283.ragium.setup

import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.common.fluid.HTExplosiveFluidType
import hiiragi283.core.common.registry.register.HTFluidContentRegister
import hiiragi283.ragium.api.RagiumAPI
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

    // H
    @JvmField
    val HYDROGEN: HTFluidContent = REGISTER.registerVirtual("hydrogen") { properties = gas() }

    @JvmField
    val STEAM: HTFluidContent = REGISTER.registerVirtual("steam") { properties = gas().temperature(400) }

    // O
    @JvmField
    val OXYGEN: HTFluidContent = REGISTER.registerVirtual("oxygen") { properties = gas() }

    // C
    @JvmField
    val CREOSOTE: HTFluidContent = REGISTER.registerFlowing("creosote") { properties = liquid() }

    @JvmField
    val SYNTHETIC_GAS: HTFluidContent = REGISTER.registerVirtual("synthetic_gas") { properties = gas() }

    @JvmField
    val SYNTHETIC_OIL: HTFluidContent = REGISTER.registerFlowing("synthetic_oil") { properties = liquid() }

    // Organic
    @JvmField
    val METHANE: HTFluidContent = REGISTER.registerVirtual("methane") { properties = gas() }

    @JvmField
    val CRUDE_BIO: HTFluidContent = REGISTER.registerFlowing("crude_bio") { properties = liquid().motionScale(0.0001) }

    @JvmField
    val ETHANOL: HTFluidContent = REGISTER.registerFlowing("ethanol") { properties = liquid() }

    @JvmField
    val BIOFUEL: HTFluidContent = REGISTER.registerFlowing("biofuel") { properties = liquid() }

    // N
    @JvmField
    val NITROGEN: HTFluidContent = REGISTER.registerVirtual("nitrogen") { properties = gas() }

    @JvmField
    val LIQUID_NITROGEN: HTFluidContent = REGISTER.registerFlowing("liquid_nitrogen") {
        properties = liquid().temperature(300 - 196)
        typeFactory = ::HTLiquidGasFluidType
        blockFactory = null
    }

    // Na
    @JvmField
    val NAOH_SOLUTION: HTFluidContent = REGISTER.registerFlowing("sodium_hydroxide_solution") { properties = liquid() }

    // Hg
    @JvmField
    val MERCURY: HTFluidContent = REGISTER.registerVirtual("mercury") { properties = liquid() }

    //    Nether    //

    // C
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
    val NAPHTHA: HTFluidContent = REGISTER.registerFlowing("naphtha") {
        properties = liquid()
        typeFactory = ::HTExplosiveFluidType.partially1(3f)
    }

    @JvmField
    val FUEL: HTFluidContent = REGISTER.registerFlowing("fuel") {
        properties = liquid()
        typeFactory = ::HTExplosiveFluidType.partially1(4f)
    }

    // N
    @JvmField
    val NITROGEN_DIOXIDE: HTFluidContent = REGISTER.registerVirtual("nitrogen_dioxide") { properties = gas() }

    @JvmField
    val AMMONIA: HTFluidContent = REGISTER.registerFlowing("ammonia") { properties = gas() }

    @JvmField
    val NITRIC_ACID: HTFluidContent = REGISTER.registerFlowing("nitric_acid") { properties = liquid() }

    // S
    @JvmField
    val SULFUR_DIOXIDE: HTFluidContent = REGISTER.registerVirtual("sulfur_dioxide") { properties = gas() }

    @JvmField
    val SULFUR_TRIOXIDE: HTFluidContent = REGISTER.registerVirtual("sulfur_trioxide") { properties = gas() }

    @JvmField
    val SULFURIC_ACID: HTFluidContent = REGISTER.registerFlowing("sulfuric_acid") { properties = liquid() }

    //    The End    //

    // He
    @JvmField
    val HELIUM: HTFluidContent = REGISTER.registerVirtual("helium") { properties = gas() }

    //    Misc    //

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
