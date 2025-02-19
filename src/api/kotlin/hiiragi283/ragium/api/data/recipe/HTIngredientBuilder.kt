package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.commonTag
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.registries.DeferredHolder

data object HTIngredientBuilder {
    //    Item    //

    @JvmStatic
    fun item(item: ItemLike, count: Int = 1): SizedIngredient = item(Ingredient.of(item), count)

    @JvmStatic
    fun item(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): SizedIngredient = item(prefix.createTag(material), count)

    @JvmStatic
    fun item(tagKey: TagKey<Item>, count: Int = 1): SizedIngredient = item(Ingredient.of(tagKey), count)

    @JvmStatic
    fun item(ingredient: ICustomIngredient, count: Int = 1): SizedIngredient = item(ingredient.toVanilla(), count)

    @JvmStatic
    fun item(ingredient: Ingredient, count: Int = 1): SizedIngredient = SizedIngredient(ingredient, count)

    //    Fluid    //

    @JvmStatic
    fun fluid(fluid: DeferredHolder<Fluid, *>, amount: Int = FluidType.BUCKET_VOLUME): SizedFluidIngredient = fluid(fluid.commonTag, amount)

    @JvmStatic
    fun fluid(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): SizedFluidIngredient = fluid(FluidIngredient.of(fluid), amount)

    @JvmStatic
    fun fluid(tagKey: TagKey<Fluid>, amount: Int = FluidType.BUCKET_VOLUME): SizedFluidIngredient =
        fluid(FluidIngredient.tag(tagKey), amount)

    @JvmStatic
    fun fluid(ingredient: FluidIngredient, amount: Int = FluidType.BUCKET_VOLUME): SizedFluidIngredient =
        SizedFluidIngredient(ingredient, amount)

    @JvmStatic
    fun water(amount: Int = FluidType.BUCKET_VOLUME): SizedFluidIngredient = fluid(Tags.Fluids.WATER, amount)

    @JvmStatic
    fun milk(amount: Int = FluidType.BUCKET_VOLUME): SizedFluidIngredient = fluid(Tags.Fluids.MILK, amount)
}
