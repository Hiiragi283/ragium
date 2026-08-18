package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.crafting.RecipeSerializer

data object RagiumRecipeSerializers {
    // Mechanical
    @JvmField
    val ASSEMBLING: RecipeSerializer<RTAssemblingRecipe> = RTAssemblingRecipe.SERIALIZER

    @JvmField
    val CRUSHING: RecipeSerializer<RTCrushingRecipe> = RTCrushingRecipe.SERIALIZER

    // Heat
    @JvmField
    val FREEZING: RecipeSerializer<RTFreezingRecipe> = RTFreezingRecipe.SERIALIZER

    @JvmField
    val MELTING: RecipeSerializer<RTMeltingRecipe> = RTMeltingRecipe.SERIALIZER

    @JvmField
    val SMELTING: RecipeSerializer<RTSmeltingRecipe> = RTSmeltingRecipe.SERIALIZER

    // Chemical

    // Bio
    @JvmField
    val BREWING: RecipeSerializer<RTBrewingRecipe> = RTBrewingRecipe.SERIALIZER

    // Electronics

    // Arcane
}
