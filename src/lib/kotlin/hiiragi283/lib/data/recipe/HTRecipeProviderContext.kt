package hiiragi283.lib.data.recipe

import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialManager
import hiiragi283.lib.material.part.HTPart
import hiiragi283.lib.material.part.HTPartManager
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTTagPrefix
import kotlin.collections.toSortedSet
import net.minecraft.core.HolderLookup
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

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

    fun baseOrPrefix(key: HTMaterialKey, prefix: HTTagPrefix): Set<TagKey<Item>> = setOfNotNull(prefix.itemTagKey(key), materialManager[key]?.getDefaultPart(key)).toSortedSet(HTComparators.TAG_KEY)

    fun baseOrDust(key: HTMaterialKey): Set<TagKey<Item>> = baseOrPrefix(key, CommonTagPrefixes.DUST)

    //    Delegated    //

    abstract class Delegated : HTRecipeProviderContext() {
        protected lateinit var delegate: HTRecipeProviderContext

        final override val exporter: HTRecipeExporter get() = delegate.exporter
    }
}
