package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.*
import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.HTBuddingCrystalBlock
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.inventory.HTBackpackInventory
import hiiragi283.ragium.common.item.*
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.block.AbstractBlock
import net.minecraft.block.AmethystClusterBlock
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.component.type.FoodComponent
import net.minecraft.component.type.FoodComponents
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.*
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.text.Text
import net.minecraft.util.Rarity
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import java.awt.Color

object RagiumContents : HTContentRegister {
    //    Ores    //

    enum class Ores(override val material: RagiumMaterials) : HTBlockContent {
        RAGINITE(RagiumMaterials.RAGINITE),
        // NICKEL(RagiumMaterials.NICKEL),
        ;

        override val entry: HTRegistryEntry<Block> =
            HTRegistryEntry.ofBlock(RagiumAPI.id("${name.lowercase()}_ore"))
        override val enPattern: String = "%s Ore"
        override val jaPattern: String = "%s鉱石"
    }

    enum class DeepOres(override val material: RagiumMaterials) : HTBlockContent {
        RAGINITE(RagiumMaterials.RAGINITE),
        // NICKEL(RagiumMaterials.NICKEL),
        ;

        override val entry: HTRegistryEntry<Block> =
            HTRegistryEntry.ofBlock(RagiumAPI.id("deepslate_${name.lowercase()}_ore"))
        override val enPattern: String = "Deep %s Ore"
        override val jaPattern: String = "深層%s鉱石"
    }

    @JvmStatic
    fun getOres(): List<HTBlockContent> = buildList {
        addAll(Ores.entries)
        addAll(DeepOres.entries)
    }

    //    Storage Blocks    //

    enum class StorageBlocks(override val material: RagiumMaterials) : HTBlockContent {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        INVAR(RagiumMaterials.INVAR),
        NICKEL(RagiumMaterials.NICKEL),
        SILVER(RagiumMaterials.SILVER),
        STEEL(RagiumMaterials.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val entry: HTRegistryEntry<Block> =
            HTRegistryEntry.ofBlock(RagiumAPI.id("${name.lowercase()}_block"))
        override val enPattern: String = "Block of %s"
        override val jaPattern: String = "%sブロック"
    }

    //    Dusts    //

    enum class Dusts(override val material: RagiumMaterials) : HTItemContent {
        RAW_RAGINITE(RagiumMaterials.RAW_RAGINITE),
        RAGINITE(RagiumMaterials.RAGINITE),
        RAGI_CRYSTAL(RagiumMaterials.RAGI_CRYSTAL),

        ASH(RagiumMaterials.ASH),
        COPPER(RagiumMaterials.COPPER),
        GOLD(RagiumMaterials.GOLD),
        IRON(RagiumMaterials.IRON),
        NICKEL(RagiumMaterials.NICKEL),
        NITER(RagiumMaterials.NITER),
        SILVER(RagiumMaterials.SILVER),
        SULFUR(RagiumMaterials.SULFUR),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("${name.lowercase()}_dust"))
        override val enPattern: String = "%s Dust"
        override val jaPattern: String = "%sの粉"
    }

    //    Ingots    //

    enum class Ingots(override val material: RagiumMaterials) : HTItemContent {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        INVAR(RagiumMaterials.INVAR),
        NICKEL(RagiumMaterials.NICKEL),
        SILVER(RagiumMaterials.SILVER),
        STEEL(RagiumMaterials.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("${name.lowercase()}_ingot"))
        override val enPattern: String = "%s Ingot"
        override val jaPattern: String = "%sインゴット"
    }

    //    Plates    //

    enum class Plates(override val material: RagiumMaterials) : HTItemContent {
        // tier1
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        IRON(RagiumMaterials.IRON),
        COPPER(RagiumMaterials.COPPER),

        // tier2
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        BASALT_FIBER(RagiumMaterials.BASALT_FIBER),
        GOLD(RagiumMaterials.GOLD),
        INVAR(RagiumMaterials.INVAR),
        SILICON(RagiumMaterials.SILICON),
        SILVER(RagiumMaterials.SILVER),
        STEEL(RagiumMaterials.STEEL),

        // tier3
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        PE(RagiumMaterials.PE),
        PVC(RagiumMaterials.PVC),
        PTFE(RagiumMaterials.PTFE),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("${name.lowercase()}_plate"))
        override val enPattern: String = "%s Plate"
        override val jaPattern: String = "%s板"
    }

    //    Raw Materials    //

    enum class RawMaterials(override val material: RagiumMaterials) : HTItemContent {
        RAGINITE(RagiumMaterials.RAGINITE),
        // NICKEL(RagiumMaterials.NICKEL),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("raw_${name.lowercase()}"))
        override val enPattern: String = "Raw %s"
        override val jaPattern: String = "%sの原石"
    }

    //    Armors    //

    enum class Armors(override val material: RagiumMaterials, val armorType: HTArmorType, private val multiplier: Int) :
        HTItemContent,
        HTTranslationFormatter by armorType {
        STEEL_HELMET(RagiumMaterials.STEEL, HTArmorType.HELMET, 25),
        STEEL_CHESTPLATE(RagiumMaterials.STEEL, HTArmorType.CHESTPLATE, 25),
        STEEL_LEGGINGS(RagiumMaterials.STEEL, HTArmorType.LEGGINGS, 25),
        STEEL_BOOTS(RagiumMaterials.STEEL, HTArmorType.BOOTS, 25),
        ;

        internal fun createItem(): ArmorItem = armorType.itemType.let {
            ArmorItem(
                material.armor!!,
                it,
                itemSettings().maxDamage(it.getMaxDamage(multiplier)),
            )
        }

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id(name.lowercase()))

        override fun asItem(): Item = value
    }

    //    Tools    //

    enum class Tools(override val material: RagiumMaterials, val toolType: HTToolType) :
        HTItemContent,
        HTTranslationFormatter by toolType {
        STEEL_AXE(RagiumMaterials.STEEL, HTToolType.AXE),
        STEEL_HOE(RagiumMaterials.STEEL, HTToolType.HOE),
        STEEL_PICKAXE(RagiumMaterials.STEEL, HTToolType.PICKAXE),
        STEEL_SHOVEL(RagiumMaterials.STEEL, HTToolType.SHOVEL),
        STEEL_SWORD(RagiumMaterials.STEEL, HTToolType.SWORD),
        ;

        internal fun createItem(): ToolItem = toolType.createToolItem(material.tool!!, itemSettings())

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id(name.lowercase()))

        override fun asItem(): Item = value
    }

    //    Accessories    //

    enum class Accessories :
        HTEntryDelegated<Item>,
        ItemConvertible {
        BACKPACK {
            override fun createItem(): Item = Item(
                itemSettings()
                    .maxCount(1)
                    .fireproof()
                    .component(RagiumComponentTypes.INVENTORY, HTBackpackInventory(false)),
            )
        },
        LARGE_BACKPACK {
            override fun createItem(): Item = Item(
                itemSettings()
                    .maxCount(1)
                    .fireproof()
                    .component(RagiumComponentTypes.INVENTORY, HTBackpackInventory(true)),
            )
        },
        ENDER_BACKPACK,
        DIVING_GOGGLES,
        NIGHT_VISION_GOGGLES,
        PISTON_BOOTS,
        PARACHUTE,
        ;

        internal open fun createItem(): Item = Item(itemSettings().maxCount(1))

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id(name.lowercase()))

        override fun asItem(): Item = value
    }

    //    Hulls    //

    enum class Hulls(override val material: RagiumMaterials) : HTBlockContent {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val entry: HTRegistryEntry<Block> =
            HTRegistryEntry.ofBlock(RagiumAPI.id("${name.lowercase()}_hull"))
        override val enPattern: String = "%s Hull"
        override val jaPattern: String = "%s筐体"
    }

    //    Coils    //

    enum class Coils(override val material: RagiumMaterials) : HTBlockContent {
        COPPER(RagiumMaterials.COPPER),
        GOLD(RagiumMaterials.GOLD),
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        ;

        override val entry: HTRegistryEntry<Block> =
            HTRegistryEntry.ofBlock(RagiumAPI.id("${name.lowercase()}_coil"))
        override val enPattern: String = "%s Coil"
        override val jaPattern: String = "%sコイル"
    }

    //    Motors    //

    enum class Motors(val tier: HTMachineTier) :
        HTEntryDelegated<Block>,
        HTTranslationProvider by tier,
        ItemConvertible {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val entry: HTRegistryEntry<Block> =
            HTRegistryEntry.ofBlock(RagiumAPI.id("${name.lowercase()}_motor"))

        override fun asItem(): Item = value.asItem()
    }

    //    Circuits    //

    enum class Circuits(val tier: HTMachineTier) :
        HTEntryDelegated<Item>,
        HTTranslationProvider by tier,
        ItemConvertible {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("${name.lowercase()}_circuit"))

        override fun asItem(): Item = value
    }

    //    Foods    //

    enum class Foods :
        HTEntryDelegated<Item>,
        ItemConvertible {
        BEE_WAX,
        BUTTER {
            override fun food(): FoodComponent = FoodComponents.APPLE
        },
        CANDY_APPLE,
        CARAMEL {
            override fun food(): FoodComponent = FoodComponents.DRIED_KELP
        },
        CHOCOLATE {
            override fun food(): FoodComponent = FoodComponent
                .Builder()
                .nutrition(3)
                .saturationModifier(0.3f)
                .statusEffect(
                    StatusEffectInstance(StatusEffects.STRENGTH, 10 * 20, 0),
                    1.0f,
                ).snack()
                .alwaysEdible()
                .build()
        },
        CHOCOLATE_APPLE {
            override fun food(): FoodComponent = FoodComponents.COOKED_CHICKEN
        },
        CHOCOLATE_BREAD {
            override fun food(): FoodComponent = FoodComponents.COOKED_BEEF
        },
        FLOUR,
        DOUGH,
        MINCED_MEAT,
        PULP,
        ;

        internal open fun food(): FoodComponent? = null

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id(name.lowercase()))

        override fun asItem(): Item = value
    }

    //    Misc    //

    enum class Misc :
        HTEntryDelegated<Item>,
        ItemConvertible {
        ALCHEMY_STUFF {
            override fun createItem(): Item = Item(itemSettings().rarity(Rarity.EPIC).maxCount(1))
        },
        BASALT_FIBER,
        BEDROCK_DYNAMITE {
            override fun createItem(): Item = HTBedrockDynamiteItem
        },
        DYNAMITE {
            override fun createItem(): Item = HTDynamiteItem
        },
        EMPTY_FLUID_CUBE,
        ENGINE,
        FORGE_HAMMER {
            override fun createItem(): Item = HTForgeHammerItem
        },
        HEART_OF_THE_NETHER {
            override fun createItem(): Item = Item(itemSettings().rarity(Rarity.UNCOMMON))
        },
        MODULAR_TOOL {
            override fun createItem(): Item = HTModularMiningToolItem(RagiumMaterials.STEEL.tool!!, itemSettings())
        },
        OBLIVION_CRYSTAL {
            override fun createItem(): Item = Item(itemSettings().rarity(Rarity.EPIC))
        },
        OBLIVION_CUBE_SPAWN_EGG {
            override fun createItem(): Item = SpawnEggItem(
                RagiumEntityTypes.OBLIVION_CUBE,
                0x000000,
                0xffffff,
                itemSettings(),
            )
        },
        RAGI_ALLOY_COMPOUND,
        RAGI_CRYSTAL,
        SOAP_INGOT,
        SOLAR_PANEL,
        ;

        internal open fun createItem(): Item = Item(itemSettings())

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id(name.lowercase()))

        override fun asItem(): Item = value
    }

    //    Elements    //

    enum class Element(
        private val suitableBiome: TagKey<Biome>,
        override val enName: String,
        override val jaName: String,
        mapColor: MapColor,
    ) : HTTranslationProvider,
        StringIdentifiable {
        RAGIUM(ConventionalBiomeTags.IS_NETHER, "Ragium", "ラギウム", MapColor.RED),
        RIGIUM(ConventionalBiomeTags.IS_WASTELAND, "Rigium", "リギウム", MapColor.YELLOW),
        RUGIUM(ConventionalBiomeTags.IS_JUNGLE, "Rugium", "ルギウム", MapColor.GREEN),
        REGIUM(ConventionalBiomeTags.IS_OCEAN, "Regium", "レギウム", MapColor.BLUE),
        ROGIUM(ConventionalBiomeTags.IS_END, "Rogium", "ロギウム", MapColor.PURPLE),
        ;

        private val settings: AbstractBlock.Settings =
            blockSettings().mapColor(mapColor).strength(1.5f).sounds(BlockSoundGroup.AMETHYST_BLOCK)
        val buddingBlock: HTBuddingCrystalBlock = HTBuddingCrystalBlock(this, settings.ticksRandomly().requiresTool())
        val clusterBlock: AmethystClusterBlock = AmethystClusterBlock(
            7.0f,
            3.0f,
            settings
                .solid()
                .nonOpaque()
                .luminance { 5 }
                .pistonBehavior(PistonBehavior.DESTROY),
        )
        val dustItem = Item(itemSettings())

        val pendantItem = Item(itemSettings().maxDamage(63))
        val ringItem = Item(itemSettings().maxDamage(63))

        val translationKey = "element.${asString()}"
        val text: Text = Text.translatable(translationKey)

        fun isSuitableBiome(world: World, pos: BlockPos): Boolean = world.getBiome(pos).isIn(suitableBiome)

        //    StringIdentifiable    //

        override fun asString(): String = name.lowercase()
    }

    //    Fluids    //

    enum class Fluids(val color: Color, override val enName: String, override val jaName: String) :
        HTEntryDelegated<Item>,
        HTTranslationProvider,
        ItemConvertible {
        // Vanilla
        WATER(Color(0x0033ff), "Water", "水"),
        LAVA(Color(0xff6600), "Lava", "溶岩") {
            override fun createItem(): Item = object : HTFluidCubeItem() {
                override fun onDrink(stack: ItemStack, world: World, user: LivingEntity) {
                    if (!world.isClient) {
                        user.setOnFireFromLava()
                        dropStackAt(user, Items.OBSIDIAN.defaultStack)
                    }
                }
            }
        },
        MILK(Color(0xffffff), "Milk", "牛乳") {
            override fun createItem(): Item = object : HTFluidCubeItem() {
                override fun onDrink(stack: ItemStack, world: World, user: LivingEntity) {
                    if (!world.isClient) {
                        user.clearStatusEffects()
                    }
                }
            }
        },
        HONEY(Color(0xffcc33), "Honey", "蜂蜜") {
            override fun createItem(): Item = object : HTFluidCubeItem() {
                override fun onDrink(stack: ItemStack, world: World, user: LivingEntity) {
                    if (!world.isClient) {
                        user.removeStatusEffect(StatusEffects.POISON)
                    }
                }
            }
        },

        // Molten Materials
        MOLTEN_BASALT(Color(0x333333), "Molten Basalt", "溶融玄武岩"),

        // Organics
        TALLOW(Color(0xcc9933), "Tallow", "獣脂"),
        SEED_OIL(Color(0x99cc33), "Seed Oil", "種油"),
        GLYCEROL(Color(0x99cc66), "Glycerol", "グリセロール"),

        // Foods
        BATTER(Color(0xffcc66), "Batter", "バッター液"),
        CHOCOLATE(Color(0x663300), "Chocolate", "チョコレート") {
            override fun createItem(): Item = Item(
                itemSettings().food(
                    FoodComponent
                        .Builder()
                        .alwaysEdible()
                        .statusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 20 * 5, 1), 1.0f)
                        .usingConvertsTo(Misc.EMPTY_FLUID_CUBE)
                        .build(),
                ),
            )
        },
        STARCH_SYRUP(Color(0x99ffff), "Starch Syrup", "水あめ"),
        SWEET_BERRIES(Color(0x990000), "Sweet Berries", "スイートベリー") {
            override fun createItem(): Item = Item(
                itemSettings().food(
                    FoodComponent
                        .Builder()
                        .alwaysEdible()
                        .statusEffect(StatusEffectInstance(StatusEffects.SPEED, 20 * 5, 0), 1.0f)
                        .usingConvertsTo(Misc.EMPTY_FLUID_CUBE)
                        .build(),
                ),
            )
        },

        // Natural Resources
        SALT_WATER(Color(0x003399), "Salt Water", "塩水"),
        PETROLEUM(Color(0x000000), "Petroleum", "石油"),
        CRUDE_OIL(Color(0x000000), "Crude Oil", "原油"),

        // Elements
        HYDROGEN(Color(0x0000cc), "Hydrogen", "水素"),
        NITROGEN(Color(0x66cccc), "Nitrogen", "窒素"),
        OXYGEN(Color(0x99ccff), "Oxygen", "酸素"),
        FLUORINE(Color(0x66cc99), "Fluorine", "フッ素"),
        CHLORINE(Color(0xccff33), "Chlorine", "塩素"),

        // Non-organic Chemical Compounds
        NITRIC_ACID(Color(0xcc99ff), "Nitric Acid", "硝酸"),
        SODIUM_HYDROXIDE(Color(0x000099), "Sodium Hydroxide Solution", "水酸化ナトリウム水溶液"),
        SULFURIC_ACID(Color(0xff3300), "Sulfuric Acid", "硫酸"),
        MIXTURE_ACID(Color(0xcc6633), "Mixture Acid", "混酸"),

        // Oil products
        REFINED_GAS(Color(0xcccccc), "Refined Gas", "精製ガス"),
        NAPHTHA(Color(0xff9900), "Naphtha", "ナフサ"),
        TAR(Color(0x000033), "Tar", "タール"),
        METHANE(Color(0xcc0099), "Methane", "メタン"),
        METHANOL(Color(0xcc00ff), "Methanol", "メタノール"),
        LPG(Color(0xffff33), "LPG", "LGP"),
        HELIUM(Color(0xffff99), "Helium", "ヘリウム"),
        ETHYLENE(Color(0x999999), "Ethylene", "エチレン"),
        VINYL_CHLORIDE(Color(0x99cc99), "Vinyl Chloride", "塩化ビニル"),
        TETRA_FLUORO_ETHYLENE(Color(0x669999), "Tetra Fluoro Ethylene", "テトラフルオロエチレン"),
        DIESEL(Color(0xcccc00), "Diesel", "ディーゼル"),
        BENZENE(Color(0x000066), "Benzene", "ベンゼン"),
        TOLUENE(Color(0x666699), "Toluene", "トルエン"),
        PHENOL(Color(0x996633), "Phenol", "フェノール"),

        NITRO_GLYCERIN(Color(0x99cc66), "Nitroglycerin", "ニトログリセリン"),
        TRINITROTOLUENE(Color(0x666699), "Trinitrotoluene", "トリニトロトルエン"),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("${name.lowercase()}_fluid_cube"))

        internal open fun createItem(): Item = Item(itemSettings())

        override fun asItem(): Item = entry.value()
    }
}
