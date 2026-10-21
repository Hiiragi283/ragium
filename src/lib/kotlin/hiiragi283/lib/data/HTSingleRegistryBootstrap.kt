package hiiragi283.lib.data

import hiiragi283.lib.HTComparators
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.SingleRegistryBootstrap
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.BlockItemTagId
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

/**
 * @author Hiiragi Tsubasa
 * @since 26.3.0
 */
abstract class HTSingleRegistryBootstrap<T : Any> : SingleRegistryBootstrap<T> {
    lateinit var context: BootstrapContext<T>
        private set

    final override fun run(registry: BootstrapContext<T>) {
        this.context = registry
        register()
    }

    protected abstract fun register()

    //    Extensions    //

    /**
     * [HolderSet]を取得します。
     * @param tagKey 対応するタグ
     */
    protected fun <T : Any> holderSet(tagKey: TagKey<T>): HolderSet<T> = this.context.getOrThrow(tagKey)

    protected fun <T : Any> holderSet(tagKeys: Iterable<TagKey<T>>): HolderSet<T> {
        if (tagKeys is Collection<TagKey<T>>) {
            return when (tagKeys.size) {
                0 -> HolderSet.empty()
                1 -> holderSet(tagKeys.first())
                else -> tagKeys.map(::holderSet).let(HTComparators::compressHolderSet)
            }
        }

        val it: Iterator<TagKey<T>> = tagKeys.iterator()
        if (!it.hasNext()) {
            return HolderSet.empty()
        }
        val element: HolderSet<T> = holderSet(it.next())
        if (!it.hasNext()) {
            return element
        }
        val dst = ObjectArrayList<HolderSet<T>>()
        dst.add(element)
        while (it.hasNext()) dst.add(holderSet(it.next()))
        return dst.let(HTComparators::compressHolderSet)
    }

    protected fun <T : Any> holderSet(tagKeys: Sequence<TagKey<T>>): HolderSet<T> {
        val it: Iterator<TagKey<T>> = tagKeys.iterator()
        if (!it.hasNext()) {
            return HolderSet.empty()
        }
        val element: HolderSet<T> = holderSet(it.next())
        if (!it.hasNext()) {
            return element
        }
        val dst = ObjectArrayList<HolderSet<T>>()
        dst.add(element)
        while (it.hasNext()) dst.add(holderSet(it.next()))
        return dst.let(HTComparators::compressHolderSet)
    }

    protected fun <T : Any> holderSet(vararg tagKeys: TagKey<T>): HolderSet<T> = holderSet(tagKeys.toSet())

    protected fun blockSet(tagKey: BlockItemTagId): HolderSet<Block> = holderSet(tagKey.block())

    protected fun itemSet(tagKey: BlockItemTagId): HolderSet<Item> = holderSet(tagKey.item())

    /**
     * [HolderSet]を取得します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    protected fun holderSet(prefix: HTTagPrefix, material: HTMaterialLike): HolderSet<Item> =
        holderSet(prefix.itemTagKey(material))

    /**
     * [HolderSet]を取得します。
     * @param content 液体タグの提供元
     */
    protected fun holderSet(content: HTFluidContent): HolderSet<Fluid> = holderSet(content.fluidTag)

    protected fun waterSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.WATER)

    protected fun lavaSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.LAVA)

    protected fun milkSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.MILK)
}
