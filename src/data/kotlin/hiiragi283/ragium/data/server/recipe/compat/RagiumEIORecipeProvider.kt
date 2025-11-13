package hiiragi283.ragium.data.server.recipe.compat

import com.enderio.machines.common.blocks.alloy.AlloySmeltingRecipe
import com.enderio.machines.common.blocks.sag_mill.SagMillingRecipe
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.VanillaMaterialRecipeData
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe

object RagiumEIORecipeProvider : HTRecipeProvider.Integration(RagiumConst.EIO_MACHINES) {
    override fun buildRecipeInternal() {
        alloys()
        sagMill()
    }

    //    Alloy    //

    private fun alloys() {
        alloyFromData(RagiumMaterialRecipeData.RAGI_ALLOY, 4800).save(output)
        alloyFromData(RagiumMaterialRecipeData.ADVANCED_RAGI_ALLOY, 5600).save(output)

        alloyFromData(RagiumMaterialRecipeData.AZURE_STEEL, 4800).save(output)

        alloyFromData(RagiumMaterialRecipeData.DEEP_STEEL, 5600).save(output)

        alloyFromData(RagiumMaterialRecipeData.ELDRITCH_PEARL, 5600).save(output)
        alloyFromData(RagiumMaterialRecipeData.ELDRITCH_PEARL_BULK, 5600).saveSuffixed(output, "_alt")

        alloyFromData(RagiumMaterialRecipeData.NIGHT_METAL, 4800).save(output)
        alloyFromData(RagiumMaterialRecipeData.IRIDESCENTIUM, 6400).save(output)
    }

    @JvmStatic
    private fun alloyFromData(data: HTRecipeData, energy: Int, exp: Float = 0.3f): EIORecipeBuilder<*> = EIORecipeBuilder(
        RagiumConst.ALLOYING,
        AlloySmeltingRecipe(
            data.getSizedIngredients(),
            data.getOutputStacks()[0],
            energy,
            exp,
        ),
    ) { recipe: AlloySmeltingRecipe -> recipe.output().toImmutableOrThrow().getId() }

    //    Sag Mill    //

    @JvmStatic
    private fun sagMill() {
        // Vanilla
        sagMillFromData(VanillaMaterialRecipeData.AMETHYST_DUST)
        sagMillFromData(VanillaMaterialRecipeData.ECHO_DUST)

        sagMillFromData(VanillaMaterialRecipeData.BLACKSTONE_DUST)
        // Ragium
        sagMillFromData(RagiumMaterialRecipeData.RAGINITE_ORE)

        sagMillFromData(RagiumMaterialRecipeData.RAGI_CRYSTAL_ORE)
        sagMillFromData(RagiumMaterialRecipeData.CRIMSON_ORE)
        sagMillFromData(RagiumMaterialRecipeData.WARPED_ORE)
    }

    @JvmStatic
    private fun sagMillFromData(data: HTRecipeData, energy: Int = 2400) {
        EIORecipeBuilder(
            "sag_milling",
            SagMillingRecipe(
                data.getIngredient(0),
                data.getOutputs(
                    { tagKey: TagKey<Item>, count: Int, chance: Float -> SagMillingRecipe.OutputItem.of(tagKey, count, chance, false) },
                    { item: Item, count: Int, chance: Float -> SagMillingRecipe.OutputItem.of(item, count, chance, false) },
                ),
                energy,
                SagMillingRecipe.BonusType.MULTIPLY_OUTPUT,
            ),
        ) { recipe: SagMillingRecipe ->
            recipe.outputs()[0].output.map(
                { stack: ItemStack -> stack.toImmutableOrThrow().getId() },
                { tagOutput: SagMillingRecipe.OutputItem.SizedTagOutput ->
                    tagOutput.itemTag.location
                },
            )
        }.save(output)
    }

    //    Extensions    //

    private class EIORecipeBuilder<RECIPE : Recipe<*>>(
        prefix: String,
        private val recipe: RECIPE,
        private val factory: (RECIPE) -> ResourceLocation,
    ) : HTRecipeBuilder.Prefixed(prefix) {
        override fun createRecipe(): RECIPE = recipe

        override fun getPrimalId(): ResourceLocation = recipe.let(factory)
    }
}
