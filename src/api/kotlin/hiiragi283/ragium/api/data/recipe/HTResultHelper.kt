package hiiragi283.ragium.api.data.recipe

import com.almostreliable.unified.api.AlmostUnified
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.fml.ModList
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Supplier

/**
 * 各種[HTRecipeResult]を生成するヘルパー
 */
data object HTResultHelper {
    //    Item    //

    /**
     * 指定した引数から[HTItemResult]を返します。
     * @param item アイテムのインスタンス
     * @param count アイテムの量
     */
    @JvmStatic
    fun item(item: ItemLike, count: Int = 1): HTItemResult = item(ImmutableItemStack.of(item, count))

    /**
     * 指定した引数から[HTItemResult]を返します。
     * @param stack ベースとなる[ItemStack]
     */
    @JvmStatic
    fun item(stack: ItemStack): HTItemResult = item(stack.toImmutableOrThrow())

    /**
     * 指定した引数から[HTItemResult]を返します。
     * @param stack ベースとなる[ImmutableItemStack]
     */
    @JvmStatic
    fun item(stack: ImmutableItemStack): HTItemResult = item(stack.getId(), stack.amount(), stack.componentsPatch())

    /**
     * 指定した引数から[HTItemResult]を返します。
     * @param id アイテムのID
     * @param count アイテムの量
     * @param component アイテムに紐づけるコンポーネント
     */
    @JvmStatic
    fun item(id: ResourceLocation, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult =
        HTItemResult(HTKeyOrTagHelper.INSTANCE.create(Registries.ITEM, id), count, component)

    /**
     * 指定した引数から[HTItemResult]を返します。
     */
    @JvmStatic
    fun item(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): HTItemResult = item(prefix.itemTagKey(material), count)

    /**
     * 指定した引数から[HTItemResult]を返します。
     * @param tagKey アイテムのタグ
     * @param count アイテムの量
     */
    @JvmStatic
    fun item(tagKey: TagKey<Item>, count: Int = 1): HTItemResult {
        if (ModList.get().isLoaded("almostunified")) {
            val target: Item? = AlmostUnified.INSTANCE.getTagTargetItem(tagKey)
            if (target != null) {
                return item(target, count)
            }
        }
        return HTItemResult(HTKeyOrTagHelper.INSTANCE.create(tagKey), count, DataComponentPatch.EMPTY)
    }

    //    Fluid    //

    /**
     * 指定した引数から[HTFluidResult]を返します。
     * @param fluid 液体の[Supplier]
     * @param amount　液体の量
     */
    @JvmStatic
    fun fluid(fluid: Supplier<out Fluid>, amount: Int): HTFluidResult = fluid(fluid.get(), amount)

    /**
     * 指定した引数から[HTFluidResult]を返します。
     * @param fluid 液体のインスタンス
     * @param amount　液体の量
     */
    @JvmStatic
    fun fluid(fluid: Fluid, amount: Int): HTFluidResult = fluid(ImmutableFluidStack.of(fluid, amount))

    /**
     * 指定した引数から[HTFluidResult]を返します。
     * @param stack ベースとなる[FluidStack]
     */
    @JvmStatic
    fun fluid(stack: FluidStack): HTFluidResult = fluid(stack.toImmutableOrThrow())

    /**
     * 指定した引数から[HTFluidResult]を返します。
     * @param stack ベースとなる[ImmutableFluidStack]
     */
    @JvmStatic
    fun fluid(stack: ImmutableFluidStack): HTFluidResult = fluid(stack.getId(), stack.amount(), stack.componentsPatch())

    /**
     * 指定した引数から[HTFluidResult]を返します。
     * @param id 液体のID
     * @param amount 液体の量
     * @param component 液体に紐づけるコンポーネント
     */
    @JvmStatic
    fun fluid(id: ResourceLocation, amount: Int, component: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResult =
        HTFluidResult(HTKeyOrTagHelper.INSTANCE.create(Registries.FLUID, id), amount, component)

    /**
     * 指定した引数から[HTFluidResult]を返します。
     * @param tagKey 液体のタグ
     * @param amount 液体の量
     */
    @JvmStatic
    fun fluid(tagKey: TagKey<Fluid>, amount: Int): HTFluidResult =
        HTFluidResult(HTKeyOrTagHelper.INSTANCE.create(tagKey), amount, DataComponentPatch.EMPTY)
}
