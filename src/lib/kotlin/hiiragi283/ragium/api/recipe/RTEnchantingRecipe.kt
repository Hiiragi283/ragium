package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTBasicSingleRecipe
import hiiragi283.lib.recipe.base.HTItemCombineRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTEnchantingRecipe(ingredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTBasicSingleRecipe<HTItemIngredient, HTItemResult>(ingredient, result, progressData),
    HTItemCombineRecipe,
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val BOOK_INGREDIENT = HTItemIngredient(Ingredient.of(Items.BOOK), 1)

        @JvmField
        val SERIALIZER: RecipeSerializer<RTEnchantingRecipe> = RecipeSerializer(
            HTItemToRecipe.BasicItem.codec(::RTEnchantingRecipe),
            HTItemToRecipe.BasicItem.streamCodec(::RTEnchantingRecipe),
        )
    }

    override val firstIngredient: HTIngredient<Item, ItemInstance> get() = ingredient
    override val secondIngredient: HTIngredient<Item, ItemInstance> get() = BOOK_INGREDIENT

    override fun getProgressData(first: ItemInstance, second: ItemInstance): HTProgressData = progressData

    override fun apply(first: ItemInstance, second: ItemInstance): ItemStack = result.create()

    override fun getSerializer(): RecipeSerializer<RTEnchantingRecipe> = RagiumRecipeSerializers.ENCHANTING

    override fun getType(): RecipeType<RTEnchantingRecipe> = RagiumRecipeTypes.ENCHANTING
}
