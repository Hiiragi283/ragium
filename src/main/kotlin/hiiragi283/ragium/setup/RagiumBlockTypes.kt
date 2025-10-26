package hiiragi283.ragium.setup

import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.common.block.type.HTMachineBlockType
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.tier.HTMachineTier

/**
 * @see mekanism.common.registries.MekanismBlockTypes
 */
object RagiumBlockTypes {
    //    Generator    //

    @JvmField
    val THERMAL_GENERATOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.THERMAL_GENERATOR }
        .addMenu { RagiumMenuTypes.FUEL_GENERATOR }
        .addGeneratorTier(HTMachineTier.BASIC)
        .build()

    @JvmField
    val COMBUSTION_GENERATOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.COMBUSTION_GENERATOR }
        .addMenu { RagiumMenuTypes.FUEL_GENERATOR }
        .addGeneratorTier(HTMachineTier.ADVANCED)
        .build()

    @JvmField
    val ENCHANTMENT_GENERATOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.ENCHANTMENT_GENERATOR }
        .addMenu { RagiumMenuTypes.FUEL_GENERATOR }
        .addGeneratorTier(HTMachineTier.ULTIMATE)
        .build()

    @JvmField
    val SOLAR_PANEL_CONTROLLER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.SOLAR_PANEL_CONTROLLER }
        .addGeneratorTier(HTMachineTier.ELITE)
        .build()

    @JvmField
    val NUCLEAR_REACTOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.NUCLEAR_REACTOR }
        .addGeneratorTier(HTMachineTier.ULTIMATE)
        .build()

    //    Storage    //

    @JvmField
    val CRATES: Map<HTCrateTier, HTEntityBlockType> = HTCrateTier.entries.associateWith { tier: HTCrateTier ->
        HTEntityBlockType
            .builder { tier.getBlockEntityType() }
            .addTier(tier)
            .build()
    }

    @JvmField
    val DRUMS: Map<HTDrumTier, HTEntityBlockType> = HTDrumTier.entries.associateWith { tier: HTDrumTier ->
        HTEntityBlockType
            .builder { tier.getBlockEntityType() }
            .addMenu { RagiumMenuTypes.DRUM }
            .addTier(tier)
            .build()
    }
}
