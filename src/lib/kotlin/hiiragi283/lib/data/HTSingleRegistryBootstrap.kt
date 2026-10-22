package hiiragi283.lib.data

import hiiragi283.lib.HTComparators
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.toId
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.core.registries.SingleRegistryBootstrap
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockItemTagId
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import java.util.stream.Stream

/**
 * @author Hiiragi Tsubasa
 * @since 26.3.0
 */
abstract class HTSingleRegistryBootstrap<R : Any>(protected val modId: String) : SingleRegistryBootstrap<R> {
    lateinit var context: BootstrapContext<R>
        private set

    final override fun run(registry: BootstrapContext<R>) {
        this.context = object : BootstrapContext<R> {
            override fun register(key: ResourceKey<R>, value: R): Holder.Reference<R> =
                registry.register(modifyId(key), value)

            override fun <S : Any> lookup(key: ResourceKey<out Registry<out S>>): HolderGetter<S> = registry.lookup(key)

            @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
            override fun <S : Any> listContextElements(
                key: ResourceKey<out Registry<out S>>
            ): Stream<Holder.Reference<S>> = registry.listContextElements(key)
        }
        register()
    }

    protected abstract fun register()

    protected fun modifyId(key: ResourceKey<R>): ResourceKey<R> =
        ResourceKey.create(key.registryKey(), key.identifier().let(::modifyId))

    /**
     * 受け取った[id]を[context]内で変換します。
     */
    protected open fun modifyId(id: Identifier): Identifier = modId.toId(id.path)

    //    Extensions    //

    // Identifier

    /**
     * 指定した[パス][path]から[ID][Identifier]を作成します。
     * @return [modId]を[名前空間][Identifier.getNamespace]とする[ID][Identifier]
     */
    protected fun id(path: String): Identifier = modId.toId(path)

    /**
     * 指定した[パス][path]から[ID][Identifier]を作成します。
     * @return [modId]を[名前空間][Identifier.getNamespace]とする[ID][Identifier]
     */
    protected fun id(vararg path: String): Identifier = modId.toId(*path)

    // Registry

    protected fun <T : Any> holder(key: ResourceKey<T>): Holder.Reference<T> = this.context.getOrThrow(key)

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
