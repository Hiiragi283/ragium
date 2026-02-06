package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.ragium.common.recipe.base.HTChemicalIngredient
import hiiragi283.ragium.common.recipe.base.HTChemicalRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class HTReactingRecipe(
    ingredients: HTChemicalIngredient,
    val catalyst: HTItemIngredient?,
    val results: HTChemicalResult,
    parameters: SubParameters,
) : HTChemicalRecipe(ingredients, parameters) {
    companion object {
        const val MAX_FLUID_INPUT = 3
        const val MAX_FLUID_OUTPUT = 3
        const val MAX_ITEM_INPUT = 2
        const val MAX_ITEM_OUTPUT = 2
    }

    constructor(
        ingredients: HTChemicalIngredient,
        catalyst: Optional<HTItemIngredient>,
        results: HTChemicalResult,
        subParameters: SubParameters,
    ) : this(
        ingredients,
        catalyst.getOrNull(),
        results,
        subParameters,
    )

    override fun matches(input: HTChemicalRecipeInput, level: Level): Boolean {
        if (!matchIngredients(input)) return false
        return when (val catalyst: HTItemResourceType? = input.catalyst) {
            null -> this.catalyst == null
            else -> this.catalyst?.testOnlyType(catalyst) ?: true
        }
    }

    @Deprecated("Use instead")
    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REACTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.REACTING.get()
}
