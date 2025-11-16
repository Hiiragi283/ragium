package hiiragi283.ragium.api.data.tag

import hiiragi283.ragium.api.collection.ImmutableMultiMap
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.RegistryKey
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey

/**
 * 登録した[TagKey]をソートして生成するビルダー
 */
@JvmRecord
data class HTTagBuilder<T : Any>(val registryKey: RegistryKey<T>, private val entryCache: ImmutableMultiMap.Builder<TagKey<T>, TagEntry>) {
    /**
     * 指定した[ResourceKey]を[TagKey]に登録します。
     */
    fun add(tagKey: TagKey<T>, key: ResourceKey<T>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> =
        add(tagKey, key.location(), type)

    /**
     * 指定した[HTHolderLike]を[TagKey]に登録します。
     */
    fun add(tagKey: TagKey<T>, holder: HTHolderLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> =
        add(tagKey, holder.getId(), type)

    /**
     * 指定した[ResourceLocation]を[TagKey]に登録します。
     */
    fun add(tagKey: TagKey<T>, id: ResourceLocation, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = apply {
        entryCache[tagKey] = when (type) {
            HTTagDependType.REQUIRED -> TagEntry.element(id)
            HTTagDependType.OPTIONAL -> TagEntry.optionalElement(id)
        }
    }

    fun addTag(tagKey: TagKey<T>, prefix: HTPrefixLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> =
        addTag(tagKey, createCommonTag(prefix), type)

    fun addTag(
        tagKey: TagKey<T>,
        prefix: HTPrefixLike,
        material: HTMaterialLike,
        type: HTTagDependType = HTTagDependType.REQUIRED,
    ): HTTagBuilder<T> = addTag(tagKey, createTag(prefix, material), type)

    /**
     * 指定した[child]を[TagKey]に登録します。
     */
    fun addTag(tagKey: TagKey<T>, child: TagKey<T>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = apply {
        entryCache[tagKey] = when (type) {
            HTTagDependType.REQUIRED -> TagEntry.tag(child.location)
            HTTagDependType.OPTIONAL -> TagEntry.optionalTag(child.location)
        }
    }

    fun addTags(
        parent: TagKey<T>,
        child: TagKey<T>,
        holder: HTHolderLike,
        type: HTTagDependType = HTTagDependType.REQUIRED,
    ): HTTagBuilder<T> = addTag(parent, child).add(child, holder, type)

    fun addMaterial(
        prefix: HTPrefixLike,
        material: HTMaterialLike,
        holder: HTHolderLike,
        type: HTTagDependType = HTTagDependType.REQUIRED,
    ): HTTagBuilder<T> {
        val commonTag: TagKey<T> = createCommonTag(prefix)
        val tagKey: TagKey<T> = createTag(prefix, material)
        return this.addTag(commonTag, tagKey).add(tagKey, holder, type)
    }

    fun addMaterial(
        prefix: HTPrefixLike,
        material: HTMaterialLike,
        child: TagKey<T>,
        type: HTTagDependType = HTTagDependType.REQUIRED,
    ): HTTagBuilder<T> {
        val commonTag: TagKey<T> = createCommonTag(prefix)
        val tagKey: TagKey<T> = createTag(prefix, material)
        return this.addTag(commonTag, tagKey).addTag(tagKey, child, type)
    }

    fun createCommonTag(prefix: HTPrefixLike): TagKey<T> = prefix.createCommonTagKey(registryKey)

    fun createTag(prefix: HTPrefixLike, material: HTMaterialLike): TagKey<T> = prefix.createTagKey(registryKey, material)
}
