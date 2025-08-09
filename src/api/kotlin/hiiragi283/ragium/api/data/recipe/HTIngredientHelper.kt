package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConst
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

/**
 * @see [IItemStackIngredientCreator], [IFluidStackIngredientCreator]
 */
object HTIngredientHelper {
    //    Item    //

    @JvmStatic
    fun item(item: ItemLike, count: Int = 1): HTItemIngredient = item(SizedIngredient.of(item, count))

    @JvmStatic
    fun item(tagKey: TagKey<Item>, count: Int = 1): HTItemIngredient = item(SizedIngredient.of(tagKey, count))

    @JvmStatic
    fun item(ingredient: ICustomIngredient, count: Int = 1): HTItemIngredient = item(ingredient.toVanilla(), count)

    @JvmStatic
    fun item(ingredient: Ingredient, count: Int = 1): HTItemIngredient = item(SizedIngredient(ingredient, count))

    @JvmStatic
    fun item(ingredient: SizedIngredient): HTItemIngredient = HTItemIngredient.of(ingredient)

    @JvmStatic
    fun itemTags(vararg tagKeys: TagKey<Item>, count: Int): HTItemIngredient = item(
        CompoundIngredient(tagKeys.map(Ingredient::of)),
        count,
    )

    @JvmStatic
    fun coal(): Ingredient = CompoundIngredient.of(
        Ingredient.of(Items.COAL),
        Ingredient.of(RagiumCommonTags.Items.DUSTS_COAL),
    )

    @JvmStatic
    fun charcoal(): Ingredient = CompoundIngredient.of(
        Ingredient.of(Items.CHARCOAL),
        Ingredient.of(RagiumCommonTags.Items.DUSTS_CHARCOAL),
    )

    @JvmStatic
    fun coalCoke(): Ingredient = CompoundIngredient.of(
        Ingredient.of(RagiumCommonTags.Items.COAL_COKE),
        Ingredient.of(RagiumCommonTags.Items.DUSTS_COKE),
    )

    @JvmStatic
    fun gemOrDust(name: String, count: Int = 1): HTItemIngredient = itemTags(
        itemTagKey(commonId(RagiumConst.DUSTS, name)),
        itemTagKey(commonId(RagiumConst.GEMS, name)),
        count = count,
    )

    @JvmStatic
    fun ingotOrDust(name: String, count: Int = 1): HTItemIngredient = itemTags(
        itemTagKey(commonId(RagiumConst.DUSTS, name)),
        itemTagKey(commonId(RagiumConst.INGOTS, name)),
        count = count,
    )

    //    Fluid    //

    @JvmStatic
    fun fluid(fluid: Fluid, amount: Int): HTFluidIngredient = fluid(SizedFluidIngredient.of(fluid, amount))

    @JvmStatic
    fun fluid(tagKey: TagKey<Fluid>, amount: Int): HTFluidIngredient = fluid(SizedFluidIngredient.of(tagKey, amount))

    @JvmStatic
    fun fluid(content: HTFluidContent<*, *, *>, amount: Int): HTFluidIngredient = fluid(content.commonTag, amount)

    @JvmStatic
    fun fluid(ingredient: FluidIngredient, amount: Int): HTFluidIngredient = fluid(SizedFluidIngredient(ingredient, amount))

    @JvmStatic
    fun fluid(ingredient: SizedFluidIngredient): HTFluidIngredient = HTFluidIngredient.of(ingredient)

    @JvmStatic
    fun water(amount: Int): HTFluidIngredient = fluid(Tags.Fluids.WATER, amount)

    @JvmStatic
    fun lava(amount: Int): HTFluidIngredient = fluid(Tags.Fluids.LAVA, amount)

    @JvmStatic
    fun milk(amount: Int): HTFluidIngredient = fluid(Tags.Fluids.MILK, amount)
}
