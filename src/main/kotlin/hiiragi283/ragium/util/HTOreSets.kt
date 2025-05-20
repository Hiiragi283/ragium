package hiiragi283.ragium.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTBlockSet
import hiiragi283.ragium.api.registry.HTItemRegister
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem

class HTOreSets(private val name: String) : HTBlockSet {
    private val blockRegister = HTBlockRegister(RagiumAPI.MOD_ID)
    private val itemRegister = HTItemRegister(RagiumAPI.MOD_ID)

    val blockOreTag: TagKey<Block> = blockTagKey(commonId("ores/$name"))
    val itemOreTag: TagKey<Item> = itemTagKey(commonId("ores/$name"))

    val stoneOre: DeferredBlock<Block> = blockRegister.registerSimpleBlock(
        "${name}_ore",
        BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f),
    )
    val deepOre: DeferredBlock<Block> = blockRegister.registerSimpleBlock(
        "deepslate_${name}_ore",
        BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.DEEPSLATE)
            .requiresCorrectToolForDrops()
            .strength(4.5f, 3f)
            .sound(SoundType.DEEPSLATE),
    )
    val netherOre: DeferredBlock<Block> = blockRegister.registerSimpleBlock(
        "nether_${name}_ore",
        BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.NETHER)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f)
            .sound(SoundType.NETHER_ORE),
    )
    val endOre: DeferredBlock<Block> = blockRegister.registerSimpleBlock(
        "end_${name}_ore",
        BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.SAND)
            .requiresCorrectToolForDrops()
            .strength(3f, 9f)
            .sound(SoundType.AMETHYST),
    )

    //    HTBlockSet    //

    override val blockHolders: List<DeferredBlock<*>> = listOf(stoneOre, deepOre, netherOre, endOre)

    override val itemHolders: List<DeferredItem<*>> = blockHolders.map(itemRegister::registerSimpleBlockItem)

    override fun init(eventBus: IEventBus) {
        blockRegister.register(eventBus)
        itemRegister.register(eventBus)
    }

    override fun appendBlockTags(builder: HTTagBuilder<Block>, mineableTag: TagKey<Block>) {
        for (ore: DeferredBlock<*> in blockHolders) {
            builder.add(mineableTag, ore)
            // Material Tag
            builder.addTag(Tags.Blocks.ORES, blockOreTag)
            builder.add(blockOreTag, ore)
        }
        // Ores in ground
        builder.add(Tags.Blocks.ORES_IN_GROUND_STONE, stoneOre)
        builder.add(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, deepOre)
        builder.add(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, netherOre)
        builder.add(blockTagKey(commonId("ores_in_ground/end_stone")), endOre)
    }

    override fun appendItemTags(builder: HTTagBuilder.ItemTag) {
        builder.copyFromBlock(Tags.Blocks.ORES, Tags.Items.ORES)
        builder.copyFromBlock(blockOreTag, itemOreTag)
    }

    override fun addRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {}

    override fun addBlockStates(provider: BlockStateProvider) {
        fun register(ore: DeferredBlock<*>, stonePath: String) {
            provider.simpleBlock(
                ore.get(),
                ConfiguredModel(
                    provider
                        .models()
                        .withExistingParent(ore.id.path, RagiumAPI.id("block/layered"))
                        .texture("layer0", vanillaId(stonePath))
                        .texture("layer1", RagiumAPI.id(name).withPrefix("block/"))
                        .renderType("cutout"),
                ),
            )
        }

        register(stoneOre, "block/stone")
        register(deepOre, "block/deepslate")
        register(netherOre, "block/netherrack")
        register(endOre, "block/end_stone")
    }

    override fun addItemModels(provider: ItemModelProvider) {
        blockHolders.map(DeferredBlock<*>::getId).forEach(provider::simpleBlockItem)
    }

    override fun addTranslationEn(name: String, provider: LanguageProvider) {
        provider.addBlock(stoneOre, "$name Ore")
        provider.addBlock(deepOre, "Deepslate $name Ore")
        provider.addBlock(netherOre, "Nether $name Ore")
        provider.addBlock(endOre, "End $name Ore")
    }

    override fun addTranslationJp(name: String, provider: LanguageProvider) {
        provider.addBlock(stoneOre, "${name}鉱石")
        provider.addBlock(deepOre, "深層${name}鉱石")
        provider.addBlock(netherOre, "ネザー${name}鉱石")
        provider.addBlock(endOre, "エンド${name}鉱石")
    }
}
