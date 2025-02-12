package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.integration.jei.entry.HTGeneratorFuelEntry
import hiiragi283.ragium.integration.jei.entry.HTSoapEntry
import hiiragi283.ragium.integration.jei.entry.HTStirlingFuelEntry
import mezz.jei.api.recipe.RecipeType

object RagiumJEIRecipeTypes {
    @JvmField
    val MATERIAL_INFO: RecipeType<HTTypedMaterial> =
        RecipeType.create(RagiumAPI.MOD_ID, "material_info", HTTypedMaterial::class.java)

    @JvmField
    val GENERATOR: RecipeType<HTGeneratorFuelEntry> =
        RecipeType.create(RagiumAPI.MOD_ID, "generator", HTGeneratorFuelEntry::class.java)

    @JvmField
    val SOAP: RecipeType<HTSoapEntry> =
        RecipeType.create(RagiumAPI.MOD_ID, "soap", HTSoapEntry::class.java)

    //    Machine Recipe    //

    @JvmField
    val ASSEMBLER: RecipeType<HTAssemblerRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "assembler", HTAssemblerRecipe::class.java)

    @JvmField
    val BLAST_FURNACE: RecipeType<HTBlastFurnaceRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "blast_furnace", HTBlastFurnaceRecipe::class.java)

    @JvmField
    val COMPRESSOR: RecipeType<HTCompressorRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "compressor", HTCompressorRecipe::class.java)

    @JvmField
    val ENCHANTER: RecipeType<HTEnchanterRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "enchanter", HTEnchanterRecipe::class.java)

    @JvmField
    val EXTRACTOR: RecipeType<HTExtractorRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "extractor", HTExtractorRecipe::class.java)

    @JvmField
    val GRINDER: RecipeType<HTGrinderRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "grinder", HTGrinderRecipe::class.java)

    @JvmField
    val GROWTH_CHAMBER: RecipeType<HTGrowthChamberRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "growth_chamber", HTGrowthChamberRecipe::class.java)

    @JvmField
    val INFUSER: RecipeType<HTInfuserRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "infuser", HTInfuserRecipe::class.java)

    @JvmField
    val LASER_ASSEMBLY: RecipeType<HTLaserAssemblyRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "laser_assembly", HTLaserAssemblyRecipe::class.java)

    @JvmField
    val MIXER: RecipeType<HTMixerRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "mixer", HTMixerRecipe::class.java)

    @JvmField
    val REFINERY: RecipeType<HTRefineryRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "refinery", HTRefineryRecipe::class.java)

    @JvmField
    val STIRLING: RecipeType<HTStirlingFuelEntry> =
        RecipeType.create(RagiumAPI.MOD_ID, "stirling", HTStirlingFuelEntry::class.java)
}
