package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.registry.HTFluidContent
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

/**
 * @see [IItemStackIngredientCreator], [IFluidStackIngredientCreator]
 */
object HTIngredientHelper {
    //    Item    //

    fun item(item: ItemLike, count: Int = 1): HTItemIngredient = item(SizedIngredient.of(item, count))

    fun item(tagKey: TagKey<Item>, count: Int = 1): HTItemIngredient = item(SizedIngredient.of(tagKey, count))

    fun item(ingredient: ICustomIngredient, count: Int = 1): HTItemIngredient = item(ingredient.toVanilla(), count)

    fun item(ingredient: Ingredient, count: Int = 1): HTItemIngredient = item(SizedIngredient(ingredient, count))

    fun item(ingredient: SizedIngredient): HTItemIngredient = HTItemIngredient.of(ingredient)

    //    Fluid    //

    fun fluid(fluid: Fluid, amount: Int): HTFluidIngredient = fluid(SizedFluidIngredient.of(fluid, amount))

    fun fluid(tagKey: TagKey<Fluid>, amount: Int): HTFluidIngredient = fluid(SizedFluidIngredient.of(tagKey, amount))

    fun fluid(content: HTFluidContent<*, *, *>, amount: Int): HTFluidIngredient = fluid(content.commonTag, amount)

    fun fluid(ingredient: FluidIngredient, amount: Int): HTFluidIngredient = fluid(SizedFluidIngredient(ingredient, amount))

    fun fluid(ingredient: SizedFluidIngredient): HTFluidIngredient = HTFluidIngredient.of(ingredient)

    fun water(amount: Int): HTFluidIngredient = fluid(Tags.Fluids.WATER, amount)

    fun lava(amount: Int): HTFluidIngredient = fluid(Tags.Fluids.LAVA, amount)

    fun milk(amount: Int): HTFluidIngredient = fluid(Tags.Fluids.MILK, amount)
}
