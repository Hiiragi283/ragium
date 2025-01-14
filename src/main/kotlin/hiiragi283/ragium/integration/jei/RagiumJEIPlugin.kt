package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.buildMultiMap
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.util.collection.HTMultiMap
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.integration.jei.category.HTMachineRecipeCategory
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IJeiHelpers
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.neoforged.neoforge.registries.DeferredBlock

@JeiPlugin
class RagiumJEIPlugin : IModPlugin {
    companion object {
        @JvmField
        val PLUGIN_ID: ResourceLocation = RagiumAPI.Companion.id("default")

        @JvmStatic
        val RECIPE_TYPE_MAP: Map<HTMachineKey, RecipeType<RecipeHolder<HTMachineRecipe>>> by lazy {
            RagiumAPI.getInstance().machineRegistry.keys.associateWith { key: HTMachineKey ->
                RecipeType.createRecipeHolderType(RagiumAPI.id(key.name))
            }
        }

        @JvmStatic
        fun getRecipeType(machine: HTMachineKey): RecipeType<RecipeHolder<HTMachineRecipe>> =
            RECIPE_TYPE_MAP[machine] ?: error("Unknown machine key: $machine")
    }

    override fun getPluginUid(): ResourceLocation = PLUGIN_ID

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        val handler: ISubtypeInterpreter<ItemStack> = object : ISubtypeInterpreter<ItemStack> {
            override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? = ingredient.machineTier

            override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String = ingredient.machineTier.text.string
        }

        RagiumAPI.Companion.getInstance().machineRegistry.blocks.forEach { holder: DeferredBlock<HTMachineBlock> ->
            registration.registerSubtypeInterpreter(holder.asItem(), handler)
        }
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val jeiHelper: IJeiHelpers = registration.jeiHelpers
        val guiHelper: IGuiHelper = jeiHelper.guiHelper
        RagiumAPI.getInstance().machineRegistry.keys.forEach { key: HTMachineKey ->
            registration.addRecipeCategories(HTMachineRecipeCategory(key, guiHelper))
        }
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val level: ClientLevel = Minecraft.getInstance().level ?: return
        val recipes: List<RecipeHolder<HTMachineRecipe>> =
            level.recipeManager.getAllRecipesFor(RagiumRecipes.MACHINE_TYPE.get())

        val multiMap: HTMultiMap<HTMachineKey, RecipeHolder<HTMachineRecipe>> = buildMultiMap {
            recipes.forEach { holder: RecipeHolder<HTMachineRecipe> -> put(holder.value.machineKey, holder) }
        }

        multiMap.map.forEach { machine: HTMachineKey, holders: Collection<RecipeHolder<HTMachineRecipe>> ->
            registration.addRecipes(getRecipeType(machine), holders.toList())
        }
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        RagiumAPI.getInstance().machineRegistry.keys.forEach { key: HTMachineKey ->
            HTMachineTier.entries.mapNotNull(key::createItemStackOrNull).forEach { stack: ItemStack ->
                registration.addRecipeCatalysts(getRecipeType(key), stack)
            }
        }
    }
}
