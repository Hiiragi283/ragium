package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.extension.name
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTBlockSet
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.util.HTOreVariant
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem

class HTOreSets(val key: HTMaterialKey) : HTBlockSet {
    private val blockRegister = HTBlockRegister(RagiumAPI.MOD_ID)
    private val itemRegister = HTItemRegister(RagiumAPI.MOD_ID)

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

    operator fun get(variant: HTOreVariant): DeferredBlock<Block> = when (variant) {
        HTOreVariant.OVERWORLD -> stoneOre
        HTOreVariant.DEEPSLATE -> deepOre
        HTOreVariant.NETHER -> netherOre
        HTOreVariant.END -> endOre
    }

    //    HTBlockSet    //

    override val blockHolders: List<DeferredBlock<*>> = blockRegister.entries

    override val itemHolders: List<DeferredItem<*>> = HTOreVariant.entries.map { variant ->
        itemRegister.registerSimpleBlockItem(get(variant), itemProperty().name(variant.createText(key)))
    }

    override fun init(eventBus: IEventBus) {
        blockRegister.register(eventBus)
        itemRegister.register(eventBus)
    }

    override fun appendBlockTags(builder: HTTagBuilder<Block>, mineableTag: TagKey<Block>) {
        for (ore: DeferredBlock<*> in blockHolders) {
            builder.add(mineableTag, ore)
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

    override fun appendItemTags(builder: HTTagBuilder<Item>) {
        for (ore: DeferredItem<*> in itemHolders) {
            // Material Tag
            val oreTagKey: TagKey<Item> = HTTagPrefix.ORE.createTag(key)
            builder.addTag(Tags.Items.ORES, oreTagKey)
            builder.add(oreTagKey, ore)
        }
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
                        .texture("layer1", RagiumAPI.id(key.name).withPrefix("block/"))
                        .renderType("cutout"),
                ),
            )
        }
    }

    override fun addItemModels(provider: ItemModelProvider) {
        blockHolders.map(DeferredBlock<*>::getId).forEach(provider::simpleBlockItem)
    }

    override fun addTranslationEn(name: String, provider: LanguageProvider) {}

    override fun addTranslationJp(name: String, provider: LanguageProvider) {}
}
