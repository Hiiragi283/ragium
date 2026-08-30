package hiiragi283.lib.material.property

/**
 * ブロックの作成に要求される個数を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
enum class HTStorageBlockProperty(val baseCount: Int, val pattern: List<String>?) {
    /**
     * ブロックの作成に基本部品が1つ必要
     */
    SINGLE(1, null),

    /**
     * ブロックの作成に基本部品が4つ必要
     */
    TWO_BY_TWO(4, "AA", "AB"),

    /**
     * ブロックの作成に基本部品が9つ必要
     */
    THREE_BY_THREE(9, "AAA", "ABA", "AAA"),
    ;

    constructor(baseCount: Int, vararg pattern: String) : this(baseCount, pattern.toList())
}
