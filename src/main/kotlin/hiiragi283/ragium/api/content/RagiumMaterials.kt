package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tags.HTTagPrefix
import hiiragi283.ragium.api.tags.HTTagPrefixes
import hiiragi283.ragium.common.RagiumContents
import net.minecraft.block.Block
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.SoundEvent
import net.minecraft.util.StringIdentifiable

enum class RagiumMaterials(val type: Type, override val enName: String, override val jaName: String) :
    HTTranslationProvider,
    StringIdentifiable {
    // tier1
    CRUDE_RAGINITE(Type.MINERAL, "Crude Raginite", "粗製ラギナイト"),
    RAGI_ALLOY(Type.ALLOY, "Ragi-Alloy", "ラギ合金"),
    COPPER(Type.METAL, "Copper", "銅"),
    IRON(Type.METAL, "Iron", "鉄"),
    ASH(Type.DUST, "Ash", "灰"),
    NITER(Type.MINERAL, "Niter", "硝石"),
    SULFUR(Type.MINERAL, "Sulfur", "硫黄"),

    // tier2
    RAGINITE(Type.MINERAL, "Raginite", "ラギナイト"),
    RAGI_STEEL(Type.ALLOY, "Ragi-Steel", "ラギスチール"),
    BAUXITE(Type.MINERAL, "Bauxite", "ボーキサイト"),
    FLUORITE(Type.GEM, "Fluorite", "蛍石"),
    GOLD(Type.METAL, "Gold", "金"),
    PLASTIC(Type.PLASTIC, "Plastic", "プラスチック"), // PE
    SILICON(Type.METAL, "Silicon", "シリコン"),
    STEEL(Type.ALLOY, "Steel", "スチール"),

    // tier3
    RAGI_CRYSTAL(Type.GEM, "Ragi-Crystal", "ラギクリスタリル"),
    REFINED_RAGI_STEEL(Type.ALLOY, "Refined Ragi-Steel", "精製ラギスチール"),
    ALUMINUM(Type.METAL, "Aluminum", "アルミニウム"),
    ENGINEERING_PLASTIC(Type.PLASTIC, "Engineering Plastic", "エンジニアリングプラスチック"), // PC
    STELLA(Type.PLASTIC, "S.T.E.L.L.A.", "S.T.E.L.L.A."),

    // tier4
    RAGIUM(Type.GEM, "Ragium", "ラギウム"),

    // integration
    EMERALD(Type.GEM, "Emerald", "エメラルド"),
    DIAMOND(Type.GEM, "Diamond", "ダイヤモンド"),
    LAPIS(Type.GEM, "Lapis", "ラピス"),
    PERIDOT(Type.GEM, "Peridot", "ペリドット"),
    QUARTZ(Type.GEM, "Quartz", "クォーツ"),
    RUBY(Type.GEM, "Ruby", "ルビー"),
    SAPPHIRE(Type.GEM, "Sapphire", "サファイア"),

    IRIDIUM(Type.METAL, "Iridium", "イリジウム"),
    LEAD(Type.METAL, "Lead", "鉛"),
    NICKEL(Type.METAL, "Nickel", "ニッケル"),
    PLATINUM(Type.METAL, "Platinum", "プラチナ"),
    SILVER(Type.METAL, "Silver", "シルバー"),
    TIN(Type.METAL, "Tin", "スズ"),
    TITANIUM(Type.METAL, "Titanium", "チタン"),
    TUNGSTEN(Type.METAL, "Tungsten", "タングステン"),
    ZINC(Type.METAL, "Zinc", "亜鉛"),

    BRASS(Type.ALLOY, "Brass", "真鍮"),
    BRONZE(Type.ALLOY, "Bronze", "青銅"),
    ELECTRUM(Type.ALLOY, "Electrum", "琥珀金"),
    INVAR(Type.ALLOY, "Invar", "インバー"),
    ;

    fun isValidPrefix(prefix: HTTagPrefix): Boolean = prefix in type.validPrefixes

    fun getBlock(): RagiumContents.StorageBlocks? = RagiumContents.StorageBlocks.entries.firstOrNull { it.material == this }

    fun getIngot(): RagiumContents.Ingots? = RagiumContents.Ingots.entries.firstOrNull { it.material == this }

    fun getGem(): RagiumContents.Gems? = RagiumContents.Gems.entries.firstOrNull { it.material == this }

    fun getPlate(): RagiumContents.Plates? = RagiumContents.Plates.entries.firstOrNull { it.material == this }

    fun getRawMaterial(): RagiumContents.RawMaterials? = RagiumContents.RawMaterials.entries.firstOrNull { it.material == this }

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()

    //    Type    //

    enum class Type(val validPrefixes: List<HTTagPrefix>) {
        ALLOY(HTTagPrefixes.DUSTS, HTTagPrefixes.INGOTS, HTTagPrefixes.PLATES, HTTagPrefixes.STORAGE_BLOCKS),
        DUST(HTTagPrefixes.DUSTS),
        GEM(HTTagPrefixes.DUSTS, HTTagPrefixes.GEMS, HTTagPrefixes.ORES, HTTagPrefixes.STORAGE_BLOCKS),
        METAL(
            HTTagPrefixes.DUSTS,
            HTTagPrefixes.INGOTS,
            HTTagPrefixes.ORES,
            HTTagPrefixes.PLATES,
            HTTagPrefixes.RAW_MATERIALS,
            HTTagPrefixes.STORAGE_BLOCKS,
        ),
        MINERAL(HTTagPrefixes.DUSTS, HTTagPrefixes.ORES, HTTagPrefixes.RAW_MATERIALS),
        PLASTIC(HTTagPrefixes.PLATES),
        ;

        constructor(vararg prefixed: HTTagPrefix) : this(prefixed.toList())
    }

    //    Holder    //

    interface Holder {
        val material: RagiumMaterials
    }

    //    Armor    //

    object Armor {
        @JvmField
        val STEEL: RegistryEntry<ArmorMaterial> = register(
            "steel",
            ArmorMaterials.IRON,
        ) { Ingredient.fromTag(RagiumContents.Ingots.STEEL.prefixedTagKey) }

        @JvmField
        val STELLA: RegistryEntry<ArmorMaterial> = register(
            "stella",
            ArmorMaterials.DIAMOND,
        ) { Ingredient.ofItems(RagiumContents.Plates.STELLA) }

        @JvmField
        val RAGIUM: RegistryEntry<ArmorMaterial> = register(
            "ragium",
            ArmorMaterials.NETHERITE,
        ) { Ingredient.ofItems(RagiumContents.Gems.RAGIUM) }

        @JvmStatic
        private fun register(
            name: String,
            entry: RegistryEntry<ArmorMaterial>,
            repairment: () -> Ingredient,
        ): RegistryEntry<ArmorMaterial> = entry.value().let { parent: ArmorMaterial ->
            register(
                name,
                parent.defense,
                parent.enchantability,
                parent.equipSound,
                parent.toughness,
                parent.knockbackResistance,
                repairment,
            )
        }

        @JvmStatic
        private fun register(
            name: String,
            defenceMap: Map<ArmorItem.Type, Int>,
            enchantability: Int,
            equipSound: RegistryEntry<SoundEvent>,
            toughness: Float,
            kResistance: Float,
            repairment: () -> Ingredient,
        ): RegistryEntry<ArmorMaterial> = RagiumAPI.id(name).let {
            Registry.registerReference(
                Registries.ARMOR_MATERIAL,
                it,
                ArmorMaterial(
                    defenceMap,
                    enchantability,
                    equipSound,
                    repairment,
                    listOf(ArmorMaterial.Layer(it)),
                    toughness,
                    kResistance,
                ),
            )
        }
    }

    //    Tool    //

    enum class Tool(
        private val inverseTag: TagKey<Block>,
        private val durability: Int,
        private val miningSpeed: Float,
        private val attackDamage: Float,
        private val enchantability: Int,
        private val repairment: Ingredient,
    ) : ToolMaterial {
        STEEL(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            750,
            8.0f,
            3.0f,
            14,
            Ingredient.fromTag(RagiumContents.Ingots.STEEL.prefixedTagKey),
        ),
        ;

        constructor(from: ToolMaterial) : this(
            from.inverseTag,
            from.durability,
            from.miningSpeedMultiplier,
            from.attackDamage,
            from.enchantability,
            from.repairIngredient,
        )

        override fun getDurability(): Int = durability

        override fun getMiningSpeedMultiplier(): Float = miningSpeed

        override fun getAttackDamage(): Float = attackDamage

        override fun getEnchantability(): Int = enchantability

        override fun getRepairIngredient(): Ingredient = repairment

        override fun getInverseTag(): TagKey<Block> = inverseTag
    }
}
