package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.single.HTSingleInputRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.common.util.HTPotionHelper
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HTBrewingRecipe(val ingredient: HTItemIngredient, val contents: PotionContents) : HTSingleInputRecipe {
    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    override fun assembleItem(input: SingleRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = when {
        test(input) -> HTPotionHelper.createPotion(RagiumItems.POTION_DROP, contents).toImmutable()
        else -> null
    }

    override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || contents.allEffects.none()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BREWING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BREWING.get()

    override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)
}
