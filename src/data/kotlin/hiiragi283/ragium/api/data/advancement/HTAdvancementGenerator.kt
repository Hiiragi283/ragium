package hiiragi283.ragium.api.data.advancement

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.data.ExistingFileHelper

abstract class HTAdvancementGenerator {
    protected lateinit var output: HTAdvancementOutput

    fun generate(provider: HolderLookup.Provider, output: HTAdvancementOutput, helper: ExistingFileHelper) {
        this.output = output
        generate(provider)
    }

    protected abstract fun generate(registries: HolderLookup.Provider)

    //    Extension    //

    protected inline fun root(key: HTAdvancementKey, builderAction: HTAdvancementBuilder.() -> Unit) {
        HTAdvancementBuilder.root().apply(builderAction).save(output, key)
    }

    protected inline fun child(key: HTAdvancementKey, parent: HTAdvancementKey, builderAction: HTAdvancementBuilder.() -> Unit) {
        HTAdvancementBuilder.child(parent).apply(builderAction).save(output, key)
    }

    protected inline fun createSimple(
        key: HTAdvancementKey,
        parent: HTAdvancementKey,
        prefix: HTMaterialPrefix,
        material: HTMaterialKey,
        builderAction: HTDisplayInfoBuilder.() -> Unit = {},
    ) {
        createSimple(
            key,
            parent,
            RagiumItems.getMaterial(prefix, material),
            prefix.itemTagKey(material),
            builderAction,
        )
    }

    protected inline fun createSimple(
        key: HTAdvancementKey,
        parent: HTAdvancementKey,
        item: HTItemHolderLike,
        tagKey: TagKey<Item>? = null,
        builderAction: HTDisplayInfoBuilder.() -> Unit = {},
    ) {
        child(key, parent) {
            display {
                setIcon(item)
                setTitleFromKey(key)
                setDescFromKey(key)
                builderAction()
            }
            if (tagKey != null) {
                hasAnyItem(tagKey)
            } else {
                hasAnyItem(item)
            }
        }
    }
}
