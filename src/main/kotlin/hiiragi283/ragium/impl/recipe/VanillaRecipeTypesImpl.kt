package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.VanillaRecipeTypes
import hiiragi283.ragium.api.recipe.manager.HTRecipeType
import hiiragi283.ragium.impl.recipe.manager.HTSimpleRecipeType
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmithingRecipe
import net.minecraft.world.item.crafting.SmithingRecipeInput
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.item.crafting.StonecutterRecipe

class VanillaRecipeTypesImpl : VanillaRecipeTypes {
    companion object {
        @JvmStatic
        private val CRAFTING: HTSimpleRecipeType<CraftingInput, CraftingRecipe> =
            HTSimpleRecipeType(RecipeType.CRAFTING)

        @JvmStatic
        private val SMELTING: HTSimpleRecipeType<SingleRecipeInput, SmeltingRecipe> =
            HTSimpleRecipeType(RecipeType.SMELTING)

        @JvmStatic
        private val BLASTING: HTSimpleRecipeType<SingleRecipeInput, BlastingRecipe> =
            HTSimpleRecipeType(RecipeType.BLASTING)

        @JvmStatic
        private val SMOKING: HTSimpleRecipeType<SingleRecipeInput, SmokingRecipe> =
            HTSimpleRecipeType(RecipeType.SMOKING)

        @JvmStatic
        private val STONECUTTING: HTSimpleRecipeType<SingleRecipeInput, StonecutterRecipe> =
            HTSimpleRecipeType(RecipeType.STONECUTTING)

        @JvmStatic
        private val SMITHING: HTSimpleRecipeType<SmithingRecipeInput, SmithingRecipe> =
            HTSimpleRecipeType(RecipeType.SMITHING)
    }

    override fun crafting(): HTRecipeType.Findable<CraftingInput, CraftingRecipe> = CRAFTING

    override fun smelting(): HTRecipeType.Findable<SingleRecipeInput, SmeltingRecipe> = SMELTING

    override fun blasting(): HTRecipeType.Findable<SingleRecipeInput, BlastingRecipe> = BLASTING

    override fun smoking(): HTRecipeType.Findable<SingleRecipeInput, SmokingRecipe> = SMOKING

    override fun stonecutting(): HTRecipeType.Findable<SingleRecipeInput, StonecutterRecipe> = STONECUTTING

    override fun smithing(): HTRecipeType.Findable<SmithingRecipeInput, SmithingRecipe> = SMITHING
}
