package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumRecipeTypes {
    @JvmField
    val REGISTER: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(Registries.RECIPE_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Recipe<*>> register(name: String): DeferredHolder<RecipeType<*>, RecipeType<T>> =
        REGISTER.register(name) { id: ResourceLocation -> RecipeType.simple<T>(id) }

    @JvmField
    val ASSEMBLER: DeferredHolder<RecipeType<*>, RecipeType<HTAssemblerRecipe>> = register("assembler")

    @JvmField
    val BLAST_FURNACE: DeferredHolder<RecipeType<*>, RecipeType<HTBlastFurnaceRecipe>> = register("blast_furnace")

    @JvmField
    val COMPRESSOR: DeferredHolder<RecipeType<*>, RecipeType<HTCompressorRecipe>> = register("compressor")

    @JvmField
    val DISTILLERY: DeferredHolder<RecipeType<*>, RecipeType<HTDistilleryRecipe>> = register("distillery")

    @JvmField
    val ENCHANTER: DeferredHolder<RecipeType<*>, RecipeType<HTEnchanterRecipe>> = register("enchanter")

    @JvmField
    val EXTRACTOR: DeferredHolder<RecipeType<*>, RecipeType<HTExtractorRecipe>> = register("extractor")

    @JvmField
    val GRINDER: DeferredHolder<RecipeType<*>, RecipeType<HTGrinderRecipe>> = register("grinder")

    @JvmField
    val GROWTH_CHAMBER: DeferredHolder<RecipeType<*>, RecipeType<HTGrowthChamberRecipe>> = register("growth_chamber")

    @JvmField
    val INFUSER: DeferredHolder<RecipeType<*>, RecipeType<HTInfuserRecipe>> = register("infuser")

    @JvmField
    val LASER_ASSEMBLY: DeferredHolder<RecipeType<*>, RecipeType<HTLaserAssemblyRecipe>> = register("laser_assembly")

    @JvmField
    val MIXER: DeferredHolder<RecipeType<*>, RecipeType<HTMixerRecipe>> = register("mixer")

    @JvmField
    val REFINERY: DeferredHolder<RecipeType<*>, RecipeType<HTRefineryRecipe>> = register("refinery")
}
