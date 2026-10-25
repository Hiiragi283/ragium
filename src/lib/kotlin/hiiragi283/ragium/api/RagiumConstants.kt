package hiiragi283.ragium.api

/**
 * Ragiumで使用される定数をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object RagiumConstants {
    //    Blocks    //

    const val DYNAMO = "dynamo"
    const val MACHINE = "machine"

    // Mechanical
    const val FUEL_DYNAMO = "fuel_$DYNAMO"

    const val ASSEMBLER = "assembler"
    const val CENTRIFUGE = "centrifuge"
    const val COMPRESSOR = "compressor"
    const val CRUSHER = "crusher"
    const val CUTTING_MACHINE = "cutting_machine"
    const val PRESS_MACHINE = "press_machine"

    // Heat
    const val MAGMATIC_DYNAMO = "magmatic_$DYNAMO"

    const val ALLOY_SMELTER = "alloy_smelter"
    const val FREEZER = "freezer"
    const val MELTER = "melter"
    const val PYROLYZER = "pyrolyzer"
    const val REFINERY = "refinery"
    const val SMELTER = "smelter"

    // Chemical
    const val CHEMICAL_BATH = "chemical_bath"
    const val CHEMICAL_REACTOR = "chemical_reactor"
    const val MIXER = "mixer"
    const val WASHER = "washer"

    // Resource
    const val BREWERY = "brewery"
    const val PLANTER = "planter"

    const val RESOURCE_MINER = "resource_miner"
    const val RESOURCE_PUMP = "resource_pump"

    // Electronics
    const val BLENDER = "blender"
    const val ELECTROLYZER = "electrolyzer"
    const val ENGRAVER = "engraver"
    const val SCANNER = "scanner"

    // Arcane
    const val ENCHANTER = "enchanter"
    const val FLUID_DUPLICATOR = "fluid_duplicator"
    const val MASS_FABRICATOR = "mass_fabricator"

    //    Recipes    //

    // Mechanical
    const val ASSEMBLING = "assembling"
    const val CENTRIFUGING = "centrifuging"
    const val COMPRESSING = "compressing"
    const val CRUSHING = "crushing"
    const val CUTTING = "cutting"
    const val FILLING = "filling"
    const val PRESSING = "pressing"

    // Heat
    const val ALLOYING = "alloying"
    const val FREEZING = "freezing"
    const val MELTING = "melting"
    const val PYROLYZING = "pyrolyzing"
    const val REFINING = "refining"

    // Chemical
    const val BATHING = "bathing"
    const val EXCHANGING = "exchanging"
    const val MIXING = "mixing"
    const val REACTING = "reacting"
    const val WASHING = "washing"

    // Resource
    const val BREWING = "brewing"
    const val PLANTING = "planting"

    const val RESOURCE_MINING = "resource/mining"
    const val RESOURCE_PUMPING = "resource/pumping"

    // Electronics
    const val ELECTROLYZING = "electrolyzing"
    const val ENGRAVING = "engraving"
    const val PRINTING = "printing"

    // Arcane
    const val ENCHANTING = "enchanting"
}
