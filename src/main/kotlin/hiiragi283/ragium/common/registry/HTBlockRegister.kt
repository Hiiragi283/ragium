package hiiragi283.ragium.common.registry

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import hiiragi283.ragium.common.block.HTBlockWithEntity
import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.util.blockSettings
import hiiragi283.ragium.common.util.buildModelVariant
import hiiragi283.ragium.common.util.forEach
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ModelIds
import net.minecraft.data.client.VariantsBlockStateSupplier
import net.minecraft.data.server.loottable.BlockLootTableGenerator
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTBlockRegister(val modId: String) : Iterable<Block> {
    private val blockCache: MutableMap<Identifier, Block> = mutableMapOf()
    private val langCache: Table<String, HTLangType, String> = HashBasedTable.create()
    private val lootCache: MutableMap<Block, (BlockLootTableGenerator) -> Unit> = mutableMapOf()
    private val stateCache: MutableMap<Block, (BlockStateModelGenerator) -> Unit> = mutableMapOf()
    private val tagCache: MutableMap<Block, List<TagKey<Block>>> = mutableMapOf()

    fun registerCopy(name: String, parent: Block, action: Builder<Block>.() -> Unit): Block =
        register(name, Block(blockSettings(parent)), action)

    fun registerSimple(name: String, settings: AbstractBlock.Settings = blockSettings(), action: Builder<Block>.() -> Unit): Block =
        register(name, Block(settings), action)

    fun registerWithBE(
        name: String,
        type: BlockEntityType<*>,
        settings: AbstractBlock.Settings = blockSettings(),
        action: Builder<Block>.() -> Unit,
    ): Block = register(name, HTBlockWithEntity.build(type, settings), action)

    fun registerHorizontalWithBE(
        name: String,
        type: BlockEntityType<*>,
        settings: AbstractBlock.Settings = blockSettings(),
        action: Builder<Block>.() -> Unit,
    ): Block = register(name, HTBlockWithEntity.buildHorizontal(type, settings), action)

    fun <T : Block> register(name: String, block: T, action: Builder<T>.() -> Unit): T {
        val id: Identifier = Identifier.of(modId, name)
        check(id !in blockCache)
        Registry.register(Registries.BLOCK, id, block)
        Builder(name, block, this)
            .generateSimpleState()
            .generateSimpleLoot()
            .apply(action)
        blockCache[id] = block
        return block
    }

    //    Data Gen    //

    fun generateLang(type: HTLangType, builder: TranslationBuilder) {
        langCache.forEach { key: String, type1: HTLangType, value: String ->
            if (type == type1) builder.add(key, value)
        }
    }

    fun generateLootTable(generator: BlockLootTableGenerator) {
        lootCache.values.forEach { it(generator) }
    }

    fun generateState(generator: BlockStateModelGenerator) {
        stateCache.values.forEach { it(generator) }
    }

    fun generateTag(builderMapper: (TagKey<Block>) -> FabricTagProvider<Block>.FabricTagBuilder) {
        tagCache.forEach { (block: Block, tagKeys: List<TagKey<Block>>) ->
            tagKeys.map(builderMapper).forEach { it.add(block) }
        }
    }

    //    Iterable    //

    override fun iterator(): Iterator<Block> = blockCache.values.iterator()

    //    Builder    //

    class Builder<T : Block> internal constructor(val name: String, val block: T, val register: HTBlockRegister) {
        val id: Identifier = Registries.BLOCK.getId(block)
        val prefixedId: Identifier = id.withPrefixedPath("block/")

        //    Lang    //

        fun putTranslation(type: HTLangType, value: String): Builder<T> = apply {
            register.langCache.put(block.translationKey, type, value)
        }

        fun putEnglish(value: String): Builder<T> = putTranslation(HTLangType.EN_US, value)

        fun putJapanese(value: String): Builder<T> = putTranslation(HTLangType.JA_JP, value)

        fun putTooltip(type: HTLangType, value: String): Builder<T> = apply {
            register.langCache.put("${block.translationKey}.tooltip", type, value)
        }

        fun putEnglishTips(value: String): Builder<T> = putTooltip(HTLangType.EN_US, value)

        fun putJapaneseTips(value: String): Builder<T> = putTooltip(HTLangType.JA_JP, value)

        //    LootTable    //

        fun generateLootTable(action: (BlockLootTableGenerator) -> Unit): Builder<T> = apply {
            register.lootCache[block] = action
        }

        fun generateSimpleLoot(): Builder<T> = generateLootTable { it.addDrop(block) }

        fun setCustomLootTable(): Builder<T> = apply {
            register.lootCache.remove(block)
        }

        //    Model    //

        fun generateState(action: (BlockStateModelGenerator) -> Unit): Builder<T> = apply {
            register.stateCache[block] = action
        }

        fun generateSimpleState(): Builder<T> = generateState { it.registerSimpleCubeAll(block) }

        fun generateStateWithoutModel(): Builder<T> = generateState {
            it.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(
                    block,
                    buildModelVariant(ModelIds.getBlockModelId(block)),
                ),
            )
        }

        fun setCustomBlockState(): Builder<T> = apply {
            register.stateCache.remove(block)
        }

        //    Tag    //

        fun registerTags(tagKeys: List<TagKey<Block>>): Builder<T> = apply {
            register.tagCache[block] = tagKeys
        }

        fun registerTags(vararg tagKeys: TagKey<Block>): Builder<T> = registerTags(tagKeys.asList())
    }
}
