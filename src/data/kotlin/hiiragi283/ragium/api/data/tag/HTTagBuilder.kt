package hiiragi283.ragium.api.data.tag

import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.RegistryKey
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import java.util.function.Consumer

/**
 * 登録した[TagKey]をソートして生成するビルダー
 */
class HTTagBuilder<T : Any>(val registryKey: RegistryKey<T>, private val consumer: Consumer<TagEntry>) {
    /**
     * 指定した[ResourceKey]を[TagKey]に登録します。
     */
    fun add(key: ResourceKey<T>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = add(key.location(), type)

    /**
     * 指定した[HTHolderLike]を[TagKey]に登録します。
     */
    fun add(holder: HTHolderLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = add(holder.getId(), type)

    /**
     * 指定した[ResourceLocation]を[TagKey]に登録します。
     */
    fun add(id: ResourceLocation, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = apply {
        when (type) {
            HTTagDependType.REQUIRED -> TagEntry.element(id)
            HTTagDependType.OPTIONAL -> TagEntry.optionalElement(id)
        }.let(consumer::accept)
    }

    fun addTag(prefix: HTPrefixLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> =
        addTag(prefix.createCommonTagKey(registryKey), type)

    fun addTag(prefix: HTPrefixLike, material: HTMaterialLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> =
        addTag(prefix.createTagKey(registryKey, material), type)

    /**
     * 指定した[child]を[TagKey]に登録します。
     */
    fun addTag(child: TagKey<T>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = apply {
        when (type) {
            HTTagDependType.REQUIRED -> TagEntry.tag(child.location)
            HTTagDependType.OPTIONAL -> TagEntry.optionalTag(child.location)
        }.let(consumer::accept)
    }
}
