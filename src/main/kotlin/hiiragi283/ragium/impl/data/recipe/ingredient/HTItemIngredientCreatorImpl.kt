package hiiragi283.ragium.impl.data.recipe.ingredient

import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.core.HolderGetter
import net.minecraft.world.item.Item

@Suppress("DEPRECATION")
class HTItemIngredientCreatorImpl(getter: HolderGetter<Item>) :
    HTIngredientCreatorBase<Item, HTItemIngredient>(getter, Item::builtInRegistryHolder),
    HTItemIngredientCreator
