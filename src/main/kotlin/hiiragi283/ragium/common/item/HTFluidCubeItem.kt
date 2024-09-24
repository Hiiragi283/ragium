package hiiragi283.ragium.common.item

class HTFluidCubeItem private constructor(val fluidName: String) : HTBaseItem(Settings()) {
    companion object {
        val registry: Map<String, HTFluidCubeItem>
            get() = registry1
        private val registry1: MutableMap<String, HTFluidCubeItem> = mutableMapOf()

        @JvmStatic
        operator fun get(name: String): HTFluidCubeItem? = registry[name]

        @JvmStatic
        fun getOrThrow(name: String): HTFluidCubeItem = checkNotNull(get(name)) { "Fluid Cube; $name is not registered!" }

        @JvmStatic
        fun create(name: String): HTFluidCubeItem {
            check(name !in registry1) { "Fluid Cube; $name is already registered!" }
            val item = HTFluidCubeItem(name)
            registry1[name] = item
            return item
        }
    }
}
