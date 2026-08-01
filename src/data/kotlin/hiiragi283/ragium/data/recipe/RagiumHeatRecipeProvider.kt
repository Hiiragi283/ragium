package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.FluidIngredientBuilder
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTCombiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class RagiumHeatRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        alloying()
        imploding()
        melting()
        refining()
    }

    override fun getName(): String = "Heat Recipes"

    //    Alloying    //

    private fun alloying() {
        // Netherite
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.INGOT, VanillaMaterialKeys.NETHERITE, 2)
            ingredient {
                +baseOrDust(VanillaMaterialKeys.GOLD)
                count = 4
            }
            ingredient {
                +Items.NETHERITE_SCRAP
                count = 4
            }
        }.save(exporter)

        // Steel from Coal
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.INGOT, CommonMaterialKeys.STEEL)
            ingredient { +baseOrDust(VanillaMaterialKeys.IRON) }
            ingredient {
                +setOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL).flatMap(::baseOrDust)
                count = 2
            }
            recipeId suffix "_from_coal"
        }.save(exporter)
        // Steel from Coke
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.INGOT, CommonMaterialKeys.STEEL)
            ingredient { +baseOrDust(VanillaMaterialKeys.IRON) }
            ingredient { +baseOrDust(CommonMaterialKeys.COAL_COKE) }
            recipeId suffix "_from_coke"
        }.save(exporter)
        // Invar
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.INGOT, CommonMaterialKeys.INVAR, 3)
            ingredient {
                +baseOrDust(VanillaMaterialKeys.IRON)
                count = 2
            }
            ingredient { +baseOrDust(CommonMaterialKeys.NICKEL) }
            condition { +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.INVAR) }
        }.save(exporter)
        // Electrum
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.INGOT, CommonMaterialKeys.ELECTRUM, 2)
            ingredient { +baseOrDust(VanillaMaterialKeys.GOLD) }
            ingredient { +baseOrDust(CommonMaterialKeys.SILVER) }
            condition { +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.ELECTRUM) }
        }.save(exporter)
        // Bronze
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.INGOT, CommonMaterialKeys.BRONZE, 4)
            ingredient {
                +baseOrDust(VanillaMaterialKeys.COPPER)
                count = 3
            }
            ingredient { +baseOrDust(CommonMaterialKeys.TIN) }
        }.save(exporter)
        // Brass
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.INGOT, CommonMaterialKeys.BRASS, 2)
            ingredient { +baseOrDust(VanillaMaterialKeys.COPPER) }
            ingredient { +baseOrDust(CommonMaterialKeys.ZINC) }
        }.save(exporter)
        // Constantan
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.INGOT, CommonMaterialKeys.CONSTANTAN, 2)
            ingredient { +baseOrDust(VanillaMaterialKeys.COPPER) }
            ingredient { +baseOrDust(CommonMaterialKeys.NICKEL) }
            condition { +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.CONSTANTAN) }
        }.save(exporter)

        // Amethyst + Lapis -> Azure Shard
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.GEM, HCMaterialKeys.AZURE, 2)
            ingredient { +baseOrDust(VanillaMaterialKeys.AMETHYST) }
            ingredient { +baseOrDust(VanillaMaterialKeys.LAPIS) }
        }.save(exporter)
        // Azure Shard + Iron -> Azure Steel
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.INGOT, HCMaterialKeys.AZURE_STEEL)
            ingredient { +baseOrDust(VanillaMaterialKeys.IRON) }
            ingredient {
                +baseOrDust(HCMaterialKeys.AZURE)
                count = 2
            }
        }.save(exporter)

        // Raginite + Copper -> Ragi-Alloy
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            ingredient { +baseOrDust(VanillaMaterialKeys.COPPER) }
            ingredient {
                +baseOrDust(RagiumMaterialKeys.RAGINITE)
                count = 2
            }
        }.save(exporter)
        // Ragi-Alloy + Glowstone -> Adv Ragi-Alloy
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
            ingredient { +baseOrDust(RagiumMaterialKeys.RAGI_ALLOY) }
            ingredient {
                +baseOrDust(VanillaMaterialKeys.GLOWSTONE)
                count = 2
            }
        }.save(exporter)
        // Raginite + Diamond -> Ragi-Crystal
        HTCombiningRecipeBuilder.alloying {
            +HTItemResult.MaterialPart(CommonParts.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            ingredient { +baseOrDust(VanillaMaterialKeys.DIAMOND) }
            ingredient {
                +baseOrDust(RagiumMaterialKeys.RAGINITE)
                count = 8
            }
        }.save(exporter)
    }

    //    Imploding    //

    private fun imploding() {
        // Coal -> Diamond
        val coals: Set<HTMaterialKey> = setOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL)
        RagiumRecipeBuilder.imploding {
            ingredient {
                +coals.flatMap(::baseOrDust)
                count = 64
            }
            +HTItemResult.MaterialPart(CommonParts.GEM, VanillaMaterialKeys.DIAMOND)
            recipeId suffix "_from_coals"
        }.save(exporter)
        RagiumRecipeBuilder.imploding {
            ingredient {
                +coals.map(CommonTagPrefixes.STORAGE_BLOCK::itemTagKey)
                count = 7
            }
            +HTItemResult.MaterialPart(CommonParts.GEM, VanillaMaterialKeys.DIAMOND)
            recipeId suffix "_from_coal_blocks"
        }.save(exporter)
        // Coal Coke -> Diamond
        RagiumRecipeBuilder.imploding {
            ingredient {
                +baseOrDust(CommonMaterialKeys.COAL_COKE)
                count = 32
            }
            +HTItemResult.MaterialPart(CommonParts.GEM, VanillaMaterialKeys.DIAMOND)
            recipeId suffix "_from_coal_coke"
        }.save(exporter)
        // Carbon -> Diamond
        RagiumRecipeBuilder.imploding {
            ingredient {
                +baseOrDust(CommonMaterialKeys.CARBON)
                count = 16
            }
            +HTItemResult.MaterialPart(CommonParts.GEM, VanillaMaterialKeys.DIAMOND)
            recipeId suffix "_from_carbon"
        }.save(exporter)
    }

    //    Melting    //

    private fun melting() {
        // Water
        RagiumRecipeBuilder.melting {
            ingredient { +Items.SNOW_BLOCK }
            result {
                water()
                amount = 1000
            }
            time = 20 * 5
            recipeId suffix "_from_snow_block"
        }.save(exporter)
        RagiumRecipeBuilder.melting {
            ingredient { +Items.SNOWBALL }
            result {
                water()
                amount = 250
            }
            time = 20
            recipeId suffix "_from_snowball"
        }.save(exporter)
        // Lava
        RagiumRecipeBuilder.melting {
            ingredient { +setOf(Tags.Items.COBBLESTONES, Tags.Items.STONES) }
            result {
                lava()
                amount = 125
            }
            time = 20 * 30
            recipeId suffix "_from_stones"
        }.save(exporter)
        RagiumRecipeBuilder.melting {
            ingredient { +Tags.Items.NETHERRACKS }
            result {
                lava()
                amount = 125
            }
            recipeId suffix "_from_netherrack"
        }.save(exporter)
        RagiumRecipeBuilder.melting {
            ingredient { +Items.MAGMA_BLOCK }
            result {
                lava()
                amount = 250
            }
            recipeId suffix "_from_magma"
        }.save(exporter)
        // Honey
        RagiumRecipeBuilder.melting {
            ingredient { +Items.HONEY_BLOCK }
            result { +HCFluids.HONEY }
            recipeId suffix "_from_block"
        }.save(exporter)

        // Meat
        RagiumRecipeBuilder.melting {
            ingredient { +setOf(Tags.Items.FOODS_RAW_MEAT, Tags.Items.FOODS_RAW_FISH) }
            result {
                +HCFluids.MEAT
                amount = HTConst.INGOT_AMOUNT * 2
            }
        }.save(exporter)
        RagiumRecipeBuilder.melting {
            ingredient { +Items.ROTTEN_FLESH }
            result {
                +HCFluids.MEAT
                amount = HTConst.INGOT_AMOUNT
            }
            recipeId suffix "_from_rotten"
        }.save(exporter)
        // Glass
        RagiumRecipeBuilder.melting {
            ingredient { +Tags.Items.GLASS_PANES }
            result {
                +HCFluids.MOLTEN_GLASS
                amount = 375
            }
            recipeId suffix "_from_pane"
        }.save(exporter)

        // Cinnabar -> Mercury
        HTCombiningRecipeBuilder.alloying {
            result { +RagiumFluids.MERCURY.bucketHolder }
            ingredient {
                +baseOrDust(CommonMaterialKeys.CINNABAR)
                count = 8
            }
            ingredient { +Tags.Items.BUCKETS_EMPTY }
        }.save(exporter)
        RagiumRecipeBuilder.melting {
            ingredient { +baseOrDust(CommonMaterialKeys.CINNABAR) }
            result {
                +RagiumFluids.MERCURY
                amount = 125
            }
            recipeId suffix "_from_cinnabar"
        }.save(exporter)
    }

    //    Refining    //

    private fun refining() {
        waterRefining()
        expRefining()
        eldritchRefining()
    }

    private fun waterRefining() {
        // Cobblestone -> Mossy
        HTMixingRecipeBuilder.create {
            itemIngredient { +Tags.Items.COBBLESTONES_NORMAL }
            fluidIngredient {
                water()
                amount = 250
            }
            itemResult { +Items.MOSSY_COBBLESTONE }
            time /= 2
        }.save(exporter)
        // XX Concrete Powder -> XX Concrete
        // Dirt + Water -> Mud
        HTMixingRecipeBuilder.create {
            itemIngredient { +Items.DIRT }
            fluidIngredient {
                water()
                amount = 250
            }
            itemResult { +Items.MUD }
            time /= 2
        }.save(exporter)
        // XX Dead Coral -> XX Coral
        // Sponge -> Wet Sponge
        HTMixingRecipeBuilder.create {
            itemIngredient { +Items.SPONGE }
            fluidIngredient { water() }
            itemResult { +Items.WET_SPONGE }
            time /= 2
        }.save(exporter)

        // Sawdust -> Paper
        HTMixingRecipeBuilder.create {
            itemIngredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD) }
            fluidIngredient {
                water()
                amount = 125
            }
            itemResult { +Items.PAPER }
            time /= 2
        }.save(exporter)
    }

    private fun expRefining() {
        // Quartz Block -> Ghast Tear
        RagiumRecipeBuilder.bathing {
            itemIngredient { +tag(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ) }
            fluidIngredient {
                +HCFluids.EXPERIENCE
                amount = 500
            }
            result { +Items.GHAST_TEAR }
            recipeId suffix "_from_quartz"
        }.save(exporter)
        // Sulfur Dust -> Blaze Powder
        RagiumRecipeBuilder.bathing {
            itemIngredient { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR) }
            fluidIngredient {
                +HCFluids.EXPERIENCE
                amount = 250
            }
            result { +Items.BLAZE_POWDER }
            recipeId suffix "_from_sulfur"
        }.save(exporter)
        // Leather -> Phantom Membrane
        RagiumRecipeBuilder.bathing {
            itemIngredient { +Tags.Items.LEATHERS }
            fluidIngredient {
                +HCFluids.EXPERIENCE
                amount = 250
            }
            result { +Items.PHANTOM_MEMBRANE }
            recipeId suffix "_from_leather"
        }.save(exporter)
        // Snowball -> Wind Charge
        RagiumRecipeBuilder.bathing {
            itemIngredient { +Items.SNOWBALL }
            fluidIngredient {
                +HCFluids.EXPERIENCE
                amount = 250
            }
            result { +Items.WIND_CHARGE }
            recipeId suffix "_from_snowball"
        }.save(exporter)
    }

    private fun eldritchRefining() {
        fun eldritch(multiplier: Int): FluidIngredientBuilder.() -> Unit = {
            +HiiragiCoreTags.Fluids.ELDRITCH
            amount = HTConst.INGOT_AMOUNT * multiplier
        }
        // Obsidian -> Crying Obsidian
        RagiumRecipeBuilder.bathing {
            itemIngredient { +Tags.Items.OBSIDIANS_NORMAL }
            fluidIngredient(eldritch(1))
            result { +Items.CRYING_OBSIDIAN }
        }.save(exporter)
        // Amethyst Block -> Budding Amethyst
        RagiumRecipeBuilder.bathing {
            itemIngredient { +tag(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST) }
            fluidIngredient(eldritch(9))
            result { +Items.BUDDING_AMETHYST }
        }.save(exporter)
        // Skeleton Skull -> Wither Skeleton Skull
        RagiumRecipeBuilder.bathing {
            itemIngredient { +Items.SKELETON_SKULL }
            fluidIngredient(eldritch(1))
            result { +Items.WITHER_SKELETON_SKULL }
        }.save(exporter)

        // Trial Key -> Ominous Key
        RagiumRecipeBuilder.bathing {
            itemIngredient { +Items.TRIAL_KEY }
            fluidIngredient(eldritch(4))
            result { +Items.OMINOUS_TRIAL_KEY }
        }.save(exporter)

        // Wither Doll -> Wither Star
        RagiumRecipeBuilder.bathing {
            itemIngredient { +HCItems.WITHER_DOLL }
            fluidIngredient(eldritch(4))
            result { +HCItems.WITHER_STAR }
        }.save(exporter)
    }
}
