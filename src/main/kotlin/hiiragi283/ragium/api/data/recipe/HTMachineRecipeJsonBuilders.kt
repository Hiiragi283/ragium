package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTFluidIngredient
import hiiragi283.ragium.api.recipe.HTFluidResult
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipeNew
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

// typealias AdvBuilder = Advancement.Builder.() -> Unit

object HTMachineRecipeJsonBuilders {
    @JvmStatic
    fun createAlloy(
        exporter: RecipeExporter,
        first: HTItemIngredient,
        second: HTItemIngredient,
        output: HTItemResult,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
        recipeId: Identifier = createRecipeId(output.entryValue),
    ) {
        createRecipe(
            exporter,
            HTMachineRecipeNew.Simple(
                HTMachineDefinition(RagiumMachineTypes.Processor.ALLOY_FURNACE, tier),
                listOf(first, second),
                listOf(),
                HTItemIngredient.EMPTY_ITEM,
                listOf(output),
                listOf(),
            ),
            recipeId.withPrefixedPath("alloy_furnace/"),
        )
    }

    @JvmStatic
    fun createAssembler(
        exporter: RecipeExporter,
        inputs: List<HTItemIngredient>,
        output: HTItemResult,
        fluidInput: HTFluidIngredient? = null,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
        recipeId: Identifier = createRecipeId(output.entryValue),
    ) {
        createRecipe(
            exporter,
            HTMachineRecipeNew.Simple(
                HTMachineDefinition(RagiumMachineTypes.Processor.ASSEMBLER, tier),
                inputs,
                listOfNotNull(fluidInput),
                HTItemIngredient.EMPTY_ITEM,
                listOf(output),
                listOf(),
            ),
            recipeId.withPrefixedPath("assembler/"),
        )
    }

    @JvmStatic
    fun createBlast(
        exporter: RecipeExporter,
        inputs: List<HTItemIngredient>,
        output: HTItemResult,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
        recipeId: Identifier = createRecipeId(output.entryValue),
    ) {
        createRecipe(
            exporter,
            HTMachineRecipeNew.Large(
                HTMachineDefinition(RagiumMachineTypes.BLAST_FURNACE, tier),
                inputs,
                listOf(),
                HTItemIngredient.EMPTY_ITEM,
                listOf(output),
                listOf(),
            ),
            recipeId.withPrefixedPath("blast_furnace/"),
        )
    }

    @JvmStatic
    fun createChemical(
        exporter: RecipeExporter,
        itemInputs: List<HTItemIngredient>,
        itemOutputs: List<HTItemResult>,
        recipeId: Identifier,
        fluidInput: HTFluidIngredient? = null,
        fluidOutput: HTFluidResult? = null,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
    ) {
        createRecipe(
            exporter,
            HTMachineRecipeNew.Large(
                HTMachineDefinition(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, tier),
                itemInputs,
                listOfNotNull(fluidInput),
                HTItemIngredient.EMPTY_ITEM,
                itemOutputs,
                listOfNotNull(fluidOutput),
            ),
            recipeId.withPrefixedPath("chemical/"),
        )
    }

    @JvmStatic
    fun createDistillation(
        exporter: RecipeExporter,
        input: HTFluidIngredient,
        outputs: List<HTFluidResult>,
        recipeId: Identifier,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
    ) {
        /*createRecipe(
            exporter,
            HTDistillationRecipe(
                tier,
                input,
                outputs,
            ),
            recipeId.withPrefixedPath("distillation/"),
        )*/
    }

    @JvmStatic
    fun createElectrolyzer(
        exporter: RecipeExporter,
        itemInputs: List<HTItemIngredient>,
        fluidInputs: List<HTFluidIngredient>,
        itemOutputs: List<HTItemResult>,
        fluidOutputs: List<HTFluidResult>,
        recipeId: Identifier,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
    ) {
        createRecipe(
            exporter,
            HTMachineRecipeNew.Large(
                HTMachineDefinition(RagiumMachineTypes.Processor.ELECTROLYZER, tier),
                itemInputs,
                fluidInputs,
                HTItemIngredient.EMPTY_ITEM,
                itemOutputs,
                fluidOutputs,
            ),
            recipeId.withPrefixedPath("electrolyzer/"),
        )
    }

    @JvmStatic
    fun createExtractor(
        exporter: RecipeExporter,
        input: HTItemIngredient,
        itemOutputs: List<HTItemResult>,
        fluidOutputs: List<HTFluidResult>,
        recipeId: Identifier,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
    ) {
        createRecipe(
            exporter,
            HTMachineRecipeNew.Simple(
                HTMachineDefinition(RagiumMachineTypes.Processor.EXTRACTOR, tier),
                listOf(input),
                listOf(),
                HTItemIngredient.EMPTY_ITEM,
                itemOutputs,
                fluidOutputs,
            ),
            recipeId.withPrefixedPath("extractor/"),
        )
    }

    @JvmStatic
    fun createGrinder(
        exporter: RecipeExporter,
        input: HTItemIngredient,
        firstOutput: HTItemResult,
        secondOutput: HTItemResult? = null,
        catalyst: HTItemIngredient = HTItemIngredient.EMPTY_ITEM,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
        recipeId: Identifier = createRecipeId(firstOutput.entryValue),
    ) {
        createRecipe(
            exporter,
            HTMachineRecipeNew.Simple(
                HTMachineDefinition(RagiumMachineTypes.Processor.GRINDER, tier),
                listOf(input),
                listOf(),
                catalyst,
                listOfNotNull(firstOutput, secondOutput),
                listOf(),
            ),
            recipeId.withPrefixedPath("grinder/"),
        )
    }

    @JvmStatic
    fun createMetalFormer(
        exporter: RecipeExporter,
        input: HTItemIngredient,
        output: HTItemResult,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
        recipeId: Identifier = createRecipeId(output.entryValue),
    ) {
        createRecipe(
            exporter,
            HTMachineRecipeNew.Simple(
                HTMachineDefinition(RagiumMachineTypes.Processor.METAL_FORMER, tier),
                listOf(input),
                listOf(),
                HTItemIngredient.EMPTY_ITEM,
                listOf(output),
                listOf(),
            ),
            recipeId.withPrefixedPath("metal_former/"),
        )
    }

    @JvmStatic
    fun createRockGen(
        exporter: RecipeExporter,
        output: HTItemResult,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
        recipeId: Identifier = createRecipeId(output.entryValue),
    ) {
        createRecipe(
            exporter,
            HTMachineRecipeNew.Simple(
                HTMachineDefinition(RagiumMachineTypes.Processor.ROCK_GENERATOR, tier),
                listOf(),
                listOf(),
                HTItemIngredient.EMPTY_ITEM,
                listOf(output),
                listOf(),
            ),
            recipeId.withPrefixedPath("rock_gen/"),
        )
    }

    @JvmStatic
    fun createSawMill(
        exporter: RecipeExporter,
        input: HTItemIngredient,
        firstOutput: HTItemResult,
        secondOutput: HTItemResult? = null,
        tier: HTMachineTier = HTMachineTier.PRIMITIVE,
        recipeId: Identifier = createRecipeId(firstOutput.entryValue),
    ) {
        createRecipe(
            exporter,
            HTMachineRecipeNew.Simple(
                HTMachineDefinition(RagiumMachineTypes.SAW_MILL, tier),
                listOf(input),
                listOf(),
                HTItemIngredient.EMPTY_ITEM,
                listOfNotNull(firstOutput, secondOutput),
                listOf(),
            ),
            recipeId.withPrefixedPath("saw_mill/"),
        )
    }

    @JvmStatic
    fun createRecipeId(recipe: HTMachineRecipeNew<*>): Identifier = createRecipeId(recipe.firstOutput.item)

    @JvmStatic
    fun createRecipeId(item: ItemConvertible): Identifier = CraftingRecipeJsonBuilder
        .getItemId(item)
        .path
        .let { RagiumAPI.id(it) }

    @JvmStatic
    fun createRecipeId(fluid: Fluid): Identifier = Registries.FLUID
        .getId(fluid)
        .path
        .let { RagiumAPI.id(it) }

    @JvmStatic
    fun createRecipeId(fluid: RagiumContents.Fluids): Identifier = createRecipeId(fluid.fluidEntry.value())

    @JvmStatic
    fun createRecipe(exporter: RecipeExporter, recipe: HTMachineRecipeNew<*>, recipeId: Identifier = createRecipeId(recipe)) {
        exporter.accept(
            recipeId,
            recipe,
            null,
            /*exporter.advancementBuilder
                .apply(builderAction)
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                .build(recipeId.withPrefixedPath("recipes/misc/")),*/
        )
    }
}
