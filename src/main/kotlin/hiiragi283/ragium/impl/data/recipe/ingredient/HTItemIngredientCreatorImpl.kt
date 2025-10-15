package hiiragi283.ragium.impl.data.recipe.ingredient

import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.common.material.HTItemMaterialVariant
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

    override fun fuelOrDust(material: HTMaterialType, count: Int): HTItemIngredient = multiVariants(
        material,
        HTItemMaterialVariant.DUST,
        HTItemMaterialVariant.FUEL,
        count = count,
    )

    override fun gemOrDust(material: HTMaterialType, count: Int): HTItemIngredient = multiVariants(
        material,
        HTItemMaterialVariant.DUST,
        HTItemMaterialVariant.GEM,
        count = count,
    )

    override fun ingotOrDust(material: HTMaterialType, count: Int): HTItemIngredient = multiVariants(
        material,
        HTItemMaterialVariant.DUST,
        HTItemMaterialVariant.INGOT,
        count = count,
    )

    override fun gemOrDust(name: String, count: Int): HTItemIngredient = fromTagKeys(
        HTItemMaterialVariant.DUST.itemTagKey(name),
        HTItemMaterialVariant.GEM.itemTagKey(name),
        amount = count,
    )

    override fun ingotOrDust(name: String, count: Int): HTItemIngredient = fromTagKeys(
        HTItemMaterialVariant.DUST.itemTagKey(name),
        HTItemMaterialVariant.INGOT.itemTagKey(name),
        amount = count,
    )
}
