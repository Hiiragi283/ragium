package hiiragi283.lib.data.lang

/**
 * 言語の種類を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmInline
value class HTLangType(val name: String) : Comparable<HTLangType> {
    companion object {
        @JvmStatic
        private val instances: MutableMap<String, HTLangType> = hashMapOf()

        @JvmStatic
        fun of(name: String): HTLangType = instances.computeIfAbsent(name.lowercase(), ::HTLangType)
    }

    override fun compareTo(other: HTLangType): Int = this.name.compareTo(other.name)
}
