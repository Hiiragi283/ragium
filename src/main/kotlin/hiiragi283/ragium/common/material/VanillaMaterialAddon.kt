package hiiragi283.ragium.common.material

import hiiragi283.lib.HTConstants
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialAddon
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.part.CommonParts
import hiiragi283.lib.material.part.HTPartKey
import hiiragi283.lib.material.part.property.HTPartPropertyKeys
import hiiragi283.lib.material.part.property.addNamePattern
import hiiragi283.lib.material.property.HTDefaultPart
import hiiragi283.lib.material.property.HTMaterialPropertyKeys
import hiiragi283.lib.material.property.HTMaterialTextureSet
import hiiragi283.lib.material.property.HTStorageBlockProperty
import hiiragi283.lib.material.property.addCustomName
import hiiragi283.lib.material.property.setDefaultPart
import hiiragi283.lib.material.property.setName
import hiiragi283.lib.material.property.setTextureSet
import hiiragi283.lib.property.getOrDefault
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.tag.RagiumTags
import net.minecraft.resources.Identifier
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.common.Tags

data object VanillaMaterialAddon : HTMaterialAddon {
    override val priority: Int = 1000

    //    Part    //

    override fun registerPart(register: HTMaterialAddon.PartRegister) {
        blockPart(register)
        itemPart(register)
    }

    @JvmStatic
    private fun blockPart(register: HTMaterialAddon.PartRegister) {
        fun registerOre(
            name: String,
            enPrefix: String,
            jaPrefix: String,
            properties: BlockBehaviour.Properties,
            stoneTexture: Identifier,
        ) {
            register.register(HTPartKey("ore/$name"), "${name}_%s_ore") {
                put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.ORE)

                put(HTPartPropertyKeys.BLOCK_PROP, properties)
                put(HTPartPropertyKeys.ORE_STONE_TEX, stoneTexture)

                addNamePattern("$enPrefix %s Ore", "$jaPrefix%s鉱石")
            }
        }

        register.register(CommonParts.ORE, "%s_ore") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.ORE)

            put(
                HTPartPropertyKeys.BLOCK_PROP,
                BlockBehaviour.Properties
                    .of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(3f, 3f),
            )
            put(HTPartPropertyKeys.ORE_STONE_TEX, vanillaId(HTConstants.BLOCK, "stone"))

            addNamePattern("%s Ore", "%s鉱石")
        }
        registerOre(
            "deepslate",
            "Deepslate",
            "深層",
            BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.DEEPSLATE)
                .requiresCorrectToolForDrops()
                .strength(4.5f, 3f)
                .sound(SoundType.DEEPSLATE),
            vanillaId(HTConstants.BLOCK, "deepslate"),
        )
        registerOre(
            "nether",
            "Nether",
            "ネザー",
            BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.NETHER)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(3f, 3f)
                .sound(SoundType.NETHER_ORE),
            vanillaId(HTConstants.BLOCK, "netherrack"),
        )
        registerOre(
            "end",
            "End",
            "エンド",
            BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.SAND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(4.5f, 9f),
            vanillaId(HTConstants.BLOCK, "end_stone"),
        )

        register.register(CommonParts.BLOCK, "%s_block") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Float, material: HTMaterial ->
                base * material.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK).baseCount
            }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.STORAGE_BLOCK)

            put(HTPartPropertyKeys.BLOCK_PROP, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))

            addNamePattern("Block of %s", "%sブロック")
            put(HTPartPropertyKeys.FUEL_SCALE, 10f)
        }
        register.register(CommonParts.RAW_BLOCK, "raw_%s_block") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Float, _ -> base * 9 }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.RAW_STORAGE_BLOCK)

            put(HTPartPropertyKeys.BLOCK_PROP, BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK))

            addNamePattern("Block of Raw %s", "%sの原石ブロック")
        }
    }

    @JvmStatic
    private fun itemPart(register: HTMaterialAddon.PartRegister) {
        register.register(CommonParts.DUST, "%s_dust") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.DUST)

            addNamePattern("%s Dust", "%sの粉")
        }
        register.register(CommonParts.FUEL, "%s_fuel") {
            put(HTPartPropertyKeys.FUEL_SCALE, 1f)
        }
        register.register(CommonParts.GEAR, "%s_gear") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Float, _ -> base * 4 }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.GEAR)

            addNamePattern("%s Gear", "%sの歯車")
        }
        register.register(CommonParts.GEM, "%s_gem") {
            put(HTPartPropertyKeys.FUEL_SCALE, 1f)
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.GEM)
        }
        register.register(CommonParts.INGOT, "%s_ingot") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.INGOT)

            addNamePattern("%s Ingot", "%sインゴット")
        }
        register.register(CommonParts.NUGGET, "%s_nugget") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Float, _ -> base / 9 }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.NUGGET)

            addNamePattern("%s Nugget", "%sナゲット")
            put(HTPartPropertyKeys.FUEL_SCALE, 0.1f)
        }
        register.register(CommonParts.PLATE, "%s_plate") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.PLATE)

            addNamePattern("%s Plate", "%sの板")
        }
        register.register(CommonParts.RAW, "raw_%s") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.RAW_MATERIALS)

            addNamePattern("Raw %s", "%sの原石")
        }
        register.register(CommonParts.ROD, "%s_rod") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Float, _ -> base / 2 }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.ROD)

            addNamePattern("%s Rod", "%sの棒")
        }
        register.register(CommonParts.TINY, "tiny_%s") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Float, _ -> base / 8 }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.TINY)

            addNamePattern("Tiny %s", "小さな%s")
            put(HTPartPropertyKeys.FUEL_SCALE, 1 / 8f)
        }
    }

    //    Material    //

    override fun registerExistingBlock(consumer: HTMaterialAddon.BlockConsumer) {
        @Suppress("DEPRECATION")
        fun accept(part: HTPartKey, key: HTMaterialKey, block: Block) {
            consumer.accept(part, key, block.builtInRegistryHolder().key())
        }

        // Fuels
        accept(CommonParts.ORE, VanillaMaterialKeys.COAL, Blocks.COAL_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.COAL, Blocks.DEEPSLATE_COAL_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.COAL, Blocks.COAL_BLOCK)
        // Mineral
        accept(CommonParts.ORE, VanillaMaterialKeys.REDSTONE, Blocks.REDSTONE_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.REDSTONE, Blocks.DEEPSLATE_REDSTONE_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.REDSTONE, Blocks.REDSTONE_BLOCK)

        accept(CommonParts.BLOCK, VanillaMaterialKeys.GLOWSTONE, Blocks.GLOWSTONE)
        // Gem
        accept(CommonParts.ORE, VanillaMaterialKeys.LAPIS, Blocks.LAPIS_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.LAPIS, Blocks.DEEPSLATE_LAPIS_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.LAPIS, Blocks.LAPIS_BLOCK)

        accept(CommonParts.ORE_NETHER, VanillaMaterialKeys.QUARTZ, Blocks.NETHER_QUARTZ_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.QUARTZ, Blocks.QUARTZ_BLOCK)

        accept(CommonParts.BLOCK, VanillaMaterialKeys.AMETHYST, Blocks.AMETHYST_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.DIAMOND, Blocks.DIAMOND_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.DIAMOND, Blocks.DEEPSLATE_DIAMOND_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.DIAMOND, Blocks.DIAMOND_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.EMERALD, Blocks.EMERALD_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.EMERALD, Blocks.DEEPSLATE_EMERALD_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.EMERALD, Blocks.EMERALD_BLOCK)
        // Metal
        accept(CommonParts.ORE, VanillaMaterialKeys.COPPER, Blocks.COPPER_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.COPPER, Blocks.DEEPSLATE_COPPER_ORE)
        accept(CommonParts.RAW_BLOCK, VanillaMaterialKeys.COPPER, Blocks.RAW_COPPER_BLOCK)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.COPPER, Blocks.COPPER_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.IRON, Blocks.IRON_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.IRON, Blocks.DEEPSLATE_IRON_ORE)
        accept(CommonParts.RAW_BLOCK, VanillaMaterialKeys.IRON, Blocks.RAW_IRON_BLOCK)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.IRON, Blocks.IRON_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.GOLD, Blocks.GOLD_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.GOLD, Blocks.DEEPSLATE_GOLD_ORE)
        accept(CommonParts.ORE_NETHER, VanillaMaterialKeys.GOLD, Blocks.NETHER_GOLD_ORE)
        accept(CommonParts.RAW_BLOCK, VanillaMaterialKeys.GOLD, Blocks.RAW_GOLD_BLOCK)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.GOLD, Blocks.GOLD_BLOCK)
        // Alloy
        accept(CommonParts.BLOCK, VanillaMaterialKeys.NETHERITE, Blocks.NETHERITE_BLOCK)
    }

    override fun registerExistingItem(consumer: HTMaterialAddon.ItemConsumer) {
        @Suppress("DEPRECATION")
        fun accept(part: HTPartKey, key: HTMaterialKey, item: Item) {
            consumer.accept(part, key, item.builtInRegistryHolder().key())
        }

        // Fuel
        accept(CommonParts.FUEL, VanillaMaterialKeys.COAL, Items.COAL)
        accept(CommonParts.FUEL, VanillaMaterialKeys.CHARCOAL, Items.CHARCOAL)
        // Mineral
        accept(CommonParts.DUST, VanillaMaterialKeys.REDSTONE, Items.REDSTONE)
        accept(CommonParts.DUST, VanillaMaterialKeys.GLOWSTONE, Items.GLOWSTONE_DUST)
        // Gem
        accept(CommonParts.GEM, VanillaMaterialKeys.LAPIS, Items.LAPIS_LAZULI)
        accept(CommonParts.GEM, VanillaMaterialKeys.QUARTZ, Items.QUARTZ)
        accept(CommonParts.GEM, VanillaMaterialKeys.AMETHYST, Items.AMETHYST_SHARD)
        accept(CommonParts.GEM, VanillaMaterialKeys.DIAMOND, Items.DIAMOND)
        accept(CommonParts.GEM, VanillaMaterialKeys.EMERALD, Items.EMERALD)
        accept(CommonParts.GEM, VanillaMaterialKeys.ECHO, Items.ECHO_SHARD)
        accept(CommonParts.DUST, VanillaMaterialKeys.PRISMARINE, Items.PRISMARINE_SHARD)
        accept(CommonParts.GEM, VanillaMaterialKeys.PRISMARINE, Items.PRISMARINE_CRYSTALS)
        accept(CommonParts.GEM, VanillaMaterialKeys.ENDER, Items.ENDER_PEARL)
        // Metal
        accept(CommonParts.RAW, VanillaMaterialKeys.COPPER, Items.RAW_COPPER)
        accept(CommonParts.INGOT, VanillaMaterialKeys.COPPER, Items.COPPER_INGOT)

        accept(CommonParts.RAW, VanillaMaterialKeys.IRON, Items.RAW_IRON)
        accept(CommonParts.INGOT, VanillaMaterialKeys.IRON, Items.IRON_INGOT)
        accept(CommonParts.NUGGET, VanillaMaterialKeys.IRON, Items.IRON_NUGGET)

        accept(CommonParts.RAW, VanillaMaterialKeys.GOLD, Items.RAW_GOLD)
        accept(CommonParts.INGOT, VanillaMaterialKeys.GOLD, Items.GOLD_INGOT)
        accept(CommonParts.NUGGET, VanillaMaterialKeys.GOLD, Items.GOLD_NUGGET)
        // Alloy
        accept(CommonParts.INGOT, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_INGOT)
        // Other
        // accept(CommonParts.DUST, VanillaMaterialKeys.BLAZE, Items.BLAZE_POWDER)
        // accept(CommonParts.ROD, VanillaMaterialKeys.BLAZE, Items.BLAZE_ROD)

        // accept(CommonParts.DUST, VanillaMaterialKeys.BREEZE, Items.WIND_CHARGE)
        // accept(CommonParts.ROD, VanillaMaterialKeys.BREEZE, Items.BREEZE_ROD)
    }

    override fun modifyMaterial(provider: HTMaterialAddon.MaterialProvider) {
        fuel(provider)
        mineral(provider)
        gem(provider)
        metal(provider)
        alloy(provider)
        other(provider)
    }

    @JvmStatic
    private fun fuel(provider: HTMaterialAddon.MaterialProvider) {
        provider.builder(VanillaMaterialKeys.COAL).apply {
            setDefaultPart(HTDefaultPart.BuiltIn(vanillaId("coal")))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Coal", "石炭")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
        }
        provider.builder(VanillaMaterialKeys.CHARCOAL).apply {
            setDefaultPart(HTDefaultPart.BuiltIn(vanillaId("charcoal")))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Charcoal", "木炭")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
        }
    }

    @JvmStatic
    private fun mineral(provider: HTMaterialAddon.MaterialProvider) {
        provider.builder(VanillaMaterialKeys.REDSTONE).apply {
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, 4f)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Redstone", "赤石")
        }
        provider.builder(VanillaMaterialKeys.GLOWSTONE).apply {
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Glowstone", "グロウストーン")
        }
    }

    @JvmStatic
    private fun gem(provider: HTMaterialAddon.MaterialProvider) {
        provider.builder(VanillaMaterialKeys.LAPIS).apply {
            setDefaultPart(HTDefaultPart.Gem)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, 4f)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Lapis", "ラピス")
            setTextureSet("lapis")
        }
        provider.builder(VanillaMaterialKeys.QUARTZ).apply {
            setDefaultPart(HTDefaultPart.Gem)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, 3f)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Quartz", "水晶")
            setTextureSet("quartz", HTMaterialTextureSet.SHINE)
        }
        provider.builder(VanillaMaterialKeys.AMETHYST).apply {
            setDefaultPart(HTDefaultPart.Gem)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Amethyst", "アメジスト")
            setTextureSet("amethyst")
        }
        provider.builder(VanillaMaterialKeys.DIAMOND).apply {
            setDefaultPart(HTDefaultPart.Gem)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Diamond", "ダイヤモンド")
            setTextureSet("diamond")
        }
        provider.builder(VanillaMaterialKeys.EMERALD).apply {
            setDefaultPart(HTDefaultPart.Gem)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Emerald", "エメラルド")
            setTextureSet("emerald")
        }
        provider.builder(VanillaMaterialKeys.ECHO).apply {
            setDefaultPart(HTDefaultPart.Gem)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Echo Shard", "残響の欠片")
            setTextureSet("echo")
        }
        provider.builder(VanillaMaterialKeys.PRISMARINE).apply {
            setDefaultPart(HTDefaultPart.Gem)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Prismarine", "プリズマリン")
        }
        provider.builder(VanillaMaterialKeys.ENDER).apply {
            setDefaultPart(HTDefaultPart.BuiltIn(Tags.Items.ENDER_PEARLS, vanillaId("ender_pearl")))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Ender Pearl", "エンダーパール")
        }
    }

    @JvmStatic
    private fun metal(provider: HTMaterialAddon.MaterialProvider) {
        provider.builder(VanillaMaterialKeys.COPPER).apply {
            setDefaultPart(HTDefaultPart.Ingot)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, 1.5f)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Copper", "銅")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        provider.builder(VanillaMaterialKeys.IRON).apply {
            setDefaultPart(HTDefaultPart.Ingot)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Iron", "鉄")
        }
        provider.builder(VanillaMaterialKeys.GOLD).apply {
            setDefaultPart(HTDefaultPart.Ingot)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Gold", "金")
        }
    }

    @JvmStatic
    private fun alloy(provider: HTMaterialAddon.MaterialProvider) {
        provider.builder(VanillaMaterialKeys.NETHERITE).apply {
            setDefaultPart(HTDefaultPart.Ingot)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Netherite", "ネザライト")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
    }

    @JvmStatic
    private fun other(provider: HTMaterialAddon.MaterialProvider) {
        provider.builder(VanillaMaterialKeys.WOOD).apply {
            setDefaultPart(HTDefaultPart.BuiltIn(ItemTags.PLANKS))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Wood", "木")
            addCustomName(CommonParts.DUST, "Wood Pulp", "木パルプ")
            setTextureSet("mineral")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 15)
        }
        provider.builder(VanillaMaterialKeys.PAPER).apply {
            setDefaultPart(HTDefaultPart.BuiltIn(RagiumTags.Items.PAPER, vanillaId("paper")))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Paper", "紙")
            addCustomName(CommonParts.DUST, "Paper Pulp", "紙パルプ")
            setTextureSet("mineral")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 5)
        }
        provider.builder(VanillaMaterialKeys.GLASS).apply {
            setDefaultPart(HTDefaultPart.BuiltIn(Tags.Items.GLASS_BLOCKS, vanillaId("glass")))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Glass", "ガラス")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        // Stone
        provider.builder(VanillaMaterialKeys.OBSIDIAN).apply {
            setDefaultPart(HTDefaultPart.BuiltIn(Tags.Items.OBSIDIANS_NORMAL, vanillaId("obsidian")))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConstants.MINECRAFT)

            setName("Obsidian", "黒曜石")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
    }

    override fun registerMaterialBlock(register: HTMaterialAddon.MaterialEntryRegister) {
        register.register(VanillaMaterialKeys.CHARCOAL, CommonParts.BLOCK)
        register.register(VanillaMaterialKeys.ECHO, CommonParts.BLOCK)
    }

    override fun registerMaterialItem(register: HTMaterialAddon.MaterialEntryRegister) {
        // Fuels
        register.registerAll(VanillaMaterialKeys.COAL, CommonParts.DUST, CommonParts.TINY)
        register.registerAll(VanillaMaterialKeys.CHARCOAL, CommonParts.DUST, CommonParts.TINY)
        // Minerals
        // Gems
        register.register(VanillaMaterialKeys.LAPIS, CommonParts.DUST)
        register.register(VanillaMaterialKeys.QUARTZ, CommonParts.DUST)
        register.register(VanillaMaterialKeys.AMETHYST, CommonParts.DUST)
        register.registerAll(VanillaMaterialKeys.DIAMOND, CommonParts.DUST, CommonParts.GEAR)
        register.registerAll(VanillaMaterialKeys.EMERALD, CommonParts.DUST, CommonParts.GEAR)
        register.register(VanillaMaterialKeys.ECHO, CommonParts.DUST)
        register.register(VanillaMaterialKeys.PRISMARINE, CommonParts.DUST)
        register.register(VanillaMaterialKeys.ENDER, CommonParts.DUST)
        // Metals
        register.registerAll(VanillaMaterialKeys.COPPER, CommonParts.DUST, CommonParts.GEAR)
        register.registerAll(VanillaMaterialKeys.IRON, CommonParts.DUST, CommonParts.GEAR)
        register.registerAll(VanillaMaterialKeys.GOLD, CommonParts.DUST, CommonParts.GEAR)
        // Alloys
        register.registerAll(VanillaMaterialKeys.NETHERITE, CommonParts.DUST, CommonParts.GEAR, CommonParts.NUGGET)
        // Others
        register.registerAll(VanillaMaterialKeys.WOOD, CommonParts.DUST, CommonParts.GEAR)
        register.register(VanillaMaterialKeys.PAPER, CommonParts.DUST)
        register.register(VanillaMaterialKeys.GLASS, CommonParts.DUST)
        register.register(VanillaMaterialKeys.OBSIDIAN, CommonParts.DUST)
    }
}
