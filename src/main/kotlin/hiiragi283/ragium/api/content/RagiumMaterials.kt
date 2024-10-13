package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import net.minecraft.block.Block
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ToolMaterial
import net.minecraft.item.ToolMaterials
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

enum class RagiumMaterials(
    val tier: HTMachineTier,
    override val enName: String,
    override val jaName: String,
    val armor: RegistryEntry<ArmorMaterial>? = null,
    val tool: ToolMaterial? = null,
) : HTTranslationProvider {
    // tier1
    RAW_RAGINITE(HTMachineTier.PRIMITIVE, "Raw Raginite", "未加工のラギナイト"),
    RAGI_ALLOY(HTMachineTier.PRIMITIVE, "Ragi-Alloy", "ラギ合金"),
    COPPER(HTMachineTier.PRIMITIVE, "Copper", "銅"),
    IRON(HTMachineTier.PRIMITIVE, "Iron", "鉄", ArmorMaterials.IRON, ToolMaterials.IRON),
    ASH(HTMachineTier.BASIC, "Ash", "灰"),
    NITER(HTMachineTier.PRIMITIVE, "Niter", "硝石"),
    SULFUR(HTMachineTier.BASIC, "Sulfur", "硫黄"),

    // tier2
    RAGINITE(HTMachineTier.BASIC, "Raginite", "ラギナイト"),
    RAGI_STEEL(HTMachineTier.BASIC, "Ragi-Steel", "ラギスチール"),
    BASALT_FIBER(HTMachineTier.BASIC, "Basalt Fiber", "玄武岩繊維"),
    GOLD(HTMachineTier.BASIC, "Gold", "金", ArmorMaterials.GOLD, ToolMaterials.GOLD),
    INVAR(HTMachineTier.BASIC, "Invar", "インバー"),
    NICKEL(HTMachineTier.BASIC, "Nickel", "ニッケル"),
    SILICON(HTMachineTier.BASIC, "Silicon", "シリコン"),
    SILVER(HTMachineTier.BASIC, "Silver", "銀"),
    STEEL(HTMachineTier.BASIC, "Steel", "スチール", Armor.STEEL, Tool.STEEL),

    // tier3
    RAGI_CRYSTAL(HTMachineTier.ADVANCED, "Ragi-Crystal", "ラギクリスタリル"),
    REFINED_RAGI_STEEL(HTMachineTier.ADVANCED, "Refined Ragi-Steel", "精製ラギスチール"),
    PE(HTMachineTier.ADVANCED, "PE", "ポリエチレン"),
    PVC(HTMachineTier.ADVANCED, "PVC", "塩化ビニル"),
    PTFE(HTMachineTier.ADVANCED, "PTFE", "テフロン"),

    ;

    fun getOre(): RagiumContents.Ores? = RagiumContents.Ores.entries.firstOrNull { it.material == this }

    fun getDeepOre(): RagiumContents.DeepOres? = RagiumContents.DeepOres.entries.firstOrNull { it.material == this }

    fun getBlock(): RagiumContents.StorageBlocks? = RagiumContents.StorageBlocks.entries.firstOrNull { it.material == this }

    fun getHull(): RagiumContents.Hulls? = RagiumContents.Hulls.entries.firstOrNull { it.material == this }

    fun getIngot(): RagiumContents.Ingots? = RagiumContents.Ingots.entries.firstOrNull { it.material == this }

    fun getPlate(): RagiumContents.Plates? = RagiumContents.Plates.entries.firstOrNull { it.material == this }

    fun getRawMaterial(): RagiumContents.RawMaterials? = RagiumContents.RawMaterials.entries.firstOrNull { it.material == this }

    //    Armor    //

    object Armor {
        @JvmField
        val STEEL: RegistryEntry.Reference<ArmorMaterial> = register(
            "steel",
            mapOf(
                ArmorItem.Type.HELMET to 2,
                ArmorItem.Type.CHESTPLATE to 6,
                ArmorItem.Type.LEGGINGS to 5,
                ArmorItem.Type.BOOTS to 2,
                ArmorItem.Type.BODY to 5,
            ),
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            1.0f,
            0.0f,
        ) { Ingredient.fromTag(RagiumItemTags.STEEL_INGOTS) }

        @JvmStatic
        private fun register(
            name: String,
            defenceMap: Map<ArmorItem.Type, Int>,
            enchantability: Int,
            equipSound: RegistryEntry<SoundEvent>,
            toughness: Float,
            kResistance: Float,
            repairment: () -> Ingredient,
        ): RegistryEntry.Reference<ArmorMaterial> = RagiumAPI.id(name).let {
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
