package hiiragi283.lib.data.recipe

import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialManager
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.tag.HTTagMaterial
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.holdersets.OrHolderSet

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTRecipeProviderContext {
    /**
     * レシピの出力先
     */
    protected abstract val exporter: HTRecipeExporter

    /**
     * レジストリへのアクセス
     */
    protected abstract val registries: HolderLookup.Provider

    //    Extensions    //

    protected fun getHasName(id: HTIdLike): String = "has_${id.path}"

    protected fun getHasName(tagKey: TagKey<*>): String = "has_${tagKey.location().path.replace("/", "_")}"

    // Material
    /**
     * 素材を管理するマネージャを取得します。
     * @since 26.1.2
     */
    protected val materialManager: HTMaterialManager by lazy(HTMaterial::getManager)

    // Registry
    /**
     * [HolderSet]を取得します。
     * @param tagKey 対応するタグ
     */
    protected fun <T : Any> holderSet(tagKey: TagKey<T>): HolderSet<T> = this.registries.getOrThrow(tagKey)

    protected fun <T : Any> holderSet(tagKeys: Iterable<TagKey<T>>): HolderSet<T> = when (tagKeys.count()) {
        0 -> HolderSet.empty()
        1 -> holderSet(tagKeys.first())
        else -> tagKeys.map(::holderSet).sortedBy { it.unwrapKey().orElseThrow().location() }.let(::OrHolderSet)
    }

    protected fun <T : Any> holderSet(vararg tagKeys: TagKey<T>): HolderSet<T> = holderSet(tagKeys.toSet())

    /**
     * [HolderSet]を取得します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    protected fun holderSet(prefix: HTTagPrefix, material: HTTagMaterial): HolderSet<Item> = holderSet(prefix.itemTagKey(material))

    protected fun holderSet(prefix: HTTagPrefix, vararg materials: HTTagMaterial): HolderSet<Item> = materials.map(prefix::itemTagKey).let(::holderSet)

    /**
     * [HolderSet]を取得します。
     * @param content 液体タグの提供元
     */
    protected fun holderSet(content: HTFluidContent): HolderSet<Fluid> = holderSet(content.fluidTag)

    protected fun waterSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.WATER)

    protected fun lavaSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.LAVA)

    protected fun milkSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.MILK)

    // Recipe Builder
    protected inline fun netheriteUpgrade(builderAction: HTSmithingRecipeBuilder.() -> Unit): HTSmithingRecipeBuilder = HTSmithingRecipeBuilder.create {
        template { items { +Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE } }
        addition { +holderSet(Tags.Items.INGOTS_NETHERITE) }
        builderAction()
    }

    //    Delegated    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    abstract class Delegated : HTRecipeProviderContext() {
        protected lateinit var delegate: HTRecipeProviderContext

        final override val exporter: HTRecipeExporter get() = delegate.exporter

        final override val registries: HolderLookup.Provider get() = delegate.registries
    }
}
