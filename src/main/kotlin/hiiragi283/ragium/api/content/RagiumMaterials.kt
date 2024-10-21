package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import net.minecraft.block.Block
import net.minecraft.item.*
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.SoundEvent
import net.minecraft.util.StringIdentifiable

enum class RagiumMaterials(
    val tier: HTMachineTier,
    override val enName: String,
    override val jaName: String,
    val armor: RegistryEntry<ArmorMaterial>? = null,
    val tool: ToolMaterial? = null,
) : HTTranslationProvider, StringIdentifiable {
    // tier1
    CRUDE_RAGINITE(HTMachineTier.PRIMITIVE, "Crude Raginite", "粗製ラギナイト"),
    RAGI_ALLOY(HTMachineTier.PRIMITIVE, "Ragi-Alloy", "ラギ合金"),
    COPPER(HTMachineTier.PRIMITIVE, "Copper", "銅"),
    IRON(HTMachineTier.PRIMITIVE, "Iron", "鉄", ArmorMaterials.IRON, ToolMaterials.IRON),
    ASH(HTMachineTier.BASIC, "Ash", "灰"),

    // tier2
    RAGINITE(HTMachineTier.BASIC, "Raginite", "ラギナイト"),
    RAGI_STEEL(HTMachineTier.BASIC, "Ragi-Steel", "ラギスチール"),
    ALUMINA(HTMachineTier.BASIC, "Alumina", "アルミナ"),
    ALUMINUM(HTMachineTier.BASIC, "Aluminum", "アルミニウム"),
    BAUXITE(HTMachineTier.BASIC, "Bauxite", "ボーキサイト"),
    FLUORITE(HTMachineTier.BASIC, "Fluorite", "蛍石"),
    GOLD(HTMachineTier.BASIC, "Gold", "金", ArmorMaterials.GOLD, ToolMaterials.GOLD),
    INVAR(HTMachineTier.BASIC, "Invar", "インバー"),
    NICKEL(HTMachineTier.BASIC, "Nickel", "ニッケル"),
    PLASTIC(HTMachineTier.BASIC, "Plastic", "プラスチック"), // PE
    SILICON(HTMachineTier.BASIC, "Silicon", "シリコン"),
    STEEL(HTMachineTier.BASIC, "Steel", "スチール", Armor.STEEL, Tool.STEEL),

    // tier3
    RAGI_CRYSTAL(HTMachineTier.ADVANCED, "Ragi-Crystal", "ラギクリスタリル"),
    REFINED_RAGI_STEEL(HTMachineTier.ADVANCED, "Refined Ragi-Steel", "精製ラギスチール"),
    OBLIVION_CRYSTAL(HTMachineTier.ADVANCED, "Oblivion Crystal", "忘却の結晶"),
    ENGINEERING_PLASTIC(HTMachineTier.ADVANCED, "Engineering Plastic", "エンジニアリングプラスチック"), // PC
    STELLA(HTMachineTier.ADVANCED, "S.T.E.L.L.A", "S.T.E.L.L.A", Armor.STELLA),
    ;

    fun getBlock(): RagiumContents.StorageBlocks? = RagiumContents.StorageBlocks.entries.firstOrNull { it.material == this }

    fun getHull(): RagiumContents.Hulls? = RagiumContents.Hulls.entries.firstOrNull { it.material == this }

    fun getIngot(): RagiumContents.Ingots? = RagiumContents.Ingots.entries.firstOrNull { it.material == this }

    fun getPlate(): RagiumContents.Plates? = RagiumContents.Plates.entries.firstOrNull { it.material == this }

    fun getRawMaterial(): RagiumContents.RawMaterials? = RagiumContents.RawMaterials.entries.firstOrNull { it.material == this }

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
    
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
        ) { Ingredient.fromTag(RagiumItemTags.STEEL_INGOTS) }

        @JvmField
        val STELLA: RegistryEntry<ArmorMaterial> = register(
            "stella",
            ArmorMaterials.DIAMOND,
        ) { Ingredient.fromTag(RagiumItemTags.STEEL_INGOTS) }

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
        STEEL(BlockTags.INCORRECT_FOR_IRON_TOOL, 750, 8.0f, 3.0f, 14, Ingredient.fromTag(RagiumItemTags.STEEL_INGOTS)),
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
