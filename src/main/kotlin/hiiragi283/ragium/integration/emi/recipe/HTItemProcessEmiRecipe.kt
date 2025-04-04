package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import net.minecraft.resources.ResourceLocation

class HTItemProcessEmiRecipe private constructor(category: EmiRecipeCategory, id: ResourceLocation, definition: HTRecipeDefinition) :
    HTMachineEmiRecipe(category, id, definition),
    HTSimpleEmiRecipe {
        companion object {
            @JvmStatic
            fun create(category: EmiRecipeCategory): (ResourceLocation, HTRecipeDefinition) -> HTMachineEmiRecipe =
                { id: ResourceLocation, definition: HTRecipeDefinition -> HTItemProcessEmiRecipe(category, id, definition) }
        }

        override val firstInput: EmiIngredient
            get() = itemInputs[0]
        override val firstOutput: EmiStack
            get() = itemOutputs[0]
    }
