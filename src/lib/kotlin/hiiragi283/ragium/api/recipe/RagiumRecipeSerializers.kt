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
    val MELTING: RecipeSerializer<RTMeltingRecipe> = RTMeltingRecipe.SERIALIZER

    @JvmField
    val SMELTING: RecipeSerializer<RTSmeltingRecipe> = RTSmeltingRecipe.SERIALIZER

    // Chemical

    // Bio

    // Electronics

    // Arcane
}
