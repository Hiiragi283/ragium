package hiiragi283.ragium.setup

import hiiragi283.ragium.api.block.attribute.HTDirectionalBlockAttribute
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

    // Basic
    @JvmField
    val THERMAL_GENERATOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.THERMAL_GENERATOR }
        .addMenu { RagiumMenuTypes.FUEL_GENERATOR }
        .addGeneratorTier(HTMachineTier.BASIC)
        .build()

    // Advanced
    @JvmField
    val COMBUSTION_GENERATOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.COMBUSTION_GENERATOR }
        .addMenu { RagiumMenuTypes.FUEL_GENERATOR }
        .addGeneratorTier(HTMachineTier.ADVANCED)
        .build()

    // Elite
    @JvmField
    val SOLAR_PANEL_CONTROLLER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.SOLAR_PANEL_CONTROLLER }
        .addGeneratorTier(HTMachineTier.ELITE)
        .build()

    // Ultimate
    @JvmField
    val ENCHANTMENT_GENERATOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.ENCHANTMENT_GENERATOR }
        .addMenu { RagiumMenuTypes.FUEL_GENERATOR }
        .addGeneratorTier(HTMachineTier.ULTIMATE)
        .build()

    @JvmField
    val NUCLEAR_REACTOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.NUCLEAR_REACTOR }
        .addGeneratorTier(HTMachineTier.ULTIMATE)
        .build()

    //    Consumer    //

    // Basic
    @JvmField
    val ALLOY_SMELTER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.ALLOY_SMELTER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.BASIC)
        .addMenu { RagiumMenuTypes.ALLOY_SMELTER }
        .build()

    @JvmField
    val BLOCK_BREAKER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.BLOCK_BREAKER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.BASIC)
        .addMenu { RagiumMenuTypes.SINGLE_ITEM }
        .build()

    @JvmField
    val CUTTING_MACHINE: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.CUTTING_MACHINE }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.BASIC)
        .addMenu { RagiumMenuTypes.CUTTING_MACHINE }
        .build()

    @JvmField
    val COMPRESSOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.COMPRESSOR }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.BASIC)
        .addMenu { RagiumMenuTypes.COMPRESSOR }
        .build()

    @JvmField
    val EXTRACTOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.EXTRACTOR }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.BASIC)
        .addMenu { RagiumMenuTypes.EXTRACTOR }
        .build()

    @JvmField
    val PULVERIZER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.PULVERIZER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.BASIC)
        .addMenu { RagiumMenuTypes.PULVERIZER }
        .build()

    // Advanced

    @JvmField
    val CRUSHER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.CRUSHER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.ADVANCED)
        .addMenu { RagiumMenuTypes.CHANCED_ITEM_OUTPUT }
        .build()

    @JvmField
    val MELTER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.MELTER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.ADVANCED)
        .addMenu { RagiumMenuTypes.MELTER }
        .build()

    @JvmField
    val REFINERY: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.REFINERY }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.ADVANCED)
        .addMenu { RagiumMenuTypes.REFINERY }
        .build()

    @JvmField
    val WASHER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.WASHER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.ADVANCED)
        .addMenu { RagiumMenuTypes.CHANCED_ITEM_OUTPUT }
        .build()

    // Elite

    @JvmField
    val BREWERY: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.BREWERY }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.ELITE)
        .addMenu { RagiumMenuTypes.CHANCED_ITEM_OUTPUT }
        .build()

    @JvmField
    val MULTI_SMELTER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.MULTI_SMELTER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.ELITE)
        .addMenu { RagiumMenuTypes.SMELTER }
        .build()

    @JvmField
    val PLANTER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.PLANTER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.ELITE)
        .addMenu { RagiumMenuTypes.CHANCED_ITEM_OUTPUT }
        .build()

    @JvmField
    val SIMULATOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.SIMULATOR }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addConsumerTier(HTMachineTier.ELITE)
        .addMenu { RagiumMenuTypes.SIMULATOR }
        .build()

    // Ultimate

    //    Device    //

    // Basic
    @JvmField
    val ITEM_BUFFER: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.ITEM_BUFFER }
        .addMenu { RagiumMenuTypes.ITEM_BUFFER }
        .addTier(HTMachineTier.BASIC)
        .build()

    @JvmField
    val MILK_COLLECTOR: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.MILK_COLLECTOR }
        .addMenu { RagiumMenuTypes.FLUID_COLLECTOR }
        .addTier(HTMachineTier.BASIC)
        .build()

    @JvmField
    val WATER_COLLECTOR: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.WATER_COLLECTOR }
        .addMenu { RagiumMenuTypes.FLUID_COLLECTOR }
        .addTier(HTMachineTier.BASIC)
        .build()

    // Advanced
    @JvmField
    val EXP_COLLECTOR: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.EXP_COLLECTOR }
        .addMenu { RagiumMenuTypes.FLUID_COLLECTOR }
        .addTier(HTMachineTier.ADVANCED)
        .build()

    @JvmField
    val LAVA_COLLECTOR: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.LAVA_COLLECTOR }
        .addMenu { RagiumMenuTypes.FLUID_COLLECTOR }
        .addTier(HTMachineTier.ADVANCED)
        .build()

    // Elite
    @JvmField
    val DIM_ANCHOR: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.DIM_ANCHOR }
        .addTier(HTMachineTier.ELITE)
        .build()

    @JvmField
    val ENI: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.ENI }
        .addMenu { RagiumMenuTypes.ENERGY_NETWORK_ACCESS }
        .addTier(HTMachineTier.ELITE)
        .build()

    // Ultimate
    @JvmField
    val MOB_CAPTURER: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.MOB_CAPTURER }
        .addMenu { RagiumMenuTypes.MOB_CAPTURER }
        .addTier(HTMachineTier.ULTIMATE)
        .build()

    @JvmField
    val TELEPAD: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.TELEPAD }
        .addMenu { RagiumMenuTypes.TELEPAD }
        .addTier(HTMachineTier.ULTIMATE)
        .build()

    // Creative
    @JvmField
    val CEU: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.CEU }
        .addMenu { RagiumMenuTypes.ENERGY_NETWORK_ACCESS }
        .addTier(HTMachineTier.CREATIVE)
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
