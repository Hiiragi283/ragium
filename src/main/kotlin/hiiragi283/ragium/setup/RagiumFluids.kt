package hiiragi283.ragium.setup

import hiiragi283.core.api.function.partially1
import hiiragi283.core.common.fluid.HTExplosiveFluidType
import hiiragi283.core.common.registry.register.HTFluidContentRegister
import hiiragi283.core.common.registry.register.HTSimpleFluidContent
import hiiragi283.core.common.registry.register.HTVirtualFluidContent
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
    val HYDROGEN: HTVirtualFluidContent = REGISTER.registerVirtual("hydrogen", gas())

    @JvmField
    val CARBON_MONOXIDE: HTVirtualFluidContent = REGISTER.registerVirtual("carbon_monoxide", gas())

    @JvmField
    val CARBON_DIOXIDE: HTVirtualFluidContent = REGISTER.registerVirtual("carbon_dioxide", gas())

    @JvmField
    val OXYGEN: HTVirtualFluidContent = REGISTER.registerVirtual("oxygen", gas())

    //    Organic    //

    // Coal
    @JvmField
    val CREOSOTE: HTVirtualFluidContent = REGISTER.registerVirtual("creosote", liquid())

    @JvmField
    val COAL_GAS: HTVirtualFluidContent = REGISTER.registerVirtual("coal_gas", gas())

    @JvmField
    val COAL_LIQUID: HTVirtualFluidContent = REGISTER.registerVirtual("coal_liquid", liquid())

    // Resource
    @JvmField
    val CRUDE_OIL: HTSimpleFluidContent = REGISTER.registerFlowing(
        "crude_oil",
        molten()
            .canSwim(false)
            .pathType(PathType.LAVA)
            .density(3000)
            .viscosity(6000)
            .motionScale(0.0001),
        ::HTExplosiveFluidType.partially1(2f),
    ) { it.speedFactor(0.4f) }

    @JvmField
    val LPG: HTVirtualFluidContent = REGISTER.registerVirtual("lpg", gas())

    @JvmField
    val NAPHTHA: HTVirtualFluidContent = REGISTER.registerVirtual("naphtha", liquid(), ::HTExplosiveFluidType.partially1(3f))

    @JvmField
    val RESIDUE_OIL: HTVirtualFluidContent = REGISTER.registerVirtual("residue_oil", liquid(), ::HTExplosiveFluidType.partially1(1f))

    // Gaseous
    @JvmField
    val METHANE: HTVirtualFluidContent = REGISTER.registerVirtual("methane", gas())

    @JvmField
    val ETHYLENE: HTVirtualFluidContent = REGISTER.registerVirtual("ethylene", gas())

    @JvmField
    val BUTADIENE: HTVirtualFluidContent = REGISTER.registerVirtual("butadiene", gas())

    // Alcohol
    @JvmField
    val METHANOL: HTVirtualFluidContent = REGISTER.registerVirtual("methanol", liquid())

    @JvmField
    val ETHANOL: HTVirtualFluidContent = REGISTER.registerVirtual("ethanol", liquid())

    // Liquid
    @JvmField
    val SUNFLOWER_OIL: HTVirtualFluidContent = REGISTER.registerVirtual("sunflower_oil", liquid())

    @JvmField
    val BIOFUEL: HTVirtualFluidContent = REGISTER.registerVirtual("biofuel", liquid())

    @JvmField
    val GASOLINE: HTVirtualFluidContent = REGISTER.registerVirtual("gasoline", liquid(), ::HTExplosiveFluidType.partially1(4f))

    @JvmField
    val LUBRICANT: HTVirtualFluidContent = REGISTER.registerVirtual("lubricant", liquid())

    //    Misc    //

    @JvmField
    val MOLTEN_RAGINITE: HTVirtualFluidContent = REGISTER.registerVirtual("molten_raginite", molten().temperature(1300))

    @JvmField
    val COOLANT: HTVirtualFluidContent = REGISTER.registerVirtual("coolant", liquid().temperature(273))

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
