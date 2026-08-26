package hiiragi283.ragium.data.lang

import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.effect.RagiumMobEffects
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.item.alchemy.RagiumPotions
import net.minecraft.data.PackOutput

class RagiumEnglishLangProvider(output: PackOutput) :
    HTLangProvider(output, RagiumAPI.MOD_ID, HTLangTypes.EN_US),
    RagiumLangProvider {
    override fun addTranslations() {
        addPatternTranslations(this)

        // Block
        add(RagiumBlocks.CRUSHER, "Crusher")
        add(RagiumBlocks.CUTTING_MACHINE, "Cutting Machine")

        add(RagiumBlocks.FREEZER, "Freezer")
        add(RagiumBlocks.MELTER, "Melter")

        add(RagiumBlocks.BREWERY, "Brewery")

        // Fluid
        addFluid(RagiumFluids.HONEY, "Honey")
        add(RagiumFluids.POTION.getFluidType().descriptionId, "Invalid Potion")
        add(RagiumFluids.POTION.bucketHolder, $$"%1$s Bucket")
        addFluid(RagiumFluids.OMINOUS_FLUX, "Ominous Flux")
        addFluid(RagiumFluids.MOLTEN_GLASS, "Molten Glass")
        addFluid(RagiumFluids.MOLTEN_REDSTONE, "Destabilized Redstone")
        addFluid(RagiumFluids.MOLTEN_GLOWSTONE, "Energized Glowstone")
        addFluid(RagiumFluids.MOLTEN_ENDER, "Resonant Ender")
        addFluid(RagiumFluids.MOLTEN_BLAZE, "Blaze Blood")

        addFluid(RagiumFluids.HYDROGEN, "Hydrogen")
        addFluid(RagiumFluids.OXYGEN, "Oxygen")
        addFluid(RagiumFluids.CHLORINE, "Chlorine")

        addFluid(RagiumFluids.CREOSOTE, "Creosote")
        addFluid(RagiumFluids.CRUDE_OIL, "Crude Oil")
        addFluid(RagiumFluids.NAPHTHA, "Naphtha")
        addFluid(RagiumFluids.FUEL, "Fuel")
        addFluid(RagiumFluids.AROMATIC_COMPOUND, "Aromatic Compound")
        addFluid(RagiumFluids.NAOH_SOLUTION, "Sodium Hydroxide Solution")
        addFluid(RagiumFluids.SULFUR_DIOXIDE, "Sulfur Dioxide")
        addFluid(RagiumFluids.SULFUR_TRIOXIDE, "Sulfur Trioxide")
        addFluid(RagiumFluids.SULFURIC_ACID, "Sulfuric Acid")
        addFluid(RagiumFluids.HYDROGEN_CHLORIDE, "Hydrogen Chloride")
        addFluid(RagiumFluids.HYDROCHLORIC_ACID, "Hydrochloric Acid")
        addFluid(RagiumFluids.CAOH_SOLUTION, "Calcium Hydroxide Solution")

        // Item
        add(RagiumItems.TAR, "Tar")
        add(RagiumItems.BAMBOO_CHARCOAL, "Bamboo Charcoal")
        add(RagiumItems.PARTICLE_BOARD, "Particle Board")
        add(RagiumItems.PLASTIC_PLATE, "Plastic Plate")
        add(RagiumItems.SYNTHETIC_FEATHER, "Synthetic Feather")
        add(RagiumItems.SYNTHETIC_FIBER, "Synthetic Fiber")
        add(RagiumItems.SYNTHETIC_LEATHER, "Synthetic Leather")
        add(RagiumItems.ELDER_HEART, "Elder Heart")
        add(RagiumItems.WITHER_DOLL, "Wither Doll")
        add(RagiumItems.WITHER_STAR, "Wither Star")

        add(RagiumItems.MEMORY_DISC, "Memory Disc")

        add(RagiumItems.BLOCK_SHAPE_PATTERN, "Shape Pattern (Block)")
        add(RagiumItems.INGOT_SHAPE_PATTERN, "Shape Pattern (Ingot)")
        add(RagiumItems.BALL_SHAPE_PATTERN, "Shape Pattern (Ball)")

        // Mob Effect
        add(RagiumMobEffects.FROSTBITE, "Frostbite")

        // Potion
        addPotion(RagiumPotions.FROSTBITE, "Frostbite")

        addCustomPotion("hunger", "Hunger")
        addCustomPotion("darkness", "Darkness")
        addCustomPotion("golden_apple", "Golden Apple")
        addCustomPotion("enchanted_golden_apple", "Enchanted Golden Apple")

        // Recipe Type
        add(RagiumRecipeTypes.ASSEMBLING, "Assembling")
        add(RagiumRecipeTypes.COMPRESSING, "Compressing")
        add(RagiumRecipeTypes.CRUSHING, "Crushing")
        add(RagiumRecipeTypes.CUTTING, "Cutting")

        add(RagiumRecipeTypes.FREEZING, "Freezing")
        add(RagiumRecipeTypes.MELTING, "Melting")
        add(RagiumRecipeTypes.PYROLYZING, "Pyrolyzing")
        add(RagiumRecipeTypes.REFINING, "Refining")

        add(RagiumRecipeTypes.BATHING, "Chemical Bathing")
        add(RagiumRecipeTypes.ELECTROLYZING, "Electrolyzing")

        add(RagiumRecipeTypes.BREWING, "Brewing")

        // Text
        add(RagiumTranslation.RAGIUM, "Ragium")

        add(RagiumTranslation.CONFIG_ENERGY_CAPACITY, "Energy Capacity")
        add(RagiumTranslation.CONFIG_ENERGY_RATE, "Energy Rate")

        add(RagiumTranslation.TOOLTIPS_MEMORY_DISC_DATA, $$"Scanned Item: %1$s")
    }
}
