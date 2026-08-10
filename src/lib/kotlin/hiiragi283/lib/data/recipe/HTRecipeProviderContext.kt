package hiiragi283.lib.data.recipe

import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialManager
import hiiragi283.lib.material.part.HTPart
import hiiragi283.lib.material.part.HTPartManager
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.holdersets.OrHolderSet

abstract class HTRecipeProviderContext {
    /**
     * レシピの出力先
     */
    abstract val exporter: HTRecipeExporter

    /**
     * レジストリへのアクセス
     */
    abstract val registries: HolderLookup.Provider

    //    Extensions    //

    fun getHasName(id: HTIdLike): String = "has_${id.path}"

    fun getHasName(tagKey: TagKey<*>): String = "has_${tagKey.location().path.replace("/", "_")}"

    // Material
    /**
     * 部品を管理するマネージャを取得します。
     */
    protected val partManager: HTPartManager by lazy(HTPart::getManager)

    /**
     * 素材を管理するマネージャを取得します。
     */
    protected val materialManager: HTMaterialManager by lazy(HTMaterial::getManager)

    /**
     * [TagKey]を取得します。
     * @param prefix タグのプレフィックス
     * @param key タグの種類を表す素材
     */
    fun tag(prefix: HTTagPrefix, key: HTMaterialKey): TagKey<Item> = prefix.itemTagKey(key)

    fun baseOrPrefix(key: HTMaterialKey, prefix: HTTagPrefix): HolderSet<Item> = setOfNotNull(
        prefix.itemTagKey(key),
        /*, materialManager[key]?.getDefaultPart(key)*/
    ).let(::holderSet)

    fun baseOrDust(key: HTMaterialKey): HolderSet<Item> = baseOrPrefix(key, CommonTagPrefixes.DUST)

    // Registry

    /**
     * [HolderSet]を取得します。
     * @param tagKey 対応するタグ
     */
    fun <T : Any> holderSet(tagKey: TagKey<T>): HolderSet<T> = this.registries.getOrThrow(tagKey)

    fun <T : Any> holderSet(tagKeys: Iterable<TagKey<T>>): HolderSet<T> = tagKeys.map(this.registries::getOrThrow).sortedBy { it.unwrapKey().orElseThrow().location() }.let(::OrHolderSet)

    fun <T : Any> holderSet(vararg tagKeys: TagKey<T>): HolderSet<T> = holderSet(tagKeys.toSet())

    /**
     * [HolderSet]を取得します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    fun holderSet(prefix: HTTagPrefix, material: HTMaterialKey): HolderSet<Item> = holderSet(prefix.itemTagKey(material))

    /**
     * [HolderSet]を取得します。
     * @param content 液体タグの提供元
     */
    fun holderSet(content: HTFluidContent): HolderSet<Fluid> = holderSet(content.fluidTag)

    //    Delegated    //

    abstract class Delegated : HTRecipeProviderContext() {
        protected lateinit var delegate: HTRecipeProviderContext

        final override val exporter: HTRecipeExporter get() = delegate.exporter
    }
}
