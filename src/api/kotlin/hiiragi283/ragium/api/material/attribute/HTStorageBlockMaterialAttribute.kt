package hiiragi283.ragium.api.material.attribute

enum class HTStorageBlockMaterialAttribute(val baseCount: Int, val pattern: List<String>) : HTMaterialAttribute {
    SINGLE(1, "A"),
    TWO_BY_TWO(4, "AA", "AB"),
    THREE_BY_THREE(9, "AAA", "ABA", "AAA"),
    ;

    constructor(baseCount: Int, vararg pattern: String) : this(baseCount, pattern.toList())
}
