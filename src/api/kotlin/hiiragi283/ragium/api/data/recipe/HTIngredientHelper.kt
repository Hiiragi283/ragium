package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
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
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

/**
 * @see [IItemStackIngredientCreator], [IFluidStackIngredientCreator]
 */
interface HTIngredientHelper {
    companion object {
        @JvmField
        val INSTANCE: HTIngredientHelper = RagiumAPI.getService()
    }

    //    Item    //

    fun item(item: ItemLike, count: Int = 1): HTItemIngredient = item(SizedIngredient.of(item, count))

    fun item(variant: HTMaterialVariant.ItemTag, material: HTMaterialType, count: Int = 1): HTItemIngredient =
        item(variant.itemTagKey(material), count)

    fun item(tagKey: TagKey<Item>, count: Int = 1): HTItemIngredient = item(SizedIngredient.of(tagKey, count))

    fun item(ingredient: ICustomIngredient, count: Int = 1): HTItemIngredient = item(ingredient.toVanilla(), count)

    fun item(ingredient: Ingredient, count: Int = 1): HTItemIngredient = item(SizedIngredient(ingredient, count))

    fun item(ingredient: SizedIngredient): HTItemIngredient = HTItemIngredient.of(ingredient)

    fun itemTags(vararg tagKeys: TagKey<Item>, count: Int = 1): HTItemIngredient = itemTags(tagKeys.toList(), count)

    fun itemTags(tagKeys: Iterable<TagKey<Item>>, count: Int = 1): HTItemIngredient = item(
        tagKeys.map(Ingredient::TagValue).stream().let(Ingredient::fromValues),
        count,
    )

    fun multiVariants(material: HTMaterialType, vararg variant: HTMaterialVariant.ItemTag, count: Int = 1): HTItemIngredient =
        itemTags(variant.map { it.itemTagKey(material) }, count)

    fun fuelOrDust(material: HTMaterialType, count: Int = 1): HTItemIngredient

    fun gemOrDust(material: HTMaterialType, count: Int = 1): HTItemIngredient

    fun ingotOrDust(material: HTMaterialType, count: Int = 1): HTItemIngredient

    @Deprecated("Use gemOrDust(HTMaterialType, Int) instead of this")
    fun gemOrDust(name: String, count: Int = 1): HTItemIngredient

    @Deprecated("Use ingotOrDust(HTMaterialType, Int) instead of this")
    fun ingotOrDust(name: String, count: Int = 1): HTItemIngredient

    //    Fluid    //

    fun fluid(fluid: Fluid, amount: Int): HTFluidIngredient = fluid(SizedFluidIngredient.of(fluid, amount))

    fun fluid(tagKey: TagKey<Fluid>, amount: Int): HTFluidIngredient = fluid(SizedFluidIngredient.of(tagKey, amount))

    fun fluid(content: HTFluidContent<*, *, *>, amount: Int): HTFluidIngredient = fluid(content.commonTag, amount)

    fun fluid(ingredient: FluidIngredient, amount: Int): HTFluidIngredient = fluid(SizedFluidIngredient(ingredient, amount))

    fun fluid(ingredient: SizedFluidIngredient): HTFluidIngredient = HTFluidIngredient.of(ingredient)

    fun water(amount: Int): HTFluidIngredient = fluid(HTFluidContent.WATER, amount)

    fun lava(amount: Int): HTFluidIngredient = fluid(HTFluidContent.LAVA, amount)

    fun milk(amount: Int): HTFluidIngredient = fluid(HTFluidContent.MILK, amount)
}
