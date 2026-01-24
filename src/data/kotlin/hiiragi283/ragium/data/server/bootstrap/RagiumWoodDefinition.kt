package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.resource.toId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.registry.HTWoodDefinition
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

object RagiumWoodDefinition : RegistrySetBuilder.RegistryBootstrap<HTWoodDefinition> {
    override fun run(context: BootstrapContext<HTWoodDefinition>) {
        register(context, "oak", ItemTags.OAK_LOGS)
        register(context, "birch", ItemTags.BIRCH_LOGS)
        register(context, "acacia", ItemTags.ACACIA_LOGS)
        register(context, "cherry", ItemTags.CHERRY_LOGS)
        register(context, "jungle", ItemTags.JUNGLE_LOGS)
        register(context, "spruce", ItemTags.SPRUCE_LOGS)
        register(context, "mangrove", ItemTags.MANGROVE_LOGS)

        register(
            context,
            "crimson",
            ItemTags.CRIMSON_STEMS,
        ) {
            put(HTWoodDefinition.Variant.LOG, HTItemHolderLike.Simple(Items.CRIMSON_STEM))
            put(HTWoodDefinition.Variant.WOOD, HTItemHolderLike.Simple(Items.CRIMSON_HYPHAE))
            remove(HTWoodDefinition.Variant.BOAT)
            remove(HTWoodDefinition.Variant.CHEST_BOAT)
        }
        register(
            context,
            "warped",
            ItemTags.WARPED_STEMS,
        ) {
            put(HTWoodDefinition.Variant.LOG, HTItemHolderLike.Simple(Items.WARPED_STEM))
            put(HTWoodDefinition.Variant.WOOD, HTItemHolderLike.Simple(Items.WARPED_HYPHAE))
            remove(HTWoodDefinition.Variant.BOAT)
            remove(HTWoodDefinition.Variant.CHEST_BOAT)
        }
        register(
            context,
            "bamboo",
            ItemTags.BAMBOO_BLOCKS,
        ) {
            put(HTWoodDefinition.Variant.LOG, HTItemHolderLike.Simple(Items.BAMBOO_BLOCK))
            put(HTWoodDefinition.Variant.BOAT, HTItemHolderLike.Simple(Items.BAMBOO_RAFT))
            put(HTWoodDefinition.Variant.CHEST_BOAT, HTItemHolderLike.Simple(Items.BAMBOO_CHEST_RAFT))
            remove(HTWoodDefinition.Variant.WOOD)
        }
    }

    @HTBuilderMarker
    @JvmStatic
    private fun register(
        context: BootstrapContext<HTWoodDefinition>,
        path: String,
        logTag: TagKey<Item>,
        operator: MutableMap<HTWoodDefinition.Variant, HTItemHolderLike<*>>.() -> Unit = {},
    ) {
        register(
            context,
            path,
            logTag,
            HTWoodDefinition.Variant.entries,
            operator,
        )
    }

    @HTBuilderMarker
    @JvmStatic
    private fun register(
        context: BootstrapContext<HTWoodDefinition>,
        path: String,
        logTag: TagKey<Item>,
        variants: Iterable<HTWoodDefinition.Variant>,
        operator: MutableMap<HTWoodDefinition.Variant, HTItemHolderLike<*>>.() -> Unit = {},
    ) {
        register(
            context,
            path,
            HTWoodDefinition(
                buildMap {
                    for (variant in variants) {
                        put(
                            variant,
                            HTItemHolderLike.Simple(HTConst.MINECRAFT.toId("${path}_${variant.serializedName}")),
                        )
                    }
                    operator()
                },
                logTag,
            ),
        )
    }

    @JvmStatic
    private fun register(context: BootstrapContext<HTWoodDefinition>, path: String, definition: HTWoodDefinition) {
        context.register(
            RagiumAPI.WOOD_DEFINITION_KEY.createKey(HTConst.MINECRAFT.toId(path)),
            definition,
        )
    }
}
