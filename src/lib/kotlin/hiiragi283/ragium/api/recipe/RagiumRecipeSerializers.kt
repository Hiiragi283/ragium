package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.crafting.RecipeSerializer

data object RagiumRecipeSerializers {

    // Mechanical

    // Heat
    @JvmField
    val MELTING: RecipeSerializer<RTMeltingRecipe> = RTMeltingRecipe.SERIALIZER

    @JvmField
    val SMELTING: RecipeSerializer<RTSmeltingRecipe> = RTSmeltingRecipe.SERIALIZER

    // Chemical

    // Bio

    // Electronics

    // Arcane
}
