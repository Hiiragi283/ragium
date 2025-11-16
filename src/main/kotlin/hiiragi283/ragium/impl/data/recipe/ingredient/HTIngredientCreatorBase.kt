package hiiragi283.ragium.impl.data.recipe.ingredient

import hiiragi283.ragium.api.data.recipe.ingredient.HTIngredientCreator
import hiiragi283.ragium.api.recipe.ingredient.HTIngredient
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.registries.holdersets.OrHolderSet

abstract class HTIngredientCreatorBase<TYPE : Any, INGREDIENT : HTIngredient<TYPE, *>>(
    protected val getter: HolderGetter<TYPE>,
    protected val holderFactory: (TYPE) -> Holder<TYPE>,
) : HTIngredientCreator<TYPE, INGREDIENT> {
    final override fun from(type: TYPE, amount: Int): INGREDIENT = fromHolder(holderFactory(type), amount)

    final override fun from(types: Collection<TYPE>, amount: Int): INGREDIENT = when (types.size) {
        1 -> from(types.first(), amount)
        else -> fromHolders(types.map(holderFactory), amount)
    }

    final override fun fromTagKey(tagKey: TagKey<TYPE>, amount: Int): INGREDIENT = fromSet(getter.getOrThrow(tagKey), amount)

    final override fun fromTagKeys(tagKeys: Collection<TagKey<TYPE>>, amount: Int): INGREDIENT = when (tagKeys.size) {
        1 -> fromTagKey(tagKeys.first(), amount)
        else -> fromSet(OrHolderSet(tagKeys.map(getter::getOrThrow)), amount)
    }
}
