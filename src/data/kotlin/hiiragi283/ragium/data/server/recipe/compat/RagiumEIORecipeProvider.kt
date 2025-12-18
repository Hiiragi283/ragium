package hiiragi283.ragium.data.server.recipe.compat

import com.enderio.base.api.soul.binding.ingredients.AnySoulBindableIngredient
import com.enderio.machines.common.blocks.alloy.AlloySmeltingRecipe
import com.enderio.machines.common.blocks.sag_mill.SagMillingRecipe
import com.enderio.machines.common.blocks.soul_binder.SoulBindingRecipe
import com.enderio.machines.common.blocks.vat.FermentingRecipe
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.VanillaMaterialRecipeData
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import java.util.Optional

object RagiumEIORecipeProvider : HTRecipeProvider.Integration(RagiumConst.EIO_MACHINES) {
    override fun buildRecipeInternal() {
        // Soul Binding for Imitation Spawner
        save(
            RagiumAPI.id("soulbinding/imitation_spawner"),
            SoulBindingRecipe(
                RagiumBlocks.IMITATION_SPAWNER.toStack(),
                AnySoulBindableIngredient.of(RagiumBlocks.IMITATION_SPAWNER),
                288000,
                8,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                false,
            ),
        )

        alloys()
        sagMill()
        fermenting()
    }

    //    Alloy    //

    private fun alloys() {
        alloyFromData(RagiumMaterialRecipeData.RAGI_ALLOY, 4800)
        alloyFromData(RagiumMaterialRecipeData.ADVANCED_RAGI_ALLOY, 5600)

        alloyFromData(RagiumMaterialRecipeData.AZURE_SHARD, 3200)
        alloyFromData(RagiumMaterialRecipeData.AZURE_STEEL, 4800)

        alloyFromData(RagiumMaterialRecipeData.DEEP_STEEL, 5600)

        alloyFromData(RagiumMaterialRecipeData.NIGHT_METAL, 4800)
        alloyFromData(RagiumMaterialRecipeData.NIGHT_METAL_ALT, 4800)

        alloyFromData(RagiumMaterialRecipeData.RUBBER_SHEET, 4800)
    }

    @JvmStatic
    private fun alloyFromData(data: HTRecipeData, energy: Int, exp: Float = 0.3f) {
        save(
            data.getModifiedId().withPrefix("${RagiumConst.ALLOYING}/"),
            AlloySmeltingRecipe(
                data.getSizedItemIngredients(),
                data.getItemStacks()[0].first,
                energy,
                exp,
            ),
        )
    }

    //    Sag Mill    //

    @JvmStatic
    private fun sagMill() {
        // Vanilla
        sagMillFromData(VanillaMaterialRecipeData.AMETHYST_DUST)
        sagMillFromData(VanillaMaterialRecipeData.ECHO_DUST)
        // Ragium
        sagMillFromData(RagiumMaterialRecipeData.RAGINITE_ORE)

        sagMillFromData(RagiumMaterialRecipeData.AZURE_DUST)

        sagMillFromData(RagiumMaterialRecipeData.RAGI_CRYSTAL_ORE)
        sagMillFromData(RagiumMaterialRecipeData.CRIMSON_ORE)
        sagMillFromData(RagiumMaterialRecipeData.WARPED_ORE)
    }

    @JvmStatic
    private fun sagMillFromData(data: HTRecipeData, energy: Int = 2400) {
        save(
            data.getModifiedId().withPrefix("sag_milling/"),
            SagMillingRecipe(
                data.getIngredients()[0],
                data.itemOutputs.map { (entry: Ior<Item, TagKey<Item>>, amount: Int, chance: Float) ->
                    entry.map(
                        { item: Item -> SagMillingRecipe.OutputItem.of(item, amount, chance, false) },
                        { tagKey: TagKey<Item> -> SagMillingRecipe.OutputItem.of(tagKey, amount, chance, false) },
                    )
                },
                energy,
                SagMillingRecipe.BonusType.MULTIPLY_OUTPUT,
            ),
        )
    }

    //    Vat    //

    @JvmStatic
    private fun fermenting() {
        fermentFromData(RagiumMaterialRecipeData.ELDRITCH_FLUX_CRIMSON)
        fermentFromData(RagiumMaterialRecipeData.ELDRITCH_FLUX_WARPED)
    }

    @JvmStatic
    private fun fermentFromData(data: HTRecipeData, ticks: Int = 200) {
        save(
            data.getModifiedId().withPrefix("fermenting/"),
            FermentingRecipe(
                data.getSizedFluidIngredients()[0],
                data.itemInputs[0]
                    .entry
                    .right()
                    .orElse(listOf())[0],
                data.itemInputs[1]
                    .entry
                    .right()
                    .orElse(listOf())[0],
                data.getFluidStacks()[0],
                ticks,
            ),
        )
    }
}
