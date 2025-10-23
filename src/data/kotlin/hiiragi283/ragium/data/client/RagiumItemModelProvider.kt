package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.blockId
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.api.registry.itemId
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.common.integration.RagiumMekanismAddon
import hiiragi283.ragium.common.integration.food.RagiumDelightAddon
import hiiragi283.ragium.common.integration.food.RagiumFoodAddon
import hiiragi283.ragium.common.integration.food.RagiumKaleidoCookeryAddon
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder

class RagiumItemModelProvider(context: HTDataGenContext) : ItemModelProvider(context.output, RagiumAPI.MOD_ID, context.fileHelper) {
    override fun registerModels() {
        registerBlocks()
        registerItems()
    }

    private fun registerBlocks() {
        // Blocks
        buildSet {
            // Ragium
            addAll(RagiumBlocks.REGISTER.firstEntries)

            remove(RagiumBlocks.AZURE_CLUSTER)

            remove(RagiumBlocks.EXP_BERRIES)
            remove(RagiumBlocks.WARPED_WART)

            removeAll(RagiumBlocks.GENERATORS.values)
            removeAll(RagiumBlocks.LED_BLOCKS.values)
            removeAll(RagiumBlocks.WALLS.values)
            // Delight
            addAll(RagiumDelightAddon.BLOCK_REGISTER.firstEntries)

            remove(RagiumDelightAddon.RAGI_CHERRY_PIE)
            remove(RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK)
        }.forEach(::simpleBlockItem)

        for ((variant: HTDecorationVariant, wall: HTHolderLike) in RagiumBlocks.WALLS) {
            withExistingParent(wall.getPath(), vanillaId("block", "wall_inventory"))
                .texture("wall", variant.textureId)
        }
        for (block: HTSimpleDeferredBlock in RagiumBlocks.LED_BLOCKS.values) {
            withExistingParent(block.getPath(), RagiumAPI.id("block", "led_block"))
        }
    }

    private fun registerItems() {
        val tools: Collection<HTDeferredItem<*>> = RagiumItems.TOOLS.values

        buildSet {
            // Ragium
            addAll(RagiumItems.REGISTER.entries)

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)

            remove(RagiumItems.BLAST_CHARGE)
            remove(RagiumItems.MEDIUM_DRUM_UPGRADE)
            remove(RagiumItems.LARGE_DRUM_UPGRADE)
            remove(RagiumItems.HUGE_DRUM_UPGRADE)
            removeAll(tools)

            // Food
            addAll(RagiumFoodAddon.ITEM_REGISTER.entries)

            removeAll(RagiumDelightAddon.KNIFE_MAP.values)
            removeAll(RagiumKaleidoCookeryAddon.KNIFE_MAP.values)

            remove(RagiumDelightAddon.RAGI_CHERRY_TOAST) // TODO
            // Mekanism
            addAll(RagiumMekanismAddon.ITEM_REGISTER.entries)
        }.forEach(::basicItem)

        blockTexItem(RagiumBlocks.AZURE_CLUSTER)

        basicItem(RagiumBlocks.EXP_BERRIES)
        basicItem(RagiumBlocks.WARPED_WART)

        basicItem(RagiumDelightAddon.RAGI_CHERRY_PIE)
        // basicItem(RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCk)

        withExistingParent(RagiumItems.RAGI_ALLOY_COMPOUND.getPath(), vanillaId("item", "generated"))
            .texture("layer0", "minecraft:item/copper_ingot")
            .texture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.itemId)

        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            withExistingParent(content.getId().withSuffix("_bucket").path, RagiumConst.NEOFORGE.toId("item", "bucket"))
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(content.get())
        }

        // Tools
        buildList {
            addAll(tools)
            add(RagiumItems.BLAST_CHARGE)

            addAll(RagiumDelightAddon.KNIFE_MAP.values)
            addAll(RagiumKaleidoCookeryAddon.KNIFE_MAP.values)
        }.forEach(::handheldItem)

        // Built-In
        buildList {
            addAll(RagiumBlocks.GENERATORS.values)
        }.forEach(::builtIn)
    }

    //    Extensions    //

    private fun getBuilder(holder: HTHolderLike): ItemModelBuilder = getBuilder(holder.getId().toString())

    private fun simpleBlockItem(block: HTHolderLike): ItemModelBuilder = simpleBlockItem(block.getId())

    private fun basicItem(item: HTHolderLike): ItemModelBuilder = basicItem(item.getId())

    private fun blockTexItem(item: HTHolderLike): ItemModelBuilder =
        withExistingParent(item.getPath(), vanillaId("item", "generated")).texture("layer0", item.blockId)

    // private fun basicItem(block: HTDeferredBlock<*, *>): ItemModelBuilder = basicItem(block.itemHolder)

    private fun handheldItem(item: HTHolderLike): ItemModelBuilder = handheldItem(item.getId())

    private fun builtIn(holder: HTHolderLike): ItemModelBuilder = getBuilder(holder)
        .parent(ModelFile.UncheckedModelFile(vanillaId("builtin", "entity")))
        /*.transforms {
            transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
                rotation(75f, 45f, 0f).translation(0f, 2.5f, 0f).scale(0.375f)
            }
            transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
                rotation(75f, 45f, 0f).translation(0f, 2.5f, 0f).scale(0.375f)
            }
            transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) { rotation(0f, 45f, 0f).scale(0.4f) }
            transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND) { rotation(0f, 225f, 0f).scale(0.4f) }
            transform(ItemDisplayContext.GROUND) { translation(0f, 3f, 0f).scale(0.25f) }
            transform(ItemDisplayContext.GUI) { rotation(30f, 225f, 0f).scale(0.625f) }
            transform(ItemDisplayContext.FIXED) { scale(0.5f) }
        }*/

    /*private fun ItemModelBuilder.transforms(builderAction: ModelBuilder<*>.TransformsBuilder.() -> Unit): ItemModelBuilder =
        this.transforms().apply(builderAction).end()

    private fun ModelBuilder<*>.TransformsBuilder.transform(
        context: ItemDisplayContext,
        builderAction: ModelBuilder<*>.TransformsBuilder.TransformVecBuilder.() -> Unit,
    ): ModelBuilder<*>.TransformsBuilder = this.transform(context).apply(builderAction).end()*/
}
