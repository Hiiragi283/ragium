package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.color.VanillaColoredCollections
import hiiragi283.core.api.data.recipe.FluidIngredientBuilder
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.HTTankInteractionRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTFreezingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class RagiumFluidRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        refining()
        mixing()
        washing()

        tankInteraction()
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

    //    Mixing    //

    private fun mixing() {
        // Diamond + Raginite -> Ragi-Crystal
        HTMixingRecipeBuilder.create {
            itemIngredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.DIAMOND) }
            fluidIngredient {
                +RagiumFluids.MOLTEN_RAGINITE
                amount = HTConst.INGOT_AMOUNT * 6
            }
            +HTItemResult.MaterialPart(CommonParts.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
        }.save(exporter)
        // Liquid Dyes
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DYES.asSequenceWithColor()) {
            // Dye + Water -> Liquid Dye
            HTMixingRecipeBuilder.create {
                itemIngredient { +color.dyesTag }
                fluidIngredient {
                    water()
                    amount = 250
                }
                fluidResult {
                    +content
                    amount = 250
                }
            }.save(exporter)
            // Liquid Dye -> Dye
            HTFreezingRecipeBuilder.create {
                ingredient {
                    +content
                    amount = 250
                }
                catalyst { +HTBluePrintIngredient(0) }
                result { +VanillaColoredCollections.DYE[color] }
            }.save(exporter)
            // Gravel + Sand + Liquid Dye -> Concrete
            HTMixingRecipeBuilder.create {
                itemIngredient {
                    +Tags.Items.GRAVELS
                    count = 4
                }
                itemIngredient {
                    +Tags.Items.SANDS
                    count = 4
                }
                fluidIngredient {
                    +content
                    amount = 250
                }
                itemResult {
                    +VanillaColoredCollections.CONCRETE[color]
                    count = 8
                }
            }.save(exporter)
            // Powder + Water -> Concrete
            HTMixingRecipeBuilder.create {
                itemIngredient { +VanillaColoredCollections.CONCRETE_POWDER[color] }
                fluidIngredient {
                    water()
                    amount = 125
                }
                itemResult { +VanillaColoredCollections.CONCRETE[color] }
                time /= 8
                recipeId suffix "_by_water"
            }.save(exporter)
        }
    }

    //    Washing    //

    private fun washing() {
        // Gravel + Water -> Flint
        RagiumRecipeBuilder.washing {
            ingredient { +Tags.Items.GRAVELS }
            result { +Items.FLINT }
            result {
                +Items.FLINT
                chance = fraction(1, 3)
            }
            time = 20 * 5
        }.save(exporter)
        // Sand -> Quartz Dust + Borax Dust
        RagiumRecipeBuilder.washing {
            ingredient { +Tags.Items.SANDS }
            result {
                +HTItemResult.MaterialPart(CommonParts.DUST, VanillaMaterialKeys.QUARTZ)
                chance = fraction(1, 2)
            }
            result {
                +HTItemResult.MaterialPart(CommonParts.DUST, RagiumMaterialKeys.BORAX)
                chance = fraction(1, 4)
            }
        }.save(exporter)
        // Ash + Water -> Carbon
        RagiumRecipeBuilder.washing {
            ingredient {
                +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.ASH)
                count = 4
            }
            +HTItemResult.MaterialPart(CommonParts.DUST, CommonMaterialKeys.CARBON, 3)
            +HTItemResult.MaterialPart(CommonParts.DUST, CommonMaterialKeys.CARBON).withChance(fraction(1, 4))
            time = 20 * 5
        }.save(exporter)
    }

    //    Tank Interaction    //

    private fun tankInteraction() {
        // Glass Bottle + Mercury -> Thermometer
        HTTankInteractionRecipeBuilder.filling {
            itemIngredient { +Items.GLASS_BOTTLE }
            fluidIngredient {
                +RagiumFluids.MERCURY
                amount = 250
            }
            result { +RagiumItems.THERMOMETER }
        }.save(exporter)
    }

    override fun getName(): String = "Fluid Recipes"
}
