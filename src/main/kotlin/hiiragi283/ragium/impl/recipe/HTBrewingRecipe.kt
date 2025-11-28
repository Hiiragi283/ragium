package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.common.util.HTPotionHelper
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleItemRecipe
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HTBrewingRecipe(ingredient: HTItemIngredient, val contents: PotionContents) : HTBasicSingleItemRecipe(ingredient) {
    override fun assembleItem(input: SingleRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = when {
        test(input) -> HTPotionHelper.createPotion(RagiumItems.POTION_DROP, contents).toImmutable()
        else -> null
    }

    override fun isIncompleteResult(): Boolean = HTPotionHelper.isEmpty(contents)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BREWING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BREWING.get()
}
