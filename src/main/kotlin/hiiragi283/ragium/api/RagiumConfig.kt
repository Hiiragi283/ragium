package hiiragi283.ragium.api

import hiiragi283.ragium.api.extension.getModMetadata
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry
import me.shedaniel.autoconfig.serializer.PartitioningSerializer
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants

private fun getActualVersion(): String = getModMetadata(RagiumAPI.MOD_ID)?.version?.friendlyString ?: "MISSING"

@Config(name = RagiumAPI.MOD_ID)
class RagiumConfig(
    @JvmField
    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.TransitiveObject
    val common: Common,
    @JvmField
    @ConfigEntry.Category("machine")
    @ConfigEntry.Gui.TransitiveObject
    val machine: Machine,
    @JvmField
    @ConfigEntry.Category("utility")
    @ConfigEntry.Gui.TransitiveObject
    val utility: Utility,
) : PartitioningSerializer.GlobalData() {
    constructor() : this(Common(), Machine(), Utility())

    //    Client    //

    //    Common    //

    @Config(name = "common")
    class Common(
        @JvmField
        @ConfigEntry.Gui.Excluded
        val version: String,
        @JvmField
        val enableRadioactiveEffect: Boolean,
    ) : ConfigData {
        constructor() : this(
            version = getActualVersion(),
            enableRadioactiveEffect = true,
        )

        override fun validatePostLoad() {
            check(this.version == getActualVersion()) { "Not matching config version! Remove old config file!" }
        }
    }

    //    Machine    //

    @Config(name = "machine")
    class Machine(
        @JvmField
        @ConfigEntry.Gui.CollapsibleObject
        val generator: Generator,
        @JvmField
        val showParticle: Boolean,
    ) : ConfigData {
        constructor() : this(Generator(), false)
    }

    class Generator(
        @JvmField
        @ConfigEntry.BoundedDiscrete(min = 1, max = FluidConstants.BUCKET)
        val coolant: Long,
        @JvmField
        @ConfigEntry.BoundedDiscrete(min = 1, max = FluidConstants.BUCKET)
        val nitroFuel: Long,
        @JvmField
        @ConfigEntry.BoundedDiscrete(min = 1, max = FluidConstants.BUCKET)
        val nonNitroFuel: Long,
        @JvmField
        @ConfigEntry.BoundedDiscrete(min = 1, max = FluidConstants.BUCKET)
        val steamWater: Long,
        @JvmField
        @ConfigEntry.BoundedDiscrete(min = 1, max = FluidConstants.BUCKET)
        val thermalFuel: Long,
    ) {
        constructor() : this(
            coolant = FluidConstants.BUCKET,
            nitroFuel = FluidConstants.NUGGET,
            nonNitroFuel = FluidConstants.INGOT,
            steamWater = FluidConstants.INGOT,
            thermalFuel = FluidConstants.INGOT,
        )
    }

    //    Mechanic    //

    //    Utilities    //

    @Config(name = "utility")
    class Utility(
        @JvmField
        val autoIlluminatorRadius: Int,
        @JvmField
        val dynamitePlaceRadius: Int,
        @JvmField
        @ConfigEntry.Gui.RequiresRestart
        val defaultDynamitePower: Float,
        @JvmField
        val gigantHammerMiningSpeed: Float,
    ) : ConfigData {
        constructor() : this(
            autoIlluminatorRadius = 16,
            dynamitePlaceRadius = 2,
            defaultDynamitePower = 2f,
            gigantHammerMiningSpeed = 12f,
        )
    }
}
