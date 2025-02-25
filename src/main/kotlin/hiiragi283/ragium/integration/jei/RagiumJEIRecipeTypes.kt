package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.integration.jei.entry.HTGeneratorFuelEntry
import hiiragi283.ragium.integration.jei.entry.HTSoapEntry
import hiiragi283.ragium.integration.jei.entry.HTStirlingFuelEntry
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType as MCRecipeType

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

    @JvmField
    val STIRLING: RecipeType<HTStirlingFuelEntry> =
        RecipeType.create(RagiumAPI.MOD_ID, "stirling", HTStirlingFuelEntry::class.java)

    //    Machine Recipe    //

    @JvmStatic
    private fun <R : HTMachineRecipeBase> create(recipeType: MCRecipeType<R>): RecipeType<RecipeHolder<R>> =
        RecipeType.createFromVanilla(recipeType)

    @JvmField
    val ASSEMBLER: RecipeType<RecipeHolder<HTAssemblerRecipe>> = create(HTRecipeTypes.ASSEMBLER)

    @JvmField
    val BLAST_FURNACE: RecipeType<RecipeHolder<HTBlastFurnaceRecipe>> = create(HTRecipeTypes.BLAST_FURNACE)

    @JvmField
    val BREWERY: RecipeType<RecipeHolder<HTBreweryRecipe>> = create(HTRecipeTypes.BREWERY)

    @JvmField
    val COMPRESSOR: RecipeType<RecipeHolder<HTCompressorRecipe>> = create(HTRecipeTypes.COMPRESSOR)

    @JvmField
    val CRUSHER: RecipeType<RecipeHolder<HTCrusherRecipe>> = create(HTRecipeTypes.CRUSHER)

    @JvmField
    val ENCHANTER: RecipeType<RecipeHolder<HTEnchanterRecipe>> = create(HTRecipeTypes.ENCHANTER)

    @JvmField
    val EXTRACTOR: RecipeType<RecipeHolder<HTExtractorRecipe>> = create(HTRecipeTypes.EXTRACTOR)

    @JvmField
    val GRINDER: RecipeType<RecipeHolder<HTGrinderRecipe>> = create(HTRecipeTypes.GRINDER)

    @JvmField
    val GROWTH_CHAMBER: RecipeType<RecipeHolder<HTGrowthChamberRecipe>> = create(HTRecipeTypes.GROWTH_CHAMBER)

    @JvmField
    val INFUSER: RecipeType<RecipeHolder<HTInfuserRecipe>> = create(HTRecipeTypes.INFUSER)

    @JvmField
    val LASER_ASSEMBLY: RecipeType<RecipeHolder<HTLaserAssemblyRecipe>> = create(HTRecipeTypes.LASER_ASSEMBLY)

    @JvmField
    val MIXER: RecipeType<RecipeHolder<HTMixerRecipe>> = create(HTRecipeTypes.MIXER)

    @JvmField
    val REFINERY: RecipeType<RecipeHolder<HTRefineryRecipe>> = create(HTRecipeTypes.REFINERY)

    @JvmField
    val SOLIDIFIER: RecipeType<RecipeHolder<HTSolidifierRecipe>> = create(HTRecipeTypes.SOLIDIFIER)
}
