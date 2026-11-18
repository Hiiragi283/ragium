package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.collection.forEach
import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.HTItemToMultiItemRecipeBuilder
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.recipe.ingredient.HTMaterialTagsIngredient
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.recipe.RTCrushingRecipe
import hiiragi283.ragium.api.recipe.RTSiftingRecipe
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.material.RagiumMaterialHelper
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import java.util.concurrent.CompletableFuture

class RagiumOreProcessingRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    companion object {
        const val PREFIX = "ore_processing/"
    }

    override fun exportValues() {
        crushing()
        alloying()
        sifting()

        // Crushed XX Ore -> XX Ingot
        setOf(
            RagiumMaterial.Metal.COPPER,
            RagiumMaterial.Metal.IRON,
            RagiumMaterial.Metal.GOLD,
        ).forEach { metal: RagiumMaterial.MetalLike ->
            val ingot: HTSimpleDeferredItem = RagiumMaterialHelper.getIngot(metal) ?: return@forEach
            VanillaRecipeBuilders.smeltingAndBlasting(exporter) {
                ingredient { +holderSet(CommonTagPrefixes.CRUSHED_ORES, metal) }
                result { +ingot }
                recipeId suffix "_from_crushed"
            }
        }
    }

    override fun getName(): String = "Ore Processing Recipes"

    private fun oreOrRaw(material: HTMaterialLike): HTMaterialTagsIngredient =
        materialTags(nelOf(CommonTagPrefixes.ORE, CommonTagPrefixes.RAW_MATERIALS), material)

    private fun dustOrThrow(material: RagiumMaterial): HTSimpleDeferredItem = RagiumItems.getOrThrow(HTItemPart.DUST, material)

    private fun smallDustOrThrow(material: RagiumMaterial): HTSimpleDeferredItem = RagiumItems.getOrThrow(HTItemPart.SMALL_DUST, material)

    //    Crushing    //

    private fun crushing() {
        // XX Ore || Raw XX -> 2x Crushed XX + extra
        crushOre(RagiumMaterial.Fuel.COAL)

        crushOre(RagiumMaterial.Gem.LAPIS)
        crushOre(RagiumMaterial.Gem.QUARTZ)
        crushOre(RagiumMaterial.Gem.DIAMOND)
        crushOre(RagiumMaterial.Gem.EMERALD)

        crushOre(RagiumMaterial.Metal.COPPER) { result { +smallDustOrThrow(RagiumMaterial.Metal.GOLD) } }
        crushOre(RagiumMaterial.Metal.IRON) { result { +smallDustOrThrow(RagiumMaterial.Metal.NICKEL) } }
        crushOre(RagiumMaterial.Metal.GOLD) { result { +smallDustOrThrow(RagiumMaterial.Metal.SILVER) } }

        crushOre(RagiumMaterial.Other.REDSTONE) { result { +dustOrThrow(RagiumMaterial.Gem.CINNABAR) } }
        // Crushed XX -> XX Dust
        pulverizeOre(RagiumMaterial.Fuel.COAL)

        pulverizeOre(RagiumMaterial.Gem.LAPIS, 3)
        pulverizeOre(RagiumMaterial.Gem.QUARTZ, 3)
        pulverizeOre(RagiumMaterial.Gem.DIAMOND)
        pulverizeOre(RagiumMaterial.Gem.EMERALD)

        pulverizeOre(RagiumMaterial.Metal.COPPER)
        pulverizeOre(RagiumMaterial.Metal.IRON)
        pulverizeOre(RagiumMaterial.Metal.GOLD)

        pulverizeOre(RagiumMaterial.Other.REDSTONE, 4)
    }

    private inline fun crushOre(material: RagiumMaterial, builderAction: HTItemToMultiItemRecipeBuilder<RTCrushingRecipe>.() -> Unit = {}) {
        val crushedOre: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.CRUSHED_ORES, material] ?: return
        RagiumRecipeBuilders.crushing {
            ingredient { +oreOrRaw(material) }
            result {
                +crushedOre
                count = 2
            }
            builderAction()
            recipeId prefix PREFIX
        }.save(exporter)
    }

    private fun pulverizeOre(material: RagiumMaterial, count: Int = 1) {
        val dust: HTSimpleDeferredItem = when (material) {
            RagiumMaterial.Gem.QUARTZ -> RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, RagiumMaterial.Chemical.SILICA]
            RagiumMaterial.Other.REDSTONE -> HTSimpleDeferredItem(vanillaId("redstone"))
            else -> RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, material]
        } ?: return
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.CRUSHED_ORES, material) }
            result {
                +dust
                this.count = count
            }
            recipeId prefix PREFIX
        }.save(exporter)
    }

    //    Alloying    //

    private fun alloying() {
        // XX Ore || Raw XX + Flux -> 3x XX
        buildTable {
            put(RagiumMaterial.Fuel.COAL, Items.COAL, 1)
            put(RagiumMaterial.Other.REDSTONE, Items.REDSTONE, 4)

            put(RagiumMaterial.Gem.LAPIS, Items.LAPIS_LAZULI, 3)
            put(RagiumMaterial.Gem.QUARTZ, Items.QUARTZ, 3)
            put(RagiumMaterial.Gem.DIAMOND, Items.DIAMOND, 1)
            put(RagiumMaterial.Gem.EMERALD, Items.EMERALD, 1)

            put(RagiumMaterial.Metal.COPPER, Items.COPPER_INGOT, 1)
            put(RagiumMaterial.Metal.IRON, Items.IRON_INGOT, 1)
            put(RagiumMaterial.Metal.GOLD, Items.GOLD_INGOT, 1)
        }.forEach { (material: HTMaterialLike, result: Item, multiplier: Int) ->
            RagiumRecipeBuilders.alloying {
                ingredient { +oreOrRaw(material) }
                ingredient { +holderSet(RagiumTags.Items.SMELTING_FLUXES) }
                result {
                    +result
                    count *= 3 * multiplier
                }
                recipeId prefix PREFIX
            }.save(exporter)
        }
    }

    //    Sifting    //

    private fun sifting() {
        // 3x Crushed XX Ore -> 2x XX + 1x extra
        sifting(RagiumMaterial.Fuel.COAL, Items.COAL) { result { +dustOrThrow(RagiumMaterial.Chemical.SULFUR) } }

        sifting(RagiumMaterial.Gem.LAPIS, Items.LAPIS_LAZULI, 3) { result { +RagiumItems.getOrThrow(HTItemPart.GEM, RagiumMaterial.Gem.APATITE) } }
        sifting(RagiumMaterial.Gem.QUARTZ, Items.QUARTZ, 3) { result { +RagiumItems.getOrThrow(HTItemPart.GEM, RagiumMaterial.Gem.FLUORITE) } }
        sifting(RagiumMaterial.Gem.DIAMOND, Items.DIAMOND) { result { +dustOrThrow(RagiumMaterial.Chemical.CARBON) } }
        sifting(RagiumMaterial.Gem.EMERALD, Items.EMERALD)

        sifting(RagiumMaterial.Other.REDSTONE, Items.REDSTONE, 4) { result { +smallDustOrThrow(RagiumMaterial.Other.RAGINITE) } }
    }

    private inline fun sifting(
        material: RagiumMaterial,
        output: ItemLike,
        multiplier: Int = 1,
        builderAction: HTItemToMultiItemRecipeBuilder<RTSiftingRecipe>.() -> Unit = {},
    ) {
        RagiumRecipeBuilders.sifting {
            ingredient {
                +holderSet(CommonTagPrefixes.CRUSHED_ORES, material)
                count = 3
            }
            result {
                +output.asItem()
                this.count = 2 * multiplier
            }
            builderAction()
            recipeId prefix PREFIX
        }.save(exporter)
    }
}
