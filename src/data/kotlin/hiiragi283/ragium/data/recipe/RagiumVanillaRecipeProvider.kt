package hiiragi283.ragium.data.recipe

import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.data.recipe.HTCookingRecipeBuilder
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.HTShapedRecipeBuilder
import hiiragi283.lib.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.lib.data.recipe.HTStonecuttingRecipeBuilder
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class RagiumVanillaRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        machine()

        // Gunpowder
        HTShapelessRecipeBuilder.create {
            ingredient { +holderSet(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterialKeys.SULFUR) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterialKeys.NITER) }
            result {
                +Items.GUNPOWDER
                count = 3
            }
        }.save(exporter)

        // Bamboo Charcoal
        HTCookingRecipeBuilder.smelting {
            ingredient { items { +Items.BAMBOO } }
            result { +RagiumItems.BAMBOO_CHARCOAL }
            exp = 0.5f
        }.save(exporter)
        // Particle Board
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +holderSet(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD) }
            define('B') { +holderSet(RagiumTags.Items.STICKY_BALLS) }
            result {
                +RagiumItems.PARTICLE_BOARD
                count = 4
            }
        }.save(exporter)
        // Synthetic
        for (item: HTSimpleDeferredItem in listOf(RagiumItems.SYNTHETIC_FEATHER, RagiumItems.SYNTHETIC_FIBER, RagiumItems.SYNTHETIC_LEATHER)) {
            HTStonecuttingRecipeBuilder.create {
                ingredient { +holderSet(RagiumTags.Items.PLASTICS) }
                result { +item }
            }.save(exporter)
        }
        // XX Shape Pattern
        for (item: HTSimpleDeferredItem in RagiumItems.SHAPE_PATTERNS) {
            HTStonecuttingRecipeBuilder.create {
                ingredient { +holderSet(RagiumTags.Items.SHAPE_PATTERNS) }
                result { +item }
            }.save(exporter)
        }

        // XX Dye Bucket
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            HTShapelessRecipeBuilder.create {
                ingredient { +holderSet(Tags.Items.BUCKETS_WATER) }
                repeat(4) { ingredient { +holderSet(color.dyesTag) } }
                result { +RagiumFluids.DYES[color].bucketHolder }
            }.save(exporter)
        }

        // Alloy Dust
        /*useItem(CommonParts.DUST, null) {
            HTShapelessRecipeBuilder.create {
                repeat(3) { ingredient { +holderSet(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON) } }
                ingredient { +holderSet(CommonTagPrefixes.DUST, CommonMaterials.COAL_COKE) }
                result {
                    +it.item
                    count = 4
                }
            }.save(exporter)
        }*/
    }

    //    Machine    //

    private fun machine() {
        // Mechanical
        // Heat
        // Chemical
        // Bio
        // Electronics
        // Arcane
    }

    override fun getName(): String = "Vanilla Recipes"
}
