package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.material.RagiumMaterialHelper
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumChemicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        autoclaving()
        bathing()

        element()
    }

    override fun getName(): String = "Chemical Recipes"

    private fun autoclaving() {
        val steamSet: HolderSet<Fluid> = holderSet(RagiumFluids.STEAM)
        // XX Dust -> XX Gem
        for (gem: RagiumMaterial.Gem in RagiumMaterial.Gem.entries) {
            val item: HTSimpleDeferredItem = RagiumMaterialHelper.getGem(gem) ?: continue
            RagiumRecipeBuilders.autoclaving {
                itemIngredient { +holderSet(CommonTagPrefixes.DUST, gem) }
                fluidIngredient { +steamSet }
                result { +item }
                condition { itemTagPresent(CommonTagPrefixes.DUST, gem) }
                recipeId suffix "_from_dust"
            }.save(exporter)
        }
        // Silica + Water -> Quartz
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA) }
            fluidIngredient { +steamSet }
            result { +Items.QUARTZ }
            recipeId suffix "_from_silica"
        }.save(exporter)
        // Silica + Purple Dye -> Amethyst
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA) }
            fluidIngredient { +waterSet() }
            result { +Items.AMETHYST_SHARD }
            recipeId suffix "_from_silica"
        }.save(exporter)

        // Amethyst -> Amethyst Bud -> Amethyst Cluster
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.AMETHYST) }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result { +Items.SMALL_AMETHYST_BUD }
        }.save(exporter)
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { items { +Items.SMALL_AMETHYST_BUD } }
            fluidIngredient {
                +steamSet
                amount /= 4
            }
            result { +Items.MEDIUM_AMETHYST_BUD }
        }.save(exporter)
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { items { +Items.MEDIUM_AMETHYST_BUD } }
            fluidIngredient {
                +steamSet
                amount /= 4
            }
            result { +Items.LARGE_AMETHYST_BUD }
        }.save(exporter)
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { items { +Items.LARGE_AMETHYST_BUD } }
            fluidIngredient {
                +steamSet
                amount /= 4
            }
            result { +Items.AMETHYST_CLUSTER }
        }.save(exporter)

        // Molten Glass + Niter -> Ghast Tear
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.NITER) }
            fluidIngredient { +holderSet(RagiumFluids.MOLTEN_GLASS) }
            result { +Items.GHAST_TEAR }
        }.save(exporter)
    }

    private fun bathing() {
        // Wood Pulp + Glue -> Particle Board
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.WOOD) }
            fluidIngredient {
                +holderSet(RagiumFluids.GLUE)
                amount /= 8
            }
            result { +RagiumItems.PARTICLE_BOARD }
        }.save(exporter)
        // Piston + Glue -> Sticky Piston
        RagiumRecipeBuilders.bathing {
            itemIngredient { items { +Items.PISTON } }
            fluidIngredient {
                +holderSet(RagiumFluids.GLUE)
                amount /= 8
            }
            result { +Items.STICKY_PISTON }
        }.save(exporter)

        // Poison + Spider Eye -> Fermented Spider Eye
        RagiumRecipeBuilders.bathing {
            itemIngredient { items { +Items.SPIDER_EYE } }
            fluidIngredient {
                +HTPotionFluidIngredient(Potions.POISON)
                amount = 250
            }
            result { +Items.FERMENTED_SPIDER_EYE }
        }.save(exporter)
        // Poison + Potato -> Poisonous Potato
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(Tags.Items.CROPS_POTATO) }
            fluidIngredient {
                +HTPotionFluidIngredient(Potions.POISON)
                amount = 250
            }
            result { +Items.POISONOUS_POTATO }
        }.save(exporter)

        // Gravel + Water -> Flint
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(Tags.Items.GRAVELS) }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result { +Items.FLINT }
            time /= 4
        }.save(exporter)
    }

    //    Element    //

    private fun element() {
        nitrogen()
        explosive()
        fluorine()

        alkali()
        // aluminum()
        silica()
        phosphor()
        sulfur()
        chlorine()

        calcium()

        // Oxygen
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.WIND_CHARGE } }
            result {
                +RagiumFluids.OXYGEN
                amount /= 4
            }
            recipeId suffix "_from_wind_charge"
        }.save(exporter)
    }

    private fun saltAndSolution(fluid: HTFluidContent, item: HTSimpleDeferredItem) {
        // Salt + Water -> Salt (aq)
        RagiumRecipeBuilders.mixing {
            itemIngredient { items { +item } }
            fluidIngredient { +waterSet() }
            result { +fluid }
            recipeId replace fluid.id().withSuffix("_from_salt")
        }.save(exporter)
        // Salt (aq) -> Salt + Water
        RagiumRecipeBuilders.drying {
            ingredient { +holderSet(fluid) }
            fluidResult { water() }
            itemResult { +item }
            recipeId replace item.id().withSuffix("_from_solution")
        }.save(exporter)
    }

    /*private fun saltAndSolution(fluid: HTFluidContent, material: RagiumMaterial) {
        // Salt + Water -> Salt (aq)
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, material) }
            fluidIngredient { +waterSet() }
            result { +fluid }
            recipeId replace fluid.id().withSuffix("_from_salt")
        }.save(exporter)
        // Salt (aq) -> Salt + Water
        val item: HTSimpleDeferredItem = RagiumItems.getOrThrow(HTItemPart.DUST, material)
        RagiumRecipeBuilders.drying {
            ingredient { +holderSet(fluid) }
            fluidResult { water() }
            itemResult { +item }
            recipeId replace item.id().withSuffix("_from_solution")
        }.save(exporter)
    }*/

    private fun nitrogen() {
        // Gunpowder -> KNO3
        RagiumRecipeBuilders.extracting {
            ingredient {
                +holderSet(Tags.Items.GUNPOWDERS)
                count = 3
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.NITER) }
            recipeId suffix "_from_gunpowder"
        }.save(exporter)
        // Gunpowder -> C + S + KNO3
        RagiumRecipeBuilders.centrifuging {
            ingredient {
                +holderSet(Tags.Items.GUNPOWDERS)
                count = 3
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.CARBON) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.SULFUR) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.NITER) }
            recipeId suffix "_from_gunpowder"
        }.save(exporter)
        // H2SO4 + 2x KNO3 -> 2x HNO3 + K2SO4
        RagiumRecipeBuilders.itemReacting {
            itemIngredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.NITER)
                count = 2
            }
            fluidIngredient { +holderSet(RagiumFluids.SULFURIC_ACID) }
            itemResult { +RagiumItems.ALKALI_SULFATE }
            fluidResult {
                +RagiumFluids.NITRIC_ACID
                amount *= 2
            }
            recipeId suffix "_from_niter"
        }.save(exporter)
    }

    private fun explosive() {
        // Aromatic Compound + HNO3 -> Liquid Explosive
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.AROMATIC_COMPOUND) }
            ingredient { +holderSet(RagiumFluids.NITRIC_ACID) }
            fluidResult { +RagiumFluids.LIQUID_EXPLOSIVE }
        }.save(exporter)

        // Sand + Liquid Explosive -> TNT
        RagiumRecipeBuilders.bathing {
            itemIngredient {
                +holderSet(Tags.Items.SANDS)
                count = 8
            }
            fluidIngredient { +holderSet(RagiumFluids.LIQUID_EXPLOSIVE) }
            result {
                +Items.TNT
                count = 8
            }
        }.save(exporter)
        // Clay + Liquid Explosive -> Plastic Explosive TODO
    }

    private fun fluorine() {
        // CaF2 + H2SO4 -> 2x HF + CaSO4
        RagiumRecipeBuilders.itemReacting {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Gem.FLUORITE) }
            fluidIngredient { +holderSet(RagiumFluids.SULFURIC_ACID) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.GYPSUM) }
            fluidResult {
                +RagiumFluids.HYDROGEN_FLUORIDE
                amount *= 2
            }
            recipeId suffix "_from_fluorite"
        }.save(exporter)
        // HF + H2O -> HF(aq)
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.HYDROGEN_FLUORIDE) }
            ingredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROFLUORIC_ACID }
        }.save(exporter)
    }

    private fun alkali() {
        // NaOH (aq)
        saltAndSolution(RagiumFluids.HYDROXIDE_SOLUTION, RagiumItems.ALKALI_HYDROXIDE)
        // Na2SO4 (aq)
        saltAndSolution(RagiumFluids.SULFATE_SOLUTION, RagiumItems.ALKALI_SULFATE)

        // Dried Kelp -> Na2CO3
        VanillaRecipeBuilders.smeltingAndBlasting(exporter) {
            ingredient { items { +Items.DRIED_KELP } }
            result { +RagiumItems.ALKALI_CARBONATE }
            exp = 0.15f
        }
        // 2x NaOH (aq) + CO2 -> Na2CO3 + H2O
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.HYDROXIDE_SOLUTION) }
            ingredient { +holderSet(RagiumFluids.CARBON_DIOXIDE) }
            itemResult { +RagiumItems.ALKALI_CARBONATE }
            fluidResult { water() }
            recipeId replace RagiumItems.ALKALI_CARBONATE.id
        }.save(exporter)
        // SiO2 + Na2CO3 -> Glass
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA)
                count = 2
            }
            ingredient { items { +RagiumItems.ALKALI_CARBONATE } }
            result {
                +Items.GLASS
                count = 6
            }
        }.save(exporter)

        // KOH (aq)
        RagiumRecipeBuilders.mixing {
            itemIngredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.ASH)
                count = 4
            }
            fluidIngredient { +waterSet() }
            result { +RagiumFluids.HYDROXIDE_SOLUTION }
            recipeId suffix "_from_ash"
        }.save(exporter)
        // KOH + Plant Oil -> Soap
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.ASH) }
            fluidIngredient {
                +holderSet(RagiumFluids.PLANT_OIL)
                amount /= 4
            }
            result { +RagiumItems.SOAP }
        }.save(exporter)
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.HYDROXIDE_SOLUTION) }
            ingredient { +holderSet(RagiumFluids.PLANT_OIL) }
            itemResult {
                +RagiumItems.SOAP
                count = 4
            }
        }.save(exporter)

        // Wood Pulp + Alkali aq -> Paper Pulp + Black Liquor
        RagiumRecipeBuilders.itemReacting {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.WOOD) }
            fluidIngredient {
                +holderSet(setOf(RagiumFluids.HYDROXIDE_SOLUTION, RagiumFluids.SULFATE_SOLUTION))
                amount = 250
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.PAPER) }
            fluidResult {
                +RagiumFluids.BLACK_LIQUOR
                amount = 250
            }
            recipeId suffix "_from_wood"
        }.save(exporter)
        // Paper Pulp + Water -> Paper
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.PAPER) }
            fluidIngredient {
                +waterSet()
                amount = 250
            }
            result { +Items.PAPER }
            recipeId suffix "_from_pulp"
        }.save(exporter)
    }

    /*private fun aluminum() {
        // Bauxite + NaOH aq -> Al(OH)3 (aq)
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.BAUXITE) }
            fluidIngredient {
                +holderSet(RagiumFluids.NAOH_SOLUTION)
                amount /= 2
            }
            result { +RagiumFluids.ALUMINA_SOLUTION }
        }.save(exporter)
        // Al(OH)3 (aq) -> Al2O3 + Water
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.ALUMINA_SOLUTION) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.ALUMINA) }
            fluidResult { water() }
            recipeId replace RagiumFluids.ALUMINA_SOLUTION.id()
        }.save(exporter)
        // Al(OH)3 (aq) + HF (aq) -> AlF3 (aq)
        RagiumRecipeBuilders.mixing {
            primaryIngredient { +holderSet(RagiumFluids.ALUMINA_SOLUTION) }
            secondaryIngredient {
                +holderSet(RagiumFluids.HYDROFLUORIC_ACID)
                amount *= 3
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.ALUMINUM_FLUORIDE) }
            fluidResult { water() }
        }.save(exporter)
        // 2x AlF3 + 3x Na2CO3 -> Na3AlF6 + 3x CO2
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.ALUMINUM_FLUORIDE)
                count = 2
            }
            extra {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.SODIUM_CARBONATE)
                count = 3
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.CRYOLITE) }
        }.save(exporter)
        // Al2O3 + Cokes + Na3AlF6 -> Al
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.ALUMINA) }
            extra { +holderSet(RagiumTags.Items.COKES) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Metal.ALUMINUM)
                count = 2
            }
            time *= 4
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.ALUMINA) }
            extra { +holderSet(RagiumTags.Items.COKES) }
            extra { +dustOrGem(RagiumMaterial.Gem.CRYOLITE) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Metal.ALUMINUM)
                count = 3
            }
            recipeId suffix "_with_cryolite"
        }.save(exporter)
        // Al + Cu -> Alclad Plate
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.ALUMINUM)
                count = 3
            }
            extra { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.COPPER) }
            result {
                +RagiumItems.ALCLAD_PLATE
                count = 4
            }
        }.save(exporter)
    }*/

    private fun silica() {
        // Glass -> Molten Glass
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.GLASS_BLOCKS_CHEAP) }
            result { +RagiumFluids.MOLTEN_GLASS }
            recipeId suffix "_from_block"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.GLASS_PANES) }
            result {
                +RagiumFluids.MOLTEN_GLASS
                amount = 375
            }
            recipeId suffix "_from_pane"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient {
                +holderSet(Tags.Items.GLASS_PANES)
                count = 8
            }
            result {
                +RagiumFluids.MOLTEN_GLASS
                amount *= 3
            }
            time *= 8
            recipeId suffix "_from_panes"
        }.save(exporter)
        // Molten Glass -> Glass
        RagiumRecipeBuilders.freezing {
            ingredient { +holderSet(RagiumFluids.MOLTEN_GLASS) }
            catalyst { items { +RagiumItems.BLOCK_MOLD } }
            result { +Items.GLASS }
        }.save(exporter)
        RagiumRecipeBuilders.freezing {
            ingredient {
                +holderSet(RagiumFluids.MOLTEN_GLASS)
                amount = 375
            }
            catalyst { items { +RagiumItems.PLATE_MOLD } }
            result { +Items.GLASS_PANE }
        }.save(exporter)
        // SiO2 + Sand -> Quartz Glass
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA) }
            ingredient { +holderSet(Tags.Items.SANDS) }
            result {
                +RagiumBlocks.QUARTZ_GLASS
                count = 2
            }
        }.save(exporter)
    }

    private fun phosphor() {
        // Ca3(PO4)2 + 3x H2SO4 -> H3PO4 + 3x CaSO4
        RagiumRecipeBuilders.itemReacting {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Gem.APATITE) }
            fluidIngredient {
                +holderSet(RagiumFluids.SULFURIC_ACID)
                amount *= 3
            }
            itemResult {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.GYPSUM)
                count = 3
            }
            fluidResult { +RagiumFluids.PHOSPHORIC_ACID }
            recipeId suffix "_from_apatite"
        }.save(exporter)
        // H3PO4 + Ca(OH2) aq -> Bone
        RagiumRecipeBuilders.fluidReacting {
            ingredient {
                +holderSet(RagiumFluids.PHOSPHORIC_ACID)
                amount = 600
            }
            ingredient { +holderSet(RagiumFluids.HYDROXIDE_SOLUTION) }
            itemResult {
                +Items.BONE
                count = 12
            }
            recipeId replace id("hydroxyapatite")
        }.save(exporter)
    }

    private fun sulfur() {
        // Netherrack -> S
        RagiumRecipeBuilders.extracting {
            ingredient {
                +holderSet(Tags.Items.NETHERRACKS)
                count = 8
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.SULFUR) }
            recipeId suffix "_from_netherrack"
        }.save(exporter)

        // S + O2 -> SO2
        RagiumRecipeBuilders.itemReacting {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SULFUR) }
            fluidIngredient { +holderSet(RagiumFluids.OXYGEN) }
            fluidResult { +RagiumFluids.SULFUR_DIOXIDE }
        }.save(exporter)
        // 2x H2S + 3x O2 -> 2x SO2 + 2x H2O
        RagiumRecipeBuilders.fluidReacting {
            ingredient {
                +holderSet(RagiumFluids.HYDROGEN_SULFIDE)
                amount *= 2
            }
            ingredient {
                +holderSet(RagiumFluids.OXYGEN)
                amount *= 3
            }
            fluidResult {
                +RagiumFluids.SULFUR_DIOXIDE
                amount *= 2
            }
            fluidResult {
                water()
                amount *= 2
            }
            recipeId suffix "_from_hydrogen_sulfide"
        }.save(exporter)

        // SO2 + 1/2 O2 -> SO3
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.SULFUR_DIOXIDE) }
            ingredient {
                +holderSet(RagiumFluids.OXYGEN)
                amount /= 2
            }
            fluidResult { +RagiumFluids.SULFUR_TRIOXIDE }
        }.save(exporter)
        // Blaze Powder -> SO3
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +Items.BLAZE_POWDER } }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.ASH) }
            fluidResult { +RagiumFluids.SULFUR_TRIOXIDE }
        }.save(exporter)
        // SO3 + H2O -> H2SO4
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.SULFUR_TRIOXIDE) }
            ingredient { +waterSet() }
            fluidResult { +RagiumFluids.SULFURIC_ACID }
        }.save(exporter)
    }

    private fun chlorine() {
        // 2x NaCl + H2SO4 -> 2x HCl + Na2SO4
        RagiumRecipeBuilders.itemReacting {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SALT) }
            fluidIngredient {
                +holderSet(RagiumFluids.SULFURIC_ACID)
                amount /= 2
            }
            itemResult { +RagiumItems.ALKALI_SULFATE }
            fluidResult { +RagiumFluids.HYDROGEN_CHLORIDE }
            recipeId suffix "_from_salt"
        }.save(exporter)

        // H2 + Cl2 -> 2x HCl
        /*RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.HYDROGEN) }
            ingredient { +holderSet(RagiumFluids.CHLORINE) }
            fluidResult {
                +RagiumFluids.HYDROGEN_CHLORIDE
                amount *= 2
            }
        }*/
        // HCl + H2O -> HCl(aq)
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.HYDROGEN_CHLORIDE) }
            ingredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROCHLORIC_ACID }
        }.save(exporter)
    }

    private fun calcium() {
        // CaCO3 -> CaO + CO2
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.LIME) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.QUICK_LIME) }
            fluidResult { +RagiumFluids.CARBON_DIOXIDE }
            recipeId suffix "_from_lime"
        }.save(exporter)
        // CaO + H2O -> Ca(OH)2
        RagiumRecipeBuilders.itemReacting {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.QUICK_LIME) }
            fluidIngredient { +waterSet() }
            fluidResult { +RagiumFluids.LIMEWATER }
        }.save(exporter)
        // Ca(OH)2 + CO2 -> CaCO3 + H2O
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.LIMEWATER) }
            ingredient { +holderSet(RagiumFluids.CARBON_DIOXIDE) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.LIME) }
            fluidResult { water() }
            recipeId replace id("lime_dust")
        }.save(exporter)

        // (Na,K)2SO4 + Ca(OH)2 -> CaSO4 + 2x KOH
        RagiumRecipeBuilders.itemReacting {
            itemIngredient { items { +RagiumItems.ALKALI_SULFATE } }
            fluidIngredient { +holderSet(RagiumFluids.LIMEWATER) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.GYPSUM) }
            fluidResult {
                +RagiumFluids.HYDROXIDE_SOLUTION
                amount *= 2
            }
            recipeId suffix "_from_limewater"
        }.save(exporter)
        // CaSO4 -> CaO + SO3
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.GYPSUM) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.QUICK_LIME) }
            fluidResult { +RagiumFluids.SULFUR_TRIOXIDE }
            time *= 4
            recipeId suffix "_from_gypsum"
        }.save(exporter)
    }
}
