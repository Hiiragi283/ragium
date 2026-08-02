package hiiragi283.ragium.api

data object RagiumConst {
    const val BASIC = "basic"
    const val ADVANCED = "advanced"
    const val ELITE = "elite"
    const val ULTIMATE = "ultimate"

    const val MAX_MATTER_POINT = 1024

    //    Blocks    //

    // Generator - Basic
    const val BOILER = "boiler"

    // Machine - Mechanical
    const val ALLOY_SMELTER = "alloy_smelter"
    const val ASSEMBLER = "assembler"
    const val AUTO_CHISEL = "auto_chisel"
    const val CRUSHER = "crusher"
    const val COMPRESSOR = "compressor"
    const val CUTTING_MACHINE = "cutting_machine"
    const val ELECTRIC_FURNACE = "electric_furnace"

    // Machine - Heat
    const val FREEZER = "freezer"
    const val IMPLOSION_COMPRESSOR = "implosion_compressor"
    const val MELTER = "melter"
    const val PYROLYZER = "pyrolyzer"
    const val REFINERY = "refinery"

    // Machine - Chemical
    const val BREWERY = "brewery"
    const val CHEMICAL_BATH = "chemical_bath"
    const val CHEMICAL_REACTOR = "chemical_reactor"
    const val MIXER = "mixer"
    const val WASHER = "washer"

    // Machine - Bio
    const val PLANTER = "planter"

    // Machine - Electronics

    // Machine - Arcane
    const val ENCHANTER = "enchanter"
    const val FLUID_DUPLICATOR = "fluid_duplicator"
    const val MASS_FABRICATOR = "mass_fabricator"

    // Storage
    const val UNIVERSAL_CHEST = "universal_chest"

    //    Serialization    //

    const val DEVICE = "device"
    const val LOCATION = "location"
    const val MACHINE = "machine"
    const val MAX_PROGRESS = "max_progress"
    const val PROGRESS = "progress"
    const val SLOT_INFO = "slot_info"

    //    Recipes    //

    // Mechanical
    const val ALLOYING = "alloying"
    const val ASSEMBLING = "assembling"
    const val COMPRESSING = "compressing"
    const val CUTTING = "cutting"

    // Heat
    const val FREEZING = "freezing"
    const val IMPLODING = "imploding"
    const val MELTING = "melting"
    const val PYROLYZING = "pyrolyzing"
    const val REFINING = "refining"

    // Chemical
    const val BATHING = "bathing"
    const val CHEMICAL_REACTING = "chemical_reacting"
    const val MIXING = "mixing"
    const val WASHING = "washing"

    // Bio
    const val PLANTING = "planting"

    // Electronics
    const val PRINTING = "printing"

    // Arcane
    const val ENCHANTING = "enchanting"
    const val MASS_FABRICATING = "mass_fabricating"
}
