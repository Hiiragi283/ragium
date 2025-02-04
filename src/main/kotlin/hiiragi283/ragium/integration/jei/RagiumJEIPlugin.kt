package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.recipe.HTMachineRecipeBase
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.recipe.HTRecipeConverters
import hiiragi283.ragium.integration.jei.category.*
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IJeiHelpers
import mezz.jei.api.registration.IGuiHandlerRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import java.util.function.Supplier
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
                RagiumJEIRecipeTypes.ASSEMBLER,
                RagiumMachineKeys.ASSEMBLER,
                RagiumRecipeSerializers.ASSEMBLER.get(),
            ),
            HTMultiItemRecipeCategory(
                guiHelper,
                RagiumJEIRecipeTypes.BLAST_FURNACE,
                RagiumMachineKeys.BLAST_FURNACE,
                RagiumRecipeSerializers.BLAST_FURNACE.get(),
            ),
            HTSingleItemRecipeCategory(
                guiHelper,
                RagiumJEIRecipeTypes.COMPRESSOR,
                RagiumMachineKeys.COMPRESSOR,
                RagiumRecipeSerializers.COMPRESSOR.get(),
            ),
            HTExtractorRecipeCategory(guiHelper),
            HTGrinderRecipeCategory(guiHelper),
            HTInfuserRecipeCategory(guiHelper),
            HTSingleItemRecipeCategory(
                guiHelper,
                RagiumJEIRecipeTypes.LASER_ASSEMBLY,
                RagiumMachineKeys.LASER_ASSEMBLY,
                RagiumRecipeSerializers.LASER_ASSEMBLY.get(),
            ),
            HTMixerRecipeCategory(guiHelper),
            HTRefineryRecipeCategory(guiHelper),
            HTMaterialInfoCategory(guiHelper),
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val level: ClientLevel = Minecraft.getInstance().level ?: return
        val recipeManager: RecipeManager = level.recipeManager
        val registry: HTMaterialRegistry = RagiumAPI.materialRegistry

        fun <T : HTMachineRecipeBase> register(recipeType: JEIRecipeType<T>, recipe: Supplier<RecipeType<T>>) {
            registration.addRecipes(
                recipeType,
                recipeManager.getAllRecipesFor(recipe.get()).map(RecipeHolder<T>::value),
            )
        }

        register(RagiumJEIRecipeTypes.ASSEMBLER, RagiumRecipeTypes.ASSEMBLER)
        register(RagiumJEIRecipeTypes.BLAST_FURNACE, RagiumRecipeTypes.BLAST_FURNACE)
        registration.addRecipes(
            RagiumJEIRecipeTypes.COMPRESSOR,
            buildList {
                HTRecipeConverters.compressor(recipeManager, registry, this::add)
            },
        )
        register(RagiumJEIRecipeTypes.EXTRACTOR, RagiumRecipeTypes.EXTRACTOR)
        registration.addRecipes(
            RagiumJEIRecipeTypes.GRINDER,
            buildList {
                HTRecipeConverters.grinder(recipeManager, registry, this::add)
            },
        )
        registration.addRecipes(
            RagiumJEIRecipeTypes.INFUSER,
            buildList {
                HTRecipeConverters.infuser(recipeManager, registry, this::add)
            },
        )
        register(RagiumJEIRecipeTypes.LASER_ASSEMBLY, RagiumRecipeTypes.LASER_ASSEMBLY)
        register(RagiumJEIRecipeTypes.MIXER, RagiumRecipeTypes.MIXER)
        register(RagiumJEIRecipeTypes.REFINERY, RagiumRecipeTypes.REFINERY)

        // Material Info
        registration.addRecipes(RagiumJEIRecipeTypes.MATERIAL_INFO, RagiumAPI.materialRegistry.typedMaterials)
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        // Assembler
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.ASSEMBLER, RagiumMachineKeys.ASSEMBLER.getBlock())
        // Blast Furnace
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.BLAST_FURNACE, RagiumMachineKeys.BLAST_FURNACE.getBlock())
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.BLAST_FURNACE, RagiumBlocks.PRIMITIVE_BLAST_FURNACE)
        // Compressor
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.COMPRESSOR, RagiumMachineKeys.COMPRESSOR.getBlock())
        // Extractor
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.EXTRACTOR, RagiumMachineKeys.EXTRACTOR.getBlock())
        // Grinder
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.GRINDER, RagiumMachineKeys.GRINDER.getBlock())
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.GRINDER, RagiumBlocks.MANUAL_GRINDER)
        // Infuser
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.INFUSER, RagiumMachineKeys.INFUSER.getBlock())
        // Laser Assembly
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.LASER_ASSEMBLY, RagiumMachineKeys.LASER_ASSEMBLY.getBlock())
        // Mixer
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.MIXER, RagiumMachineKeys.MIXER.getBlock())
        // Refinery
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.REFINERY, RagiumMachineKeys.REFINERY.getBlock())

        // Material
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.MATERIAL_INFO, Items.IRON_INGOT)
    }
}
