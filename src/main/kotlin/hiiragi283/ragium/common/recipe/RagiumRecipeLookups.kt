package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.core.api.recipe.base.HTItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.util.identity
import hiiragi283.core.common.recipe.VanillaRecipeLookups
import hiiragi283.core.support.recipe.cache.HTCompoundRecipeLookup
import hiiragi283.core.support.recipe.cache.HTVanillaRecipeLookup
import hiiragi283.core.support.recipe.cache.fromRecipeType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.base.HTPlantingRecipe
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.recipe.custom.HTBookMeltingRecipe
import hiiragi283.ragium.common.recipe.ingredient.HTMemoryDiscIngredient
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.common.Tags

data object RagiumRecipeLookups {
    // Mechanical
    @JvmField
    val ALLOYING: HTRecipeLookup<HTAlloyingRecipe> = create(RagiumRecipeTypes.ALLOYING)

    @JvmField
    val ASSEMBLING: HTCompoundRecipeLookup<HTDoubleItemToItemRecipe> = create(RagiumConst.ASSEMBLING)

    @JvmField
    val COMPRESSING: HTCompoundRecipeLookup<HTItemToItemRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val CUTTING: HTCompoundRecipeLookup<HTItemToMultiItemRecipe> = create(RagiumConst.CUTTING)

    @JvmField
    val SMELTING: HTCompoundRecipeLookup<HTItemToItemRecipe> = create(HTConst.SMELTING)

    // Heat
    @JvmField
    val FREEZING: HTCompoundRecipeLookup<HTItemAndFluidToItemRecipe> = create(RagiumConst.FREEZING)

    @JvmField
    val IMPLODING: HTRecipeLookup<HTImplodingRecipe> = create(RagiumRecipeTypes.IMPLODING)

    @JvmField
    val MELTING: HTCompoundRecipeLookup<HTItemToFluidRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: HTCompoundRecipeLookup<HTItemOrFluidRecipe> = create(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTRecipeLookup<HTRefiningRecipe> = create(RagiumRecipeTypes.REFINING)

    // Chemical
    @JvmField
    val BATHING: HTCompoundRecipeLookup<HTItemAndFluidToItemRecipe> = create(RagiumConst.BATHING)

    @JvmField
    val CHEMICAL_REACTING: HTRecipeLookup<HTChemicalReactingRecipe> = create(RagiumRecipeTypes.CHEMICAL_REACTING)

    @JvmField
    val MIXING: HTRecipeLookup<HTMixingRecipe> = create(RagiumRecipeTypes.MIXING)

    @JvmField
    val WASHING: HTRecipeLookup<HTWashingRecipe> = create(RagiumRecipeTypes.WASHING)

    // Bio
    @JvmField
    val PLANTING: HTCompoundRecipeLookup<HTPlantingRecipe> = create(RagiumConst.PLANTING)

    // Electronics
    @JvmField
    val PRINTING: HTCompoundRecipeLookup<HTDoubleItemToItemRecipe> = create(RagiumConst.PRINTING)

    // Arcane
    @JvmField
    val ENCHANTING: HTCompoundRecipeLookup<HTEnchantingRecipe> = create(RagiumConst.ENCHANTING)

    @JvmStatic
    private fun <RECIPE : Any> create(path: String): HTCompoundRecipeLookup<RECIPE> = HTCompoundRecipeLookup.create(RagiumAPI.id(path))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(recipeType: HTRecipeType<RECIPE>): HTRecipeLookup<RECIPE> = HTVanillaRecipeLookup(recipeType)

    @JvmStatic
    fun init() {
        ASSEMBLING.fromRecipeType(RagiumRecipeTypes.ASSEMBLING, identity())
        COMPRESSING.fromRecipeType(RagiumRecipeTypes.COMPRESSING, identity())
        CUTTING.fromRecipeType(RagiumRecipeTypes.CUTTING, identity())
        SMELTING.addSubLookup(VanillaRecipeLookups.SMELTING)
        SMELTING.fromRecipeType(RagiumRecipeTypes.SMELTING, identity())

        FREEZING.fromRecipeType(RagiumRecipeTypes.FREEZING, identity())
        MELTING.fromRecipeType(RagiumRecipeTypes.MELTING, identity())
        MELTING.addRecipes(RagiumAPI.id(RagiumConst.MELTING, "exp_from_ench_book") to HTBookMeltingRecipe)
        PYROLYZING.fromRecipeType(RagiumRecipeTypes.PYROLYZING, identity())

        BATHING.fromRecipeType(RagiumRecipeTypes.BATHING, identity())

        PLANTING.fromRecipeType(RagiumRecipeTypes.PLANTING, identity())

        PRINTING.fromRecipeType(RagiumRecipeTypes.PRINTING, identity())
        PRINTING.addSubLookup {
            buildMap {
                for (holder: Holder<Item> in BuiltInRegistries.ITEM.getTagOrEmpty(Tags.Items.MUSIC_DISCS)) {
                    val item: Item = holder.value()
                    RagiumRecipeBuilder.printing {
                        primary { +RagiumItems.BLANK_DISC }
                        secondary { +HTMemoryDiscIngredient.create { +item } }
                        result { +item }
                    }.save(this::put)
                }
            }
        }

        ENCHANTING.fromRecipeType(RagiumRecipeTypes.ENCHANTING, identity())
    }
}
