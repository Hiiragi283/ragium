package hiiragi283.ragium.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTBlockSet
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.util.HTOreVariant
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
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

    private val oreMap: Map<HTOreVariant, DeferredBlock<Block>> = HTOreVariant.entries.associateWith { variant: HTOreVariant ->
        blockRegister
            .registerSimpleBlock(
                variant.path.replace("%s", name),
                BlockBehaviour.Properties.of().apply(variant::setupProperty),
            )
    }

    operator fun get(variant: HTOreVariant): DeferredBlock<Block> = oreMap[variant] ?: error("Unknown ore variant: $variant!")

    //    HTBlockSet    //

    override val blockHolders: List<DeferredBlock<*>> = blockRegister.entries

    override val itemHolders: List<DeferredItem<*>> = HTOreVariant.entries.map { variant: HTOreVariant ->
        itemRegister.registerSimpleBlockItem(get(variant))
    }

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
        builder.add(Tags.Blocks.ORES_IN_GROUND_STONE, get(HTOreVariant.OVERWORLD))
        builder.add(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, get(HTOreVariant.DEEPSLATE))
        builder.add(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, get(HTOreVariant.NETHER))
        builder.add(blockTagKey(commonId("ores_in_ground/end_stone")), get(HTOreVariant.END))
    }

    override fun appendItemTags(builder: HTTagBuilder.ItemTag) {
        builder.copyFromBlock(Tags.Blocks.ORES, Tags.Items.ORES)
        builder.copyFromBlock(blockOreTag, itemOreTag)
    }

    override fun addRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {}

    override fun addBlockStates(provider: BlockStateProvider) {
        for (variant: HTOreVariant in HTOreVariant.entries) {
            val ore: DeferredBlock<Block> = get(variant)
            provider.simpleBlock(
                ore.get(),
                ConfiguredModel(
                    provider
                        .models()
                        .withExistingParent(ore.id.path, RagiumAPI.id("block/layered"))
                        .texture("layer0", variant.baseStoneName.withPrefix("block/"))
                        .texture("layer1", RagiumAPI.id(name).withPrefix("block/"))
                        .renderType("cutout"),
                ),
            )
        }
    }

    override fun addItemModels(provider: ItemModelProvider) {
        blockHolders.map(DeferredBlock<*>::getId).forEach(provider::simpleBlockItem)
    }

    override fun addTranslationEn(name: String, provider: LanguageProvider) {
        provider.addBlock(get(HTOreVariant.OVERWORLD), "$name Ore")
        provider.addBlock(get(HTOreVariant.DEEPSLATE), "Deepslate $name Ore")
        provider.addBlock(get(HTOreVariant.NETHER), "Nether $name Ore")
        provider.addBlock(get(HTOreVariant.END), "End $name Ore")
    }

    override fun addTranslationJp(name: String, provider: LanguageProvider) {
        provider.addBlock(get(HTOreVariant.OVERWORLD), "${name}鉱石")
        provider.addBlock(get(HTOreVariant.DEEPSLATE), "深層${name}鉱石")
        provider.addBlock(get(HTOreVariant.NETHER), "ネザー${name}鉱石")
        provider.addBlock(get(HTOreVariant.END), "エンド${name}鉱石")
    }
}
