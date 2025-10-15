package hiiragi283.ragium.api.data.recipe.ingredient

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.registry.HTFluidContent
import net.minecraft.world.level.material.Fluid

/**
 * @see [mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator]
 */
interface HTFluidIngredientCreator : HTIngredientCreator<Fluid, HTFluidIngredient> {
    fun fromContent(content: HTFluidContent<*, *, *>, amount: Int): HTFluidIngredient = fromTagKey(content.commonTag, amount)

    fun water(amount: Int): HTFluidIngredient = fromContent(HTFluidContent.WATER, amount)

    fun lava(amount: Int): HTFluidIngredient = fromContent(HTFluidContent.LAVA, amount)

    fun milk(amount: Int): HTFluidIngredient = fromContent(HTFluidContent.MILK, amount)
}
