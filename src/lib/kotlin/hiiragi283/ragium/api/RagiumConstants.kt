package hiiragi283.ragium.api

/**
 * Ragiumで使用される定数をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object RagiumConstants {
    //    Blocks    //

    // Mechanical
    const val ASSEMBLER = "assembler"
    const val COMPRESSOR = "compressor"
    const val CRUSHER = "crusher"
    const val CUTTING_MACHINE = "cutting_machine"

    // Heat
    const val FREEZER = "freezer"
    const val MELTER = "melter"
    const val PYROLYZER = "pyrolyzer"
    const val REFINERY = "refinery"
    const val SMELTER = "smelter"

    // Chemical
    const val CHEMICAL_BATH = "chemical_bath"
    const val CHEMICAL_REACTOR = "chemical_reactor"
    const val ELECTROLYZER = "electrolyzer"
    const val MIXER = "mixer"

    // Bio
    const val BREWERY = "brewery"
    const val PLANTER = "planter"

    // Electronics
    const val SCANNER = "scanner"

    // Arcane
    const val FLUID_DUPLICATOR = "fluid_duplicator"
    const val MASS_FABRICATOR = "mass_fabricator"

    //    Recipes    //

    // Mechanical
    const val ASSEMBLING = "assembling"
    const val COMPRESSING = "compressing"
    const val CRUSHING = "crushing"
    const val CUTTING = "cutting"
    const val DRAINING = "draining"
    const val FILLING = "filling"

    // Heat
    const val FREEZING = "freezing"
    const val MELTING = "melting"
    const val PYROLYZING = "pyrolyzing"
    const val REFINING = "refining"

    // Chemical
    const val BATHING = "bathing"
    const val ELECTROLYZING = "electrolyzing"
    const val MIXING = "mixing"
    const val REACTING = "reacting"

    // Bio
    const val BREWING = "brewing"
    const val PLANTING = "planting"

    // Electronics
    const val PRINTING = "printing"

    // Arcane
    const val ENCHANTING = "enchanting"
}
