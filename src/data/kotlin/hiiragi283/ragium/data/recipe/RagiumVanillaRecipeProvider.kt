package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTCookingRecipeBuilder
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.HTShapedRecipeBuilder
import hiiragi283.lib.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.lib.data.recipe.HTStonecuttingRecipeBuilder
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.item.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

class RagiumVanillaRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        material()

        // Bamboo Charcoal
        HTCookingRecipeBuilder.smelting {
            ingredient { items { +Items.BAMBOO } }
            result { +RagiumItems.BAMBOO_CHARCOAL }
            exp = 0.5f
        }.save(exporter)
        // Particle Board
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +holderSet(CommonTagPrefixes.DUST, HTMaterial.Other.WOOD) }
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
    }

    //    Material    //

    private fun material() {
        // XX Block -> XX
        setOf(
            HTMaterial.Mineral.GLOWSTONE to Items.GLOWSTONE_DUST,
            HTMaterial.Gem.QUARTZ to Items.QUARTZ_BLOCK,
            HTMaterial.Gem.AMETHYST to Items.AMETHYST_BLOCK,
        ).forEach { (material, item) ->
            HTShapelessRecipeBuilder.create {
                ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, material) }
                result {
                    +item
                    count = 4
                }
                recipeId suffix "_from_block"
            }.save(exporter)
        }

        // Ingot <-> Nugget
        ingotToNugget(HTMaterial.Metal.NETHERITE, ingot = Items.NETHERITE_INGOT)
        ingotToNugget(HTMaterial.Metal.STEEL)
        ingotToNugget(HTMaterial.Metal.RAGI_ALLOY)
        ingotToNugget(HTMaterial.Metal.ADVANCED_RAGI_ALLOY)

        // Alloy Dust
        HTShapelessRecipeBuilder.create {
            repeat(3) { ingredient { +holderSet(CommonTagPrefixes.DUST, HTMaterial.Metal.IRON) } }
            ingredient { +holderSet(CommonTagPrefixes.DUST, HTMaterial.Fuel.COAL_COKE) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, HTMaterial.Metal.STEEL)
                count = 4
            }
        }.save(exporter)
        HTShapelessRecipeBuilder.create {
            ingredient { +holderSet(CommonTagPrefixes.DUST, HTMaterial.Metal.COPPER) }
            repeat(3) { ingredient { +holderSet(CommonTagPrefixes.DUST, HTMaterial.Mineral.RAGINITE) } }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, HTMaterial.Metal.RAGI_ALLOY)
                count = 4
            }
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

        // Dust -> Ingot
        for (metal: HTMaterial.Metal in HTMaterial.Metal.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, metal] ?: continue
            val item: ItemLike = when (metal) {
                HTMaterial.Metal.COPPER -> Items.COPPER_INGOT
                HTMaterial.Metal.IRON -> Items.IRON_INGOT
                HTMaterial.Metal.GOLD -> Items.GOLD_INGOT
                HTMaterial.Metal.NETHERITE -> Items.NETHERITE_INGOT
                else -> RagiumItems.MATERIAL_ITEMS[HTItemPart.INGOT, metal]
            } ?: continue
            HTCookingRecipeBuilder.smeltingAndBlasting {
                ingredient { items { +dust } }
                result { +item.asItem() }
                exp = 0.35f
                recipeId suffix "_from_dust"
            }.forEach { it.save(exporter) }
        }

        // Tiny
        for (fuel: HTMaterial.Fuel in HTMaterial.Fuel.entries) {
            val base: Item = when (fuel) {
                HTMaterial.Fuel.COAL -> Items.COAL
                HTMaterial.Fuel.CHARCOAL -> Items.CHARCOAL
                HTMaterial.Fuel.COAL_COKE -> RagiumItems.COAL_COKE
            }.asItem()

            HTShapelessRecipeBuilder.create {
                ingredient { items { +base } }
                result {
                    +RagiumItems.getOrThrow(HTItemPart.TINY, fuel)
                    count = 8
                }
            }.save(exporter)
            HTShapedRecipeBuilder.create {
                hollow()
                define('A') { +holderSet(CommonTagPrefixes.TINY, fuel) }
                result { +base }
                recipeId suffix "_from_tiny"
            }.save(exporter)
        }
    }

    private fun ingotToNugget(
        material: HTMaterial,
        ingot: ItemLike? = RagiumItems.MATERIAL_ITEMS[HTItemPart.INGOT, material],
        nugget: ItemLike? = RagiumItems.MATERIAL_ITEMS[HTItemPart.NUGGET, material],
    ) {
        if (ingot == null || nugget == null) return
        HTShapelessRecipeBuilder.create {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, material) }
            result {
                +nugget.asItem()
                count = 9
            }
            recipeId suffix "_from_ingot"
        }.save(exporter)
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +holderSet(CommonTagPrefixes.NUGGET, material) }
            define('B') { items { +nugget.asItem() } }
            result { +ingot.asItem() }
            recipeId suffix "_from_nugget"
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
