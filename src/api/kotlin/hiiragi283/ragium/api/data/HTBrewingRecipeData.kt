package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.function.identity
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.world.item.alchemy.PotionContents

interface HTBrewingRecipeData {
    companion object {
        @JvmField
        val CODEC: Codec<HTBrewingRecipeData> = RagiumAPI.BREWING_RECIPE_TYPE_REGISTRY
            .byNameCodec()
            .dispatch(HTBrewingRecipeData::type, identity())
    }

    fun type(): MapCodec<out HTBrewingRecipeData>

    fun getIngredient(): HTItemIngredient

    fun getBasePotion(): PotionContents

    fun getLongPotion(): PotionContents

    fun getStrongPotion(): PotionContents
}
