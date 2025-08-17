package hiiragi283.ragium.api.data.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Supplier

object HTResultHelper {
    //    Item    //

    fun item(item: ItemLike, count: Int = 1): HTItemResult = item(ItemStack(item, count))

    fun item(stack: ItemStack): HTItemResult = item(stack.itemHolder.idOrThrow, stack.count, stack.componentsPatch)

    fun item(id: ResourceLocation, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult =
        HTItemResult(Either.left(id), count, component)

    fun item(variant: HTMaterialVariant, material: HTMaterialType, count: Int = 1): HTItemResult = item(variant.itemTagKey(material), count)

    fun item(tagKey: TagKey<Item>, count: Int = 1): HTItemResult = HTItemResult(Either.right(tagKey), count, DataComponentPatch.EMPTY)

    //    Fluid    //

    fun fluid(fluid: Supplier<out Fluid>, amount: Int): HTFluidResult = fluid(fluid.get(), amount)

    fun fluid(fluid: Fluid, amount: Int): HTFluidResult = fluid(FluidStack(fluid, amount))

    fun fluid(stack: FluidStack): HTFluidResult = fluid(stack.fluidHolder.idOrThrow, stack.amount, stack.componentsPatch)

    fun fluid(id: ResourceLocation, amount: Int, component: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResult =
        HTFluidResult(Either.left(id), amount, component)

    fun fluid(tagKey: TagKey<Fluid>, amount: Int): HTFluidResult = HTFluidResult(Either.right(tagKey), amount, DataComponentPatch.EMPTY)
}
