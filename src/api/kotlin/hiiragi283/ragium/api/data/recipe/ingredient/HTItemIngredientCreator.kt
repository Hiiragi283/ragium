package hiiragi283.ragium.api.data.recipe.ingredient

import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient

/**
 * [HTItemIngredient]を返す[HTIngredientCreator]の拡張インターフェース
 * @see mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
 */
interface HTItemIngredientCreator : HTIngredientCreator<Item, HTItemIngredient> {
    override fun fromSet(holderSet: HolderSet<Item>, amount: Int): HTItemIngredient = HTItemIngredient.of(holderSet, amount)

    // Vanilla Ingredient
    fun fromVanilla(vararg ingredients: Ingredient, count: Int = 1): HTItemIngredient =
        fromVanilla(CompoundIngredient.of(*ingredients), count)

    fun fromVanilla(ingredient: ICustomIngredient, count: Int = 1): HTItemIngredient = fromVanilla(ingredient.toVanilla(), count)

    fun fromVanilla(ingredient: Ingredient, count: Int = 1): HTItemIngredient {
        require(!ingredient.isEmpty) { "Empty ingredient is not valid for HTItemIngredient" }
        return HTItemIngredient.of(ingredient, count)
    }

    // Default Count
    fun fromItem(item: ItemLike, count: Int = 1): HTItemIngredient = from(item.asItem(), count)

    fun fromItems(vararg items: ItemLike, count: Int = 1): HTItemIngredient = from(items.map(ItemLike::asItem), count)

    fun fromHolder(holder: Holder<Item>): HTItemIngredient = fromHolder(holder, 1)

    fun fromHolders(vararg holders: Holder<Item>): HTItemIngredient = fromHolders(*holders, amount = 1)

    fun fromHolders(holders: List<Holder<Item>>): HTItemIngredient = fromHolders(holders, amount = 1)

    fun fromSet(holderSet: HolderSet<Item>): HTItemIngredient = fromSet(holderSet, 1)

    fun fromTagKey(tagKey: TagKey<Item>): HTItemIngredient = fromTagKey(tagKey, 1)

    fun fromTagKeys(vararg tagKeys: TagKey<Item>): HTItemIngredient = fromTagKeys(*tagKeys, amount = 1)

    fun fromTagKeys(tagKeys: Iterable<TagKey<Item>>): HTItemIngredient = fromTagKeys(tagKeys, 1)

    // Material
    fun fromTagKey(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): HTItemIngredient =
        fromTagKey(prefix.itemTagKey(material), count)

    fun multiPrefixes(material: HTMaterialLike, vararg prefix: HTPrefixLike, count: Int = 1): HTItemIngredient =
        fromTagKeys(prefix.map { it.itemTagKey(material) }, count)

    fun fuelOrDust(material: HTMaterialLike, count: Int = 1): HTItemIngredient

    fun gemOrDust(material: HTMaterialLike, count: Int = 1): HTItemIngredient

    fun ingotOrDust(material: HTMaterialLike, count: Int = 1): HTItemIngredient
}
