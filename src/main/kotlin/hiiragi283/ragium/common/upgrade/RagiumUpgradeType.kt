package hiiragi283.ragium.common.upgrade

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.data.lang.HTLangTypes
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.util.Either
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

enum class RagiumUpgradeType(val group: Group, private val enName: String, private val jpName: String) :
    StringRepresentable,
    HTLangName,
    HTItemHolderLike.Simple<Item> {
    // Creative
    CREATIVE(Group.CREATIVE, "Creative", "クリエイティブ"),

    // Generator

    // Processor
    EFFICIENCY(Group.PROCESSOR, "Efficiency", "効率"),
    SPEED(Group.PROCESSOR, "Speed", "スピード"),
    HIGH_SPEED(Group.PROCESSOR, "High-Speed", "ハイスピード"),

    // Specific Processor
    BLASTING(Group.PROCESSOR, "Blasting", "溶鉱炉"),
    EFFICIENT_CRUSHING(Group.PROCESSOR, "Efficient Crushing", "効率的粉砕"),
    EXTRA_VOIDING(Group.PROCESSOR, "Extra Voiding", "副産物廃棄"),
    SMOKING(Group.PROCESSOR, "Smoking", "燻製器"),

    // Device

    // Storage
    ENERGY_CAPACITY(Group.STORAGE, "Energy Capacity", "エネルギー容量"),
    FLUID_CAPACITY(Group.STORAGE, "Fluid Capacity", "液体容量"),
    ITEM_CAPACITY(Group.STORAGE, "Item Capacity", "アイテム容量"),
    ;

    private val delegate: HTSimpleItemHolderLike by lazy { RagiumItems.UPGRADES[this]!! }

    override fun unwrap(): Either<ResourceKey<Item>, Holder<Item>> = delegate.unwrap()

    override fun get(): Item = delegate.get()

    override fun getTranslatedName(type: HTLangType): String = when (type) {
        HTLangTypes.JA_JP -> jpName
        else -> enName
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
