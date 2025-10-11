package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.api.serialization.codec.BiCodec
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Supplier

/**
 * 各種[hiiragi283.ragium.api.recipe.result.HTRecipeResult]を生成するヘルパー
 */
interface HTResultHelper {
    companion object {
        /**
         * [HTResultHelper]の実装のインスタンス
         */
        @JvmField
        val INSTANCE: HTResultHelper = RagiumAPI.getService()
    }

    fun itemCodec(): BiCodec<RegistryFriendlyByteBuf, HTItemResult>

    fun fluidCodec(): BiCodec<RegistryFriendlyByteBuf, HTFluidResult>

    //    Item    //

    /**
     * 指定した引数から[HTItemResult]を返します。
     * @param item アイテムのインスタンス
     * @param count アイテムの量
     */
    fun item(item: ItemLike, count: Int = 1): HTItemResult = item(ItemStack(item, count))

    /**
     * 指定した引数から[HTItemResult]を返します。
     * @param stack ベースとなる[ItemStack]
     */
    fun item(stack: ItemStack): HTItemResult = item(stack.itemHolder.idOrThrow, stack.count, stack.componentsPatch)

    /**
     * 指定した引数から[HTItemResult]を返します。
     * @param id アイテムのID
     * @param count アイテムの量
     * @param component アイテムに紐づけるコンポーネント
     */
    fun item(id: ResourceLocation, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult

    /**
     * 指定した引数から[HTItemResult]を返します。
     */
    fun item(variant: HTMaterialVariant.ItemTag, material: HTMaterialType, count: Int = 1): HTItemResult =
        item(variant.itemTagKey(material), count)

    /**
     * 指定した引数から[HTItemResult]を返します。
     * @param tagKey アイテムのタグ
     * @param count アイテムの量
     */
    fun item(tagKey: TagKey<Item>, count: Int = 1): HTItemResult

    //    Fluid    //

    /**
     * 指定した引数から[HTFluidResult]を返します。
     * @param fluid 液体の[Supplier]
     * @param amount　液体の量
     */
    fun fluid(fluid: Supplier<out Fluid>, amount: Int): HTFluidResult = fluid(fluid.get(), amount)

    /**
     * 指定した引数から[HTFluidResult]を返します。
     * @param fluid 液体のインスタンス
     * @param amount　液体の量
     */
    fun fluid(fluid: Fluid, amount: Int): HTFluidResult = fluid(FluidStack(fluid, amount))

    /**
     * 指定した引数から[HTFluidResult]を返します。
     * @param stack ベースとなる[FluidStack]
     */
    fun fluid(stack: FluidStack): HTFluidResult = fluid(stack.fluidHolder.idOrThrow, stack.amount, stack.componentsPatch)

    /**
     * 指定した引数から[HTFluidResult]を返します。
     * @param id 液体のID
     * @param amount 液体の量
     * @param component 液体に紐づけるコンポーネント
     */
    fun fluid(id: ResourceLocation, amount: Int, component: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResult

    /**
     * 指定した引数から[HTFluidResult]を返します。
     * @param tagKey 液体のタグ
     * @param amount 液体の量
     */
    fun fluid(tagKey: TagKey<Fluid>, amount: Int): HTFluidResult
}
