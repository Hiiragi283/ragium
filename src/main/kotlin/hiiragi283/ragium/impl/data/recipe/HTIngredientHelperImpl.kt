package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.impl.recipe.ingredient.HTFluidIngredientImpl
import hiiragi283.ragium.impl.recipe.ingredient.HTItemIngredientImpl
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.holdersets.OrHolderSet

class HTIngredientHelperImpl(private val itemGetter: HolderGetter<Item>, private val fluidGetter: HolderGetter<Fluid>) :
    HTIngredientHelper {
    constructor(
        registries: HolderLookup.Provider,
    ) : this(registries.lookupOrThrow(Registries.ITEM), registries.lookupOrThrow(Registries.FLUID))

    override fun item(item: ItemLike, count: Int): HTItemIngredient = HTItemIngredientImpl.of(item, count = count)

    override fun item(vararg items: ItemLike, count: Int): HTItemIngredient = HTItemIngredientImpl.of(*items, count = count)

    override fun item(holderSet: HolderSet<Item>, count: Int): HTItemIngredient = HTItemIngredientImpl.of(holderSet, count)

    // override fun item(ingredient: ICustomIngredient, count: Int): HTItemIngredient = HTItemIngredientImpl.of(ingredient, count)

    override fun item(tagKey: TagKey<Item>, count: Int): HTItemIngredient = item(itemGetter.getOrThrow(tagKey), count)

    override fun itemTags(tagKeys: Iterable<TagKey<Item>>, count: Int): HTItemIngredient =
        item(OrHolderSet(tagKeys.map(itemGetter::getOrThrow)), count)

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

    override fun gemOrDust(name: String, count: Int): HTItemIngredient = itemTags(
        HTItemMaterialVariant.DUST.itemTagKey(name),
        HTItemMaterialVariant.GEM.itemTagKey(name),
        count = count,
    )

    override fun ingotOrDust(name: String, count: Int): HTItemIngredient = itemTags(
        HTItemMaterialVariant.DUST.itemTagKey(name),
        HTItemMaterialVariant.INGOT.itemTagKey(name),
        count = count,
    )

    override fun fluid(fluid: Fluid, amount: Int): HTFluidIngredient = HTFluidIngredientImpl.of(fluid, amount = amount)

    override fun fluid(holderSet: HolderSet<Fluid>, amount: Int): HTFluidIngredient = HTFluidIngredientImpl.of(holderSet, amount)

    // override fun fluid(ingredient: FluidIngredient, amount: Int): HTFluidIngredient = HTFluidIngredientImpl.of(ingredient, amount)

    override fun fluid(tagKey: TagKey<Fluid>, amount: Int): HTFluidIngredient = fluid(fluidGetter.getOrThrow(tagKey), amount)
}
