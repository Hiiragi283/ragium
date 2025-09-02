package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.keyOrThrow
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Supplier

interface HTResultHelper {
    companion object {
        @JvmField
        val INSTANCE: HTResultHelper = RagiumAPI.getInstance().getResultHelper()
    }

    //    Item    //

    fun item(item: ItemLike, count: Int = 1): HTItemResult = item(ItemStack(item, count))

    fun item(stack: ItemStack): HTItemResult = item(stack.itemHolder.keyOrThrow, stack.count, stack.componentsPatch)

    fun item(id: ResourceLocation, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult =
        item(ResourceKey.create(Registries.ITEM, id), count, component)

    fun item(key: ResourceKey<Item>, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult

    fun item(variant: HTMaterialVariant.ItemTag, material: HTMaterialType, count: Int = 1): HTItemResult =
        item(variant.itemTagKey(material), count)

    fun item(tagKey: TagKey<Item>, count: Int = 1): HTItemResult

    //    Fluid    //

    fun fluid(fluid: Supplier<out Fluid>, amount: Int): HTFluidResult = fluid(fluid.get(), amount)

    fun fluid(fluid: Fluid, amount: Int): HTFluidResult = fluid(FluidStack(fluid, amount))

    fun fluid(stack: FluidStack): HTFluidResult = fluid(stack.fluidHolder.keyOrThrow, stack.amount, stack.componentsPatch)

    fun fluid(id: ResourceLocation, amount: Int, component: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResult =
        fluid(ResourceKey.create(Registries.FLUID, id), amount, component)

    fun fluid(key: ResourceKey<Fluid>, amount: Int, component: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResult

    fun fluid(tagKey: TagKey<Fluid>, amount: Int): HTFluidResult
}
