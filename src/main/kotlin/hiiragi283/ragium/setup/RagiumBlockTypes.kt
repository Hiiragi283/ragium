package hiiragi283.ragium.setup

import hiiragi283.ragium.api.block.attribute.HTDirectionalBlockAttribute
import hiiragi283.ragium.api.block.type.HTBlockType
import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.common.block.type.HTMachineBlockType
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.tier.HTMachineTier
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.world.level.block.Block

/**
 * @see mekanism.common.registries.MekanismBlockTypes
 */
object RagiumBlockTypes {
    //    Generator    //

    // Basic
    @JvmField
    val THERMAL_GENERATOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.THERMAL_GENERATOR }
        .add(HTDirectionalBlockAttribute.FACING)
        .addMenu { RagiumMenuTypes.FUEL_GENERATOR }
        .addGeneratorTier(HTMachineTier.BASIC, RagiumConfig.COMMON.generator.thermal)
        .build(RagiumCommonTranslation.THERMAL_GENERATOR)

    // Advanced
    @JvmField
    val COMBUSTION_GENERATOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.COMBUSTION_GENERATOR }
        .add(HTDirectionalBlockAttribute.FACING)
        .addMenu { RagiumMenuTypes.FUEL_GENERATOR }
        .addGeneratorTier(HTMachineTier.ADVANCED, RagiumConfig.COMMON.generator.combustion)
        .build(RagiumCommonTranslation.THERMAL_GENERATOR)

    // Elite
    @JvmField
    val SOLAR_PANEL_UNIT: HTBlockType = HTBlockType
        .builder()
        .addShape(Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0))
        .build(RagiumCommonTranslation.SOLAR_PANEL_UNIT)

    @JvmField
    val SOLAR_PANEL_CONTROLLER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.SOLAR_PANEL_CONTROLLER }
        .addGeneratorTier(HTMachineTier.ELITE, RagiumConfig.COMMON.generator.solarPanelController)
        .build(RagiumCommonTranslation.SOLAR_PANEL_CONTROLLER)

    // Ultimate
    @JvmField
    val ENCHANTMENT_GENERATOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.ENCHANTMENT_GENERATOR }
        .add(HTDirectionalBlockAttribute.FACING)
        .addMenu { RagiumMenuTypes.FUEL_GENERATOR }
        .addGeneratorTier(HTMachineTier.ULTIMATE, RagiumConfig.COMMON.generator.enchantment)
        .build(RagiumCommonTranslation.ENCHANTMENT_GENERATOR)

    @JvmField
    val NUCLEAR_REACTOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.NUCLEAR_REACTOR }
        .addGeneratorTier(HTMachineTier.ULTIMATE, RagiumConfig.COMMON.generator.nuclearReactor)
        .build(RagiumCommonTranslation.NUCLEAR_REACTOR)

    //    Processor    //

    // Basic
    @JvmField
    val ALLOY_SMELTER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.ALLOY_SMELTER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.BASIC, RagiumConfig.COMMON.processor.alloySmelter)
        .addMenu { RagiumMenuTypes.PROCESSOR }
        .build(RagiumCommonTranslation.ALLOY_SMELTER)

    @JvmField
    val BLOCK_BREAKER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.BLOCK_BREAKER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.BASIC, RagiumConfig.COMMON.processor.blockBreaker)
        .addMenu { RagiumMenuTypes.PROCESSOR }
        .build(RagiumCommonTranslation.BLOCK_BREAKER)

    @JvmField
    val CUTTING_MACHINE: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.CUTTING_MACHINE }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.BASIC, RagiumConfig.COMMON.processor.cuttingMachine)
        .addMenu { RagiumMenuTypes.PROCESSOR }
        .build(RagiumCommonTranslation.CUTTING_MACHINE)

    @JvmField
    val COMPRESSOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.COMPRESSOR }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.BASIC, RagiumConfig.COMMON.processor.compressor)
        .addMenu { RagiumMenuTypes.ITEM_WITH_CATALYST }
        .build(RagiumCommonTranslation.COMPRESSOR)

    @JvmField
    val ELECTRIC_FURNACE: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.ELECTRIC_FURNACE }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.BASIC, RagiumConfig.COMMON.processor.multiSmelter)
        .addMenu { RagiumMenuTypes.SMELTER }
        .build()

    @JvmField
    val EXTRACTOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.EXTRACTOR }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.BASIC, RagiumConfig.COMMON.processor.extractor)
        .addMenu { RagiumMenuTypes.ITEM_WITH_CATALYST }
        .build(RagiumCommonTranslation.EXTRACTOR)

    @JvmField
    val PULVERIZER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.PULVERIZER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.BASIC, RagiumConfig.COMMON.processor.crusher)
        .addMenu { RagiumMenuTypes.SINGLE_ITEM_WITH_FLUID }
        .build(RagiumCommonTranslation.PULVERIZER)

    // Advanced

    @JvmField
    val CRUSHER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.CRUSHER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ADVANCED, RagiumConfig.COMMON.processor.crusher)
        .addMenu { RagiumMenuTypes.SINGLE_ITEM_WITH_FLUID }
        .build(RagiumCommonTranslation.CRUSHER)

    @JvmField
    val MELTER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.MELTER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ADVANCED, RagiumConfig.COMMON.processor.melter)
        .addMenu { RagiumMenuTypes.MELTER }
        .build(RagiumCommonTranslation.MELTER)

    @JvmField
    val MIXER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.MIXER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ADVANCED, RagiumConfig.COMMON.processor.mixer)
        .addMenu { RagiumMenuTypes.MIXER }
        .build(RagiumCommonTranslation.MIXER)

    @JvmField
    val REFINERY: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.REFINERY }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ADVANCED, RagiumConfig.COMMON.processor.refinery)
        .addMenu { RagiumMenuTypes.REFINERY }
        .build(RagiumCommonTranslation.REFINERY)

    // Elite

    @JvmField
    val ADVANCED_MIXER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.ADVANCED_MIXER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ELITE, RagiumConfig.COMMON.processor.advancedMixer)
        .build(RagiumCommonTranslation.MIXER)

    @JvmField
    val BREWERY: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.BREWERY }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ELITE, RagiumConfig.COMMON.processor.brewery)
        .addMenu { RagiumMenuTypes.SINGLE_ITEM_WITH_FLUID }
        .build(RagiumCommonTranslation.BREWERY)

    @JvmField
    val MULTI_SMELTER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.MULTI_SMELTER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ELITE, RagiumConfig.COMMON.processor.multiSmelter)
        .addMenu { RagiumMenuTypes.SMELTER }
        .build(RagiumCommonTranslation.MULTI_SMELTER)

    @JvmField
    val PLANTER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.PLANTER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ELITE, RagiumConfig.COMMON.processor.planter)
        .addMenu { RagiumMenuTypes.FLUID_TO_CHANCED }
        .build(RagiumCommonTranslation.PLANTER)

    @JvmField
    val WASHER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.WASHER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ELITE, RagiumConfig.COMMON.processor.washer)
        .addMenu { RagiumMenuTypes.FLUID_TO_CHANCED }
        .build(RagiumCommonTranslation.WASHER)

    // Ultimate

    @JvmField
    val ENCHANT_COPIER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.ENCHANT_COPIER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ULTIMATE, RagiumConfig.COMMON.processor.enchantCopier)
        .addMenu { RagiumMenuTypes.ENCHANT_COPIER }
        .build(RagiumCommonTranslation.ENCHANT_COPIER)

    @JvmField
    val ENCHANTER: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.ENCHANTER }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ULTIMATE, RagiumConfig.COMMON.processor.enchanter)
        .addMenu { RagiumMenuTypes.SINGLE_ITEM_WITH_FLUID }
        .build(RagiumCommonTranslation.ENCHANTER)

    @JvmField
    val SIMULATOR: HTMachineBlockType = HTMachineBlockType
        .builder { RagiumBlockEntityTypes.SIMULATOR }
        .add(HTDirectionalBlockAttribute.HORIZONTAL)
        .addProcessorTier(HTMachineTier.ULTIMATE, RagiumConfig.COMMON.processor.simulator)
        .addMenu { RagiumMenuTypes.ITEM_WITH_CATALYST }
        .build(RagiumCommonTranslation.SIMULATOR)

    //    Device    //

    // Basic
    @JvmField
    val FLUID_COLLECTOR: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.FLUID_COLLECTOR }
        .addMenu { RagiumMenuTypes.FLUID_COLLECTOR }
        .addTier(HTMachineTier.BASIC)
        .build(RagiumCommonTranslation.FLUID_COLLECTOR)

    @JvmField
    val ITEM_COLLECTOR: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.ITEM_COLLECTOR }
        .addMenu { RagiumMenuTypes.ITEM_COLLECTOR }
        .addTier(HTMachineTier.BASIC)
        .build(RagiumCommonTranslation.ITEM_COLLECTOR)

    // Elite
    @JvmField
    val DIM_ANCHOR: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.DIM_ANCHOR }
        .addTier(HTMachineTier.ELITE)
        .build(RagiumCommonTranslation.DIM_ANCHOR)

    @JvmField
    val ENI: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.ENI }
        .addMenu { RagiumMenuTypes.ENERGY_NETWORK_ACCESS }
        .addTier(HTMachineTier.ELITE)
        .build(RagiumCommonTranslation.ENI)

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
        .build(RagiumCommonTranslation.CEU)

    //    Storage    //

    @JvmField
    val CRATES: Map<HTCrateTier, HTEntityBlockType> = HTCrateTier.entries.associateWith { tier: HTCrateTier ->
        HTEntityBlockType
            .builder { tier.getBlockEntityType() }
            .addTier(tier)
            .build(RagiumCommonTranslation.CRATE)
    }

    @JvmField
    val OPEN_CRATE: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.OPEN_CRATE }
        .build(RagiumCommonTranslation.OPEN_CRATE)

    @JvmField
    val DRUMS: Map<HTDrumTier, HTEntityBlockType> = HTDrumTier.entries.associateWith { tier: HTDrumTier ->
        HTEntityBlockType
            .builder { tier.getBlockEntityType() }
            .addMenu { RagiumMenuTypes.DRUM }
            .addTier(tier)
            .build(RagiumCommonTranslation.DRUM)
    }

    @JvmField
    val EXP_DRUM: HTEntityBlockType = HTEntityBlockType
        .builder { RagiumBlockEntityTypes.EXP_DRUM }
        .addMenu { RagiumMenuTypes.DRUM }
        .build(RagiumCommonTranslation.EXP_DRUM)
}
