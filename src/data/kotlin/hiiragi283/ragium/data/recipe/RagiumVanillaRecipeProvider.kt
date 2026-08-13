package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.HTShapedRecipeBuilder
import hiiragi283.lib.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.item.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

class RagiumVanillaRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        // Netherite Ingot <-> Nugget
        HTShapelessRecipeBuilder.create {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, HTMaterial.Metal.NETHERITE) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.NUGGET, HTMaterial.Metal.NETHERITE)
                count = 9
            }
            recipeId suffix "_from_ingot"
        }.save(exporter)
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +holderSet(CommonTagPrefixes.NUGGET, HTMaterial.Metal.NETHERITE) }
            define('B') { items { +RagiumItems.getOrThrow(HTItemPart.NUGGET, HTMaterial.Metal.NETHERITE) } }
            result { +Items.NETHERITE_INGOT }
            recipeId suffix "_from_nugget"
        }.save(exporter)

        // Gear
        HTShapedRecipeBuilder.create {
            hollow4()
            define('A') { +holderSet(ItemTags.PLANKS) }
            define('B') { +holderSet(ItemTags.WOODEN_BUTTONS) }
            result { +RagiumItems.getOrThrow(HTItemPart.GEAR, HTMaterial.Other.WOOD) }
        }.save(exporter)

        gear(CommonTagPrefixes.GEM, HTMaterial.Gem.DIAMOND)
        gear(CommonTagPrefixes.GEM, HTMaterial.Gem.EMERALD)
        gear(CommonTagPrefixes.INGOT, HTMaterial.Metal.COPPER)
        gear(CommonTagPrefixes.INGOT, HTMaterial.Metal.IRON)
        gear(CommonTagPrefixes.INGOT, HTMaterial.Metal.GOLD)

        netheriteUpgrade {
            base { +holderSet(CommonTagPrefixes.GEAR, HTMaterial.Gem.DIAMOND) }
            result { +RagiumItems.getOrThrow(HTItemPart.GEAR, HTMaterial.Metal.NETHERITE) }
        }.save(exporter)
    }

    private fun gear(basePrefix: HTTagPrefix, material: HTMaterial) {
        HTShapedRecipeBuilder.create {
            hollow4()
            define('A') { +holderSet(basePrefix, material) }
            define('B') { +holderSet(CommonTagPrefixes.GEAR, HTMaterial.Other.WOOD) }
            result { +RagiumItems.getOrThrow(HTItemPart.GEAR, material) }
        }.save(exporter)
    }

    override fun getName(): String = "Vanilla Recipes"
}
