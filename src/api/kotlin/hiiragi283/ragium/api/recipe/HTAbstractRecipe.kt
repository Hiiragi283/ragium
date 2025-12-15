package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.Level

interface HTAbstractRecipe {
    fun matches(input: HTRecipeInput, level: Level): Boolean

    fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack?

    //    Modifiable    //

    interface Modifiable<RECIPE : HTAbstractRecipe> : HTAbstractRecipe {
        fun copyAndMultiply(multiplier: Int): RECIPE
    }
}
