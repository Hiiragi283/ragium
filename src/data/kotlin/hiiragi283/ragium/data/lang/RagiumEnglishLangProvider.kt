package hiiragi283.ragium.data.lang

import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.chemical.RagiumChemicals
import hiiragi283.ragium.api.data.element.RagiumElements
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.advancment.RagiumAdvancementKeys
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

        // Advancement
        addAdvancement(RagiumAdvancementKeys.ROOT, "Ragium", "Welcome to Ragium!")
        addAdvancement(RagiumAdvancementKeys.SOOTY_IRON, "Acquire Blackware", "Craft a Sooty Iron Ingot")

        addAdvancement(
            RagiumAdvancementKeys.MECHANICAL_MACHINE_PARTS,
            "Mechanical Machines",
            "Craft a Machine Casing (Mechanical)"
        )
        addAdvancement(RagiumAdvancementKeys.ASSEMBLER, "Rava(n)gers, assemble!", "Acquire Assembler")
        addAdvancement(RagiumAdvancementKeys.CRUSHER, "Macerator, Pulverizer, or Crusher?", "Acquire Crusher")

        addAdvancement(RagiumAdvancementKeys.HEAT_MACHINE_PARTS, "Heat And Cool", "Craft a Machine Casing (Heat)")
        addAdvancement(RagiumAdvancementKeys.BLACK_STEEL, "Black Roaring", "Craft a Black Steel Ingot")
        addAdvancement(RagiumAdvancementKeys.FREEZER, "My power is 530,000.", "Acquire Freezer")
        addAdvancement(RagiumAdvancementKeys.MELTER, "(S)melter(y)", "Acquire Melter")
        // Block
        add(RagiumBlocks.QUARTZ_GLASS, "Quartz Glass")
        add(RagiumBlocks.QUARTZ_GLASS_PANE, "Quartz Glass Pane")

        add(RagiumBlocks.ASSEMBLER, "Assembler")
        add(RagiumBlocks.CRUSHER, "Crusher")
        add(RagiumBlocks.COMPRESSOR, "Compressor")
        add(RagiumBlocks.CUTTING_MACHINE, "Cutting Machine")

        add(RagiumBlocks.ALLOY_SMELTER, "Alloy Smelter")
        add(RagiumBlocks.FREEZER, "Freezer")
        add(RagiumBlocks.MELTER, "Melter")
        add(RagiumBlocks.PYROLYZER, "Pyrolyzer")
        add(RagiumBlocks.REFINERY, "Refinery")
        add(RagiumBlocks.SMELTER, "Smelter")

        add(RagiumBlocks.CHEMICAL_BATH, "Chemical Bath")
        add(RagiumBlocks.CHEMICAL_REACTOR, "Chemical Reactor")
        add(RagiumBlocks.MIXER, "Mixer")

        add(RagiumBlocks.BREWERY, "Brewery")
        add(RagiumBlocks.PLANTER, "Planter")

        add(RagiumBlocks.ELECTROLYZER, "Electrolyzer")
        add(RagiumBlocks.PRECISION_ASSEMBLER, "Precision Assembler")
        add(RagiumBlocks.SCANNER, "Laser Scanner")

        add(RagiumBlocks.ENCHANTER, "Enchanter")

        add(RagiumBlocks.FLUID_OUTPUT_BUS, "Fluid Output Bus")

        add(RagiumBlocks.TANK, "Variable Tank")
        add(RagiumBlocks.POTION_TANK, "Potion Tank")
        add(RagiumBlocks.VOID_TANK, "Void Tank")
        add(RagiumBlocks.CREATIVE_BATTERY, "Creative Battery")
        add(RagiumBlocks.CREATIVE_TANK, "Creative Tank")

        add(RagiumBlocks.MACHINE_CASING, "Machine Casing")
        // Damage Type
        add("death.attack.chemicalBurn", $$"%1$s was injured chemical burn")
        add("death.attack.chemicalBurn.player", $$"%1$s was injured chemical burn while fighting %2$s")
        // Fluid
        addFluid(RagiumFluids.HONEY, "Honey")
        addFluid(RagiumFluids.RESIN, "Resin")
        // add(RagiumFluids.POTION.getFluidType().descriptionId, "Invalid Potion")
        add(RagiumFluids.POTION.bucketHolder, $$"%1$s Bucket")
        addFluid(RagiumFluids.OMINOUS_FLUX, "Ominous Flux")
        addFluid(RagiumFluids.MOLTEN_GLASS, "Molten Glass")
        addFluid(RagiumFluids.MOLTEN_REDSTONE, "Destabilized Redstone")
        addFluid(RagiumFluids.MOLTEN_GLOWSTONE, "Energized Glowstone")
        addFluid(RagiumFluids.MOLTEN_ENDER, "Resonant Ender")

        addFluid(RagiumFluids.HYDROGEN, "Hydrogen")
        addFluid(RagiumFluids.NITROGEN, "Nitrogen")
        addFluid(RagiumFluids.OXYGEN, "Oxygen")
        addFluid(RagiumFluids.CHLORINE, "Chlorine")

        addFluid(RagiumFluids.WOOD_TAR, "Wood Tar")
        addFluid(RagiumFluids.COAL_TAR, "Coal Tar")
        addFluid(RagiumFluids.ALCOHOL, "Alcohol")
        addFluid(RagiumFluids.ALDEHYDE, "Aldehyde")
        addFluid(RagiumFluids.AROMATIC_COMPOUND, "Aromatic Compound")
        addFluid(RagiumFluids.CRUDE_OIL, "Crude Oil")
        addFluid(RagiumFluids.NAPHTHA, "Naphtha")
        addFluid(RagiumFluids.FUEL, "Fuel")
        addFluid(RagiumFluids.ANTI_RUST_OIL, "Anti-rust Oil")
        addFluid(RagiumFluids.MOLTEN_PLASTIC, "Molten Plastic")
        addFluid(RagiumFluids.SYNTHETIC_RESIN, "Synthetic Resin")
        addFluid(RagiumFluids.NITRIC_ACID, "Nitric Acid")
        addFluid(RagiumFluids.LIQUID_EXPLOSIVE, "Liquid Explosive")
        addFluid(RagiumFluids.HYDROGEN_FLUORIDE, "Hydrogen Fluoride")
        addFluid(RagiumFluids.HYDROFLUORIC_ACID, "Hydrofluoric Acid")
        addFluid(RagiumFluids.SALT_WATER, "Salt Water")
        addFluid(RagiumFluids.NAOH_SOLUTION, "Sodium Hydroxide Solution")
        addFluid(RagiumFluids.ALUMINA_SOLUTION, "Alumina Solution")
        addFluid(RagiumFluids.SULFUR_DIOXIDE, "Sulfur Dioxide")
        addFluid(RagiumFluids.SULFUR_TRIOXIDE, "Sulfur Trioxide")
        addFluid(RagiumFluids.SULFURIC_ACID, "Sulfuric Acid")
        addFluid(RagiumFluids.HYDROGEN_CHLORIDE, "Hydrogen Chloride")
        addFluid(RagiumFluids.HYDROCHLORIC_ACID, "Hydrochloric Acid")
        addFluid(RagiumFluids.BLEACH, "Bleach")
        // Item
        add(RagiumItems.BAMBOO_CHARCOAL, "Bamboo Charcoal")
        add(RagiumItems.PARTICLE_BOARD, "Particle Board")
        add(RagiumItems.CEMENT, "Cement")
        add(RagiumItems.MORTAR, "Mortar")
        add(RagiumItems.TAR, "Tar")
        add(RagiumItems.STICKY_BALL, "Sticky Ball")
        add(RagiumItems.PLASTIC_PLATE, "Plastic Plate")
        add(RagiumItems.SYNTHETIC_FEATHER, "Synthetic Feather")
        add(RagiumItems.SYNTHETIC_LEATHER, "Synthetic Leather")
        add(RagiumItems.SYNTHETIC_FIBER, "Synthetic Fiber")
        add(RagiumItems.CARBON_FIBER, "Carbon Fiber")
        add(RagiumItems.CFRP_PLATE, "CFRP Plate")
        // add(RagiumItems.ALCLAD_PLATE, "Alclad Plate")
        add(RagiumItems.BEESWAX, "Beeswax")
        add(RagiumItems.SPLASH_BOTTLE, "Splash Bottle")
        add(RagiumItems.LINGERING_BOTTLE, "Lingering Bottle")
        add(RagiumItems.CRUDE_SILICON, "Crude Silicon")
        add(RagiumItems.SILICON_WAFER, "Silicon Wafer")
        add(RagiumItems.SILICON_CHIP, "Silicon Chip")
        add(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        add(RagiumItems.ELECTRIC_CIRCUIT, "Electric Circuit")
        add(RagiumItems.ELDER_HEART, "Elder Heart")
        add(RagiumItems.WITHER_DOLL, "Wither Doll")
        add(RagiumItems.WITHER_STAR, "Wither Star")

        add(RagiumItems.BLANK_MOLD, "Blank Mold")
        add(RagiumItems.BALL_MOLD, "Ball Mold")
        add(RagiumItems.BLOCK_MOLD, "Block Mold")
        add(RagiumItems.PLATE_MOLD, "Plate Mold")
        add(RagiumItems.MEMORY_DISC, "Memory Disc")

        // Mob Effect
        add(RagiumMobEffects.FROSTBITE, "Frostbite")
        add(RagiumMobEffects.CHEMICAL_BURN, "Chemical Burn")

        // Potion
        addPotion(RagiumPotions.FROSTBITE, "Frostbite")
        addPotion(RagiumPotions.CHEMICAL_BURN, "Chemical Burn")

        addCustomPotion("nausea", "Nausea")
        addCustomPotion("blindness", "Blindness")
        addCustomPotion("hunger", "Hunger")
        addCustomPotion("wither", "Wither")
        addCustomPotion("darkness", "Darkness")
        addCustomPotion("golden_apple", "Golden Apple")
        addCustomPotion("enchanted_golden_apple", "Enchanted Golden Apple")

        // Recipe Type
        addRecipeType(RagiumRecipeTypes.ASSEMBLING, "Assembling")
        addRecipeType(RagiumRecipeTypes.COMPRESSING, "Compressing")
        addRecipeType(RagiumRecipeTypes.CRUSHING, "Crushing")
        addRecipeType(RagiumRecipeTypes.CUTTING, "Cutting")
        addRecipeType(RagiumRecipeTypes.DRAINING, "Draining")
        addRecipeType(RagiumRecipeTypes.FILLING, "Filling")

        addRecipeType(RagiumRecipeTypes.ALLOYING, "Alloying")
        addRecipeType(RagiumRecipeTypes.FREEZING, "Freezing")
        addRecipeType(RagiumRecipeTypes.MELTING, "Melting")
        addRecipeType(RagiumRecipeTypes.PYROLYZING, "Pyrolyzing")
        addRecipeType(RagiumRecipeTypes.REFINING, "Refining")

        addRecipeType(RagiumRecipeTypes.BATHING, "Chemical Bathing")
        addRecipeType(RagiumRecipeTypes.CENTRIFUGING, "Centrifuging")
        addRecipeType(RagiumRecipeTypes.MIXING, "Mixing")
        addRecipeType(RagiumRecipeTypes.REACTING, "Chemical Reacting")
        addRecipeType(RagiumRecipeTypes.WASHING, "Washing")

        addRecipeType(RagiumRecipeTypes.BREWING, "Brewing")
        addRecipeType(RagiumRecipeTypes.PLANTING, "Planting")

        addRecipeType(RagiumRecipeTypes.ELECTROLYZING, "Electrolyzing")
        addRecipeType(RagiumRecipeTypes.RESOURCE_EXTRACTING, "Resource Extracting")

        addRecipeType(RagiumRecipeTypes.ENCHANTING, "Enchanting")

        // Text - Lib
        add(HTCommonTranslation.ERROR, "Error")
        add(HTCommonTranslation.INFINITE, "Infinite")
        add(HTCommonTranslation.NONE, "None")
        add(HTCommonTranslation.EMPTY, "Empty")

        add(HTCommonTranslation.DOWN, "Down")
        add(HTCommonTranslation.UP, "Up")
        add(HTCommonTranslation.NORTH, "North")
        add(HTCommonTranslation.SOUTH, "South")
        add(HTCommonTranslation.WEST, "West")
        add(HTCommonTranslation.EAST, "East")

        add(HTCommonTranslation.INVALID_PACKET_S2C, $$"Invalid packet received from server side: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_C2S, $$"Invalid packet received from client side: %1$s")

        add(HTCommonTranslation.PROGRESS, $$"Progress: %1$s %%")
        add(HTCommonTranslation.SECONDS, $$"%1$s sec (%2$s ticks)")

        add(HTCommonTranslation.BIOME, $$"Biome: %1$s")

        add(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"Always has at least %1$s")
        add(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION, "Press Shift to show description")
        add(HTCommonTranslation.TOOLTIP_SHOW_DETAILS, "Press Ctrl to show details")

        add(HTCommonTranslation.DATAPACK_WIP, "Enables work in progress contents")
        // Text - Ragium
        add(RagiumTranslation.RAGIUM, "Ragium")

        add(RagiumTranslation.CONFIG_ENERGY_CAPACITY, "Energy Capacity")
        add(RagiumTranslation.CONFIG_ENERGY_RATE, "Energy Rate")

        add(RagiumTranslation.TOOLTIPS_MEMORY_DISC_DATA, $$"Scanned Item: %1$s")

        addDataTranslations()
    }

    private fun addDataTranslations() {
        // Chemical
        addFromKey(RagiumChemicals.HYDROGEN, "Hydrogen")

        addFromKey(RagiumChemicals.CARBON, "Carbon")
        addFromKey(RagiumChemicals.DIAMOND, "Diamond")
        addFromKey(RagiumChemicals.NITROGEN, "Nitrogen")
        addFromKey(RagiumChemicals.OXYGEN, "Oxygen")

        addFromKey(RagiumChemicals.ALUMINUM, "Aluminum")
        addFromKey(RagiumChemicals.SILICON, "Silicon")
        addFromKey(RagiumChemicals.SULFUR, "Sulfur")
        addFromKey(RagiumChemicals.CHLORINE, "Chlorine")

        addFromKey(RagiumChemicals.IRON, "Iron")
        addFromKey(RagiumChemicals.COPPER, "Copper")
        addFromKey(RagiumChemicals.GOLD, "Gold")

        addFromKey(RagiumChemicals.HYDROXIDE, "Hydroxide")
        addFromKey(RagiumChemicals.WATER, "Water")

        addFromKey(RagiumChemicals.CARBONATE, "Carbonate")
        addFromKey(RagiumChemicals.NITRATE, "Nitrate")
        addFromKey(RagiumChemicals.NITRIC_ACID, "Nitric Acid")
        addFromKey(RagiumChemicals.HYDROGEN_FLUORIDE, "Hydrogen Fluoride")
        addFromKey(RagiumChemicals.FLUORITE, "Fluorite")
        addFromKey(RagiumChemicals.CRYOLITE, "Cryolite")

        addFromKey(RagiumChemicals.SODIUM_HYDROXIDE, "Sodium Hydroxide")
        addFromKey(RagiumChemicals.SODIUM_CARBONATE, "Sodium Carbonate")
        addFromKey(RagiumChemicals.SODIUM_CHLORIDE, "Sodium Chloride")
        addFromKey(RagiumChemicals.ALUMINUM_OXIDE, "Aluminum Oxide")
        addFromKey(RagiumChemicals.ALUMINUM_FLUORIDE, "Aluminum Fluoride")
        addFromKey(RagiumChemicals.SILICON_DIOXIDE, "Silicon Dioxide")
        addFromKey(RagiumChemicals.SULFUR_DIOXIDE, "Sulfur Dioxide")
        addFromKey(RagiumChemicals.SULFUR_TRIOXIDE, "Sulfur Trioxide")
        addFromKey(RagiumChemicals.SULFATE, "Sulfate")
        addFromKey(RagiumChemicals.SULFURIC_ACID, "Sulfuric Acid")
        addFromKey(RagiumChemicals.HYDROGEN_CHLORIDE, "Hydrogen chloride")

        addFromKey(RagiumChemicals.POTASSIUM_NITRATE, "Potassium Nitrate")
        addFromKey(RagiumChemicals.CALCIUM_CARBONATE, "Calcium Carbonate")
        addFromKey(RagiumChemicals.CALCIUM_OXIDE, "Calcium Oxide")
        addFromKey(RagiumChemicals.CALCIUM_SULFATE, "Calcium Sulfate")
        // Element
        addFromKey(RagiumElements.HYDROGEN, "Hydrogen")

        addFromKey(RagiumElements.CARBON, "Carbon")
        addFromKey(RagiumElements.NITROGEN, "Nitrogen")
        addFromKey(RagiumElements.OXYGEN, "Oxygen")
        addFromKey(RagiumElements.FLUORINE, "Fluorine")

        addFromKey(RagiumElements.SODIUM, "Sodium")
        addFromKey(RagiumElements.ALUMINUM, "Aluminum")
        addFromKey(RagiumElements.SILICON, "Silicon")
        addFromKey(RagiumElements.SULFUR, "Sulfur")
        addFromKey(RagiumElements.CHLORINE, "Chlorine")

        addFromKey(RagiumElements.POTASSIUM, "Potassium")
        addFromKey(RagiumElements.CALCIUM, "Calcium")
        addFromKey(RagiumElements.IRON, "Iron")
        addFromKey(RagiumElements.COPPER, "Copper")

        addFromKey(RagiumElements.GOLD, "Gold")
    }
}
