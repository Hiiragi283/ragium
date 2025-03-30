package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

interface HTTagBuilder<T : Any> {
    val registryKey: ResourceKey<out Registry<T>>

    fun add(tagKey: TagKey<T>, entry: Entry)

    fun add(
        tagKey: TagKey<T>,
        mod: IntegrationMods,
        path: String,
        type: DependType = DependType.REQUIRED,
    ) {
        add(tagKey, mod.id(path), type)
    }

    fun add(tagKey: TagKey<T>, id: ResourceLocation, type: DependType = DependType.REQUIRED) {
        add(tagKey, Entry(id, false, type))
    }

    fun add(tagKey: TagKey<T>, holder: Holder<T>, type: DependType = DependType.REQUIRED) {
        add(tagKey, Entry(holder.idOrThrow, false, type))
    }

    fun addTag(tagKey: TagKey<T>, child: ResourceLocation, type: DependType = DependType.REQUIRED) {
        addTag(tagKey, TagKey.create(registryKey, child), type)
    }

    fun addTag(tagKey: TagKey<T>, child: TagKey<T>, type: DependType = DependType.REQUIRED) {
        add(tagKey, Entry(child.location, true, type))
    }

    enum class DependType {
        OPTIONAL,
        REQUIRED,
    }

    data class Entry(val id: ResourceLocation, val isTag: Boolean, val type: DependType) {
        fun toTagEntry(): TagEntry = if (isTag) {
            when (type) {
                DependType.OPTIONAL -> TagEntry.optionalTag(id)
                DependType.REQUIRED -> TagEntry.tag(id)
            }
        } else {
            when (type) {
                DependType.OPTIONAL -> TagEntry.optionalElement(id)
                DependType.REQUIRED -> TagEntry.element(id)
            }
        }
    }

    //    ItemTag    //

    interface ItemTag : HTTagBuilder<Item> {
        fun addItem(prefix: HTTagPrefix, key: HTMaterialKey, itemLike: ItemLike) {
            addItem(prefix.createItemTag(key), itemLike)
        }

        fun addItem(tagKey: TagKey<Item>, itemLike: ItemLike) {
            add(tagKey, itemLike.asItemHolder())
        }

        fun copyFromBlock(blockTag: TagKey<Block>, itemTag: TagKey<Item>)

        fun copyFromBlock(prefix: HTTagPrefix, key: HTMaterialKey) {
            copyFromBlock(
                prefix.createBlockTag(key),
                prefix.createItemTag(key),
            )
        }
    }
}
