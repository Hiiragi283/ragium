package hiiragi283.ragium.data.server.integration

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item

object HTMDRecipeProvider : HTRecipeProvider.Modded("moderndynamics") {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        fun getCable(name: String): Holder.Reference<Item> = lookup.getOrThrow(ResourceKey.create(Registries.ITEM, id(name)))

        fun registerCable(name: String, key: HTMaterialKey) {
            HTShapedRecipeBuilder(getCable(name).value(), 3)
                .pattern(
                    "AB",
                    "B ",
                ).define('A', HTTagPrefix.INGOT, key)
                .define('B', ItemTags.WOOL_CARPETS)
                .save(output)
        }

        registerCable("lv_cable", CommonMaterials.TIN)
        registerCable("mv_cable", VanillaMaterials.GOLD)
        registerCable("hv_cable", CommonMaterials.ALUMINUM)
        // EV Cable
        registerCable("superconductor_cable", RagiumMaterials.ECHORIUM)
    }
}
