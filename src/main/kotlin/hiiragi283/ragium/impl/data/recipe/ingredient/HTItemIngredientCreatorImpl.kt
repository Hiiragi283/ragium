package hiiragi283.ragium.impl.data.recipe.ingredient

import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.impl.recipe.ingredient.HTItemIngredientImpl
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item

@Suppress("DEPRECATION")
class HTItemIngredientCreatorImpl(getter: HolderGetter<Item>) :
    HTIngredientCreatorBase<Item, HTItemIngredient>(getter, Item::builtInRegistryHolder),
    HTItemIngredientCreator {
    override fun fromSet(holderSet: HolderSet<Item>, amount: Int): HTItemIngredient = HTItemIngredientImpl(holderSet, amount)

    override fun codec(): BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> = HTItemIngredientImpl.CODEC

    override fun fuelOrDust(material: HTMaterialLike, count: Int): HTItemIngredient = multiPrefixes(
        material,
        CommonMaterialPrefixes.DUST,
        CommonMaterialPrefixes.FUEL,
        count = count,
    )

    override fun gemOrDust(material: HTMaterialLike, count: Int): HTItemIngredient = multiPrefixes(
        material,
        CommonMaterialPrefixes.DUST,
        CommonMaterialPrefixes.GEM,
        count = count,
    )

    override fun ingotOrDust(material: HTMaterialLike, count: Int): HTItemIngredient = multiPrefixes(
        material,
        CommonMaterialPrefixes.DUST,
        CommonMaterialPrefixes.INGOT,
        count = count,
    )
}
