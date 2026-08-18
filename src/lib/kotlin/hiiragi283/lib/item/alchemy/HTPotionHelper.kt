package hiiragi283.lib.item.alchemy

import hiiragi283.lib.data.DataComponentSetter
import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.util.HTTextResult
import kotlin.jvm.optionals.getOrNull
import net.minecraft.core.Holder
import net.minecraft.core.TypedInstance
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

/**
 * @author Hiiragi Tsubasa
 * @since 0.3.0
 */
data object HTPotionHelper {
    //    DataComponentGetter    //

    /**
     * 指定した[getter]から[PotionContents]を取得します。
     * @return 値を保持していない場合は[PotionContents.EMPTY]
     * @since 0.10.0
     */
    @JvmStatic
    fun getPotion(getter: DataComponentGetter): PotionContents = getter.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)

    /**
     * @since 0.14.0
     */
    @JvmStatic
    fun setPotion(setter: DataComponentSetter, contents: PotionContents?) {
        setter.setOrRemove(DataComponents.POTION_CONTENTS, contents)
    }

    /**
     * 指定した[getter]からポーションのMod IDを取得します。
     * @since 0.11.0
     */
    @JvmStatic
    fun getPotionModId(getter: DataComponentGetter): String? = getPotion(getter)
        .potion()
        .flatMap(Holder<Potion>::unwrapKey)
        .map(ResourceKey<Potion>::identifier)
        .map(Identifier::getNamespace)
        .getOrNull()

    //    ItemStack    //

    @JvmStatic
    fun createPotion(potion: Holder<Potion>, bottleType: HTBottleType = HTBottleType.DEFAULT): ItemStackTemplate = ItemInstanceBuilder.buildTemplate {
        +bottleType.asItem()
        components { set(DataComponents.POTION_CONTENTS, PotionContents(potion)) }
    }

    /**
     * 指定した[contents]からポーションの[ItemStack]を作成します。
     * @since 0.11.0
     */
    @JvmStatic
    fun createPotion(contents: BottledPotionContents): ItemStackTemplate = ItemInstanceBuilder.buildTemplate {
        +contents.bottleType.asItem()
        components { set(DataComponents.POTION_CONTENTS, contents.contents) }
    }

    /**
     * 指定した引数からポーションの[ItemStack]を作成します。
     * @param item アイテムの種類
     * @param potion ポーションの中身
     * @param count [ItemStack]の個数
     */
    @JvmStatic
    fun createPotion(item: ItemLike, potion: Holder<Potion>, count: Int = 1): HTTextResult<ItemStackTemplate> = createPotion(item, PotionContents(potion), count)

    /**
     * 指定した引数からポーションの[ItemStack]を作成します。
     * @param item アイテムの種類
     * @param contents ポーションの中身
     * @param count [ItemStack]の個数
     */
    @JvmStatic
    fun createPotion(item: ItemLike, contents: PotionContents, count: Int = 1): HTTextResult<ItemStackTemplate> = ItemInstanceBuilder.buildSafeTemplate {
        +item.asItem()
        this.count = count
        components { set(DataComponents.POTION_CONTENTS, contents) }
    }

    /**
     * 指定した[instance]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmName("getContentsFromItem")
    @JvmStatic
    fun <T> getContents(instance: T): BottledPotionContents? where T : TypedInstance<Item>, T : DataComponentGetter {
        val bottleType: HTBottleType = HTPotionFluidManager.Handler.DEFAULT[instance] ?: return null
        val contents: PotionContents = getPotion(instance)
        return BottledPotionContents(contents, bottleType)
    }

    /**
     * 指定した[instance]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmStatic
    fun <T> getContentsFromBottle(instance: T): BottledPotionContents? where T : TypedInstance<Item>, T : DataComponentGetter {
        val bottleType: HTBottleType = HTBottleType.getBottleType(instance) ?: return null
        val contents: PotionContents = getPotion(instance)
        return BottledPotionContents(contents, bottleType)
    }

    @JvmStatic
    fun fillItemPatch(contents: BottledPotionContents, builder: DataComponentPatch.Builder) {
        val (contents1: PotionContents, bottleType: HTBottleType) = contents
        builder.set(DataComponents.POTION_CONTENTS, contents1)
        HTPotionFluidManager.Handler.DEFAULT[builder] = bottleType
    }

    @JvmStatic
    fun createItemPatch(contents: BottledPotionContents): DataComponentPatch = buildDataPatch { fillItemPatch(contents, this) }

    //    FluidStack    //

    /**
     * 指定した[instance]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmName("getContentsFromFluid")
    @JvmStatic
    fun <T> getContents(instance: T): BottledPotionContents? where T : TypedInstance<Fluid>, T : DataComponentGetter = when {
        instance.`is`(Tags.Fluids.WATER) -> BottledPotionContents(Potions.WATER)
        else -> HTPotionFluidManager.getHandlerOrDefault(instance.typeHolder().value())[instance]?.let { bottleType: HTBottleType ->
            val contents: PotionContents = getPotion(instance)
            BottledPotionContents(contents, bottleType)
        }
    }

    @JvmStatic
    fun <T> fillFluidPatch(instance: T, contents: BottledPotionContents, builder: DataComponentPatch.Builder) where T : TypedInstance<Fluid>, T : DataComponentGetter {
        fillFluidPatch(instance.typeHolder().value(), contents, builder)
    }

    @JvmStatic
    fun fillFluidPatch(fluid: Fluid, contents: BottledPotionContents, builder: DataComponentPatch.Builder) {
        val handler: HTPotionFluidManager.Handler = HTPotionFluidManager.getHandler(fluid) ?: return
        val (contents1: PotionContents, bottleType: HTBottleType) = contents
        builder.set(DataComponents.POTION_CONTENTS, contents1)
        handler[builder] = bottleType
    }

    @JvmStatic
    fun createFluidPatch(fluid: Fluid, contents: BottledPotionContents): DataComponentPatch = buildDataPatch { fillFluidPatch(fluid, contents, this) }
}
