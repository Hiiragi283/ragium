package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.RagiumMaterial
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumArcaneRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        enchanting()
        transforming()
    }

    private fun enchanting() {
        // Iron -> Protection
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.PROTECTION) }
        }.save(exporter)
        // Diamond -> Protection + Unbreaking
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.DIAMOND)
                count = 16
            }
            result {
                enchantedBook {
                    set(registries.getOrThrow(Enchantments.PROTECTION), 4)
                    set(registries.getOrThrow(Enchantments.UNBREAKING), 3)
                }
            }
            recipeId replace id("diamond_armor")
        }.save(exporter)
    }

    private fun transforming() {
        // Honey Bottle -> Exp Bottle
        RagiumRecipeBuilders.transforming {
            ingredient { +holderSet(Tags.Items.DRINKS_HONEY) }
            result { +Items.EXPERIENCE_BOTTLE }
            energy = 15 * 20 * 10
        }.save(exporter)
    }

    override fun getName(): String = "Arcane Recipes"
}
