package hiiragi283.ragium.common.datagen.bootstrap

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.HTServerResourceGenTask
import hiiragi283.core.api.data.register
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.resource.toId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.registry.HTWoodDefinition
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

data object RagiumWoodDefinition : HTServerResourceGenTask {
    override fun accept(sink: ResourceSink) {
        register(sink, "oak", ItemTags.OAK_LOGS)
        register(sink, "birch", ItemTags.BIRCH_LOGS)
        register(sink, "acacia", ItemTags.ACACIA_LOGS)
        register(sink, "cherry", ItemTags.CHERRY_LOGS)
        register(sink, "jungle", ItemTags.JUNGLE_LOGS)
        register(sink, "spruce", ItemTags.SPRUCE_LOGS)
        register(sink, "mangrove", ItemTags.MANGROVE_LOGS)

        register(
            sink,
            "crimson",
            ItemTags.CRIMSON_STEMS,
        ) {
            put(HTWoodDefinition.Variant.LOG, HTItemHolderLike.of(Items.CRIMSON_STEM))
            put(HTWoodDefinition.Variant.WOOD, HTItemHolderLike.of(Items.CRIMSON_HYPHAE))
            remove(HTWoodDefinition.Variant.BOAT)
            remove(HTWoodDefinition.Variant.CHEST_BOAT)
        }
        register(
            sink,
            "warped",
            ItemTags.WARPED_STEMS,
        ) {
            put(HTWoodDefinition.Variant.LOG, HTItemHolderLike.of(Items.WARPED_STEM))
            put(HTWoodDefinition.Variant.WOOD, HTItemHolderLike.of(Items.WARPED_HYPHAE))
            remove(HTWoodDefinition.Variant.BOAT)
            remove(HTWoodDefinition.Variant.CHEST_BOAT)
        }
        register(
            sink,
            "bamboo",
            ItemTags.BAMBOO_BLOCKS,
        ) {
            put(HTWoodDefinition.Variant.LOG, HTItemHolderLike.of(Items.BAMBOO_BLOCK))
            put(HTWoodDefinition.Variant.BOAT, HTItemHolderLike.of(Items.BAMBOO_RAFT))
            put(HTWoodDefinition.Variant.CHEST_BOAT, HTItemHolderLike.of(Items.BAMBOO_CHEST_RAFT))
            remove(HTWoodDefinition.Variant.WOOD)
        }
    }

    @HTBuilderMarker
    @JvmStatic
    private fun register(
        sink: ResourceSink,
        path: String,
        logTag: TagKey<Item>,
        operator: MutableMap<HTWoodDefinition.Variant, HTItemHolderLike<*>>.() -> Unit = {},
    ) {
        register(
            sink,
            path,
            logTag,
            HTWoodDefinition.Variant.entries,
            operator,
        )
    }

    @HTBuilderMarker
    @JvmStatic
    private fun register(
        sink: ResourceSink,
        path: String,
        logTag: TagKey<Item>,
        variants: Iterable<HTWoodDefinition.Variant>,
        operator: MutableMap<HTWoodDefinition.Variant, HTItemHolderLike<*>>.() -> Unit = {},
    ) {
        register(
            sink,
            path,
            HTWoodDefinition(
                buildMap {
                    for (variant in variants) {
                        put(
                            variant,
                            HTItemHolderLike.of(HTConst.MINECRAFT.toId("${path}_${variant.serializedName}")),
                        )
                    }
                    operator()
                },
                logTag,
            ),
        )
    }

    @JvmStatic
    private fun register(sink: ResourceSink, path: String, definition: HTWoodDefinition) {
        sink.register(
            RagiumAPI.WOOD_DEFINITION_KEY.createKey(HTConst.MINECRAFT.toId(path)),
            HTWoodDefinition.CODEC,
            definition,
        )
    }
}
