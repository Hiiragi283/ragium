package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTSoap
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTMachineRecipe
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.client.screen.HTSingleItemScreen
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.integration.jei.category.*
import hiiragi283.ragium.integration.jei.entry.HTGeneratorFuelEntry
import hiiragi283.ragium.integration.jei.entry.HTSoapEntry
import hiiragi283.ragium.integration.jei.entry.HTStirlingFuelEntry
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IJeiHelpers
import mezz.jei.api.registration.IGuiHandlerRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import mezz.jei.api.recipe.RecipeType as JEIRecipeType

@JeiPlugin
class RagiumJEIPlugin : IModPlugin {
    companion object {
        @JvmField
        val PLUGIN_ID: ResourceLocation = RagiumAPI.id("default")
    }

    override fun getPluginUid(): ResourceLocation = PLUGIN_ID

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val jeiHelper: IJeiHelpers = registration.jeiHelpers
        val guiHelper: IGuiHelper = jeiHelper.guiHelper

        registration.addRecipeCategories(
            HTMultiItemRecipeCategory(
                guiHelper,
                HTMachineType.ASSEMBLER,
                RagiumJEIRecipeTypes.ASSEMBLER,
            ),
            HTMultiItemRecipeCategory(
                guiHelper,
                HTMachineType.BLAST_FURNACE,
                RagiumJEIRecipeTypes.BLAST_FURNACE,
            ),
            HTMultiItemRecipeCategory(
                guiHelper,
                HTMachineType.ALCHEMICAL_BREWERY,
                RagiumJEIRecipeTypes.BREWERY,
            ),
            HTSingleItemRecipeCategory(
                guiHelper,
                HTMachineType.COMPRESSOR,
                RagiumJEIRecipeTypes.COMPRESSOR,
            ),
            HTCrusherRecipeCategory(guiHelper),
            HTEnchanterRecipeCategory(guiHelper),
            HTExtractorRecipeCategory(guiHelper),
            HTSingleItemRecipeCategory(
                guiHelper,
                HTMachineType.GRINDER,
                RagiumJEIRecipeTypes.GRINDER,
            ),
            HTSingleItemRecipeCategory(
                guiHelper,
                HTMachineType.GROWTH_CHAMBER,
                RagiumJEIRecipeTypes.GROWTH_CHAMBER,
            ),
            HTInfuserRecipeCategory(guiHelper),
            HTSingleItemRecipeCategory(
                guiHelper,
                HTMachineType.LASER_ASSEMBLY,
                RagiumJEIRecipeTypes.LASER_ASSEMBLY,
            ),
            HTMixerRecipeCategory(guiHelper),
            HTRefineryRecipeCategory(guiHelper),
            HTSolidifierRecipeCategory(guiHelper),
            HTGeneratorFuelCategory(guiHelper),
            HTStirlingFuelCategory(guiHelper),
            HTMaterialInfoCategory(guiHelper),
            HTSoapCategory(guiHelper),
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val recipeManager: RecipeManager = Minecraft.getInstance().level?.recipeManager ?: return

        fun <T : HTMachineRecipe> register(recipeType: JEIRecipeType<RecipeHolder<T>>, recipe: HTRecipeType<T>) {
            recipe.reloadCache(recipeManager)
            registration.addRecipes(
                recipeType,
                recipe.getAllRecipes(),
            )
        }

        register(RagiumJEIRecipeTypes.ASSEMBLER, HTRecipeTypes.ASSEMBLER)
        register(RagiumJEIRecipeTypes.BLAST_FURNACE, HTRecipeTypes.BLAST_FURNACE)
        register(RagiumJEIRecipeTypes.COMPRESSOR, HTRecipeTypes.COMPRESSOR)
        register(RagiumJEIRecipeTypes.CRUSHER, HTRecipeTypes.CRUSHER)
        register(RagiumJEIRecipeTypes.BREWERY, HTRecipeTypes.BREWERY)
        register(RagiumJEIRecipeTypes.ENCHANTER, HTRecipeTypes.ENCHANTER)
        register(RagiumJEIRecipeTypes.EXTRACTOR, HTRecipeTypes.EXTRACTOR)
        register(RagiumJEIRecipeTypes.GRINDER, HTRecipeTypes.GRINDER)
        register(RagiumJEIRecipeTypes.GROWTH_CHAMBER, HTRecipeTypes.GROWTH_CHAMBER)
        register(RagiumJEIRecipeTypes.INFUSER, HTRecipeTypes.INFUSER)
        register(RagiumJEIRecipeTypes.LASER_ASSEMBLY, HTRecipeTypes.LASER_ASSEMBLY)
        register(RagiumJEIRecipeTypes.MIXER, HTRecipeTypes.MIXER)
        register(RagiumJEIRecipeTypes.REFINERY, HTRecipeTypes.REFINERY)
        register(RagiumJEIRecipeTypes.SOLIDIFIER, HTRecipeTypes.SOLIDIFIER)
        // Generator Fuel
        registration.addRecipes(
            RagiumJEIRecipeTypes.GENERATOR,
            listOf(
                HTGeneratorFuelEntry(HTMachineType.COMBUSTION_GENERATOR, RagiumFluidTags.NITRO_FUEL, 10),
                HTGeneratorFuelEntry(HTMachineType.COMBUSTION_GENERATOR, RagiumFluidTags.NON_NITRO_FUEL, 100),
                HTGeneratorFuelEntry(HTMachineType.THERMAL_GENERATOR, RagiumFluidTags.THERMAL_FUEL, 100),
            ),
        )
        // Stirling
        registration.addRecipes(
            RagiumJEIRecipeTypes.STIRLING,
            BuiltInRegistries.ITEM
                .getDataMap(NeoForgeDataMaps.FURNACE_FUELS)
                .mapNotNull { (key: ResourceKey<Item>, fuelTime: FurnaceFuel) ->
                    val holder: Holder.Reference<Item> = BuiltInRegistries.ITEM.getHolderOrThrow(key)
                    HTStirlingFuelEntry(holder, fuelTime.burnTime)
                }.sorted(),
        )
        // Material Info
        registration.addRecipes(RagiumJEIRecipeTypes.MATERIAL_INFO, RagiumAPI.getInstance().getMaterialRegistry().typedMaterials)
        // Soap
        registration.addRecipes(
            RagiumJEIRecipeTypes.SOAP,
            BuiltInRegistries.BLOCK
                .getDataMap(RagiumDataMaps.SOAP)
                .mapNotNull { (key: ResourceKey<Block>, soap: HTSoap) ->
                    val holder: Holder.Reference<Block> = BuiltInRegistries.BLOCK.getHolderOrThrow(key)
                    if (holder.value() == soap.block) return@mapNotNull null
                    HTSoapEntry(holder, soap)
                }.sorted(),
        )
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        // Compressor, Grinder
        registration.addRecipeClickArea(
            HTSingleItemScreen::class.java,
            HTSlotPos.getSlotPosX(4.5),
            HTSlotPos.getSlotPosY(1),
            18,
            18,
            RagiumJEIRecipeTypes.COMPRESSOR,
            RagiumJEIRecipeTypes.GRINDER,
            RagiumJEIRecipeTypes.LASER_ASSEMBLY,
        )
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        // Assembler
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.ASSEMBLER, HTMachineType.ASSEMBLER)
        // Blast Furnace
        registration.addRecipeCatalysts(
            RagiumJEIRecipeTypes.BLAST_FURNACE,
            HTMachineType.BLAST_FURNACE,
            RagiumBlocks.PRIMITIVE_BLAST_FURNACE,
        )
        // Brewery
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.BREWERY, HTMachineType.ALCHEMICAL_BREWERY)
        // Compressor
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.COMPRESSOR, HTMachineType.COMPRESSOR)
        // Crusher
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.CRUSHER, HTMachineType.CRUSHER)
        // Enchanter
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.ENCHANTER, HTMachineType.ARCANE_ENCHANTER)
        // Extractor
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.EXTRACTOR, HTMachineType.EXTRACTOR)
        // Grinder
        registration.addRecipeCatalysts(
            RagiumJEIRecipeTypes.GRINDER,
            HTMachineType.GRINDER,
            RagiumBlocks.MANUAL_GRINDER,
        )
        // Growth Chamber
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.GROWTH_CHAMBER, HTMachineType.GROWTH_CHAMBER)
        // Infuser
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.INFUSER, HTMachineType.INFUSER)
        // Laser Assembly
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.LASER_ASSEMBLY, HTMachineType.LASER_ASSEMBLY)
        // Mixer
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.MIXER, HTMachineType.MIXER)
        // Refinery
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.REFINERY, HTMachineType.REFINERY)
        // Solidifier
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.SOLIDIFIER, HTMachineType.SOLIDIFIER)

        // Generator
        registration.addRecipeCatalysts(
            RagiumJEIRecipeTypes.GENERATOR,
            HTMachineType.COMBUSTION_GENERATOR,
            HTMachineType.THERMAL_GENERATOR,
        )
        // Stirling
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.STIRLING, HTMachineType.STIRLING_GENERATOR)
        // Material
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.MATERIAL_INFO, Items.IRON_INGOT)
        // Soap
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.SOAP, RagiumItems.SOAP)

        // Vanilla
        registration.addRecipeCatalyst(HTMachineType.AUTO_CHISEL, RecipeTypes.STONECUTTING)
        registration.addRecipeCatalyst(HTMachineType.ELECTRIC_FURNACE, RecipeTypes.BLASTING)
        registration.addRecipeCatalyst(HTMachineType.ELECTRIC_FURNACE, RecipeTypes.SMELTING)
        registration.addRecipeCatalyst(HTMachineType.ELECTRIC_FURNACE, RecipeTypes.SMOKING)
        registration.addRecipeCatalyst(HTMachineType.MULTI_SMELTER, RecipeTypes.SMELTING)
    }
}
