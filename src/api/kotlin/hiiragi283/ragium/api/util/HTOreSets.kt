package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

class HTOreSets(val key: HTMaterialKey) {
    private val blockRegister: DeferredRegister.Blocks = DeferredRegister.createBlocks(RagiumAPI.MOD_ID)
    private val itemRegister: DeferredRegister.Items = DeferredRegister.createItems(RagiumAPI.MOD_ID)

    @JvmField
    val stoneOre: DeferredBlock<Block> = blockRegister.registerSimpleBlock(
        HTOreVariant.OVERWORLD.createId(key),
        HTOreVariant.OVERWORLD.createProperty(),
    )

    @JvmField
    val deepOre: DeferredBlock<Block> = blockRegister.registerSimpleBlock(
        HTOreVariant.DEEPSLATE.createId(key),
        HTOreVariant.DEEPSLATE.createProperty(),
    )

    @JvmField
    val netherOre: DeferredBlock<Block> = blockRegister.registerSimpleBlock(
        HTOreVariant.NETHER.createId(key),
        HTOreVariant.NETHER.createProperty(),
    )

    @JvmField
    val endOre: DeferredBlock<Block> = blockRegister.registerSimpleBlock(
        HTOreVariant.END.createId(key),
        HTOreVariant.END.createProperty(),
    )

    val oreBlocks: List<DeferredBlock<out Block>>
        get() = blockRegister.blockEntries

    val oreItems: List<DeferredItem<out Item>>
        get() = itemRegister.itemEntries

    init {
        for (variant: HTOreVariant in HTOreVariant.entries) {
            itemRegister.registerSimpleBlockItem(get(variant), itemProperty().name(variant.createText(key)))
        }
    }

    operator fun get(variant: HTOreVariant): DeferredBlock<Block> = when (variant) {
        HTOreVariant.OVERWORLD -> stoneOre
        HTOreVariant.DEEPSLATE -> deepOre
        HTOreVariant.NETHER -> netherOre
        HTOreVariant.END -> endOre
    }

    fun init(eventBus: IEventBus) {
        blockRegister.register(eventBus)
        itemRegister.register(eventBus)
    }

    //    Data Gen    //

    fun appendTags(mineable: TagKey<Block>, builder: HTTagBuilder<Block>) {
        for (ore: DeferredBlock<out Block> in oreBlocks) {
            builder.add(mineable, ore)
            // Material Tag
            val oreTagKey: TagKey<Block> = HTTagPrefix.ORE.createBlockTag(key) ?: continue
            builder.addTag(Tags.Blocks.ORES, oreTagKey)
            builder.add(oreTagKey, ore)
        }
        // Ores in ground
        builder.add(Tags.Blocks.ORES_IN_GROUND_STONE, stoneOre)
        builder.add(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, deepOre)
        builder.add(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, netherOre)
        builder.add(blockTagKey(commonId("ores_in_ground/end_stone")), endOre)
    }

    fun appendTags(builder: HTTagBuilder<Item>) {
        for (ore: DeferredItem<out Item> in oreItems) {
            // Material Tag
            val oreTagKey: TagKey<Item> = HTTagPrefix.ORE.createTag(key)
            builder.addTag(Tags.Items.ORES, oreTagKey)
            builder.add(oreTagKey, ore)
        }
    }

    fun generateStates(generator: BlockStateProvider) {
        for (variant: HTOreVariant in HTOreVariant.entries) {
            val ore: DeferredBlock<Block> = get(variant)
            generator.simpleBlock(
                ore.get(),
                ConfiguredModel(
                    generator
                        .models()
                        .withExistingParent(ore.id.path, RagiumAPI.id("block/layered"))
                        .texture("layer0", variant.baseStoneName.withPrefix("block/"))
                        .texture("layer1", RagiumAPI.id(key.name).withPrefix("block/"))
                        .renderType("cutout"),
                ),
            )
        }
    }
}
