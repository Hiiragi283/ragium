package hiiragi283.lib.data

import hiiragi283.lib.HTComparators
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTIdOrValue
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.holdersets.OrHolderSet

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
abstract class RegistryDataProvider {
    protected fun getHasName(id: HTIdOrValue<*>): String = "has_${id.idOrThrow.path}"

    protected fun getHasName(tagKey: TagKey<*>): String = "has_${tagKey.location().path.replace("/", "_")}"

    protected fun getHasName(prefix: HTTagPrefix, material: HTMaterialLike): String = getHasName(prefix.itemTagKey(material))

    // Registry

    /**
     * レジストリへのアクセス
     */
    protected abstract val registries: HolderLookup.Provider

    /**
     * [HolderSet]を取得します。
     * @param tagKey 対応するタグ
     */
    protected fun <T : Any> holderSet(tagKey: TagKey<T>): HolderSet<T> = this.registries.getOrThrow(tagKey)

    protected fun <T : Any> holderSet(tagKeys: Iterable<TagKey<T>>): HolderSet<T> = when (tagKeys.count()) {
        0 -> HolderSet.empty()
        1 -> holderSet(tagKeys.first())
        else -> tagKeys.sortedWith(HTComparators.TAG_KEY).map(::holderSet).let(::OrHolderSet)
    }

    protected fun <T : Any> holderSet(vararg tagKeys: TagKey<T>): HolderSet<T> = holderSet(tagKeys.toSet())

    /**
     * [HolderSet]を取得します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    protected fun holderSet(prefix: HTTagPrefix, material: HTMaterialLike): HolderSet<Item> = holderSet(prefix.itemTagKey(material))

    protected fun holderSet(prefix: HTTagPrefix, vararg materials: HTMaterialLike): HolderSet<Item> = materials.map(prefix::itemTagKey).let(::holderSet)

    /**
     * [HolderSet]を取得します。
     * @param content 液体タグの提供元
     */
    protected fun holderSet(content: HTFluidContent): HolderSet<Fluid> = holderSet(content.fluidTag)

    protected fun waterSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.WATER)

    protected fun lavaSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.LAVA)

    protected fun milkSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.MILK)
}
