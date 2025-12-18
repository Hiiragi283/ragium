package hiiragi283.ragium.common

import hiiragi283.ragium.api.data.lang.HTLangName
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.util.StringRepresentable

enum class HTUpgradeType(val group: Group, private val enName: String, private val jpName: String) :
    StringRepresentable,
    HTLangName,
    HTItemHolderLike.Delegate {
    // Creative
    CREATIVE(Group.CREATIVE, "Creative", "クリエイティブ"),

    // Generator

    // Processor
    EFFICIENCY(Group.PROCESSOR, "Efficiency", "効率"),
    SPEED(Group.PROCESSOR, "Speed", "スピード"),
    HIGH_SPEED(Group.PROCESSOR, "High-Speed", "ハイスピード"),

    // Specific Processor
    BIO_COMPOSTING(Group.PROCESSOR, "Bio Composting", "有機コンポスト"),
    BLASTING(Group.PROCESSOR, "Blasting", "溶鉱炉"),
    EFFICIENT_CRUSHING(Group.PROCESSOR, "Efficient Crushing", "効率的粉砕"),
    EXP_EXTRACTING(Group.PROCESSOR, "Exp Extracting", "経験値抽出"),
    EXTRA_VOIDING(Group.PROCESSOR, "Extra Voiding", "副産物廃棄"),
    SMOKING(Group.PROCESSOR, "Smoking", "燻製器"),

    // Device
    EXP_COLLECTING(Group.DEVICE, "Exp Collecting", "経験値収集"),
    FISHING(Group.DEVICE, "Fishing", "自動釣り"),
    MOB_CAPTURING(Group.DEVICE, "Mob Capturing", "モブ捕獲"),

    // Storage
    ENERGY_CAPACITY(Group.STORAGE, "Energy Capacity", "エネルギー容量"),
    FLUID_CAPACITY(Group.STORAGE, "Fluid Capacity", "液体容量"),
    ITEM_CAPACITY(Group.STORAGE, "Item Capacity", "アイテム容量"),
    ;

    override fun getDelegate(): HTItemHolderLike = RagiumItems.MACHINE_UPGRADES[this]!!

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jpName
    }

    override fun getSerializedName(): String = name.lowercase()

    enum class Group : StringRepresentable {
        CREATIVE,
        GENERATOR,
        PROCESSOR,
        DEVICE,
        STORAGE,
        ;

        override fun getSerializedName(): String = name.lowercase()
    }
}
