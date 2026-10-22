package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.RagiumMaterial
import net.minecraft.world.item.enchantment.Enchantments

data object RagiumArcaneRecipeProvider : HTRecipeProvider(RagiumAPI.MOD_ID) {
    override fun register() {
        enchanting()
    }

    private fun enchanting() {
        // Iron -> Protection
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
                count = 64
            }
            result { +holder(Enchantments.PROTECTION) }
        }.save(context)
        // Diamond -> Protection + Unbreaking
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.DIAMOND)
                count = 16
            }
            result {
                enchantedBook {
                    set(holder(Enchantments.PROTECTION), 4)
                    set(holder(Enchantments.UNBREAKING), 3)
                }
            }
            recipeId replace id("diamond_armor")
        }.save(context)
    }
}
