package hiiragi283.ragium.api.data.recipe.ingredient

import hiiragi283.ragium.api.recipe.ingredient.HTIngredient
import hiiragi283.ragium.api.serialization.codec.BiCodec
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey

/**
 * [HTIngredient]を作成するヘルパーのインターフェース
 * @param TYPE タイプのクラス
 * @param INGREDIENT [HTIngredient]の実装
 * @see mekanism.api.recipes.ingredients.creator.IIngredientCreator
 */
interface HTIngredientCreator<TYPE : Any, INGREDIENT : HTIngredient<*>> {
    // Type
    fun from(type: TYPE, amount: Int): INGREDIENT

    fun from(vararg types: TYPE, amount: Int): INGREDIENT = from(types.toList(), amount)

    fun from(types: Collection<TYPE>, amount: Int): INGREDIENT

    // Holder
    fun fromHolder(holder: Holder<TYPE>, amount: Int): INGREDIENT = fromSet(HolderSet.direct(holder), amount)

    fun fromHolders(vararg holders: Holder<TYPE>, amount: Int): INGREDIENT = fromSet(HolderSet.direct(*holders), amount)

    fun fromHolders(holders: List<Holder<TYPE>>, amount: Int): INGREDIENT = fromSet(HolderSet.direct(holders), amount)

    fun fromSet(holderSet: HolderSet<TYPE>, amount: Int): INGREDIENT

    // TagKey
    fun fromTagKey(tagKey: TagKey<TYPE>, amount: Int): INGREDIENT

    fun fromTagKeys(vararg tagKeys: TagKey<TYPE>, amount: Int): INGREDIENT = fromTagKeys(tagKeys.toList(), amount)

    fun fromTagKeys(tagKeys: Iterable<TagKey<TYPE>>, amount: Int): INGREDIENT

    // Codec
    fun codec(): BiCodec<RegistryFriendlyByteBuf, INGREDIENT>
}
